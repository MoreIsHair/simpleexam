package cn.tycad.oa.exam.service;

import cn.tycad.oa.exam.common.enums.QuestionType;
import cn.tycad.oa.exam.common.enums.RoleTypeEnum;
import cn.tycad.oa.exam.common.util.JwtUtils;
import cn.tycad.oa.exam.common.util.QrCodeUtils;
import cn.tycad.oa.exam.excel.ExcelExamInfo;
import cn.tycad.oa.exam.excel.ExcelQuestionInfo;
import cn.tycad.oa.exam.excel.ExcelUtil;
import cn.tycad.oa.exam.exception.BusinessException;
import cn.tycad.oa.exam.common.enums.ExceptionInfoEnum;
import cn.tycad.oa.exam.interceptor.AuthorityInterceptor;
import cn.tycad.oa.exam.model.bo.*;
import cn.tycad.oa.exam.model.entity.*;
import cn.tycad.oa.exam.model.param.MarkPapersParam;
import cn.tycad.oa.exam.model.param.PageInfoAndUserNameAndExamNameParam;
import cn.tycad.oa.exam.model.param.UserIdAndTimeAndExamNameAndCreatorParam;
import cn.tycad.oa.exam.model.vo.UpdateExamInfoVo;
import cn.tycad.oa.exam.repository.*;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * @author YY
 * @date 2019/3/5
 * @description
 */
@Service
@Slf4j
public class ExamService {

    @Autowired
    private TbExamMapper tbExamMapper;

    @Autowired
    private TbQuestionMapper tbQuestionMapper;

    @Autowired
    private TbUserExamMapper tbUserExamMapper;

    @Autowired
    private TbOptionMapper tbOptionMapper;

    @Autowired
    private TbValidAnswerMapper tbValidAnswerMapper;

    @Autowired
    private TbUserAnswerMapper tbUserAnswerMapper;

    @Autowired
    private SystemUserMapper userMapper;

    @Autowired
    private TbReviewMapper reviewMapper;

    @Autowired
    private TbDisposableExamInfoMapper tbDisposableExamInfoMapper;

    @Value("${qrcode.logoPath}")
    private  String logoPath;

    @Value("${qrcode.basePath}")
    private  String basePath;

    @Value("${jwt.secret}")
    private String secret;

    @Autowired
    private AuthorityInterceptor authorityInterceptor;


    /**
     * 新增考试任务(支持用户录入关键参数)
     * TODO 培训考试设计到一次性用户参考，是否不能有简单题（一次性用户的答题不做具体回答保存，只留有总分数）
     */
    @Transactional(rollbackFor = Exception.class)
    public void addExamTask(File file, String token) throws Exception {
        String creator  = JwtUtils.parseJWT(token, SystemUser.class, secret).getUsername();
        String fileExtension = ExcelUtil.getFileExtension(file);
        // 校验模板
        if (!ExcelUtil.validateTemplate(file, fileExtension, 0, ExcelExamInfo.class)){
            throw new BusinessException(ExceptionInfoEnum.INVALID_TEMPLATE);
        }
        // 获取第一个所有试卷（第一个sheet)
        List<Object> examList = new ArrayList<>();
        try {
            examList = ExcelUtil.readExcelWithModel(new BufferedInputStream(new FileInputStream(file)), 1, ExcelExamInfo.class, true);
        } catch (ExcelAnalysisException e) {
            log.debug(e.getMessage());
            throw new BusinessException(ExceptionInfoEnum.INVALID_TEMPLATE);
        }
        // 获取试卷条数
        int size = examList.size();
        for (int i = 2; i <(2+size); i++) {
            List<Object> questionList = new ArrayList<>();
            try {
                questionList = ExcelUtil.readExcelWithModel(new BufferedInputStream(new FileInputStream(file)), i, ExcelQuestionInfo.class,true);
            }catch (ExcelAnalysisException e) {
                log.debug(e.getMessage());
                throw new BusinessException(ExceptionInfoEnum.INVALID_TEMPLATE);
            }
            ExcelExamInfo excelExamInfo =(ExcelExamInfo)examList.get(i-2);
            if (StringUtils.isBlank(excelExamInfo.getDuration())){
                log.debug("导入文件考试时长为空");
                throw new BusinessException(ExceptionInfoEnum.EXAM_IMPORT_DURATION_INVALID_ERROR);
            }
            if (!ExcelUtil.isInteger(excelExamInfo.getDuration())){
                log.debug("考试时长错误，不能存在非法字母");
                throw new BusinessException(ExceptionInfoEnum.EXAM_IMPORT_TIME_NOT_NUM);
            }
            String examId= UUID.randomUUID().toString();
            TbExam tbExam = new TbExam(excelExamInfo);
            tbExam.setExamId(examId);
            tbExam.setCreateTime(new Date());
            tbExam.setLastEditor(creator);
            tbExam.setLastEditTime(new Date());
            tbExam.setCreator(creator);
            if (StringUtils.isBlank(excelExamInfo.getExamName())){
                log.debug("试题名称为空");
                throw new BusinessException(ExceptionInfoEnum.BLANK_EXAM_NAME);
            }
            // 选定阅卷老师
            if (StringUtils.isNotBlank(excelExamInfo.getTeacher())){
                SystemUser one = userMapper.findByUserNameAndRoleType(excelExamInfo.getTeacher(),1);
                if (one==null){
                    log.debug("{},指定阅卷老师用户名不存在", examId);
                    throw new BusinessException(ExceptionInfoEnum.EXAM_IMPORT_TEACHER_IS_NOT_VALID);
                }else {
                    tbExam.setTeacherUserName(excelExamInfo.getTeacher());
                }
            } else {
                log.debug("{},指定阅卷老师用户名不存在", examId);
                throw new BusinessException(ExceptionInfoEnum.EXAM_IMPORT_TEACHER_IS_NOT_VALID);
            }

            // 如果发布时间为空或者截止时间为空，则全部置空
            if (tbExam.getPublishTime() == null || tbExam.getDeadLine() == null) {
                tbExam.setPublishTime(null);
                tbExam.setDeadLine(null);
            } else {
                if (tbExam.getDeadLine() != null && new Date().after(tbExam.getDeadLine())) {
                    tbExam.setDeadLine(null);
                    // 可能出现导入时没问题，随着时间流转出现发布时间开始了，但是没有结束时间的问题
                    tbExam.setPublishTime(null);
                }
                if (tbExam.getPublishTime()!= null && new Date().after(tbExam.getPublishTime())) {
                    tbExam.setPublishTime(null);
                    // 可能出现导入时没问题，随着时间流转出现一直没有发布时间，一直不能开始，但是考试已近过了结束时间的问题
                    tbExam.setDeadLine(null);
                }
                // 判断指定的考试发布时间与截止时间
                if (tbExam.getPublishTime()!= null && tbExam.getDeadLine()!= null) {
                    if (tbExam.getPublishTime().before(new Date()) || tbExam.getPublishTime().after(tbExam.getDeadLine()) || new Date().after(tbExam.getDeadLine())){
                        log.debug("导入时间不合法被置空 p:{},d:{}", tbExam.getPublishTime(), tbExam.getDeadLine());
                        tbExam.setPublishTime(null);
                        tbExam.setDeadLine(null);
                    }
                }
            }
            if (tbExam.isValid()) {
                tbExamMapper.insert(tbExam);
                // 选定参加的用户
                if (StringUtils.isNotBlank(excelExamInfo.getStudents())){
                    String students = excelExamInfo.getStudents().replace('，',',');

                    String[] split = students.split(",");
                    // 去重
                    HashSet<String> strings = new HashSet<>(Arrays.asList(split));
                    for (String s : strings) {
                        SystemUser one = userMapper.findByUserNameAndRoleType(s,0);
                        if (one==null){
                            log.debug("{},指定参加考生用户名称不存在",examId);
                            continue;
                        }
                        TbUserExam tbUserExam = new TbUserExam();
                        tbUserExam.setExamId(examId);
                        tbUserExam.setExamStatus(0);
                        tbUserExam.setUserId(one.getUserId());
                        tbUserExamMapper.insert(tbUserExam);
                    }
                }
                // 问题
                int optionOrderNum =1;
                int muchOptionOrderNum =1;
                int tfOrderNum =1;
                int textOrderNum =1;
                for (Object o : questionList) {
                    ExcelQuestionInfo excelQuestionInfo = ((ExcelQuestionInfo)o);
                    if (excelQuestionInfo.isValid()) {
                        // 选择题，需要插入选项
                        if (excelQuestionInfo.getQuestionType()==0 || excelQuestionInfo.getQuestionType()==-1){
                            String questionId = UUID.randomUUID().toString();
                            TbQuestion tbQuestion = new TbQuestion(excelQuestionInfo);
                            tbQuestion.setQuestionId(questionId);
                            tbQuestion.setExamId(examId);
                            // 单选
                            if (excelQuestionInfo.getQuestionType()==0){
                                tbQuestion.setOrderNum(optionOrderNum++);
                            }
                            // 多选
                            if (excelQuestionInfo.getQuestionType()==-1){
                                tbQuestion.setOrderNum(optionOrderNum++);
                            }
                            tbQuestionMapper.insert(tbQuestion);
                            // 选项
                            int optionNum = 1;
                            String one = "";
                            String two = "";
                            String three = "";
                            String four = "";
                            String five = "";
                            String six = "";
                            if (StringUtils.isNotBlank(excelQuestionInfo.getOptionOne())){
                                one = this.judgeInsertOption(excelQuestionInfo.getOptionOne(), questionId, optionNum++);
                            }
                            if (StringUtils.isNotBlank(excelQuestionInfo.getOptionTwo())){
                                two = this.judgeInsertOption(excelQuestionInfo.getOptionTwo(), questionId, optionNum++);
                            }
                            if (StringUtils.isNotBlank(excelQuestionInfo.getOptionThree())){
                                three = this.judgeInsertOption(excelQuestionInfo.getOptionThree(), questionId, optionNum++);
                            }
                            if (StringUtils.isNotBlank(excelQuestionInfo.getOptionFour())){
                                four = this.judgeInsertOption(excelQuestionInfo.getOptionFour(), questionId, optionNum++);
                            }
                            if (StringUtils.isNotBlank(excelQuestionInfo.getOptionFive())){
                                five = this.judgeInsertOption(excelQuestionInfo.getOptionFive(), questionId, optionNum++);
                            }
                            if (StringUtils.isNotBlank(excelQuestionInfo.getOptionSix())){
                                six = this.judgeInsertOption(excelQuestionInfo.getOptionSix(), questionId, optionNum++);
                            }
                            // 添加正确答案
                            if (StringUtils.isNotBlank(excelQuestionInfo.getValidAnswer())){
                                String validAnswer = excelQuestionInfo.getValidAnswer();
                                if (validAnswer.contains("，")){
                                    validAnswer = validAnswer.replace("，",",");
                                }
                                String[] split = validAnswer.split(",");
                                for (String s : split) {
                                    switchInsertValidAnswer(questionId, one, two, three, four, five, six, s);
                                }
                            }else{
                                log.debug("选择题未添加正确答案");
                                throw new BusinessException(ExceptionInfoEnum.EXAM_IMPORT_ANSWER_IS_NULL_ERROR);
                            }
                        }
                        // 判断题
                        if (excelQuestionInfo.getQuestionType()==1){
                            String questionId = UUID.randomUUID().toString();
                            TbQuestion tbQuestion = new TbQuestion(excelQuestionInfo);
                            tbQuestion.setOrderNum(tfOrderNum++);
                            tbQuestion.setQuestionId(questionId);
                            tbQuestion.setExamId(examId);
                            tbQuestionMapper.insert(tbQuestion);
                            if (StringUtils.isBlank(excelQuestionInfo.getValidAnswer())){
                                log.debug("判断题未添加正确答案");
                                throw new BusinessException(ExceptionInfoEnum.EXAM_IMPORT_ANSWER_IS_NULL_ERROR);
                            }
                            TbValidAnswer tbValidAnswer = new TbValidAnswer();
                            tbValidAnswer.setQuestionId(questionId);
                            HashSet<String> strings = new HashSet<>(Arrays.asList("正确", "错误"));
                            strings.add(excelQuestionInfo.getValidAnswer());
                            if (strings.size() > 2){
                                log.debug("判断题未添加正确答案或者格式不对");
                                throw new BusinessException(ExceptionInfoEnum.EXAM_IMPORT_ANSWER_IS_NULL_ERROR);
                            }
                            tbValidAnswer.setAnswerValue(excelQuestionInfo.getValidAnswer());
                            tbValidAnswerMapper.insert(tbValidAnswer);
                        }
                        // 简单题
                        if (excelQuestionInfo.getQuestionType()==2){
                            String questionId = UUID.randomUUID().toString();
                            TbQuestion tbQuestion = new TbQuestion(excelQuestionInfo);
                            tbQuestion.setQuestionId(questionId);
                            tbQuestion.setOrderNum(textOrderNum++);
                            tbQuestion.setExamId(examId);
                            tbQuestionMapper.insert(tbQuestion);
                            // 插入正确答案
                            if (StringUtils.isNotBlank(excelQuestionInfo.getValidAnswer())){
                                TbValidAnswer validAnswer = new TbValidAnswer();
                                validAnswer.setAnswerValue(excelQuestionInfo.getValidAnswer());
                                validAnswer.setQuestionId(questionId);
                                tbValidAnswerMapper.insert(validAnswer);
                            }
                        }
                    } else {
                        throw new BusinessException(ExceptionInfoEnum.EXAM_IMPORT_DATA_ERROR);
                    }
                }
            }
        }
    }

    /**
     * 判断选项的正确答案
     * @param questionId
     * @param one
     * @param two
     * @param three
     * @param four
     * @param five
     * @param six
     * @param s
     */
    private void switchInsertValidAnswer(String questionId, String one, String two, String three, String four, String five, String six, String s) {
        switch (Integer.valueOf(s)){
            case 1:{
                insertOptionValidAnswer(questionId, one);
                break;
            }
            case 2:{
                insertOptionValidAnswer(questionId, two);
                break;
            }
            case 3:{
                insertOptionValidAnswer(questionId, three);
                break;
            }
            case 4:{
                insertOptionValidAnswer(questionId, four);
                break;
            }
            case 5:{
                insertOptionValidAnswer(questionId, five);
                break;
            }
            case 6:{
                insertOptionValidAnswer(questionId, six);
                break;
            }
            default: {
                log.debug("{}指定的正确选项不合法",questionId);
                throw new BusinessException(ExceptionInfoEnum.EXAM_IMPORT_ANSWER_IS_NULL_ERROR);
            }
        }
    }



    /**
     * 校验导入数据合法性
     * @param file
     * @return
     */
    private boolean validateImportData(File file) {
        Workbook wb = null;
        try
        {
            if (ExcelUtil.isExcel2007(file.getPath())) {
                wb = new XSSFWorkbook(new FileInputStream(file));
            } else {
                wb = new HSSFWorkbook(new FileInputStream(file));
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();

            return false;
        }

        Sheet sheet = wb.getSheetAt(0);//获取第一张表
        for (int i = 1; i < sheet.getLastRowNum(); i++)
        {
            Row row = sheet.getRow(i);//获取索引为i的行，以0开始
            String name= row.getCell(0).getStringCellValue(); //试题名称
            if (StringUtils.isBlank(name)) {
                log.error("试题名称为空");
                throw new BusinessException(ExceptionInfoEnum.EXAM_IMPORT_NAME_BLANK_ERROR);
            }

            //考试时长校验
            String duration = row.getCell(5).getStringCellValue();
            try {
                Integer.parseInt(duration);
            } catch (NumberFormatException ne) {
                log.error("导入试题时长不合法");
                throw new BusinessException(ExceptionInfoEnum.EXAM_IMPORT_NAME_BLANK_ERROR);
            }

            //考试时间校验
            String publishTime = row.getCell(3).getStringCellValue();
            try {
                //inputDate = "2010-01-04 01:32:27 UTC";
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(publishTime);
            } catch (Exception e) {
                log.error("导入试题时间格式错误");
                throw new BusinessException(ExceptionInfoEnum.EXAM_IMPORT_NAME_BLANK_ERROR);
            }
            String deadline = row.getCell(4).getStringCellValue();
            try {
                //inputDate = "2010-01-04 01:32:27 UTC";
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(deadline);
            } catch (Exception e) {
                log.error("导入试题时间格式错误");
                throw new BusinessException(ExceptionInfoEnum.EXAM_IMPORT_DEADLINE_INVALID_ERROR);
            }
        }
        try
        {
            wb.close();
        }
        catch (IOException e)
        {
            log.error(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 判断并插入选择题正确选项
     * @param questionId
     * @param num
     */
    private void insertOptionValidAnswer(String questionId, String num) {
        if (StringUtils.isBlank(num)){
            log.debug("正确答案对应的选项不存在");
            throw new BusinessException(ExceptionInfoEnum.EXAM_IMPORT_ANSWER_IS_NULL_ERROR);
        }
        TbValidAnswer tbValidAnswer = new TbValidAnswer();
        tbValidAnswer.setOptionId(num);
        tbValidAnswer.setQuestionId(questionId);
        tbValidAnswerMapper.insert(tbValidAnswer);
    }

    private String judgeInsertOption(String optionText,String questionId,int optionNum){
            String optionId = UUID.randomUUID().toString();
            TbOption tbOption = new TbOption();
            tbOption.setQuestionId(questionId);
            tbOption.setOptionText(optionText);
            tbOption.setOptionId(optionId);
            tbOption.setOrderNum(optionNum);
            tbOptionMapper.insert(tbOption);
            return optionId;
    }

    /**
     * 获得单次考试任务成绩和试题
     * @return
     */
    @Transactional(readOnly = true,rollbackFor = Exception.class)
    public SingleScoreAndQuestionBo getSingleScore(String examId,String userId){
        if(StringUtils.isBlank(examId)){
            log.debug("【获取考试成绩以及试题】:ExamID不合法");
            throw new BusinessException(ExceptionInfoEnum.ID_ILLEGAL_ERROR);
        }
        TbExam tbExam = tbExamMapper.selectByExamId(examId);
        if (tbExam==null){
            log.debug("【获取考试成绩以及试题】:ExamID不合法");
            throw new BusinessException(ExceptionInfoEnum.ID_ILLEGAL_ERROR);
        }
        // 查询分数
        TbUserExam tbUserExam = tbUserExamMapper.selectByExamIdAndUserId(examId,userId);
        // 查询试题所有的非选择题（判断题1、简单题2）
        List<UserQuestionAndAnswerValueBo> tFQuestionAndAnswerValue = tbQuestionMapper.findQuestionAndAnswerValue(examId, userId,1);
        List<UserQuestionAndAnswerValueBo> textQuestionAndAnswerValue = tbQuestionMapper.findQuestionAndAnswerValue(examId, userId,2);
        // 查询选择题及其选项
        List<OptionQuestionAndOptionBo> options = tbQuestionMapper.findOptionQuestionAndOption(examId);
        for (OptionQuestionAndOptionBo option : options) {
            List<TbOption> byUserIdAndQuestionId = tbOptionMapper.findByUserIdAndQuestionId(userId,option.getQuestionId());
            Float userScore = tbUserAnswerMapper.findSingleUserScore(userId,option.getQuestionId());
            option.setUserAnswers(byUserIdAndQuestionId);
            option.setUserScore(userScore == null ? 0: userScore);
        }
        SingleScoreAndQuestionBo singleScoreAndQuestionBo = new SingleScoreAndQuestionBo();
        singleScoreAndQuestionBo.setOptions(options);
        singleScoreAndQuestionBo.setExam(tbExam);
        singleScoreAndQuestionBo.setTFQuestions(tFQuestionAndAnswerValue);
        singleScoreAndQuestionBo.setTextQuestions(textQuestionAndAnswerValue);
        singleScoreAndQuestionBo.setScore(tbUserExam == null || tbUserExam.getScore()==null ? -1 : tbUserExam.getScore());
        return singleScoreAndQuestionBo;
    }

    /**
     * 个人所有考试成绩和试题
     * @return
     */
    @Transactional(readOnly = true,rollbackFor = Exception.class)
    public List<SingleScoreAndQuestionBo> getScores(String userId, String examName, String creator, Date minTime, Date maxTime){
        // 仅仅只是查询出该用户的所有考试分数和试题（没有进行筛选、排序）
        List<SingleScoreAndQuestionBo> singleScoreAndQuestionBos= new ArrayList<>();
        List<TbUserExam> byUserId = tbUserExamMapper.findByUserId(userId);
        for (TbUserExam tbUserExam : byUserId) {
            singleScoreAndQuestionBos.add(getSingleScore(tbUserExam.getExamId(),tbUserExam.getUserId()));
        }
        return singleScoreAndQuestionBos;
    }

    /**
     * 查找所有考试（基本信息,不包括试题）
     * @return
     */
    public PageInfo<TbExamPagerBo> findExamAll(Integer pageNum,Integer pageSize,String examName) {
        //pageNum：当前页数   pageSize：当前页需要显示的数量
        if (pageNum == null ) {
            pageNum = new Integer(0);
        }
        if (pageSize == null ) {
            pageSize = new Integer(0);
        }
        PageHelper.startPage(pageNum, pageSize);
        List<TbExamPagerBo> all = tbExamMapper.findAllFilterExamName(examName);
        List<TbExamPagerBo> result = all.stream().map(m -> {
            Date now = new Date();
            String status;
            if (m.getPublishTime() == null) {
                status = "未开始";
            } else if (m.getPublishTime() != null && m.getDeadLine() == null) {
                status = now.before(m.getPublishTime()) ? "未开始" : "进行中";
            } else {
                status = now.before(m.getPublishTime()) ? "未开始" : (now.after(m.getDeadLine()) ? "已结束" : "进行中");
            }
            m.setStatus(status);
            return m;
        }).collect(toList());

        PageInfo<TbExamPagerBo> pageInfo = new PageInfo<>(all);
        pageInfo.setList(result);
        return pageInfo;
    }



    /**
     * 更新考试的发布时间与截至时间
     * @param param
     */
   /* @Transactional(rollbackFor = Exception.class)
    public void updatePublishAndDeadLine(ExamIdAndPublishAndDeadLineParam param){
        if (param==null || StringUtils.isBlank(param.getExamId())){
            log.debug("ExamId不合法");
            throw new BusinessException(ExceptionInfoEnum.ID_ILLEGAL_ERROR);
        }
        Date publishTime = param.getPublishTime();
        Date deadLine = param.getDeadLine();
        if (publishTime==null || deadLine==null){
            log.debug("考试发布时间和截至时间不能为空");
            throw new BusinessException(ExceptionInfoEnum.TIME_NULL_ERROR);
        }
        TbExam proto= tbExamMapper.selectByExamId(param.getExamId());
        if (proto.getPublishTime().before(new Date())){
            log.debug("考试已经发布，不能修改发布时间");
            throw new BusinessException(ExceptionInfoEnum.PUBLISH_TIME_BEFORE_NOW_ERROR);
        }
        if (publishTime.after(deadLine)){
            log.debug("发布时间不能在截至时间之后");
            throw new BusinessException(ExceptionInfoEnum.PUBLISH_TIME_AFTER_DEADLINE_ERROR);
        }
        TbExam exam = new TbExam(param.getExamId(),param.getPublishTime(),param.getDeadLine());
        tbExamMapper.updateByExam(exam);
    }*/


    /**
     * 获取考试的详情（题目、选项）
     * 两种用户：
     *     1内部用户（可是否有权限可查看，根据时候有考试可参考，可能存在暂时考试，获取之前暂停时保存的数据）
     *     2扫码用户（仅仅可以参考）
     * 根据type区分内部用户是查看还是参考
     * @param examId
     * @param token
     * @param type
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Object getExamDetail(String examId, String token, int type){
        ExamDetailBo examDetailBo = new ExamDetailBo();
        boolean isDisposable = "uname".equals(StringUtils.substring(token, 0, 4));
        TbExam tbExam = tbExamMapper.selectByExamId(examId);
        if (tbExam==null){
            log.debug("ID不合法,该考试ID不存在");
            throw new BusinessException(ExceptionInfoEnum.ID_ILLEGAL_ERROR);
        }
        if (!isDisposable){
            // 内部用户处理逻辑
            SystemUser systemUser = JwtUtils.parseJWT(token, SystemUser.class, secret);
            String userId = systemUser.getUserId();
            Integer code = RoleTypeEnum.ADMIN_USER.getCode();
            // 是否是管理员
            int isRoleByUserIdAndRoleType = userMapper.findIsRoleByUserIdAndRoleType(userId,code);
            // 是否是该考试的阅卷老师
            boolean equals = systemUser.getUsername().equals(tbExam.getTeacherUserName());
            // 是否在考试范围中
            // 是否有分配到考试
            TbUserExam isHasFutureExam = tbExamMapper.findIsHasFutureExam(examId, systemUser.getUserId());
            boolean time = false;
            boolean b = isRoleByUserIdAndRoleType == 0 && !equals;
            if (b){
                if (isHasFutureExam == null){
                    log.debug("无权限或者不在考试时间内");
                    throw new BusinessException(ExceptionInfoEnum.NO_PRIVILEGE_OR_NO_TIME_RANGE);
                }
                if (tbExam.getPublishTime() != null && tbExam.getDeadLine() != null) {
                    time = new Date().after(tbExam.getPublishTime()) && new Date().before(tbExam.getDeadLine());
                }
                if (tbExam.getPublishTime() == null) {
                    time = true;
                }
                if (tbExam.getPublishTime() != null && tbExam.getDeadLine() == null) {
                    time = new Date().after(tbExam.getPublishTime());
                }
                if (!time){
                    log.debug("无权限或者不在考试时间内");
                    throw new BusinessException(ExceptionInfoEnum.NO_PRIVILEGE_OR_NO_TIME_RANGE);
                }
            }
//        记录开始考试的时间
            if (type!=1){
                if (isHasFutureExam == null){
                    log.debug("无权限或者不在考试时间内");
                    throw new BusinessException(ExceptionInfoEnum.NO_PRIVILEGE_OR_NO_TIME_RANGE);
                }
                if (isHasFutureExam.getSuspendTime()!= null ){
                    log.debug("{}继续考试{}",isHasFutureExam.getUserId(),isHasFutureExam.getExamId());
                    SuspendExamBo suspendExamBo = new SuspendExamBo();
                    suspendExamBo.setExam(tbExamMapper.selectByExamId(examId));
                    suspendExamBo.setSuspendTime(isHasFutureExam.getSuspendTime());
                    List<UserQuestionAndAnswerValueBo> tFQuestionAndAnswerValue = tbQuestionMapper.findQuestionAndAnswerValue(examId, userId,1);
                    suspendExamBo.setQuestions(tFQuestionAndAnswerValue);
                    List<UserQuestionAndAnswerValueBo> textQuestionAndAnswerValue = tbQuestionMapper.findQuestionAndAnswerValue(examId, userId,2);
                    suspendExamBo.setTextQuestions(textQuestionAndAnswerValue);
                    // 查询选择题及其选项
                    List<OptionQuestionAndOptionBo> options = tbQuestionMapper.findOptionQuestionAndOption(examId);
                    for (OptionQuestionAndOptionBo option : options) {
                        List<TbOption> byUserIdAndQuestionId = tbOptionMapper.findByUserIdAndQuestionId(userId,option.getQuestionId());
                        option.setUserAnswers(byUserIdAndQuestionId);
                        option.setUserScore(0f);
                    }
                    suspendExamBo.setOptionQuestionAndOptionBos(options);
                    return suspendExamBo;
                }
                log.debug("记录{}，{}的开始考试时间{}",systemUser.getUsername(),examId,new Date());
                tbUserExamMapper.updateStartExamTime(systemUser.getUserId(),examId,new Date());
            }
        }
        List<OptionQuestionAndOptionBo> options = tbQuestionMapper.findOptionQuestionAndOption(examId);
        List<TbQuestion> questions = tbQuestionMapper.findByExamId(examId);
        List<TbQuestion> textQuestions = new ArrayList<>();
        // 将简单题与判断题分开
        for (TbQuestion question : questions) {
            if (question.getQuestionType()==2){
                textQuestions.add(question);
            }
        }
        if (textQuestions.size()>0){
            questions.removeAll(textQuestions);
        }
        examDetailBo.setExam(tbExam);
        examDetailBo.setOptionQuestionAndOptionBos(options);
        examDetailBo.setQuestions(questions);
        examDetailBo.setTextQuestions(textQuestions);
        return examDetailBo;
    }

    /**
     * 删除一场考试(根据时间判断能否被删除)
     * @param examId
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteExamById(String examId) {
        if (StringUtils.isBlank(examId)){
            log.debug("ExamId不合法");
            throw new BusinessException(ExceptionInfoEnum.ID_ILLEGAL_ERROR);
        }
        TbExam tbExam = tbExamMapper.selectByExamId(examId);
        // 判断当前时间是否在试卷的发布时间之前
        if (tbExam.getPublishTime() != null && new Date().after(tbExam.getPublishTime())){
            log.debug("【删除试卷】:该试卷已经被发布不能删除");
            throw new BusinessException(ExceptionInfoEnum.NOW_AFTER_PUBLISH_TIME_ERROR);
        }else {
            List<TbQuestion> byExamId = tbQuestionMapper.findAllByExamId(examId);
            // 删除考试信息
            tbExamMapper.deleteByExamId(examId);
            // 删除考试对应的问题
            tbQuestionMapper.deleteExamId(examId);
            for (TbQuestion tbQuestion : byExamId) {
                if (tbQuestion.getQuestionType()==0 ||tbQuestion.getQuestionType()==-1){
                    // 删除问题的选项
                    tbOptionMapper.deleteByQuestionId(tbQuestion.getQuestionId());
                }
                // 删除正确答案
                tbValidAnswerMapper.deleteByQuestionId(tbQuestion.getQuestionId());
            }
        }
    }


    /**
     * 查找考试列表
     * 按时间范围，考试任务名称，出题人等参数筛选
     * @param param 接受用户ID以及相关过滤条件
     * @param examStatus 考试状态（已完成、未完成）
     * @return
     */
    public PageInfo<SingleExamAndScoreBo> getUserExamList(UserIdAndTimeAndExamNameAndCreatorParam param, int examStatus, String token) throws Exception {
        if (param == null) {
            log.debug("请求参数不合法");
            throw new BusinessException(ExceptionInfoEnum.PARAM_IS_NULL_ERROR);
        }
        if (param.getMinTime()!=null && param.getMaxTime()!=null){
            if (param.getMinTime().after(param.getMaxTime())){
                log.debug("筛选时间不合法");
                param.setMinTime(param.getMaxTime());
                param.setMaxTime(null);
            }
        }
        String userId = JwtUtils.parseJWT(token,SystemUser.class,secret).getUserId();
        param.setUserId(userId);

        Integer pageNum = param.getPageNum();
        Integer pageSize = param.getPageSize();
        if (pageNum == null || pageSize ==null){
            PageHelper.startPage(0,0);
        }else {
            PageHelper.startPage(pageNum, pageSize);
        }
        List<SingleExamAndScoreBo> examAndUserScore = tbExamMapper.findFilterExamAndUserScore(param, examStatus, new Date());
        List<SingleExamAndScoreBo> collect = examAndUserScore.stream().map(e -> {
            if (e.getSuspendTime() == null) {
                    e.setSurplus(Double.valueOf(e.getDuration()));
            }
            return e;
        }).collect(toList());
        PageInfo<SingleExamAndScoreBo> bosPageInfo = new PageInfo<>(examAndUserScore);
        bosPageInfo.setList(collect);
        return bosPageInfo;
    }

    /**
     * 创建该考试的二维码
     * @param examId
     * @return
     */
    public BufferedImage getImage(String examId){
        if (StringUtils.isBlank(examId)|| tbExamMapper.selectByExamId(examId)==null){
            log.debug("ExamId不合法");
            throw new BusinessException(ExceptionInfoEnum.ID_ILLEGAL_ERROR);
        }
        StringBuffer url = new StringBuffer(basePath);
        url.append(examId);
        BufferedImage image;
        try {
            image = QrCodeUtils.createImage(url.toString(), logoPath, true);
        }catch (Exception e){
            log.debug("{},二维码生成错误",examId);
            throw new BusinessException(ExceptionInfoEnum.EXAM_QR_CODE_CREATE_ERROR);
        }
        return image;
    }

    /**
     * 用户提交答卷
     * @param answer 用户答卷
     */
    @Transactional(rollbackFor = Exception.class)
    public void commitAnswer(UserAnswerBo answer,TbReview tbReview, String token, int commitType) throws Exception {
        if (StringUtils.isBlank(answer.getExamId())){
            log.debug("考试ID为空");
            throw new BusinessException(ExceptionInfoEnum.BLANK_EXAM_ID);
        }
        // 校验评价分数
        tbReview.isValid();
        // 是否存在该内部考试
        int examByIdAndInner = tbExamMapper.findExamByIdAndType(answer.getExamId(),0);
        // 是否存在该培训考试
        int examById = tbExamMapper.findExamByIdAndType(answer.getExamId(),1);
        TbUserExam isHasFutureExam = tbExamMapper.findIsHasFutureExam(answer.getExamId(), answer.getUserId());
        int count = tbUserExamMapper.checkDone(answer.getUserId(), answer.getExamId());

        //如果已经回答过，则不允许重复提交
        if (count > 0 && examByIdAndInner > 0) {
            throw new BusinessException(ExceptionInfoEnum.EXAM_ALREADY_TAKEN);
        }

        if (isHasFutureExam== null && examByIdAndInner > 0) {
            log.debug("没有参加考试的权利");
            throw new BusinessException(ExceptionInfoEnum.NO_PRIVILEGE_OR_NO_TIME_RANGE);
        }

        //如果已经超时，则不允许提交
        TbExam exam = tbExamMapper.selectByExamId(answer.getExamId());
        if (examByIdAndInner > 0 || (examById > 0 && answer.getUserType() == 1)){
            TbUserExam userExam = tbUserExamMapper.findByUserIdAndExamId(answer.getUserId(), answer.getExamId());
            if (userExam.getSuspendTime()==null){
                if (new Date().after(new Date(userExam.getStartExamTime().getTime() + exam.getDuration() * 60 * 1000))) {
                    //考试超时
                    if (commitType == 0) {
                        //自动提交，不允许
                        throw new BusinessException(ExceptionInfoEnum.OVER_TIME_ERROR);
                    }
                }
            }
        }else {
            // 扫码用户超时判断
            TbDisposableExamInfo byDisUserName = tbDisposableExamInfoMapper.findByDisUserName(answer.getUserId());
            if (new Date().after(new Date(byDisUserName.getStartExamTime().getTime() + exam.getDuration() * 60 * 1000))){
                if (commitType == 0) {
                    throw new BusinessException(ExceptionInfoEnum.OVER_TIME_ERROR);
                }
            }
        }

        //获取所有正确答案
        List<UserOptionBo> validAnswers = tbUserExamMapper.getAllValidAnswers(answer.getExamId());

        List<TbUserAnswer> result = new ArrayList<>();

        double sum = 0;
        boolean isNullTextAnswer = true;
        for (UserOptionBo validAnswer:validAnswers) {
            for (UserOptionBo userAnswer: answer.getAnswers()) {
                if (userAnswer.getQuestionId().toLowerCase().equals(validAnswer.getQuestionId().toLowerCase())) {
                    QuestionType questionType = QuestionType.getByCode(validAnswer.getQuestionType());
                    if (questionType == QuestionType.MCQ || questionType == QuestionType.MMCQ) {
                        //选择题
                        boolean valid = true;
                        int validCount = 0;
                        List<String> optionIds = validAnswer.getOptionIds()
                                .stream()
                                .map(String::toLowerCase).collect(toList());
                        for (String userOptionId : userAnswer.getOptionIds()) {
                            if (!optionIds.contains(userOptionId.toLowerCase())) {
                                valid = false;
                            } else {
                                validCount += 1;
                            }
                        }

                        if (valid) {
                            //正确
                            if (validCount == validAnswer.getOptionIds().size()) {
                                //全部选项都正确
                                sum+=validAnswer.getQuestionScore();
                                result.addAll(buildMCQAnswers(answer.getUserId(), userAnswer.getQuestionId(),
                                        userAnswer.getOptionIds(), validAnswer.getQuestionScore()));
                            }else {
                                result.addAll(buildMCQAnswers(answer.getUserId(), userAnswer.getQuestionId(),
                                        userAnswer.getOptionIds(), 0f));
                            }
                        } else {
                            result.addAll(buildMCQAnswers(answer.getUserId(), userAnswer.getQuestionId(),
                                    userAnswer.getOptionIds(), 0f));
                        }
                            /*else {
                                //部分选项正确
                                result.addAll(buildMCQAnswers(answer.getUserId(), userAnswer.getQuestionId(),
                                        userAnswer.getOptionIds(), validAnswer.getQuestionScore() / 2));
                            }
                        } else {
                            //不正确
                            result.addAll(buildMCQAnswers(answer.getUserId(), userAnswer.getQuestionId(),
                                    userAnswer.getOptionIds(), 0f));
                        }*/
                    } else if (questionType == QuestionType.YNQ) {
                        // 判断题
                        TbUserAnswer option = new TbUserAnswer();
                        option.setQuestionId(userAnswer.getQuestionId());
                        option.setAnswerValue(userAnswer.getOptionText());

                        if (userAnswer.getOptionText().equals(validAnswer.getOptionText())) {
                            sum+=validAnswer.getQuestionScore();
                            option.setUserScore(validAnswer.getQuestionScore());
                        } else {
                            option.setUserScore(0f);
                        }
                        result.add(option);
                    } else if (questionType == QuestionType.SAQ) {
                        //简答题
                        // 判断用户所有简单题是否全未作答
                        isNullTextAnswer = false;
                        TbUserAnswer option = new TbUserAnswer();
                        option.setQuestionId(userAnswer.getQuestionId());
                        option.setAnswerValue(userAnswer.getOptionText());
                        option.setUserScore(null);
                        result.add(option);
                    }
                }
            }
        }
        // 内部考试处理逻辑
        if (examByIdAndInner > 0 || (examById > 0 && answer.getUserType() == 1)){
            tbUserExamMapper.commitAnswer(answer.getExamId(), answer.getUserId(), result, new Date());
            // 如果考试有暂停，删除之前保存的数据
            if (isHasFutureExam!=null && isHasFutureExam.getSuspendTime()!= null){
                log.debug("清空暂停保存的用户数据");
                tbUserAnswerMapper.deleteByUserIdAndQuestionIds(answer.getUserId(),answer.getExamId());
            }
            Integer questionNum = tbQuestionMapper.findHasQuestionByExamIdAndQuestionType(answer.getExamId(), 2);
            if (questionNum == null || questionNum < 1 || isNullTextAnswer){
                // 判断如果只有填空选择，将总分给更新了
                tbUserExamMapper.updateScore(answer.getExamId(),answer.getUserId(),sum);
            }
        }else {
            // 扫码用户处理(不需要保存用户作答，只需要保存总分)
            tbDisposableExamInfoMapper.updateDisposableScoreByExamId(answer.getUserId(),answer.getExamId(),sum);
        }
        // 插入评价
        this.insertReview(tbReview);
    }

    /**
     * 待我参考（查询一些简单的考试信息、分数，并没有具体的试题）
     * @param userId
     * @return
     */
    public List<SingleExamAndScoreBo> underway(String userId){
        if (StringUtils.isBlank(userId)){
            log.debug("用户ID不合法");
            throw new BusinessException(ExceptionInfoEnum.ID_ILLEGAL_ERROR);
        }
        List<SingleExamAndScoreBo> examAndUserScore = tbExamMapper.findExamAndUserScore(userId, 0);
        return examAndUserScore==null?Collections.EMPTY_LIST:examAndUserScore;
    }

    /**
     * 阅卷老师批改试卷
     * @param param
     */
    @Transactional(rollbackFor = Exception.class)
    public void markPapers(MarkPapersParam param) {
        // 判断试卷是否以及被修改
        int isScore = tbUserExamMapper.findIsScore(param.getExamId(),param.getUserId());
        if (isScore==0){
            log.debug("试题已被修改");
            throw new BusinessException(ExceptionInfoEnum.EXAM_REPEAT_UPDATE_ERROR);
        }
        // 直接取选择题,判断题的得分
        List<Float> userScores = tbUserAnswerMapper.findUserScore(param.getExamId(),param.getUserId());
        //查询的选择、判断题总分
        Float  reduce = userScores.stream().reduce(new Float(0),(x,y)->x+y);
        List<MarkPapersParam.QuestionIdAndScore> questionIdAndScores = param.getQuestionIdAndScores();
        // 简单题总分
        Double  textReduce =  questionIdAndScores.stream().mapToDouble(MarkPapersParam.QuestionIdAndScore::getScore).sum();
        // 问答题（人工改卷将每道题id，用户id以及每道题得分传过来，用户回答中添加一个用户得分字段，更新字段）
        ArrayList<TbUserAnswer> scores = new ArrayList<>();
        for (MarkPapersParam.QuestionIdAndScore questionIdAndScore : questionIdAndScores) {
            TbUserAnswer tbUserAnswer = new TbUserAnswer(param.getUserId(),questionIdAndScore.getQuestionId(),questionIdAndScore.getScore());
            scores.add(tbUserAnswer);
        }
        tbUserAnswerMapper.updateBatchByUserIdAndQuestionId(scores);
        // 更新用户考试总分
        tbUserExamMapper.updateScore(param.getExamId(),param.getUserId(), (float) (reduce+textReduce));
    }

    /**
     * 提供改卷的简单题以及用户的回答
     * @param examId
     * @param userId
     * @return
     */
    public Map<String, Object> getUserTextOptionAndAnswer(String examId, String userId) {
        if (StringUtils.isBlank(examId) || StringUtils.isBlank(userId)){
            log.debug("ID不能为空");
            throw new BusinessException(ExceptionInfoEnum.ID_ILLEGAL_ERROR);
        }
        int isFinish = tbUserExamMapper.findIsFinish(examId,userId);
        if (isFinish==0){
            log.debug("考试还未完成");
            throw new BusinessException(ExceptionInfoEnum.EXAM_IS_NOT_FINISH_ERROR);
        }
        int isScore = tbUserExamMapper.findIsScore(examId,userId);
        if (isScore==0){
            log.debug("试题已被修改");
            throw new BusinessException(ExceptionInfoEnum.EXAM_REPEAT_UPDATE_ERROR);
        }
        List<UserQuestionAndAnswerValueBo> textQuestionAndAnswerValue = tbQuestionMapper.findQuestionAndAnswerValue(examId, userId,2);
        TbExam tbExam = tbExamMapper.selectByExamId(examId);
        SystemUser one = userMapper.getOne(userId);
        one.setPassword(null);
        HashMap<String, Object> stringObjectHashMap = new HashMap<>(16);
        stringObjectHashMap.put("textQuestions",textQuestionAndAnswerValue);
        stringObjectHashMap.put("exam",tbExam);
        stringObjectHashMap.put("user",one);
        return stringObjectHashMap;
    }

    /**
     * 组合用户答案
     * @param questionId 问题id
     * @param optionIds 用户回答的选项id
     * @param userScore 用户的分数
     * @return 构造列表返回
     */
    private List<TbUserAnswer> buildMCQAnswers(String userId, String questionId, List<String> optionIds, float userScore) {
        List<TbUserAnswer> result = new ArrayList<>();
        for(int i = 0; i < optionIds.size(); i++) {
            TbUserAnswer option = new TbUserAnswer();
            option.setQuestionId(questionId);
            option.setUserScore(i == 0 ? userScore : null);
            option.setOptionId(optionIds.get(i));
            option.setUserId(userId);
            result.add(option);
        }

        return result;
    }

    /**
     * 每场考试的成绩统计（该场考试的平均分）
     */
    @Transactional(rollbackFor = Exception.class)
    public PageInfo<TbExamAndAverageBo> getTbExamAndAvg(PageInfoAndUserNameAndExamNameParam param){

        if (param.getPageNum()==null || param.getPageSize() == null){
            PageHelper.startPage(0,0);
        }else {
            PageHelper.startPage(param.getPageNum(),param.getPageSize());
        }
        tbUserExamMapper.updateScoreByOvertime(new Date());
       List<TbExamAndAverageBo> tbExamAndAverageBos = tbExamMapper.findExamAndAverage(param.getExamName());
       PageInfo<TbExamAndAverageBo> tbExamAndAverageBoPageInfo = new PageInfo<>(tbExamAndAverageBos);
       return tbExamAndAverageBoPageInfo;
    }

    /**
     * 成绩统计（所有人的所有考试信息）
     */
    public PageInfo<TbExamAndUserInfoAndDepartInfoBo> getAllExamInfo(PageInfoAndUserNameAndExamNameParam param){
        if (param.getPageNum()==null || param.getPageSize() == null){
            PageHelper.startPage(0,0);
        }else {
            PageHelper.startPage(param.getPageNum(),param.getPageSize());
        }
        List<TbExamAndUserInfoAndDepartInfoBo> tbExamAndUserInfoAndDepartInfoBos = tbExamMapper.findAllExamInfoAndUserAndDepartmentAndScore(param);
        PageInfo<TbExamAndUserInfoAndDepartInfoBo> tbExamAndUserInfoAndDepartInfoBoPageInfo = new PageInfo<>(tbExamAndUserInfoAndDepartInfoBos);
        return tbExamAndUserInfoAndDepartInfoBoPageInfo;
    }

    /**
     * 单次考试的所有考生
     * @param examId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public List<SingleExamAndScoreAndUserInfo > getExamAllUserInfoAndScore(String examId){
        // 将考试时间已经截止的考生全赋予零分,并且修改考试状态
        tbUserExamMapper.updateScoreByOvertime(new Date());
        List<SingleExamAndScoreAndUserInfo > singleExamAndScoreBos = tbExamMapper.getExamAllUserInfoAndScore(examId);
        return singleExamAndScoreBos;
    }

    /**
     * 单次考试的所有考生-用于编辑考试信息-考生树
     * @param examId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public List<ExamUserBo> getExamUsers(String examId) {
        return tbExamMapper.getExamUsers(examId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateExamInfo(UpdateExamInfoVo infos, String token) {
        if (infos == null || StringUtils.isBlank(infos.getExamId())) {
            log.debug("ExamId不合法");
            throw new BusinessException(ExceptionInfoEnum.ID_ILLEGAL_ERROR);
        }

        if (StringUtils.isBlank(infos.getExamName())) {
            log.debug("试题名称不能为空");
            throw new BusinessException(ExceptionInfoEnum.BLANK_EXAM_NAME);
        }

        Date publishTime = infos.getPublishTime();
        Date deadLine = infos.getDeadLine();
        if (publishTime == null || deadLine == null) {
            log.debug("考试发布时间和截至时间不能为空");
            throw new BusinessException(ExceptionInfoEnum.TIME_NULL_ERROR);
        }

        if (infos.getDuration() == null || infos.getDuration() == 0) {
            log.debug("考试时长不能为空");
            throw new BusinessException(ExceptionInfoEnum.BLANK_EXAM_DURATION);
        }
        TbExam proto = tbExamMapper.selectByExamId(infos.getExamId());
        if (publishTime.after(deadLine)) {
            log.debug("发布时间不能在截至时间之后");
            throw new BusinessException(ExceptionInfoEnum.PUBLISH_TIME_AFTER_DEADLINE_ERROR);
        }
        if (proto.getPublishTime() != null && proto.getPublishTime().before(new Date())){
            // 考试已开始处理方式，只添加新增人员，其他信息不会被修改
            log.debug("考试已经发布，只允许增加考试人员");
            Set<String> userIds = infos.getUserIds();
            String examId = infos.getExamId();
            infos = new UpdateExamInfoVo();
            infos.setUserIds(userIds);
            infos.setExamId(examId);
            infos.setSubTitle(proto.getSubTitle());
            infos.setDescription(proto.getDescription());
        }else {
            // 删除相关考试人员以便于添加，未开始处理方式，先删除在添加
            tbUserExamMapper.deleteByExamId(infos.getExamId());
        }
        // 将不会被更新的信息设置为空
        infos.setCreator(null);
        SystemUser systemUser = JwtUtils.parseJWT(token, SystemUser.class, secret);
        infos.setLastEditor(systemUser.getUsername());
        infos.setLastEditTime(new Date());
        infos.setTeacherUserName(null);
        // 更新考试相关基本信息
        tbExamMapper.updateByExam(infos);
        // 添加参考人员
        Set<String> userIdsByExamId = tbUserExamMapper.findUserIdsByExamId(infos.getExamId());
        if (userIdsByExamId!= null && !userIdsByExamId.isEmpty()){
            Set<String> collect = infos.getUserIds().stream().filter(u -> !userIdsByExamId.contains(u)).collect(Collectors.toSet());
            infos.setUserIds(collect);
        }
        for (String userId : infos.getUserIds()) {
            TbUserExam tbUserExam = new TbUserExam();
            tbUserExam.setUserId(userId);
            tbUserExam.setExamStatus(0);
            tbUserExam.setExamId(infos.getExamId());
            tbUserExamMapper.insert(tbUserExam);
        }
    }

    /**
     * 查询所有待阅卷的试卷
     * @return
     */
    public PageInfo<TbExamAndUserInfoBo> futureMarkPapersExam(Integer pageNum,Integer pageSize,String examName,String userName,String token) throws Exception {
        if (pageNum==null){
            pageNum = 0;
        }
        if (pageSize==null){
            pageSize = 0;
        }
        String teacher = JwtUtils.parseJWT(token,SystemUser.class,secret).getUsername();
        PageHelper.startPage(pageNum, pageSize);
        List<TbExamAndUserInfoBo>  tbExamAndUserInfoBos= tbExamMapper.findFutureMarkPapersExam(examName,userName,teacher);
        PageInfo<TbExamAndUserInfoBo> info = new PageInfo<>(tbExamAndUserInfoBos);
        return info;
    }

    /**
     * 用户暂停考试（保存用户回答）
     * @param answer
     * @param token
     */
    @Transactional(rollbackFor = Exception.class)
    public void suspendExam(UserAnswerBo answer, String token) {
        String userId = JwtUtils.parseJWT(token,SystemUser.class,secret).getUserId();
        if (StringUtils.isBlank(answer.getExamId()) || StringUtils.isBlank(answer.getUserId())){
                log.debug("ExamId不合法");
                throw new BusinessException(ExceptionInfoEnum.ID_ILLEGAL_ERROR);
        }
        TbUserExam isHasFutureExam = tbExamMapper.findIsHasFutureExam(answer.getExamId(), userId);
        if (isHasFutureExam !=null){
            // 更新考试的剩余时长
            if (answer.getCommitTime() ==null) {
                log.debug("考试的剩余时长未提交");
            }
            double surplus = answer.getCommitTime()/60.0;
            DecimalFormat decimalFormat = new DecimalFormat("#0.0");
            String format = decimalFormat.format(surplus);
            tbUserExamMapper.updateSurplus(Double.valueOf(format),answer.getExamId(),userId);
            // 更新暂停时间
            tbUserExamMapper.updateSuspendTime(new Date(),answer.getExamId(),userId);
            // 保存用户回答
            List<TbUserAnswer> collect = new ArrayList<>();
            for (UserOptionBo answerAnswer : answer.getAnswers()) {
                if (answerAnswer.getOptionIds() != null || StringUtils.isNotBlank(answerAnswer.getOptionText())) {
                if (answerAnswer.getOptionIds() != null){
                    for (String optionId : answerAnswer.getOptionIds()) {
                        TbUserAnswer tbUserAnswer = new TbUserAnswer(userId, optionId, null, answerAnswer.getQuestionId());
                        collect.add(tbUserAnswer);
                    }
                }
                if (StringUtils.isNotBlank(answerAnswer.getOptionText()) && StringUtils.isNotBlank(answerAnswer.getOptionText().trim())){
                    TbUserAnswer tbUserAnswer = new TbUserAnswer(userId, null, answerAnswer.getOptionText(), answerAnswer.getQuestionId());
                    collect.add(tbUserAnswer);
                } }
            }
            // 清空保存过得暂停数据记录
            tbUserAnswerMapper.deleteByUserIdAndQuestionIds(userId,answer.getExamId());
            // 保存
            tbUserAnswerMapper.insertBatch(answer.getExamId(),userId, collect);
        }
    }

    private void insertReview(TbReview review) {

        review.setReviewId(UUID.randomUUID().toString());
        //检查是否重复写入
        int countByUserId = reviewMapper.getCountByExamIdAndUserId(review.getExamId(), review.getUserId(), null);
        int countBydisName = reviewMapper.getCountByExamIdAndUserId(review.getExamId(), null, review.getDisposableUsername());
        if (countByUserId > 0 || countBydisName > 0) {
            throw new BusinessException(ExceptionInfoEnum.DUPLICATE_INSERT);
        }

        int totalNum = review.getAnswerOnQuestion() + review.getExperience()
                + review.getHandoutQuality() + review.getHelpful()
                + review.getInteraction() + review.getLogic()
                + review.getSensible() + review.getPronunciation()
                + review.getUnderstandable() + review.getWellPrepared();

        if (review.getTotalScore() != totalNum) {
            review.setTotalScore(totalNum);
        }

        reviewMapper.insert(review);
    }
}

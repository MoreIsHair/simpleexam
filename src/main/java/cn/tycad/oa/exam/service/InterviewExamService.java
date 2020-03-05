package cn.tycad.oa.exam.service;

import cn.tycad.oa.exam.common.enums.InterviewQuestionFollowEnum;
import cn.tycad.oa.exam.common.enums.QuestionType;
import cn.tycad.oa.exam.common.util.JwtUtils;
import cn.tycad.oa.exam.excel.ExcelInterviewQuestionInfo;
import cn.tycad.oa.exam.excel.ExcelUtil;
import cn.tycad.oa.exam.exception.BusinessException;
import cn.tycad.oa.exam.common.enums.ExceptionInfoEnum;
import cn.tycad.oa.exam.model.bo.*;
import cn.tycad.oa.exam.model.entity.*;
import cn.tycad.oa.exam.model.param.QuestionIdsAndTemplateInfoParam;
import cn.tycad.oa.exam.repository.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * @author  YY
 * @date 2019/8/7
 * @description
 */
@Service
@Slf4j
public class InterviewExamService {

    @Autowired
    private TbDisposableExamInfoMapper tbDisposableExamInfoMapper;

    @Autowired
    private TbInterviewQuestionMapper tbInterviewQuestionMapper;

    @Autowired
    private TbInterviewTemplateMapper tbInterviewTemplateMapper;

    @Autowired
    private TbInterviewTemplateQuestionMapper tbInterviewTemplateQuestionMapper;

    @Autowired
    private TbValidAnswerMapper tbValidAnswerMapper;

    @Autowired
    private TbOptionMapper tbOptionMapper;

    @Autowired
    private TbUserExamMapper tbUserExamMapper;

    @Autowired
    private SystemUserMapper systemUserMapper;

    @Autowired
    private TbReviewMapper reviewMapper;

    @Autowired
    private TbExamMapper tbExamMapper;

    @Value("${jwt.secret}")
    private String secret;

    /**
     * 导入试题
     * @param dest
     * @throws FileNotFoundException
     */
    @Transactional(rollbackFor = Exception.class)
    public void importQuestion(File dest) throws Exception {
        ExcelUtil.getFileExtension(dest);
        List<Object> objects = ExcelUtil.readExcelWithModel(new BufferedInputStream(new FileInputStream(dest)), 1, ExcelInterviewQuestionInfo.class, true);
        for (Object object : objects) {
            ExcelInterviewQuestionInfo excelInterviewQuestionInfo = (ExcelInterviewQuestionInfo) object;
            if (excelInterviewQuestionInfo.isValid()) {
                // 选择题，需要插入选项
                if (excelInterviewQuestionInfo.getQuestionType() == 0 || excelInterviewQuestionInfo.getQuestionType() == -1) {
                    String questionId = UUID.randomUUID().toString();
                    TbInterviewQuestion tbInterviewQuestion = new TbInterviewQuestion(excelInterviewQuestionInfo, questionId);
                    // 插入问题
                    tbInterviewQuestionMapper.insert(tbInterviewQuestion);
                    // 选项
                    int optionNum = 1;
                    String one = "";
                    String two = "";
                    String three = "";
                    String four = "";
                    String five = "";
                    String six = "";
                    if (StringUtils.isNotBlank(excelInterviewQuestionInfo.getOptionTwo())) {
                        two = this.judgeInsertOption(excelInterviewQuestionInfo.getOptionTwo(), questionId, optionNum++);
                    }
                    if (StringUtils.isNotBlank(excelInterviewQuestionInfo.getOptionOne())) {
                        one = this.judgeInsertOption(excelInterviewQuestionInfo.getOptionOne(), questionId, optionNum++);
                    }
                    if (StringUtils.isNotBlank(excelInterviewQuestionInfo.getOptionFour())) {
                        four = this.judgeInsertOption(excelInterviewQuestionInfo.getOptionFour(), questionId, optionNum++);
                    }
                    if (StringUtils.isNotBlank(excelInterviewQuestionInfo.getOptionThree())) {
                        three = this.judgeInsertOption(excelInterviewQuestionInfo.getOptionThree(), questionId, optionNum++);
                    }
                    if (StringUtils.isNotBlank(excelInterviewQuestionInfo.getOptionSix())) {
                        six = this.judgeInsertOption(excelInterviewQuestionInfo.getOptionSix(), questionId, optionNum++);
                    }
                    if (StringUtils.isNotBlank(excelInterviewQuestionInfo.getOptionFive())) {
                        five = this.judgeInsertOption(excelInterviewQuestionInfo.getOptionFive(), questionId, optionNum++);
                    }
                    // 添加正确答案
                    if (StringUtils.isNotBlank(excelInterviewQuestionInfo.getValidAnswer())) {
                        String validAnswer = excelInterviewQuestionInfo.getValidAnswer();
                        if (validAnswer.contains("，")) {
                            validAnswer = validAnswer.replace("，", ",");
                        }
                        String[] split = validAnswer.split(",");
                        for (String s : split) {
                            switchInsertValidAnswer(questionId, one, two, three, four, five, six, s);
                        }
                    } else {
                        log.debug("选择题未添加正确答案");
                        throw new BusinessException(ExceptionInfoEnum.EXAM_IMPORT_ANSWER_IS_NULL_ERROR);
                    }
                }
                // 判断题
                if (excelInterviewQuestionInfo.getQuestionType() == 1) {
                    String questionId = UUID.randomUUID().toString();
                    TbInterviewQuestion tbInterviewQuestion = new TbInterviewQuestion(excelInterviewQuestionInfo, questionId);
                    // 插入问题
                    tbInterviewQuestionMapper.insert(tbInterviewQuestion);
                    if (StringUtils.isBlank(excelInterviewQuestionInfo.getValidAnswer())) {
                        log.debug("判断题未添加正确答案");
                        throw new BusinessException(ExceptionInfoEnum.EXAM_IMPORT_ANSWER_IS_NULL_ERROR);
                    }
                    TbValidAnswer tbValidAnswer = new TbValidAnswer();
                    HashSet<String> strings = new HashSet<>(Arrays.asList("正确", "错误"));
                    tbValidAnswer.setQuestionId(questionId);
                    strings.add(excelInterviewQuestionInfo.getValidAnswer());
                    if (strings.size() > 2) {
                        log.debug("判断题未添加正确答案或者格式不对");
                        throw new BusinessException(ExceptionInfoEnum.EXAM_IMPORT_ANSWER_IS_NULL_ERROR);
                    }
                    tbValidAnswer.setAnswerValue(excelInterviewQuestionInfo.getValidAnswer());
                    tbValidAnswerMapper.insert(tbValidAnswer);
                }
            }
        }
    }

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
            case 6:{
                insertOptionValidAnswer(questionId, six);
                break;
            }
            case 5:{
                insertOptionValidAnswer(questionId, five);
                break;
            }
            default: {
                log.debug("{}指定的正确选项不合法",questionId);
                throw new BusinessException(ExceptionInfoEnum.EXAM_IMPORT_ANSWER_IS_NULL_ERROR);
            }
        }
    }

    private void insertOptionValidAnswer(String questionId, String num) {
        if (StringUtils.isBlank(num)){
            log.debug("正确答案对应的选项不存在");
            throw new BusinessException(ExceptionInfoEnum.EXAM_IMPORT_ANSWER_IS_NULL_ERROR);
        }
        TbValidAnswer tbValidAnswer = new TbValidAnswer();
        tbValidAnswer.setQuestionId(questionId);
        tbValidAnswer.setOptionId(num);
        tbValidAnswerMapper.insert(tbValidAnswer);
        return;
    }

    public String judgeInsertOption(String optionText,String questionId,int optionNum){
        String optionId = UUID.randomUUID().toString();
        TbOption tbOption = new TbOption();
        tbOption.setOptionId(optionId);
        tbOption.setQuestionId(questionId);
        tbOption.setOptionText(optionText);
        tbOption.setOrderNum(optionNum);
        tbOptionMapper.insert(tbOption);
        return optionId;
    }

    /**
     * 根据所属类型获取问题
     * @param questionFollow
     * @param pageSize
     * @param pageNum
     * @return
     */
    public PageInfo<TbInterviewQuestion> getQuestionList(int questionFollow, Integer pageSize, Integer pageNum){
        if (pageNum==null || pageSize ==null){
            PageHelper.startPage(0,10);
        }else {
            PageHelper.startPage(pageNum,pageSize);
        }
        List<TbInterviewQuestion> allByQuestionFollow = tbInterviewQuestionMapper.findAllByQuestionFollow(questionFollow);
        return new PageInfo<>(allByQuestionFollow);
    }

    /**
     * 拼装一套试题
     * @param param
     */
    @Transactional(rollbackFor = Exception.class)
    public void collectToTemplate(QuestionIdsAndTemplateInfoParam param, String token){
        String createUser = JwtUtils.parseJWT(token,SystemUser.class,secret).getUsername();
        if (!validFollowType(param.getFollowType())){
            throw new BusinessException(ExceptionInfoEnum.INTERVIEW_IMPORT_QUESTION_FOLLOW_NOT_VALID);
        }
        param.setCreateTime(new Date());
        String templateId  = UUID.randomUUID().toString();
        param.setCreateUsername(createUser);
        param.setInterviewTemplateId(templateId);
        // 创建模板
        tbInterviewTemplateMapper.insert(param);
        // 建立模板和问题的关联
        for (QuestionIdsAndTemplateInfoParam.QuestionIdAndOrderNum qos : param.getQos()) {
            TbInterviewTemplateQuestion tbInterviewTemplateQuestion = new TbInterviewTemplateQuestion();
            tbInterviewTemplateQuestion.setInterviewQuestionId(qos.getQuestionId());
            tbInterviewTemplateQuestion.setOrderNum(qos.getOrderNum());
            tbInterviewTemplateQuestion.setInterviewTemplateId(templateId);
            tbInterviewTemplateQuestionMapper.insert(tbInterviewTemplateQuestion);
        }
    }

    /**
     * 判断所属类型是否合格
     * @param o
     * @return
     */
    private boolean validFollowType(int o) {
        Collection<InterviewQuestionFollowEnum> list = InterviewQuestionFollowEnum.map().values();
        Optional<InterviewQuestionFollowEnum> first = list.stream().filter(m -> m.getCode() == o).findFirst();
        if (first.isPresent()) {
            return true;
        }
        return false;
    }

    /**
     * 获取所有该组类型的模板试题
     * @param follow
     * @param pageSize
     * @param pageNum
     * @return
     */
    public PageInfo<TbInterviewTemplate> getTemplateList(int follow ,int pageSize ,int pageNum){
        PageHelper.startPage(pageNum,pageSize);
        List<TbInterviewTemplate> allByFollow = tbInterviewTemplateMapper.findAllByFollow(follow);
        PageInfo<TbInterviewTemplate> listPageInfo = new PageInfo<TbInterviewTemplate>(allByFollow);
        return listPageInfo;

    }

    /**
     * 获取模板（内容）所有题目
     * @param templateId
     * @return
     */
    public InterviewExamContentBo getTemplateContent(String templateId){
        TbInterviewTemplate isExistByTemplateId = tbInterviewTemplateMapper.findIsExistByTemplateId(templateId);
        List<InterviewQuestionAndOptionBo> allOptionQuestionByTemplateId = tbInterviewTemplateQuestionMapper.findAllOptionQuestionByTemplateId(templateId);
        Map<Integer, ArrayList<InterviewQuestionAndOptionBo>> collect = allOptionQuestionByTemplateId.stream().collect(Collectors.toMap(InterviewQuestionAndOptionBo::getInterviewQuestionType, m -> {
            ArrayList<InterviewQuestionAndOptionBo> arrayList = new ArrayList();
            arrayList.add(m);
            return arrayList;
        }, (key1, key2) -> {
            key2.addAll(key1);
            return key2;
        }));
        InterviewExamContentBo interviewExamContentBo = new InterviewExamContentBo();
        for (Map.Entry<Integer, ArrayList<InterviewQuestionAndOptionBo>> entry : collect.entrySet()) {
            if (QuestionType.MCQ.getCode() == entry.getKey()){
                List<TbInterviewQuestion> collect1 = entry.getValue().stream().map(m -> ((TbInterviewQuestion) m)).collect(Collectors.toList());
                interviewExamContentBo.setQuestions(collect1);
            }else {
                interviewExamContentBo.setOptionQuestionAndOptionBos(entry.getValue());
            }
        }
        interviewExamContentBo.setExam(isExistByTemplateId);
        return interviewExamContentBo;
    }

    @Transactional(rollbackFor = Exception.class)
    public void enterDisposableInfo(TbDisposableExamInfo param){
        if (StringUtils.isBlank(param.getDisposableUsername())) {
            throw new BusinessException(ExceptionInfoEnum.USERNAME_NULL);
        }
        String disposableUsername = param.getDisposableUsername();
        SystemUserBo byUserName = systemUserMapper.findByUserName(disposableUsername);
        TbDisposableExamInfo byDisUserName = tbDisposableExamInfoMapper.findByDisUserName(disposableUsername);
        if (byUserName!=null || byDisUserName != null){
            throw new BusinessException(ExceptionInfoEnum.USERNAME_ALREADY_EXIST);
        }
        String disId  = UUID.randomUUID().toString();
        param.setDisposableId(disId);
        param.setFinishExamTime(null);
        param.setSurplus(null);
        param.setSuspendTime(null);
        param.setScore(null);
        param.setStartExamTime(new Date());
        tbDisposableExamInfoMapper.insert(param);
    }

    /**
     * 智能改卷（打分）
     * @param answer
     */
    @Transactional(rollbackFor = Exception.class)
    public  Double playScore(UserAnswerBo answer,TbReview review ){
        //获取所有正确答案
        List<UserOptionBo> validAnswers = tbUserExamMapper.getAllValidAnswers(answer.getExamId());
        double sum = 0;
        for (UserOptionBo validAnswer:validAnswers) {
            for (UserOptionBo userAnswer : answer.getAnswers()) {
                if (userAnswer.getQuestionId().toLowerCase().equals(validAnswer.getQuestionId().toLowerCase())) {
                    QuestionType questionType = QuestionType.getByCode(validAnswer.getQuestionType());
                    if (questionType == QuestionType.MCQ || questionType == QuestionType.MMCQ) {
                        //选择题
                        int validCount = 0;
                        boolean valid = true;
                        List<String> optionIds = validAnswer.getOptionIds().stream().map(o -> o.toLowerCase()).collect(toList());
                        for (String userOptionId : userAnswer.getOptionIds()) {
                            if (!optionIds.contains(userOptionId.toLowerCase())) {
                                valid = false;
                            } else {
                                validCount++;
                            }
                        }
                        if (valid) {
                            //正确
                            if (validCount == validAnswer.getOptionIds().size()) {
                                //全部选项都正确
                                sum += validAnswer.getQuestionScore();
                            }
                        }
                    } else if (questionType == QuestionType.YNQ) {
                        // 判断题
                        if (userAnswer.getOptionText().equals(validAnswer.getOptionText())) {
                            sum += validAnswer.getQuestionScore();
                        }
                    }
                }
            }
        }
        // 更新总分（UserId接收的是实际传的一次性唯一用户名）
        tbDisposableExamInfoMapper.updateDisposableScoreByTemplateId(answer.getUserId(),answer.getExamId(),sum);
        // 写入评价
        review.setReviewId(UUID.randomUUID().toString());
        if (review.getReviewId().isEmpty()) {
            throw new BusinessException(ExceptionInfoEnum.ID_ILLEGAL_ERROR);
        }
        //检查是否重复写入
        if (reviewMapper.getCountByExamIdAndUserId(review.getExamId(),null, review.getUserId()) > 0) {
            throw new BusinessException(ExceptionInfoEnum.DUPLICATE_INSERT);
        }
        int totalNum = review.getAnswerOnQuestion() + review.getExperience()
                + review.getInteraction() + review.getLogic()
                + review.getSensible() + review.getPronunciation()
                + review.getHandoutQuality() + review.getHelpful()
                + review.getUnderstandable() + review.getWellPrepared();

        if (review.getTotalScore() != totalNum) {
            review.setTotalScore(totalNum);
        }
        reviewMapper.insert(review);
        return sum;
    }

    /**
     * 获取扫描用户的基本信息以及总分
     * @param pageSize 每页显示条数
     * @param pageNum 当前页数
     */
    public PageInfo getDisposableExamInfoList(Integer pageSize,Integer pageNum) {
        if (pageNum == null || pageSize == null) {
            PageHelper.startPage(1,10);
        }else {
            PageHelper.startPage(pageNum,pageSize);
        }
        List<TbDisposableExamInfo> all = tbDisposableExamInfoMapper.findAll();
        PageInfo pageInfo = new PageInfo<>(all);
        List<ExamAndDisposableExamInfoBo> collect = all.stream().map(m -> {
            ExamAndDisposableExamInfoBo examAndDisposableExamInfoBo = new ExamAndDisposableExamInfoBo();
            if (m.getExamId() != null){
                TbExam tbExam = tbExamMapper.selectByExamId(m.getExamId());
                examAndDisposableExamInfoBo.setExam(tbExam);
            }
            examAndDisposableExamInfoBo.setDisposableExamInfo(m);
            return examAndDisposableExamInfoBo;
        }).collect(toList());
        pageInfo.setList(collect);
        return pageInfo;
    }
}
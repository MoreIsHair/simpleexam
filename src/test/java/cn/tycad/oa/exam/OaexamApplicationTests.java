package cn.tycad.oa.exam;

import cn.tycad.oa.exam.repository.*;
import cn.tycad.oa.exam.service.ExamService;
import cn.tycad.oa.exam.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.io.FileNotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OaexamApplicationTests {

    @Value("${jwt.secret}")
    private String secret;

    @Autowired
    public DataSource dataSource;

    @Autowired
    public UserService userService;

    @Autowired
    private ExamService examService;

    @Autowired
    private SystemUserMapper userMapper;

    @Autowired
    private TbExamMapper tbExamMapper;

    @Autowired
    private SystemRoleMapper systemRoleMapper;

    @Autowired
    private TbUserExamMapper tbUserExamMapper;

    @Autowired
    private TbQuestionMapper tbQuestionMapper;

    @Autowired
    private TbOptionMapper tbOptionMapper;

    @Autowired
    private TbUserAnswerMapper tbUserAnswerMapper;

    @Autowired
    private TbValidAnswerMapper tbValidAnswerMapper;



    @Test
    public void contextLoads() {
    }

    @Test
    @Transactional
    @Rollback(false)
    public void dataSourceTest(){
//        System.out.println(dataSource);
//        System.out.println(userMapper);
//        System.out.println(userService);
//        User user = new User();
//        user.setUserId(UUID.randomUUID().toString());
//        user.setUsername("lisi");
//        user.setDeptId(1234520);
//        user.setCreateTime(new Date());
//        user.setBirthday(new Date());
//        user.setName("李四");
//        user.setGender("男");
//        user.setDescription("第一个 测试");
//        user.setPassword("1234");
//        user.setLastEditTime(new Date());
//        userMapper.add(user);
//package cn.tycad.oa.exam;
//
//import cn.tycad.oa.exam.excel.ExcelExamInfo;
//import cn.tycad.oa.exam.excel.ExcelQuestionInfo;
//import cn.tycad.oa.exam.excel.ExcelUtil;
//import cn.tycad.oa.exam.common.util.QrCodeUtils;
//import cn.tycad.oa.exam.exception.BusinessException;
//import cn.tycad.oa.exam.model.bo.SingleExamAndScoreBo;
//import cn.tycad.oa.exam.model.param.UserIdAndTimeAndExamNameAndCreatorParam;
//import cn.tycad.oa.exam.repository.*;
//import cn.tycad.oa.exam.service.ExamService;
//import cn.tycad.oa.exam.service.UserService;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.sql.DataSource;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.util.Calendar;
//import java.util.HashMap;
//import java.util.List;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class OaexamApplicationTests {
//
//    @Autowired
//    public DataSource dataSource;
//
//    @Autowired
//    public UserService userService;
//
//    @Autowired
//    private ExamService examService;
//
//    @Autowired
//    private UserMapper userMapper;
//
//    @Autowired
//    private TbExamMapper tbExamMapper;
//
//    @Autowired
//    private SystemRoleMapper systemRoleMapper;
//
//    @Autowired
//    private TbUserExamMapper tbUserExamMapper;
//
//    @Autowired
//    private TbQuestionMapper tbQuestionMapper;
//
//    @Autowired
//    private TbOptionMapper tbOptionMapper;
//
//    @Autowired
//    private TbUserAnswerMapper tbUserAnswerMapper;
//
//    @Autowired
//    private TbValidAnswerMapper tbValidAnswerMapper;
//
//
//
//
//    @Test
//    public void contextLoads() {
//    }
//
//    @Test
//    @Transactional
//    @Rollback(false)
//    public void dataSourceTest(){
////        System.out.println(dataSource);
////        System.out.println(userMapper);
////        System.out.println(userService);
////        User user = new User();
////        user.setUserId(UUID.randomUUID().toString());
////        user.setUsername("lisi");
////        user.setDeptId(1234520);
////        user.setCreateTime(new Date());
////        user.setBirthday(new Date());
////        user.setName("李四");
////        user.setGender("男");
////        user.setDescription("第一个 测试");
////        user.setPassword("1234");
////        user.setLastEditTime(new Date());
////        userMapper.add(user);
////
////        User user1 = new User();
////        user1.setUserId(UUID.randomUUID().toString());
////        user1.setUsername("wangyaqiu");
////        user1.setDeptId(1234520);
////        user1.setCreateTime(new Date());
////        user1.setBirthday(new Date());
////        user1.setName("王亚秋");
////        user1.setGender("女");
////        user1.setDescription("");
////        user1.setPassword("1234");
////        user1.setLastEditTime(new Date());
////        userMapper.add(user1);
////
////
////        User user2 = new User();
////        user2.setUserId(UUID.randomUUID().toString());
////        user2.setUsername("fengzhe");
////        user2.setDeptId(1234520);
////        user2.setCreateTime(new Date());
////        user2.setBirthday(new Date());
////        user2.setName("冯喆");
////        user2.setGender("女");
////        user2.setDescription("");
////        user2.setPassword("1234");
////        user2.setLastEditTime(new Date());
////        userMapper.add(user2);
//    }
//
//    @Test
//    @Transactional
//    @Rollback(false)
//    public void tbExamMapperTest(){
////        TbExam tbExam = new TbExam();
////        tbExam.setCreateTime(new Date());
////        tbExam.setCreator("pyy");
////        tbExam.setDeadLine(new Date());
////        tbExam.setDescription("考试测试的描述信息");
////        tbExam.setExamId(UUID.randomUUID().toString());
////        tbExam.setExamName("考试测试的名称");
////        tbExam.setLastEditor("pengyy");
////        tbExam.setPublishTime(new Date());
////        tbExam.setSubTitle("本次考试一小时三十分钟");
////        tbExamMapper.insert(tbExam);
//    }
//
//
//    @Test
//    @Transactional
//    @Rollback(false)
//    public void systemRoleMapperTest(){
////        SystemRole systemRole = new SystemRole();
////        systemRole.setDescription("用来行使用户的增删改查");
////        systemRole.setRoleId(UUID.randomUUID().toString());
////        systemRole.setRoleName("用户管理");
////        systemRole.setRoleType(0);
////        systemRoleMapper.insert(systemRole);
//    }
//
//    @Transactional
//    @Rollback(false)
//    @Test
//    public void tbQuestionMapperTest(){
////        TbQuestion tbQuestion = new TbQuestion();
////        tbQuestion.setExamId("89DAC0F8-18A1-4E46-900A-B371B169FD0D");
////        for (int i = 1; i < 5; i++) {
////            tbQuestion.setQuestionId(UUID.randomUUID().toString());
////            tbQuestion.setQuestionTitle("第1"+i+"个问题的title");
////            tbQuestion.setQuestionText("第1"+i+"个问题的text");
////            tbQuestionMapper.insert(tbQuestion);
////        }
//    }
//
//    @Test
//    @Transactional
//    @Rollback(false)
//    public void setTbUserExamMapperInsertTest(){
////        TbUserExam tbUserExam = new TbUserExam();
////        tbUserExam.setExamId("89DAC0F8-18A1-4E46-900A-B371B169FD0D");
////        tbUserExam.setUserId("111ED085-A949-4F93-B99B-18D8F1421AA6");
////        tbUserExamMapper.insert(tbUserExam);
//    }
//
//    @Test
//    @Transactional
//    public void examServiceWayTest(){
//        System.out.println(examService);
////        List<TbExam> examAll = examService.findExamAll();
////        List<SingleExamAndScoreBo> underway = examService.underway("111ED085-A949-4F93-B99B-18D8F1421AA6");
////        SingleScoreAndQuestionBo singleScore = examService.getSingleScore("89DAC0F8-18A1-4E46-900A-B371B169FD0D", "111ED085-A949-4F93-B99B-18D8F1421AA6");
////        System.out.println(underway);
////        List<SingleExamAndScoreBo> finish = examService.finish("111ED085-A949-4F93-B99B-18D8F1421AA6");
////        ExamDetailBo examDetail = examService.getExamDetail("89DAC0F8-18A1-4E46-900A-B371B169FD0D");
////        System.out.println(examDetail);
////        List<SingleScoreAndQuestionBo> scores = examService.getScores("111ED085-A949-4F93-B99B-18D8F1421AA6",
////                "", "", new Date(), new Date());
////        System.out.println(scores);
//
////         获取一天以后的时间
////        Calendar calendar = Calendar.getInstance();
////        calendar.set(Calendar.DAY_OF_YEAR,
////                calendar.get(Calendar.DAY_OF_YEAR) + 1);
////        TbExam tbExam = new TbExam();
////        tbExam.setExamId("89DAC0F8-18A1-4E46-900A-B371B169FD0D");
////        tbExam.setPublishTime(new Date());
////        tbExam.setDeadLine(calendar.getTime());
////        examService.updatePublishAndDeadLine(tbExam);
////        examService.deleteExamById("89DAC0F8-18A1-4E46-900A-B371B169FD0D");
//
//    }
//
//    /**
//     * 测试增加考题、选项、选择题回答
//     */
//    @Test
//    @Transactional
//    @Rollback(false)
//    public void questionAndAnswerAndOptionTest(){
////        TbQuestion tbQuestion = new TbQuestion();
////        tbQuestion.setExamId("89DAC0F8-18A1-4E46-900A-B371B169FD0D");
////        for (int i = 0; i < 5; i++) {
////            tbQuestion.setQuestionTitle("这是第"+(i+1)+"问题的title");
////            tbQuestion.setQuestionText("这是第"+(i+1)+"问题的text");
////            String tbQuestionId = UUID.randomUUID().toString();
////            tbQuestion.setQuestionId(tbQuestionId);
////            tbQuestion.setOrderNum(i+1);
////            int j = (i+1)%2;
////            tbQuestion.setQuestionType(j);
////            if (j==0) {
////                String optionId = "";
//////                新增选择题选项
////                for (int i1 = 0; i1 < 4; i1++) {
////                    TbOption tbOption = new TbOption();
////                    optionId = UUID.randomUUID().toString();
////                    tbOption.setOptionId(optionId);
////                    tbOption.setOptionText("这是选择题" + tbQuestionId + "的"+(i1+1)+"个选项text");
////                    tbOption.setQuestionId(tbQuestionId);
////                    tbOption.setOrderNum(i1+1);
////                    tbOptionMapper.insert(tbOption);
////                }
//////                新增用户选择题回答
////                TbUserAnswer tbUserAnswer = new TbUserAnswer();
////                tbUserAnswer.setUserId("111ED085-A949-4F93-B99B-18D8F1421AA6");
////                tbUserAnswer.setOptionId(optionId);
////                tbUserAnswer.setQuestionId(tbQuestionId);
////                tbUserAnswerMapper.insert(tbUserAnswer);
////                tbQuestionMapper.insert(tbQuestion);
////            }else {
////                TbUserAnswer tbUserAnswer = new TbUserAnswer();
////                tbUserAnswer.setUserId("111ED085-A949-4F93-B99B-18D8F1421AA6");
////                tbUserAnswer.setQuestionId(tbQuestionId);
////                tbUserAnswer.setAnswerValue("非选择题题的回答");
////                tbUserAnswerMapper.insert(tbUserAnswer);
////
////                tbQuestionMapper.insert(tbQuestion);
////            }
////
////        }
//    }
//
//    @Transactional
//    @Test
//    public void validAnswerTest(){
////        TbValidAnswer tbValidAnswer = new TbValidAnswer();
////        tbValidAnswer.setAnswerValue("1234520");
////        tbValidAnswer.setQuestionId("0E7A1A8E-D935-45AF-900C-01855F0619D3");
////        tbValidAnswerMapper.insert(tbValidAnswer);
////        TbValidAnswer byQuestionId = tbValidAnswerMapper.findByQuestionId("0E7A1A8E-D935-45AF-900C-01855F0619D3");
////        System.out.println(byQuestionId);
//    }
//
//    @Test
//    public void readExcelTest() throws FileNotFoundException {
////        List<Object> examList = ExcelUtil.readExcelWithModel(new FileInputStream("C:\\Users\\Administrator\\Desktop\\kaoshi.xlsx"),1, ExcelExamInfo.class,true);
////        System.out.println(examList);
////        int size = examList.size();
////        HashMap<String, List> questionExcelInfos = new HashMap<>();
////        for (int i = 2; i <(2+size); i++) {
////            List<Object> questionList = ExcelUtil.readExcelWithModel(new FileInputStream("C:\\Users\\Administrator\\Desktop\\kaoshi.xlsx"), i,ExcelQuestionInfo.class,true);
////                questionExcelInfos.put(String.valueOf(i),questionList);
////        }
////        System.out.println(questionExcelInfos);
//    }
//
//    @Test
//    @Transactional
//    @Rollback(false)
//    public void excelImportTest(){
//        try {
//            // "C:\\Users\\Administrator\\Desktop\\kaoshi.xlsx"
//            examService.addExamTask(new File("C:\\Users\\Administrator\\Desktop\\kaoshi2003.xls"),"pengyy");
//        }catch (FileNotFoundException e){
//            System.out.println(e.getMessage());
//            throw new BusinessException("文件不存在");
//        }
//    }
//
//    @Test
//    public void myFinishExamTest(){
//        UserIdAndTimeAndExamNameAndCreatorParam param = new UserIdAndTimeAndExamNameAndCreatorParam();
//        param.setUserId("111ED085-A949-4F93-B99B-18D8F1421AA6");
//        Calendar instance = Calendar.getInstance();
//        instance.set(2019,2,20);
//        param.setMinTime(instance.getTime());
//        System.out.println(instance.getTime());
//        param.setCreator("woshilaoda");
//        param.setExamName("测试");
//        instance.set(2019,2,23);
//        System.out.println(instance.getTime());
//        param.setMaxTime(instance.getTime());
//        List<SingleExamAndScoreBo> finish = examService.getUserExamList(param,0);
//        System.out.println(finish);
//
//    }
//
//    @Test
//    public void qrCodeUtilTest(){
//        String text = "https://www.baidu.com/";
//        String logoPath ="C:\\Users\\Administrator\\Desktop\\logo.jpg";
//        String destPath = "C:\\Users\\Administrator\\Desktop";
//        try {
//            QrCodeUtils.encode(text,logoPath,destPath,"跳转百度",true);
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//
//    }
//
//}
//        }
    }

    @Transactional
    @Test
    public void validAnswerTest(){
//        TbValidAnswer tbValidAnswer = new TbValidAnswer();
//        tbValidAnswer.setAnswerValue("1234520");
//        tbValidAnswer.setQuestionId("0E7A1A8E-D935-45AF-900C-01855F0619D3");
//        tbValidAnswerMapper.insert(tbValidAnswer);
//        TbValidAnswer byQuestionId = tbValidAnswerMapper.findByQuestionId("0E7A1A8E-D935-45AF-900C-01855F0619D3");
//        System.out.println(byQuestionId);
    }

    @Test
    public void readExcelTest() throws FileNotFoundException {
//        List<Object> examList = ExcelUtil.readExcelWithModel(new FileInputStream("C:\\Users\\Administrator\\Desktop\\kaoshi.xlsx"),1, ExcelExamInfo.class,true);
//        System.out.println(examList);
//        int size = examList.size();
//        HashMap<String, List> questionExcelInfos = new HashMap<>();
//        for (int i = 2; i <(2+size); i++) {
//            List<Object> questionList = ExcelUtil.readExcelWithModel(new FileInputStream("C:\\Users\\Administrator\\Desktop\\kaoshi.xlsx"), i,ExcelQuestionInfo.class,true);
//                questionExcelInfos.put(String.valueOf(i),questionList);
//        }
//        System.out.println(questionExcelInfos);
    }

    @Test
    @Transactional
    @Rollback(false)
    public void excelImportTest(){
//        try {
//            // "C:\\Users\\Administrator\\Desktop\\kaoshi.xlsx"
//            examService.addExamTask(new File("C:\\Users\\Administrator\\Desktop\\kaoshi2003.xls"),"pengyy");
//        }catch (FileNotFoundException e){
//            System.out.println(e.getMessage());
//            throw new BusinessException("文件不存在");
//        }
    }

    @Test
    public void myFinishExamTest(){
//        UserIdAndTimeAndExamNameAndCreatorParam param = new UserIdAndTimeAndExamNameAndCreatorParam();
//        param.setUserId("111ED085-A949-4F93-B99B-18D8F1421AA6");
//        Calendar instance = Calendar.getInstance();
//        instance.set(2019,2,20);
//        param.setMinTime(instance.getTime());
//        System.out.println(instance.getTime());
//        param.setCreator("woshilaoda");
//        param.setExamName("测试");
//        instance.set(2019,2,23);
//        System.out.println(instance.getTime());
//        param.setMaxTime(instance.getTime());
//        List<SingleExamAndScoreBo> finish = examService.getUserExamList(param,0);
//        System.out.println(finish);
    }

    @Test
    public void qrCodeUtilTest(){
//        String text = "https://www.baidu.com/";
//        String logoPath ="C:\\Users\\Administrator\\Desktop\\logo.jpg";
//        String destPath = "C:\\Users\\Administrator\\Desktop";
//        try {
//            QrCodeUtils.encode(text,logoPath,destPath,"跳转百度",true);
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//        }
    }

    @Test
    public void markPapersTest(){
//        ArrayList<MarkPapersParam.QuestionIdAndScore> questionIdAndScores = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            MarkPapersParam.QuestionIdAndScore e = new MarkPapersParam.QuestionIdAndScore();
//            e.setQuestionId(String.valueOf(i));
//            e.setScore(new Float(20+i));
//            questionIdAndScores.add(e);
//        }
//        List<Float> doubles = Arrays.asList(1f,2f,3f,4f,5f);
//        Float  reduce = doubles.stream().reduce(new Float(0),(x,y)->x+y);
//        Double textReduce = questionIdAndScores.stream().mapToDouble(MarkPapersParam.QuestionIdAndScore::getScore).sum();
//        System.out.println(reduce);
//        System.out.println(textReduce);

    }

    @Test
    public void getAllValidAnswerTest() {
//        List<UserOptionBo> result = tbUserExamMapper.getAllValidAnswers("EF665B2A-17EF-4820-B86D-5B26AA833AB7");
//        for(UserOptionBo uob : result) {
//            System.out.println(uob.toString());
//        }
    }

    @Test
    public void jwtUtilTest() throws Exception {
//            String jwt = JwtUtils.createJWT("1234", "{userName:\"zhangsan\",roleId:\"123456\"}", -1,secret);
//            System.out.println(jwt);
//            Claims claims = JwtUtils.parseJWT(jwt,secret);
//            System.out.println(claims.getExpiration());
//            System.out.println(claims.getSubject());
//            String jwt2 = JwtUtils.createJWT("1234", null, 0,secret);
//            System.out.println(jwt);
//            Claims claims1 = JwtUtils.parseJWT(jwt2,secret);
//            System.out.println(claims1.getExpiration());
//            System.out.println(claims1.getSubject());
    }


  /*  @Test
    public void batchUpdatePassword(){
        List<SystemUserBo> list = userMapper.list(null, null);
        Map<String, String> collect = list.stream().collect(Collectors.toMap(m -> m.getUsername(), m -> UserUtils.md5DigestAsHex(m.getUsername() + "0920", m.getUsername())));
        userMapper.batchUpdatePassword(collect);
    }*/

}

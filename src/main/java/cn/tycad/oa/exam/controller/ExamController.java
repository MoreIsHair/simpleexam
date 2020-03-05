package cn.tycad.oa.exam.controller;

import cn.tycad.oa.exam.annotation.RequiredPermission;
import cn.tycad.oa.exam.common.PermissionConstants;
import cn.tycad.oa.exam.common.util.CommonUtils;
import cn.tycad.oa.exam.common.util.UserUtils;
import cn.tycad.oa.exam.excel.ExcelExamAndAverAgeBoInfo;
import cn.tycad.oa.exam.excel.ExcelExamGradeInfo;
import cn.tycad.oa.exam.excel.ExcelUserExamScoreInfo;
import cn.tycad.oa.exam.excel.ExcelUtil;
import cn.tycad.oa.exam.model.bo.*;
import cn.tycad.oa.exam.model.entity.TbReview;
import cn.tycad.oa.exam.model.param.AnswerAndReviewParam;
import cn.tycad.oa.exam.model.param.MarkPapersParam;
import cn.tycad.oa.exam.model.param.PageInfoAndUserNameAndExamNameParam;
import cn.tycad.oa.exam.model.param.UserIdAndTimeAndExamNameAndCreatorParam;
import cn.tycad.oa.exam.model.vo.Result;
import cn.tycad.oa.exam.model.vo.UpdateExamInfoVo;
import cn.tycad.oa.exam.service.DepartmentService;
import cn.tycad.oa.exam.service.ExamService;
import cn.tycad.oa.exam.service.ReviewService;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

/**
 * @author YY
 * @date 2019/3/5
 * @description
 */
@RestController
@RequestMapping(value = "/exam")
@Api("考试管理")
public class ExamController {

    @Autowired
    public ExamService examService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private DepartmentService departmentService;

    @RequiredPermission(PermissionConstants.EXAM_SUPER_POWER)
    @ApiOperation(value = "所有考试")
    @PostMapping ("/list")
    public Result<PageInfo<TbExamPagerBo>> list(@ApiParam(name = "param",value = "pageNum、pageSize、examName")@RequestBody PageInfoAndUserNameAndExamNameParam param) {
        PageInfo<TbExamPagerBo> examAll = examService.findExamAll(param.getPageNum(),param.getPageSize(), param.getExamName());
        return Result.success(examAll);
    }

    @RequiredPermission(PermissionConstants.LOGGED_IN)
    @ApiOperation(value = "考试详情")
    @GetMapping(value = "/{id}")
    public Result examDetail(@ApiParam(name = "id",required = true) @PathVariable("id") String examId,HttpServletRequest request,
                             @ApiParam(name = "type") @RequestParam(value = "type",required = false) String type){
        String token = UserUtils.getToken(request);
        // TODO 已修改，待测试
        Object examDetail = examService.getExamDetail(examId, token, "1".equals(type)?Integer.valueOf(type):0);
        return Result.success(examDetail);
    }

    @RequiredPermission(PermissionConstants.EXAM_SUPER_POWER)
    @ApiOperation("更新考试信息")
    @PutMapping(value = "/examInfo")
    public Result<Void> updateExamInfo(@ApiParam(name = "infos",value = "待更新的考试信息以及人员ID")
                                           @RequestBody @Valid UpdateExamInfoVo infos,HttpServletRequest request){
        String token = UserUtils.getToken(request);
        examService.updateExamInfo(infos, token);
        return Result.success();
    }

    @RequiredPermission(PermissionConstants.EXAM_SUPER_POWER)
    @ApiOperation("删除考试")
    @DeleteMapping(value = "/{id}")
    public Result<Void> exam(@ApiParam(name = "id",required = true) @PathVariable("id") String examId){
            examService.deleteExamById(examId);
            return Result.success();
    }

    @RequiredPermission(PermissionConstants.EXAM_STUDENT_POWER)
    @ApiOperation("查询正在进行的考试")
    @GetMapping(value = "/underway/{userId}")
    public Result<List<SingleExamAndScoreBo>> underway(
            @ApiParam(name = "userId", required = true) @PathVariable("userId") String userId) {
        List<SingleExamAndScoreBo> result = examService.underway(userId);

        return Result.success(result);
    }

    @RequiredPermission(PermissionConstants.EXAM_STUDENT_POWER)
    @ApiOperation("提交答题结果")
    @PostMapping(value = "/answer")
    public Result<Void> answer(
            @Valid @ApiParam(name = "试题回答", value = "answer")
            @RequestBody AnswerAndReviewParam answer, HttpServletRequest request,
            @RequestParam(required = false) Integer commitType) throws Exception {
        String token = UserUtils.getToken(request);
        int _commitType = commitType == null ? 0 : commitType;
        synchronized (this) {
            // TODO 已修改，待测试，并且权限控制无法处理
            examService.commitAnswer(answer.getUserAnswerBo(),answer.getTbReview(), token, _commitType);
        }

        return Result.success();
    }

    @RequiredPermission(PermissionConstants.EXAM_SUPER_POWER)
    @ApiOperation(value = "添加考试",notes = "通过上传excel文件")
    @PostMapping(value = "/fileUpload",consumes = "multipart/*",headers = "content-type=multipart/form-data")
    public Result<Void> fileUpload(@ApiParam(name = "fileName",value = "上传的excel文件")
                                       @RequestParam(value = "fileName") MultipartFile file, HttpServletRequest request) throws Exception {
        String token = UserUtils.getToken(request);
        if(file==null || file.isEmpty()){
            return Result.error("文件为空");
        }
        // 先把文件存起来
        SaveFileToNature saveFileToNature = new SaveFileToNature(file).invoke();
        File temp = saveFileToNature.getTemp();
        File dest = saveFileToNature.getDest();
        examService.addExamTask(dest,token);
        // 删除文件夹
        CommonUtils.deleteFile(temp.getAbsolutePath());
        return Result.success();
    }


    @RequiredPermission(PermissionConstants.LOGGED_IN)
    @ApiOperation(value = "查询用户已完成的所有考试",notes = "支持条件筛选")
    @PostMapping("/finishExam")
    public Result<PageInfo<SingleExamAndScoreBo>> finishAllExam(
            @ApiParam(name = "userIdAndTimeAndExamNameAndCreatorParam",
                    value = "时间范围，考试任务名称，出题人等参数筛选,用户Id为必须")
            @RequestBody UserIdAndTimeAndExamNameAndCreatorParam param,HttpServletRequest request)
            throws Exception {
        String token = UserUtils.getToken(request);
        PageInfo<SingleExamAndScoreBo> finish = examService.getUserExamList(param, 1, token);
        return Result.success(finish);
    }

    @RequiredPermission(PermissionConstants.EXAM_STUDENT_POWER)
    @ApiOperation(value = "查询用户将要参加的所有考试",notes = "支持条件筛选")
    @PostMapping("/futureExam")
    public Result<PageInfo<SingleExamAndScoreBo>> futureExam(@ApiParam(name = "userIdAndTimeAndExamNameAndCreatorParam",value = "时间范围，考试任务名称，出题人等参数筛选,用户Id为必须")
                                                                @RequestBody UserIdAndTimeAndExamNameAndCreatorParam param,HttpServletRequest request) throws Exception {
        String token = UserUtils.getToken(request);
        PageInfo<SingleExamAndScoreBo> noFinish = examService.getUserExamList(param, 0,token);
        return Result.success(noFinish);
    }

    @RequiredPermission(PermissionConstants.EXAM_STUDENT_POWER)
    @PostMapping("/singleScoreAndQuestion")
    @ApiOperation(value = "获取一次考试的分数以及试题")
    public Result<SingleScoreAndQuestionBo>  singleScoreAndQuestion(@ApiParam(value = "用户ID（userId）、考试ID(examId)",name = "userIdAndExamIdMap",required = true) @RequestBody Map<String,Object> userIdAndExamIdMap){
        SingleScoreAndQuestionBo singleScore = examService.getSingleScore((String) userIdAndExamIdMap.get("examId"), (String) userIdAndExamIdMap.get("userId"));
        return Result.success(singleScore);
    }

    @RequiredPermission(PermissionConstants.EXAM_SUPER_POWER)
    @ApiOperation(value = "获得考试二维码")
    @GetMapping(value = "/examCode/{examId}",produces = MediaType.IMAGE_JPEG_VALUE)
    public BufferedImage getImage(@ApiParam(name = "examId",required = true)@PathVariable("examId") String examId){
        return examService.getImage(examId);
    }

    @RequiredPermission(PermissionConstants.EXAM_TEACHER_POWER)
    @ApiOperation(value = "打分（简单题）",notes = "阅卷老师评改简单题，提交分数")
    @PostMapping("/markPaper")
    public Result<Void> markPapers(@ApiParam(name = "param",value = "考试ID、用户ID、问题与分数的集合",required = true)  @RequestBody MarkPapersParam param){
        examService.markPapers(param);
        return Result.success();
    }

    @RequiredPermission(PermissionConstants.EXAM_TEACHER_POWER)
    @ApiOperation(value = "获取需要修改的简单题")
    @PostMapping("/textOptionAndAnswer")
    public Result<Map<String, Object>> getUserTextOptionAndAnswer(@ApiParam(value = "用户ID（userId）、考试ID(examId)",name = "userIdAndExamIdMap",required = true) @RequestBody Map<String,Object> userIdAndExamIdMap){
        Map<String, Object> userTextOptionAndAnswer = examService.getUserTextOptionAndAnswer((String) userIdAndExamIdMap.get("examId"), (String) userIdAndExamIdMap.get("userId"));
        return Result.success(userTextOptionAndAnswer);
    }

    @RequiredPermission(PermissionConstants.EXAM_STUDENT_POWER)
    @ApiOperation(value = "写入学生评价，修改之后不再使用")
    @PostMapping("/review")
    public Result<Void> insertReview(
            @ApiParam(value = "学生评价") @RequestBody TbReview review) {
        UUID id = UUID.randomUUID();
        review.setReviewId(id.toString());
        reviewService.insert(review);
        return Result.success();
    }

    @RequiredPermission(PermissionConstants.EXAM_REVIEW_POWER)
    @ApiOperation(value = "查询学生评价列表")
    @GetMapping("/review")
    public Result<List<TbReview>> reviewList() {
        List<TbReview> result = reviewService.getList();
        return Result.success(result);
    }

    @RequiredPermission(PermissionConstants.EXAM_SUPER_POWER)
    @ApiOperation(value = "查询学生评价详情")
    @GetMapping("/review/{examId}/{userId}")
    public Result<List<TbReview>> reviewList(
            @ApiParam(value = "考试id") @PathVariable String examId,
            @ApiParam(value = "用户id") @PathVariable String userId) {
        List<TbReview> result = reviewService.getListByExamId(examId, userId);
        return Result.success(result);
    }


    @RequiredPermission({PermissionConstants.EXAM_SUPER_POWER,PermissionConstants.EXAM_TEACHER_POWER})
    @ApiOperation(value = "每场考试的成绩统计（该场考试的平均分）")
    @PostMapping(value = "/tbExamAndAvg")
    public Result<PageInfo<TbExamAndAverageBo>> getTbExamAndAvg(@RequestBody @ApiParam(value = "pageNum、pageSize、examName",name = "param") PageInfoAndUserNameAndExamNameParam param){
        PageInfo<TbExamAndAverageBo> tbExamAndAvg1 = examService.getTbExamAndAvg(param);
        return Result.success(tbExamAndAvg1);
    }

    @RequiredPermission(PermissionConstants.LOGGED_IN)
    @ApiOperation(value = "成绩统计（所有人的所有考试信息已考试）")
    @PostMapping ("/allExamInfoAndUserAndDepartmentAndScore")
    public Result<PageInfo<TbExamAndUserInfoAndDepartInfoBo>> getAllExamInfo(
            @ApiParam(name = "param",value = "pageNum、pageSize、examName、userName")
            @RequestBody PageInfoAndUserNameAndExamNameParam param) {
        PageInfo<TbExamAndUserInfoAndDepartInfoBo> allExamInfo = examService.getAllExamInfo(param);
        return Result.success(allExamInfo);
    }

    @RequiredPermission({PermissionConstants.EXAM_SUPER_POWER,PermissionConstants.EXAM_TEACHER_POWER})
    @ApiOperation(value = "单次考试的所有考生")
    @GetMapping("/examAllUserInfoAndScore/{examId}")
    public Result<List<SingleExamAndScoreAndUserInfo>> getExamAllUserInfoAndScore(@ApiParam(name = "examId",value = "考试ID",required = true)@PathVariable("examId") String examId){
        List<SingleExamAndScoreAndUserInfo> allExamInfo = examService.getExamAllUserInfoAndScore(examId);
        return Result.success(allExamInfo);
    }

    @RequiredPermission(PermissionConstants.EXAM_SUPER_POWER)
    @ApiOperation(value = "获取考试信息，部门、分组、用户树")
    @GetMapping("/userTree/{examId}")
    public Result<List<DepartmentAndGroupBO>> getExamAllUserInfo(
            @ApiParam(name = "examId", value = "考试id", required = true) @PathVariable("examId") String examId) {
        List<DepartmentAndGroupBO> departments = departmentService.getDepartmentTree();
        List<ExamUserBo> users = examService.getExamUsers(examId);

        List<DepartmentAndGroupBO> result = departments.stream().map(m -> {
            m.setUsers(getDepUsers(m.getId(), m.getParentId(), users));
            m.setChildren(buildDeptChildren(m.getChildren(), users));
            return m;
        }).collect(toList());

        return Result.success(result);
    }

    /**
     * 重新构造子部门树，加入用户信息
     * @param children
     * @param users
     * @return
     */
    private List<DepartmentAndGroupBO> buildDeptChildren(List<DepartmentAndGroupBO> children, List<ExamUserBo> users) {
        return children.stream().map(c -> {
            c.setUsers(getDepUsers(c.getId(), c.getParentId(), users));
            List<DepartmentAndGroupBO> groups = buildDeptChildren(c.getChildren(), users);
            c.setChildren(groups);
            return c;
        }).collect(toList());
    }

    /**
     * 根据部门id，父级部门id获取用户列表
     * @param id
     * @param parentId
     * @param users
     * @return
     */
    private List<ExamUserBo> getDepUsers(String id, String parentId, List<ExamUserBo> users) {
        List<ExamUserBo> result = users.stream()
                .filter(u -> ((id.equals(u.getDeptId()) && "-1".equals(parentId) &&
                                (u.getGroupId() == null || "".equals(u.getGroupId())) ||
                        id.equals(u.getGroupId()) && parentId.equals(u.getDeptId()))
                        ))
                .collect(toList());

        return result;
    }

    @RequiredPermission(PermissionConstants.EXAM_TEACHER_POWER)
    @ApiOperation(value = "待阅列表")
    @PostMapping(value = "/markPapersExamList")
    public Result<PageInfo<TbExamAndUserInfoBo>> futureMarkPapersExamList(@ApiParam(name = "condition",value = "分页信息以及查询条件包括pageNum，pageSize，examName，userName") @RequestBody PageInfoAndUserNameAndExamNameParam condition,HttpServletRequest request) throws Exception {
        String token = UserUtils.getToken(request);
        PageInfo<TbExamAndUserInfoBo> tbExamAndUserInfoBoPageInfo = examService.futureMarkPapersExam(condition.getPageNum()
                , condition.getPageSize()
                , condition.getExamName()
                , condition.getUserName(),token);
        return Result.success(tbExamAndUserInfoBoPageInfo);
    }

    @RequiredPermission(PermissionConstants.LOGGED_IN)
    @ApiOperation(value = "考试成绩统计导出")
    @GetMapping(value = {"/exportExamAvgGrade/{examName}","/exportExamAvgGrade"},produces="application/vnd.ms-excel;charset=utf-8")
    public void exportExamAvgGrade(@ApiParam(name = "examName")@PathVariable(value = "examName",required = false)String examName, HttpServletResponse response){
        Map<String, List<? extends BaseRowModel>> stringListHashMap = new HashMap<>(16);
        List<TbExamAndAverageBo> list = examService.getTbExamAndAvg(new PageInfoAndUserNameAndExamNameParam(examName)).getList();
        List<ExcelExamGradeInfo> collect = list.stream().map(m -> {
            ExcelExamGradeInfo excelExamGradeInfo = new ExcelExamGradeInfo(m);
            return excelExamGradeInfo;
        }).collect(toList());
        if (collect.isEmpty()){
            ExcelUtil.writeSingelExcel(collect,ExcelTypeEnum.XLSX,response,ExcelExamGradeInfo.class);
        }else {
            stringListHashMap.put("考试信息", collect);
            // 将添加每套试题的考生考试信息
            for (TbExamAndAverageBo tbExamAndAverageBo : list) {
                List<SingleExamAndScoreAndUserInfo> examAllUserInfoAndScore = examService.getExamAllUserInfoAndScore(tbExamAndAverageBo.getExamId());
                List<ExcelExamAndAverAgeBoInfo> collect1 = examAllUserInfoAndScore.stream().map(e -> new ExcelExamAndAverAgeBoInfo(e)).collect(toList());
                stringListHashMap.put(tbExamAndAverageBo.getExamName(),collect1);
            }
            ExcelUtil.writeExcel(stringListHashMap, ExcelTypeEnum.XLSX,response);
        }
    }

    @RequiredPermission({PermissionConstants.EXAM_SUPER_POWER,PermissionConstants.EXAM_TEACHER_POWER})
    @ApiOperation(value = "用户考试成绩统计导出")
    @GetMapping(value = {"/exportUserExamScore/{username}","/exportUserExamScore"},produces="application/vnd.ms-excel;charset=utf-8")
    public void exportUserExamScore(@ApiParam(name = "username")@PathVariable(value = "username",required = false)String username,
                                    HttpServletResponse response){
        Map<String, List<? extends BaseRowModel>> stringListHashMap = new HashMap<>(16);
        List<TbExamAndUserInfoAndDepartInfoBo> list = examService.getAllExamInfo(new PageInfoAndUserNameAndExamNameParam(null,username)).getList();
        List<ExcelUserExamScoreInfo> collect = list.stream().map(m -> {
            ExcelUserExamScoreInfo excelUserExamScoreInfo = new ExcelUserExamScoreInfo(m);
            return excelUserExamScoreInfo;
        }).collect(toList());
        if (collect.isEmpty()){
            ExcelUtil.writeSingelExcel(collect,ExcelTypeEnum.XLSX,response, ExcelUserExamScoreInfo.class);
        }else {
            stringListHashMap.put("第一页", collect);
            ExcelUtil.writeExcel(stringListHashMap, ExcelTypeEnum.XLSX,response);
        }
    }
    @RequiredPermission(PermissionConstants.LOGIN)
    @ApiOperation(value = "用户考试暂停以及保存用户回答")
    @PostMapping(value = "/suspendExam")
    public Result<Void> suspendExam(
        @Valid @ApiParam(name = "试题回答", value = "answer")
        @RequestBody UserAnswerBo answer,HttpServletRequest request) {
            String token = UserUtils.getToken(request);
            synchronized (this) {
                examService.suspendExam(answer, token);
            }
            return Result.success();
    }

    private class SaveFileToNature {
        private MultipartFile file;
        private File temp;
        private File dest;

        public SaveFileToNature(MultipartFile file) {
            this.file = file;
        }

        public File getTemp() {
            return temp;
        }

        public File getDest() {
            return dest;
        }

        private SaveFileToNature invoke() throws IOException {
            String fileName = file.getOriginalFilename();
            String basePath = System.getProperty("user.dir")+System.getProperty("file.separator")+"temp"+System.getProperty("file.separator");
            temp = new File(basePath);
            dest = new File((basePath + fileName));
            if (!temp.exists()){
                temp.mkdirs();
            }
            if (dest.exists()){
                dest.delete();
            }
            file.transferTo(dest);
            return this;
        }
    }
}

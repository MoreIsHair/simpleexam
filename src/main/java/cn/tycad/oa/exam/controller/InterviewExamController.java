package cn.tycad.oa.exam.controller;

import cn.tycad.oa.exam.annotation.RequiredPermission;
import cn.tycad.oa.exam.common.util.CommonUtils;
import cn.tycad.oa.exam.common.enums.InterviewQuestionFollowEnum;
import cn.tycad.oa.exam.common.PermissionConstants;
import cn.tycad.oa.exam.common.util.UserUtils;
import cn.tycad.oa.exam.model.bo.InterviewExamContentBo;
import cn.tycad.oa.exam.model.entity.TbDisposableExamInfo;
import cn.tycad.oa.exam.model.entity.TbInterviewQuestion;
import cn.tycad.oa.exam.model.entity.TbInterviewTemplate;
import cn.tycad.oa.exam.model.param.AnswerAndReviewParam;
import cn.tycad.oa.exam.model.param.QuestionIdsAndTemplateInfoParam;
import cn.tycad.oa.exam.model.vo.Result;
import cn.tycad.oa.exam.service.InterviewExamService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Map;

/**
 * @author YY
 * @date 2019/8/6
 * @description 面试考试相关
 */
@RestController
@RequestMapping("/interviewExam")
@Api("面试考试")
public class InterviewExamController {

    @Autowired
    public InterviewExamService interviewExamService;

    @ApiOperation("试题批量导入")
    @PostMapping("/batchQuestionImport")
    public Result<Void> batchQuestionImport(@ApiParam(required = true,name = "questionFile") @RequestParam("questionFile") MultipartFile file) throws Exception {
        if(file==null || file.isEmpty()){
            return Result.error("文件为空");
        }
        CreateTemporaryFile createTemporaryFile = new CreateTemporaryFile(file).invoke();
        String basePath = createTemporaryFile.getBasePath();
        File dest = createTemporaryFile.getDest();
        // 暂时转存文件Excel才能读取
        file.transferTo(dest);
        interviewExamService.importQuestion(dest);
        // 删除文件
        CommonUtils.deleteFile(basePath);
        return Result.success();
    }

    @GetMapping("/questionList")
    @ApiOperation(value = "获取库中的问题",notes = "根据题目类型获取题库中问题")
    public Result<PageInfo<TbInterviewQuestion>> questionList(@ApiParam(name = "pageSize")@RequestParam(value = "pageSize",required = false) Integer pageSize,
                                     @ApiParam(name = "pageNum")@RequestParam(value = "pageNum",required = false)Integer pageNum,
                                     @ApiParam(name = "questionFollow",value = "题目类型")@RequestParam(value = "questionFollow")int questionFollow){
        PageInfo<TbInterviewQuestion> questionList = interviewExamService.getQuestionList(questionFollow, pageSize, pageNum);
        return Result.success(questionList);
    }

    @ApiOperation(value = "组成一套试题模板")
    @PostMapping("/collectTemplate")
    @RequiredPermission(PermissionConstants.LOGIN)
    public Result<Void>  collectTemplate(@ApiParam(name = "QuestionIdsAndTemplateInfoParam",required = true)
                                                     @RequestBody QuestionIdsAndTemplateInfoParam Param, HttpServletRequest request){
        String token = UserUtils.getToken(request);
        interviewExamService.collectToTemplate(Param,token);
        return Result.success();
    }

    @GetMapping("/getFollow")
    @ApiOperation(value = "获取类型所属组集合",notes = "获取类型所属组")
    public Result<Map<Integer, InterviewQuestionFollowEnum>> getFollow(){
        return Result.success(InterviewQuestionFollowEnum.map());
    }


    @GetMapping("/templateList")
    @ApiOperation(value = "获取以及形成的试题模板",notes = "根据类型获取题库中试题模板")
    public Result<PageInfo<TbInterviewTemplate>> templateList(@ApiParam(name = "pageSize")@RequestParam(value = "pageSize",required = false) int pageSize,
                                     @ApiParam(name = "pageNum")@RequestParam(value = "pageNum",required = false)int pageNum,
                                     @ApiParam(name = "followType",value = "题目类型")@RequestParam(value = "followType")int followType){
        PageInfo<TbInterviewTemplate> templateList = interviewExamService.getTemplateList(followType, pageSize, pageNum);
        return Result.success(templateList);
    }

    @GetMapping("/templateContent")
    @ApiOperation(value = "获取以及形成的试题模板的内容（所有题目）")
    public Result<InterviewExamContentBo> templateContent(@ApiParam(name = "templateId",value = "模板ID",required = true)@RequestParam(value = "templateId")String templateId){
        InterviewExamContentBo templateContent = interviewExamService.getTemplateContent(templateId);
        return Result.success(templateContent);
    }

    @ApiOperation(value = "考试之前调接口录取基本信息")
    @PostMapping("/enterDisposableInfo")
    public Result<Void>  enterDisposableInfo(@ApiParam(name = "QuestionIdsAndTemplateInfoParam",required = true)
                                                         @RequestBody TbDisposableExamInfo Param){
        interviewExamService.enterDisposableInfo(Param);
        return Result.success();
    }

    @PostMapping("/commitAnswer")
    @ApiOperation(value = "提交提交（所有题目）")
    public Result<Double> commitAnswer(@ApiParam(name = "param")@RequestBody AnswerAndReviewParam param){
        Double score = interviewExamService.playScore(param.getUserAnswerBo(), param.getTbReview());
        return Result.success(score);
    }

    @GetMapping("/getDisposableExamInfoList")
    @ApiOperation(value = "获取一次性考试用户的基本信息以及考试总分")
    public Result<PageInfo> getDisposableExamInfoList(@ApiParam(name = "pageSize")@RequestParam(value = "pageSize",required = false) Integer pageSize,
                                                  @ApiParam(name = "pageNum")@RequestParam(value = "pageNum",required = false)Integer pageNum){
        PageInfo disposableExamInfoList = interviewExamService.getDisposableExamInfoList(pageSize, pageNum);
        return Result.success(disposableExamInfoList);
    }

    /**
     * 暂存导入的Excel文件
     */
    private class CreateTemporaryFile {
        private MultipartFile file;
        /**
         *  文件所在目录
         */
        private String basePath;
        /**
         *  文件
         */
        private File dest;

        public CreateTemporaryFile(MultipartFile file) {
            this.file = file;
        }

        public String getBasePath() {
            return basePath;
        }

        public File getDest() {
            return dest;
        }

        public CreateTemporaryFile invoke() {
            basePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "temp" + System.getProperty("file.separator");
            String fileName = file.getOriginalFilename();
            dest = new File((basePath + fileName));
            File temp = new File(basePath);
            if (!temp.exists()){
                temp.mkdirs();
            }
            if (dest.exists()){
                dest.delete();
            }
            return this;
        }
    }
}

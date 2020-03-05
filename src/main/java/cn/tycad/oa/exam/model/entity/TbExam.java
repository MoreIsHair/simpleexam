package cn.tycad.oa.exam.model.entity;

import cn.tycad.oa.exam.common.enums.ExceptionInfoEnum;
import cn.tycad.oa.exam.excel.ExcelExamInfo;
import cn.tycad.oa.exam.exception.BusinessException;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: YY
 * @Date: 2019/3/5
 * @Description: 试卷
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "TbExam", description = "试卷模型")
public class TbExam {
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public TbExam(String examId, @NotBlank(message = "试题名称不能为空") @Length(max = 50, message = "试题名称长度必须小于 {max}") String examName
            , @Length(max = 100, message = "考试标题长度必须小于 {max}") String subTitle, String description, Date createTime
            , String creator, Date lastEditTime, String lastEditor, Date publishTime, @Max(value = 1000, message = "考试时间不合理") @Min(value = 1
            , message = "考试时间不合理") Integer duration, Date deadLine) {
        this.examId = examId;
        this.examName = examName;
        this.subTitle = subTitle;
        this.description = description;
        this.createTime = createTime;
        this.creator = creator;
        this.lastEditTime = lastEditTime;
        this.lastEditor = lastEditor;
        this.publishTime = publishTime;
        this.duration = duration;
        this.deadLine = deadLine;
    }

    @ApiModelProperty(value = "试卷id")
    private String examId;

    @NotBlank(message = "试题名称不能为空")
    @ApiModelProperty(value = "试卷名称")
    @Length(max = 50, message =  "试题名称长度必须小于 {max}")
    private String examName;

    @ApiModelProperty(value = "试卷子标题")
    @Length(max = 100, message =  "考试标题长度必须小于 {max}")
    private String subTitle;

    @ApiModelProperty(value = "试卷说明")
    private String description;

    @ApiModelProperty(value = "试卷创建时间")
    @JsonFormat( pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "创建人id")
    private String creator;

    @ApiModelProperty(value = "最后编辑时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date lastEditTime;

    @ApiModelProperty(value = "最后编辑人id")
    private String lastEditor;

    @ApiModelProperty(value = "发布时间，即可以开始考试的时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date publishTime;

    @Max(value = 1000,message = "考试时间不合理")
    @Min(value = 1,message = "考试时间不合理")
    @ApiModelProperty(value = "考试时长")
    private Integer duration;

    @ApiModelProperty(value = "考试类型")
    private Integer examType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(value = "考试截止时间")
    private Date deadLine;
    /**
     * 阅卷老师用户名
     */
    @ApiModelProperty(value = "阅卷老师用户名")
    private String teacherUserName;

    public TbExam(String examId, Date publishTime, Date deadLine) {
        this.examId = examId;
        this.publishTime = publishTime;
        this.deadLine = deadLine;
    }


    public TbExam(ExcelExamInfo excelExamInfo) {
        excelExamInfo.changeExamType();
        this.examName = excelExamInfo.getExamName();
        this.subTitle = excelExamInfo.getSubTitle();
        this.description = excelExamInfo.getDescription();
        this.examType = excelExamInfo.getExamType();
        try {
            if (StringUtils.isBlank(excelExamInfo.getPublishTime())){
                this.publishTime = null;
            }else{
                this.publishTime = SDF.parse(excelExamInfo.getPublishTime().trim());
            }
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            throw new BusinessException(ExceptionInfoEnum.EXAM_IMPORT_PUBLISH_TIME_ERROR);
        }
        if (StringUtils.isBlank(excelExamInfo.getDuration())|| (Integer.valueOf(excelExamInfo.getDuration().trim())) < 0) {
            this.duration = 0;
        } else {
            this.duration = Double.valueOf(excelExamInfo.getDuration())>1000?1000:Integer.valueOf(excelExamInfo.getDuration());
        }
        try {
            if (StringUtils.isBlank(excelExamInfo.getDeadLine())){
                this.deadLine = null;
            }else {
                this.deadLine = SDF.parse(excelExamInfo.getDeadLine().trim());
            }
        } catch (ParseException e) {
            throw new BusinessException(ExceptionInfoEnum.EXAM_IMPORT_DEADLINE_TIME_ERROR);
        }
    }

    public boolean isValid() {
        if(StringUtils.isBlank(this.examName)) {return false;}

        //考试名称长度校验
        if (this.examName.trim().length() > 50) {
            throw new BusinessException(ExceptionInfoEnum.EXAM_IMPORT_EXAM_NAME_OVER_SIZE);
        }

        //if(StringUtils.isBlank(this.subTitle)) return false;

        //子标题长度校验
        if (StringUtils.isNotBlank(this.subTitle) && this.subTitle.trim().length() > 100) {
            throw new BusinessException(ExceptionInfoEnum.EXAM_IMPORT_SUBTITLE_OVER_SIZE);
        }

        return true;
    }
}

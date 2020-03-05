package cn.tycad.oa.exam.model.entity;

import cn.tycad.oa.exam.common.enums.ExceptionInfoEnum;
import cn.tycad.oa.exam.exception.BusinessException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author：shizf
 * @Date: 0415
 * @Description: 用户评价模型
 */
@Data
@ApiModel(value = "TbReview", description = "用户评价")
@NoArgsConstructor
public class TbReview {
    @ApiModelProperty(value = "id, 写入时为空")
    private String reviewId;

    @ApiModelProperty(value = "用户id", required = true)
    private String userId;

    @ApiModelProperty(value = "考试id", required = true)
    private String examId;

    @ApiModelProperty(value = "扫码用户名")
    private String disposableUsername;

    @ApiModelProperty(value = "讲师课程内容准备之充分度", required = true)
    private Integer wellPrepared;

    @ApiModelProperty(value = "讲师授课的音量、口齿清晰度", required = true)
    private Integer pronunciation;

    @ApiModelProperty(value = "讲师专业知识/实践经验", required = true)
    private Integer experience;

    @ApiModelProperty(value = "讲师授课思路及条理性", required = true)
    private Integer logic;

    @ApiModelProperty(value = "课程内容理解程度", required = true)
    private Integer understandable;

    @ApiModelProperty(value = "教材制作精美度", required = true)
    private Integer handoutQuality;

    @ApiModelProperty(value = "讲师与学员课堂内互动状况", required = true)
    private Integer interaction;

    @ApiModelProperty(value = "讲师对学员提出的问题有无合理及满意的解答", required = true)
    private Integer answerOnQuestion;

    @ApiModelProperty(value = "课程对你本身工作之帮助性", required = true)
    private Integer helpful;

    @ApiModelProperty(value = "课程安排之合理性、设施配备之良好性", required = true)
    private Integer sensible;

    @ApiModelProperty(value = "总分", required = true)
    private Integer totalScore;

    @ApiModelProperty(value = "建议", required = true)
    private String suggestion;

    public TbReview(String examId, int wellPrepared, int pronunciation, int experience, int logic, int understandable, int handoutQuality, int interaction, int answerOnQuestion, int helpful, int sensible, int totalScore, String suggestion) {
        this.examId = examId;
        this.wellPrepared = wellPrepared;
        this.pronunciation = pronunciation;
        this.experience = experience;
        this.logic = logic;
        this.understandable = understandable;
        this.handoutQuality = handoutQuality;
        this.interaction = interaction;
        this.answerOnQuestion = answerOnQuestion;
        this.helpful = helpful;
        this.sensible = sensible;
        this.totalScore = totalScore;
        this.suggestion = suggestion;
    }

    /**
     * 判断评价打分字段是否有不合法的
     * @throws Exception
     */
    public void isValid() throws Exception {
        Field[] declaredFields = this.getClass().getDeclaredFields();
        List<Field> collect = Arrays.stream(declaredFields).filter(field -> field.getType() == Integer.class).collect(Collectors.toList());
        for (Field field : collect) {
            field.setAccessible(true);
            PropertyDescriptor pd = new PropertyDescriptor(field.getName(), this.getClass());
            Method method = pd.getReadMethod();
            Integer invoke = (Integer) method.invoke(this);
            if (invoke == null || invoke < 0){
                throw new BusinessException(ExceptionInfoEnum.REVIEW_SCORE_NOT_VALID);
            }
        }
    }

}

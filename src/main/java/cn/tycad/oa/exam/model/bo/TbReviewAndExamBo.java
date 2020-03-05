package cn.tycad.oa.exam.model.bo;

import cn.tycad.oa.exam.model.entity.TbReview;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Author: YY
 * @Date: 2019/8/1
 * @Description:
 */
@Data
public class TbReviewAndExamBo extends TbReview {
    public TbReviewAndExamBo(String examId, int wellPrepared, int pronunciation, int experience, int logic, int understandable, int handoutQuality, int interaction, int answerOnQuestion, int helpful, int sensible, int totalScore, String suggestion) {
        super(examId, wellPrepared, pronunciation, experience, logic, understandable, handoutQuality, interaction, answerOnQuestion, helpful, sensible, totalScore, suggestion);
    }
    private String teacherName;
    private String examName;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date publishTime;
}

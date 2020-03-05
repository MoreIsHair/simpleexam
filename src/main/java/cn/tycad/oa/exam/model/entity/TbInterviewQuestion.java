package cn.tycad.oa.exam.model.entity;

import cn.tycad.oa.exam.excel.ExcelInterviewQuestionInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author: YY
 * @Date: 2019/8/6
 * @Description: 用来表示面试考试的问题
 */
@Data
@NoArgsConstructor
public class TbInterviewQuestion {

    private String interviewQuestionId;
    /**
     * 题目类型，选择或者判断
     */
    private Integer interviewQuestionType;
    /**
     * 题目属于那些分类，java，.net，性格测试等等
     */
    private Integer interviewQuestionFollow;
    private String interviewQuestionTitle;
    private String interviewQuestionText;
    private Double interviewQuestionScore;

    @JsonFormat( pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    public TbInterviewQuestion(ExcelInterviewQuestionInfo questionInfo,String questionId) {
        this.interviewQuestionId = questionId;
        this.interviewQuestionType = questionInfo.getQuestionType();
        this.interviewQuestionFollow = questionInfo.getQuestionFollow();
        this.interviewQuestionTitle = questionInfo.getQuestionTitle();
        this.interviewQuestionText = questionInfo.getQuestionText();
        this.interviewQuestionScore = questionInfo.getQuestionScore();
        this.createTime = new Date();
    }
}

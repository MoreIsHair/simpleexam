package cn.tycad.oa.exam.model.entity;

import lombok.Data;

/**
 * @Author: YY
 * @Date: 2019/8/6
 * @Description: 面试题模板与题目的关系中间表
 */
@Data
public class TbInterviewTemplateQuestion {
    private String interviewTemplateId;
    private String InterviewQuestionId;
    private Integer OrderNum;
}

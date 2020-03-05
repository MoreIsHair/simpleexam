package cn.tycad.oa.exam.model.entity;

import lombok.Data;

import java.util.Date;

/**
 * @Author: YY
 * @Date: 2019/8/6
 * @Description: 从面试题库中挑选出来的一场试题（当做模板存储，方便以后多次使用）
 */
@Data
public class TbInterviewTemplate {
    private String interviewTemplateId;
    private String name;
    private String description;
    /**
     * 模板类型，java，Net ，cad，性格
     */
    private Integer followType;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建人（唯一用户名）
     */
    private String createUsername;
    /**
     * 考试时长
     */
    private Integer duration;
}

package cn.tycad.oa.exam.repository;

import cn.tycad.oa.exam.model.bo.InterviewQuestionAndOptionBo;
import cn.tycad.oa.exam.model.entity.TbInterviewTemplateQuestion;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: YY
 * @Date: 2019/8/6
 * @Description:
 */
@Mapper
public interface TbInterviewTemplateQuestionMapper {

    /**
     * 新增
     * @param tbInterviewTemplateQuestion
     */
    void insert(TbInterviewTemplateQuestion tbInterviewTemplateQuestion);

    /**
     * 查找试题模板对应的所有选择题及其选项
     * @param templateId
     * @return
     */
    List<InterviewQuestionAndOptionBo> findAllOptionQuestionByTemplateId(String templateId);
}

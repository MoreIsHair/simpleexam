package cn.tycad.oa.exam.repository;

import cn.tycad.oa.exam.model.entity.TbValidAnswer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: YY
 * @Date: 2019/3/8
 * @Description:
 */
@Mapper
public interface TbValidAnswerMapper {

    /**
     * 写入正确答案
     * @param tbValidAnswer
     */
    void insert(TbValidAnswer tbValidAnswer);

    /**
     * 根据问题ID查找正确答案
     * @param questionId
     * @return
     */
    TbValidAnswer findByQuestionId(@Param("questionId") String questionId);

    /**
     * 根据问题id删除正确答案
     * @param questionId
     */
    void deleteByQuestionId(String questionId);
}

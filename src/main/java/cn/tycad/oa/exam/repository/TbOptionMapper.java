package cn.tycad.oa.exam.repository;

import cn.tycad.oa.exam.model.entity.TbOption;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: YY
 * @Date: 2019/3/8
 * @Description:
 */
@Mapper
public interface TbOptionMapper {
    /**
     * 新增
     * @param tbOption
     */
    void insert(TbOption tbOption);

    /**
     * 查找用户选择题的答案
     * @param userId
     * @param questionId
     * @return
     */
    List<TbOption> findByUserIdAndQuestionId(@Param("userId") String userId, @Param("questionId") String questionId);

    /**
     * 删除问题的所有选项
     * @param questionId
     */
    void deleteByQuestionId(@Param("questionId") String questionId);
}

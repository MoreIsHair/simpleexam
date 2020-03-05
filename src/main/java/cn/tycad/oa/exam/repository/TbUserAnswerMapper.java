package cn.tycad.oa.exam.repository;

import cn.tycad.oa.exam.model.entity.TbUserAnswer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: YY
 * @Date: 2019/3/8
 * @Description:
 */
@Mapper
public interface TbUserAnswerMapper {
    /**
     * 查询用户所有回答的分数
     * @param examId
     * @param userId
     * @return
     */
     List<Float> findUserScore(@Param("examId") String examId, @Param("userId") String userId);

    /**
     * 新增
     * @param tbUserAnswer
     */
    void insert(TbUserAnswer tbUserAnswer);

    /**
     * 通过用户ID以及问题ID批量更新用户的简单题的分数
     * @param scores
     */
    void updateBatchByUserIdAndQuestionId(List<TbUserAnswer> scores);

    /**
     * 通过用户ID以及问题ID查询用户该问题的得分
     * @param userId
     * @param questionId
     * @return
     */
    Float findSingleUserScore(@Param("userId") String userId, @Param("questionId") String questionId);

    /**
     * 删除用户回答
     * @param userId 用户ID
     * @param examId 考试ID
     */
    void deleteByUserIdAndQuestionIds(@Param("userId") String userId, @Param("examId") String examId);

    /**
     * 批量保存用户回答
     * @param answers
     */
    void insertBatch(@Param("examId") String examId, @Param("userId") String userId, @Param("answers") List<TbUserAnswer> answers);
}

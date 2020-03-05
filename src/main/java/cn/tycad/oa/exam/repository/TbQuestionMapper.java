package cn.tycad.oa.exam.repository;

import cn.tycad.oa.exam.model.bo.OptionQuestionAndOptionBo;
import cn.tycad.oa.exam.model.bo.UserQuestionAndAnswerValueBo;
import cn.tycad.oa.exam.model.entity.TbQuestion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: YY
 * @Date: 2019/3/6
 * @Description:
 */
@Mapper
public interface TbQuestionMapper {
    /**
     * 新增一个问题（所属哪个试题）
     * @param tbQuestion
     */
     void insert(TbQuestion tbQuestion);

    /**
     * 修改试题中问题的标题或者选项（需要有问题ID和试题ID）
     * @param tbQuestion
     */
     void updateTitleOrTextByTbQuestion(TbQuestion tbQuestion);

    /**
     * 查询试题中的非选择题
     * 暂认-1多项选择，0为单选择题，1为判断题，2为简单题
     * @param examId
     * @return
     */
     List<TbQuestion> findByExamId(String examId);

    /**
     * 查询试题中的所有问题
     * 暂认-1多项选择，0为单选择题，1为判断题，2为简单题
     * @param examId
     * @return
     */
     List<TbQuestion> findAllByExamId(String examId);


    /**
     * 查询用户所有非选择题以及回答值
     * @param examId
     * @param userId
     * @param questionType 问题类型
     * @return
     */
     List<UserQuestionAndAnswerValueBo> findQuestionAndAnswerValue(@Param("examId") String examId, @Param("userId") String userId,@Param("questionType") int questionType);

    /**
     * 删除试题（一个ExamId对应的所有问题）
     * @param examId
     */
    void deleteExamId(String examId);

    /**
     * 查找试题中所有的选择题ID
     * @param examId
     * @param questionType
     * @return
     */
    List<String> findOptionQuestionByExamIdAndQuestionType(@Param("examId") String examId,@Param("questionType") Integer questionType);

    /**
     * 根据考试ID一级问题类型查询问题数量
     * @param examId
     * @param questionType
     * @return
     */
    Integer findHasQuestionByExamIdAndQuestionType(@Param("examId") String examId,@Param("questionType") Integer questionType);

    /**
     * 查询试题中选择题以及选项
     * @param examId
     * @return
     */
    List<OptionQuestionAndOptionBo> findOptionQuestionAndOption(@Param("examId") String examId);




}

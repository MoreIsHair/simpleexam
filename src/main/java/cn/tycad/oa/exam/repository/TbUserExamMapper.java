package cn.tycad.oa.exam.repository;

import cn.tycad.oa.exam.model.bo.UserOptionBo;
import cn.tycad.oa.exam.model.entity.TbUserAnswer;
import cn.tycad.oa.exam.model.entity.TbUserExam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @Author: YY
 * @Date: 2019/3/6
 * @Description:
 */
@Mapper
public interface TbUserExamMapper {
    /**
     * 新增
     * @param tbUserExam
     */
    void insert(TbUserExam tbUserExam);

    /**
     * 根据考试查询分数
     * @param userId
     * @param examId
     * @return
     */
    TbUserExam selectByExamIdAndUserId(@Param(value = "examId") String examId, @Param("userId") String userId);

    /**
     * 根据用户与考试修改分数
     * @param examId
     * @param userId
     * @param score
     */
    void updateScore(@Param("examId") String examId,@Param("userId") String userId,@Param("score") double score);

    /**
     * 查询用户的所有考试
     * @param userId
     * @return
     */
    List<TbUserExam> findByUserId(@Param("userId") String userId);

    /**
     * 查询用户某次考试的信息
     * @param userId
     * @param examId
     * @return
     */
    TbUserExam findByUserIdAndExamId(@Param("userId") String userId, @Param("examId") String examId);

    /**
     * 用户提交回答
     * @param answers 用户答案
     * @param examId 考试id
     * @param userId 用户id
     * @param commitTime 提交时间
     */
    void commitAnswer(String examId, String userId, @Param("answers") List<TbUserAnswer> answers, Date commitTime);

    /**
     * 查找是否已经参加过考试
     * @param userId 用户ID
     * @param examId 考试ID
     * @return 数量
     */
    int checkDone(String userId, String examId);

    /**
     * 查找一次考试对应的所有答案
     * @param examId 考试id
     * @return 题目和正确答案列表
     */
    List<UserOptionBo> getAllValidAnswers(String examId);

    /**
     * 查询该试题是否已经被评改了
     * @param examId
     * @param userId
     * @return
     */
    int findIsScore(@Param("examId") String examId, @Param("userId") String userId);

    /**
     * 查找试卷是否已经提交
     * @param examId
     * @param userId
     * @return
     */
    int findIsFinish(@Param("examId") String examId, @Param("userId") String userId);

    /**
     * 将考试时间已经截止的考生全赋予零分
     * @param date
     */
    void updateScoreByOvertime(@Param("nowDate") Date date);

    /**
     * 通过考试ID删除所有的待参考
     * @param examId
     */
    void deleteByExamId(@Param("examId") String examId);

    /**
     * 更新考试考试的时间
     * @param userId
     * @param examId
     * @param date
     */
    void updateStartExamTime(@Param("userId") String userId, @Param("examId") String examId, @Param("startExamTime") Date date);

    /**
     * 根据试题ID查询考生ID
     * @param examId 试题ID
     * @return
     */
    Set<String> findUserIdsByExamId(@Param("examId") String examId);

    /**
     * 更新考试的暂停时间
     * @param date
     * @param examId
     * @param userId
     */
    void updateSuspendTime(@Param("date") Date date,@Param("examId") String examId,@Param("userId") String userId);

    /**
     * 保存剩余考试时长
     * @param surplus
     * @param examId
     * @param userId
     */
    void updateSurplus(@Param("surplus") double surplus, @Param("examId") String examId, @Param("userId") String userId);
}

package cn.tycad.oa.exam.repository;

import cn.tycad.oa.exam.model.bo.*;
import cn.tycad.oa.exam.model.entity.TbExam;
import cn.tycad.oa.exam.model.entity.TbUserExam;
import cn.tycad.oa.exam.model.param.PageInfoAndUserNameAndExamNameParam;
import cn.tycad.oa.exam.model.param.UserIdAndTimeAndExamNameAndCreatorParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @Author: YY
 * @Date: 2019/3/5
 * @Description:
 */
@Mapper
public interface TbExamMapper {

    /**
     * 新增
     * @param exam
     */
    void insert(TbExam exam);

    /**
     * 通过主键查询
     * @param examId
     * @return
     */
    TbExam selectByExamId(String examId);

    /**
     * 删除
     * @param examId
     */
    void deleteByExamId(String examId);

    /**
     * 更新（Id不能为空）
     * @param exam
     */
    void updateByExam(TbExam exam);

    /**
     * 查询所有的考试任务
     * @param examName 通过试题名称进行模糊查找
     * @return
     */
    List<TbExamPagerBo> findAllFilterExamName(@Param("examName") String examName);

    /**
     * 查询所有的考试任务
     * @return
     */
    List<TbExam> findAll();

    /**
     * 根据用户是否参加考试来查询考试信息
     * @param userId
     * @param examStatus 是否已参加 0表示未参加 1表示已参加
     * @return
     */
    List<SingleExamAndScoreBo> findExamAndUserScore(@Param("userId") String userId,@Param("examStatus") int examStatus);

    /**
     *  查询考试信息（带条件）
     * @param term
     * @param examStatus
     * @param nowDate
     * @return
     */
    List<SingleExamAndScoreBo> findFilterExamAndUserScore(@Param("term")UserIdAndTimeAndExamNameAndCreatorParam term, @Param("examStatus") int examStatus, @Param("nowDate")Date nowDate);


    /**
     *  每场考试的成绩统计（该场考试的平均分）按照考试的发布时间升序
     * @param examName
     * @return
     */
    List<TbExamAndAverageBo> findExamAndAverage(@Param("examName") String examName);

    /**
     *  查询所有人的所有已考试信息
     * @return
     */
    List<TbExamAndUserInfoAndDepartInfoBo> findAllExamInfoAndUserAndDepartmentAndScore(PageInfoAndUserNameAndExamNameParam param);

    /**
     * 获取单次考试的所有考生信息
     * @param examId
     * @return
     */
    List<SingleExamAndScoreAndUserInfo > getExamAllUserInfoAndScore(@Param("examId") String examId);

    /**
     * 获取单次考试的所有考生信息
     * @param examId
     * @return
     */
    List<ExamUserBo> getExamUsers(String examId);

    /**
     * 获取待阅卷的列表
     * @param examName 考试名称
     * @param userName 考生名称
     * @param teacherName 阅卷考试名称
     * @return
     */
    List<TbExamAndUserInfoBo> findFutureMarkPapersExam(@Param("examName") String examName, @Param("userName") String userName,@Param("teacher") String teacherName);

    /**
     * 查询用户是否有可以参加该考试的
     * @param examId
     * @param userId
     * @return
     */
    TbUserExam findIsHasFutureExam(@Param("examId") String examId, @Param("userId")String userId);

    /**
     * 通过ID和类型查找考试是否存在
     * @param examId
     * @param type
     * @return
     */
    int findExamByIdAndType(@Param("examId") String examId,@Param("examType") int type);
}

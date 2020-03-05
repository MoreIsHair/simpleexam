package cn.tycad.oa.exam.repository;

import cn.tycad.oa.exam.model.bo.TbReviewAndExamBo;
import cn.tycad.oa.exam.model.entity.TbReview;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author shizf
 * @date 20190408
 * @description: 学生评价mapper
 */
@Mapper
public interface TbReviewMapper {

    /**
     * 写入用户评价
     * @param review
     */
    void insert(TbReview review);

    /**
     * 获取所有学生评价
     * @return
     */
    List<TbReview> getList();

    /**
     * 查看某次考试的所有评分
     * @param examId
     * @param userId
     * @return
     */
    List<TbReview> getListByExamId(String examId, String userId);

    /**
     * 根据考试id和学生id或者一次性用户名获取评分数量
     * @param examId
     * @param userId
     * @param disposableUsername
     * @return
     */
    int getCountByExamIdAndUserId(@Param("examId") String examId, @Param("userId") String userId,@Param("disposableUsername") String disposableUsername);

    /**
     * 通过考试Id获取所有评价
     * @param min
     * @param max
     * @return
     */
    List<TbReviewAndExamBo> getAllExamListByExamIdSort(@Param("minTime") Date min , @Param("maxTime") Date max);
}

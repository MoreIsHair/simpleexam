package cn.tycad.oa.exam.repository;

import cn.tycad.oa.exam.model.entity.TbDisposableExamInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: YY
 * @Date: 2019/8/6
 * @Description:
 */
@Mapper
public interface TbDisposableExamInfoMapper {

    void insert(TbDisposableExamInfo tbDisposableExamInfo);

    /**
     * 更新一次性考试总分
     * @param disposableUserName
     * @param examId
     * @param sum
     */
    void updateDisposableScoreByExamId(@Param("disposableUserName") String disposableUserName, @Param("examId") String examId, @Param("sum") Double sum);
    /**
     * 更新一次性考试总分
     * @param disposableUserName
     * @param templateId
     * @param sum
     */

    void updateDisposableScoreByTemplateId(@Param("disposableUserName") String disposableUserName, @Param("templateId") String templateId, @Param("sum") Double sum);

    /**
     * 通过名称查找该是否被使用过
     * @param disposableUsername
     * @return
     */
    TbDisposableExamInfo findByDisUserName(@Param("disposableUsername") String disposableUsername);


    /**
     * 查找一次性用户的所有信息
     */
    List<TbDisposableExamInfo> findAll();
}

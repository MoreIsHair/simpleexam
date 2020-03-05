package cn.tycad.oa.exam.repository;

import cn.tycad.oa.exam.model.entity.TbInterviewQuestion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: YY
 * @Date: 2019/8/6
 * @Description:
 */
@Mapper
public interface TbInterviewQuestionMapper {

    /**
     * 插入
     * @param tbInterviewQuestion
     */
    void insert(TbInterviewQuestion tbInterviewQuestion);

    /**
     * 获取所有问题通过问题所属组类型
     * @param follow
     * @return
     */
    List<TbInterviewQuestion> findAllByQuestionFollow(@Param("follow") int follow);
}

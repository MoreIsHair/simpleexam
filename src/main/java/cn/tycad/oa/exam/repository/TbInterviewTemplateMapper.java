package cn.tycad.oa.exam.repository;

import cn.tycad.oa.exam.model.entity.TbInterviewTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: YY
 * @Date: 2019/8/6
 * @Description:
 */
@Mapper
public interface TbInterviewTemplateMapper {
    /**
     * 新增
     * @param tbInterviewTemplate
     */
   void insert(TbInterviewTemplate tbInterviewTemplate);

    /**
     * 获取该组类型所有模板
     * @param follow
     * @return
     */
    List<TbInterviewTemplate> findAllByFollow(@Param("follow") int follow);

    /**
     *
     * @param templateId
     * @return
     */
    TbInterviewTemplate findIsExistByTemplateId(@Param("templateId") String templateId);
}

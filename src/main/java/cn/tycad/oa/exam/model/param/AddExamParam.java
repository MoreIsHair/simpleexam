package cn.tycad.oa.exam.model.param;

import cn.tycad.oa.exam.model.entity.TbExam;
import lombok.Data;

/**
 * @Author: YY
 * @Date: 2019/3/8
 * @Description:
 */
@Data
public class AddExamParam extends TbExam {
    private String[] students;
    private String teacher;
}

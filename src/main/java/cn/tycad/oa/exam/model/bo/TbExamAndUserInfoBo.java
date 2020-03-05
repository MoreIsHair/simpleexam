package cn.tycad.oa.exam.model.bo;

import cn.tycad.oa.exam.model.entity.TbExam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: YY
 * @Date: 2019/4/17
 * @Description: 阅卷列表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TbExamAndUserInfoBo extends TbExam {
    private String userId;
    private String userName;
}

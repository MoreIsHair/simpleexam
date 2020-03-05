package cn.tycad.oa.exam.model.bo;

import cn.tycad.oa.exam.model.entity.TbDisposableExamInfo;
import cn.tycad.oa.exam.model.entity.TbExam;
import lombok.Data;

/**
 * @Author: YY
 * @Date: 2019/8/15
 * @Description:
 */
@Data
public class ExamAndDisposableExamInfoBo {
    private TbExam exam;
    private TbDisposableExamInfo disposableExamInfo;
}

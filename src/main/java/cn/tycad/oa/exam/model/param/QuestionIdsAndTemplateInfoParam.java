package cn.tycad.oa.exam.model.param;

import cn.tycad.oa.exam.model.entity.TbInterviewTemplate;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: YY
 * @Date: 2019/8/12
 * @Description:
 */
@Data
@NoArgsConstructor
public class QuestionIdsAndTemplateInfoParam extends TbInterviewTemplate {
    public List<QuestionIdAndOrderNum> qos;

    @Data
    public class QuestionIdAndOrderNum {
        String questionId;
        Integer orderNum;
    }
}

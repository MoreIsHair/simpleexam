package cn.tycad.oa.exam.model.param;

import cn.tycad.oa.exam.model.bo.UserAnswerBo;
import cn.tycad.oa.exam.model.entity.TbReview;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: YY
 * @Date: 2019/8/13
 * @Description:
 */
@Data
@NoArgsConstructor
@ApiModel("提交答题参数")
public class AnswerAndReviewParam {
    @ApiModelProperty("用户所做的答案")
    private UserAnswerBo userAnswerBo;
    @ApiModelProperty("用户所做的评价")
    private TbReview tbReview;
}

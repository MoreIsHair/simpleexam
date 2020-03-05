package cn.tycad.oa.exam.model.vo;

import cn.tycad.oa.exam.model.bo.TbReviewAndExamBo;
import lombok.Data;

import java.util.List;

/**
 * @Author: YY
 * @Date: 2019/8/2
 * @Description:
 */
@Data
public class TbReviewAvgAndExamRankVo {
    private List<TbReviewAndExamBo> tbReviewAndExamBos;
    private Double average;
}

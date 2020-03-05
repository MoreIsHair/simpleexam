package cn.tycad.oa.exam.model.bo;

import cn.tycad.oa.exam.model.entity.TbExam;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: YY
 * @Date: 2019/4/3
 * @Description: 考试信息以及参加该场考试的平均分
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "考试信息以及参加该场考试的平均分")
public class TbExamAndAverageBo extends TbExam {
    @ApiModelProperty(value = "平均分")
    private Float average;
}

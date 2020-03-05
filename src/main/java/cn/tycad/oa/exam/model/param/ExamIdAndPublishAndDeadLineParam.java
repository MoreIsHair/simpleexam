package cn.tycad.oa.exam.model.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @Author: YY
 * @Date: 2019/3/14
 * @Description:
 */
@Data
@AllArgsConstructor
public class ExamIdAndPublishAndDeadLineParam {
    @ApiModelProperty(value = "考试id")
    private String examId;

    @ApiModelProperty(value = "发布时间")
    private Date publishTime;

    @ApiModelProperty(value = "考试截止时间")
    private Date deadLine;
}

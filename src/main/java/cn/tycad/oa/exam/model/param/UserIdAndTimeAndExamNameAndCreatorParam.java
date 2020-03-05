package cn.tycad.oa.exam.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author: YY
 * @Date: 2019/3/20
 * @Description: 查询用户的所有考试信息的参数以及过滤条件
 */
@Data
@ApiModel(description = "查询用户的所有考试信息的参数以及过滤条件")
public class UserIdAndTimeAndExamNameAndCreatorParam {
    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "参与考试的用户名，用于过滤查询结果")
    private String username;

    @ApiModelProperty(value = "最老的时间")
    private Date minTime;

    @ApiModelProperty(value = "最新的时间")
    private Date maxTime;

    @ApiModelProperty(value = "考试名称")
    private String examName;

    @ApiModelProperty(value = "创建人")
    private String creator;

    @ApiModelProperty("当前页数")
    private Integer pageNum;

    @ApiModelProperty("当页显示的数量")
    private Integer pageSize;
}

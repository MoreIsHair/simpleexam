package cn.tycad.oa.exam.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author: YY
 * @Date: 2019/8/6
 * @Description: 用来描述对于一次性用户的基本信息以及考试总分的模型对象
 */
@Data
@NoArgsConstructor

@ApiModel("扫码用户信息表")
public class TbDisposableExamInfo {
    private String disposableId;
    @ApiModelProperty(value = "扫描用户名",required = true)
    private String disposableUsername;
    private Double score;
    private String examId;
    private String templateId;
    private Date finishExamTime;
    private Date startExamTime;
    private Date suspendTime;
    private Double surplus;
    @ApiModelProperty(value = "扫描用户所在公司名",required = true)
    private String disposableCompany;
    @ApiModelProperty(value = "扫描用户电话号码",required = true)
    private String disposableTel;
    private Integer examStatus;
}

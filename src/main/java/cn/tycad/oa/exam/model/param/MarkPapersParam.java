package cn.tycad.oa.exam.model.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: YY
 * @Date: 2019/3/28
 * @Description: 管理员修改试卷提交的参数
 */
@Data
public class MarkPapersParam {
    @ApiModelProperty(value = "考试id")
    private String examId;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "问题和分数列表")
    private List<QuestionIdAndScore> questionIdAndScores;

    @Data
    public static class QuestionIdAndScore{
        private String questionId;
        private Float score;
    }
}

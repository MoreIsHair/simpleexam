package cn.tycad.oa.exam.model.vo;

import cn.tycad.oa.exam.model.entity.TbExam;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

/**
 * @Author: YY
 * @Date: 2019/4/15
 * @Description: 用来接收更新考试相关信息（试题信息、指定参考的用户）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonDeserialize(using = UpdateExamInfoVoDeserializer.class)
public class UpdateExamInfoVo extends TbExam {
    private Set<String> userIds;

    public UpdateExamInfoVo(String examId, String examName, String subTitle,
                            String description, Date createTime, String creatorId,
                            Date lastEditTime, String lastEditor, Date publishTime,
                            Integer duration, Date deadline, Set<String> userIds) {
        super(examId, examName, subTitle, description, createTime, creatorId,
                lastEditTime, lastEditor, publishTime, duration, deadline);
        this.userIds = userIds;
    }
}

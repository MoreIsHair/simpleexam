package cn.tycad.oa.exam.model.bo;

import cn.tycad.oa.exam.model.entity.TbExam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: shizf
 * @Date: 0415
 * @Description: 用于分页的考试信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TbExamPagerBo extends TbExam {

    private String status;

    private String teacherName;

    public TbExamPagerBo(TbExam exam, String status) {
        this.status = status;
        this.setCreateTime(exam.getCreateTime());
        this.setCreator(exam.getCreator());
        this.setExamId(exam.getExamId());
        this.setLastEditor(exam.getLastEditor());
        this.setLastEditTime(exam.getLastEditTime());
        this.setTeacherUserName(exam.getTeacherUserName());
        this.setDeadLine(exam.getDeadLine());
        this.setDescription(exam.getDescription());
        this.setDuration(exam.getDuration());
        this.setExamName(exam.getExamName());
        this.setPublishTime(exam.getPublishTime());
        this.setSubTitle(exam.getSubTitle());
    }
}

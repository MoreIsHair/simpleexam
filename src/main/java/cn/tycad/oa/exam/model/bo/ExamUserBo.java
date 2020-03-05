package cn.tycad.oa.exam.model.bo;

import cn.tycad.oa.exam.model.entity.SystemUser;
import lombok.Data;

/**
 * @Author: shizf
 * @Date: 0417
 * @Description: 某次考试的用户模型
 */
@Data
public class ExamUserBo extends SystemUser {
    private boolean belongTo;
}

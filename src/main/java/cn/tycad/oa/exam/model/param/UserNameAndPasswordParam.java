package cn.tycad.oa.exam.model.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: YY
 * @Date: 2019/3/14
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserNameAndPasswordParam {
    private String userName;
    private String password;
}

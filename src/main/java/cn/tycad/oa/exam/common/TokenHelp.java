package cn.tycad.oa.exam.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: YY
 * @Date: 2019/4/22
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenHelp implements Serializable {
    private String userId;
    private String userName;
    private Date lastRequestTime;
    private String token;

    public TokenHelp(String userId, String userName, Date lastRequestTime) {
        this.userId = userId;
        this.userName = userName;
        this.lastRequestTime = lastRequestTime;
    }
}

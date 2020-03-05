package cn.tycad.oa.exam.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: YY
 * @Date: 2019/3/15
 * @Description:
 */
@Slf4j
public class UserUtils {
    /**
     * 密码以及盐值加密
     * @param original
     * @param salt
     * @return
     */
    public static String md5DigestAsHex(String original,String salt){
        String s = DigestUtils.md5DigestAsHex(original.getBytes());
        String result = DigestUtils.md5DigestAsHex((s + salt).getBytes());
        return result;
    }

    /**
     * 格式化日期
     * @param date
     * @return
     */
    public static Date getFormattedDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        String resultDate = sdf.format(date);
        return new Date(resultDate);
    }
    public static String getToken(HttpServletRequest request){
        String token = request.getHeader("token");
        if (StringUtils.isBlank(token)){
            log.debug("token为空");
            return "";
        }
        return token;
    }
}

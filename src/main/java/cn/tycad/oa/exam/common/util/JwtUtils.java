package cn.tycad.oa.exam.common.util;

import cn.tycad.oa.exam.common.enums.ExceptionInfoEnum;
import cn.tycad.oa.exam.exception.BusinessException;
import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.internal.com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: YY
 * @Date: 2019/4/8
 * @Description: jwt的工具类
 */
@Slf4j
public class JwtUtils {

    private static final String EXP = "exp";

    /**
     * 负荷
     */
    private static final String PAYLOAD = "payload";

    /**
     * get jwt String of object
     * @param object
     * @param maxAge
     * @return the jwt token
     */
    public static <T> String createJWT(T object, Float maxAge, String secret) {
        try {
            final JWTSigner signer = new JWTSigner(secret);
            final Map<String, Object> claims = new HashMap<>(16);
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(object);
            claims.put(PAYLOAD, jsonString);
            // 失效时间
            claims.put(EXP, System.currentTimeMillis() + maxAge);
            return signer.sign(claims);
        } catch(Exception e) {
            log.error("创建token异常" + e.getMessage());
            return null;
        }
    }


    /**
     * get the object of jwt if not expired
     * @param jwt
     * @return POJO object
     */
    public static<T> T parseJWTNotExpired(String jwt, Class<T> classT,String secret) throws Exception{
        final JWTVerifier verifier = new JWTVerifier(secret);
        try {
            log.debug("错误排查==============>查找token");
            final Map<String,Object> claims= verifier.verify(jwt);
            if (claims.containsKey(EXP) && claims.containsKey(PAYLOAD)) {
                long exp = (Long)claims.get(EXP);
                long currentTimeMillis = System.currentTimeMillis();
                log.debug("错误排查==============>找到token");
                log.debug("错误排查==============> exp :" + exp + ",,,current: " + currentTimeMillis);
                if (exp > currentTimeMillis) {
                    log.debug("错误排查==============>token合法");
                    String json = (String)claims.get(PAYLOAD);
                    ObjectMapper objectMapper = new ObjectMapper();
                    // 表示可用
                    return objectMapper.readValue(json, classT);
                }else {
                    log.debug("错误排查==============>token过期");
                    // 表示失效
                    log.debug("Token已经失效");
                    throw new BusinessException(ExceptionInfoEnum.JWT_TIME_EXPIRE_ERROR);
                }
            }
        } catch (Exception e) {
            if (e instanceof BusinessException){
                throw e;
            }
            log.debug("JWT解析异常");
            log.error("token 解析异常" + e.getMessage());
            throw new BusinessException(ExceptionInfoEnum.JWT_RESOLVE_ERROR);
        }
        return null;
    }
    /**
     * get the object of jwt AnyHow
     * @param jwt
     * @return POJO object
     */
    public static<T> T parseJWT(String jwt, Class<T> classT,String secret){
        final JWTVerifier verifier = new JWTVerifier(secret);
        final Map<String,Object> claims;
        try {
            claims = verifier.verify(jwt);
            if (claims.containsKey(EXP) && claims.containsKey(PAYLOAD)) {
                ObjectMapper objectMapper = new ObjectMapper();
                String json = (String)claims.get(PAYLOAD);
                return objectMapper.readValue(json, classT);
            }
        } catch (Exception e) {
            log.debug("JWT解析异常");
            log.debug(e.getMessage());
            throw new BusinessException(ExceptionInfoEnum.JWT_RESOLVE_ERROR);
        }
        return null;
    }
}


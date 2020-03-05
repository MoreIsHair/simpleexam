package cn.tycad.oa.exam.interceptor;

import cn.tycad.oa.exam.annotation.RequiredPermission;
import cn.tycad.oa.exam.common.PermissionConstants;
import cn.tycad.oa.exam.common.TokenHelp;
import cn.tycad.oa.exam.common.util.JwtUtils;
import cn.tycad.oa.exam.common.util.UserUtils;
import cn.tycad.oa.exam.exception.BusinessException;
import cn.tycad.oa.exam.model.entity.SystemUser;
import cn.tycad.oa.exam.model.entity.TbDisposableExamInfo;
import cn.tycad.oa.exam.model.vo.Result;
import cn.tycad.oa.exam.repository.TbDisposableExamInfoMapper;
import cn.tycad.oa.exam.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @Author: YY
 * @Date: 2019/3/6
 * @Description: 权限拦截器
 */
@Component
@Slf4j
public class AuthorityInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Autowired
    private TbDisposableExamInfoMapper tbDisposableExamInfoMapper;

    @Value("${jwt.expire}")
    private Float expire;

    @Value("${jwt.secret}")
    private String secret;


    private static Map<String, TokenHelp> systemUserMap = new HashMap<>();

    public Map<String, TokenHelp> getSystemUserMap() {
        return systemUserMap;
    }


    /**
     * 进入controller层之前拦截请求
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断是否属于需要验证的权限
        if (isControlPermission(handler)){
                return true;
        }
        ObjectMapper mapper = new ObjectMapper();
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        // 检查Token
        String token= UserUtils.getToken(request);

        if (StringUtils.isBlank(token)){
            response.getWriter().print(mapper.writeValueAsString(Result.error("未携带Token")));
            return false;
        }
        // 如果是扫码用户，直接放行，不进行权限控制(约定扫码用户传递一个用户名进行校验使用uname为前缀)
        if (StringUtils.contains(token,"uname")){
            TbDisposableExamInfo un = tbDisposableExamInfoMapper.findByDisUserName(StringUtils.remove(token, "uname"));
            if (un !=null) {
                return true;
            }else {
                response.getWriter().print(mapper.writeValueAsString(Result.error("不是有效的用户")));
                return false;
            }
        }

        try {
            SystemUser user =  checkTokenAndRefresh(token);
            if (user == null) {
                response.getWriter().print(mapper.writeValueAsString(Result.error("Token失效")));
                return false;
            }
        if (PermissionConstants.LOGOUT.equals(getPermission(handler).value())){
            return true;
        }
        // 验证权限
        if (this.hasPermission(handler,user)) {
            return true;
        }
        //  null == request.getHeader("x-requested-with") 暂时用这个来判断是否为ajax请求
        // 如果没有权限 则抛403异常
        response.getWriter().print(mapper.writeValueAsString(Result.error("无权限")));
        return false;
        }catch (Exception e){
            if (e instanceof BusinessException){
                response.getWriter().print(mapper.writeValueAsString(Result.error(((BusinessException) e).getExceptionInfoEnum().getMsg())));
                return false;
            }
            else {
                response.getWriter().print(mapper.writeValueAsString(Result.error("未知错误")));
                return false;
            }
        }
    }

    /**
     * 是否有权限
     * @param handler
     * @return
     */
    private boolean hasPermission(Object handler,SystemUser user) {
        RequiredPermission requiredPermission = this.getPermission(handler);
        // 如果标记了注解，则判断权限
            if (requiredPermission != null && requiredPermission.value().length>=1) {
                // 数据库中获取该用户的权限信息 并判断是否有权限
                Set<String> permissionSet = new HashSet<>(userService.getPermission(user.getUserId()));
                if (CollectionUtils.isEmpty(permissionSet) ){
                    return false;
                }
                return !Collections.disjoint(permissionSet,Arrays.asList(requiredPermission.value()));
            }
        return true;
    }

    /**
     * 是否是不需要控制的权限
     * @param handler
     * @return
     */
    private boolean isControlPermission(Object handler) {
        RequiredPermission requiredPermission = this.getPermission(handler);
        if (requiredPermission != null && requiredPermission.value().length>=1) {
                Set<String> permissionSet = PermissionConstants.getNotNeedControlPermissions();
                if (CollectionUtils.isEmpty(permissionSet) ){
                    return false;
                }
                return !Collections.disjoint(permissionSet,Arrays.asList(requiredPermission.value()));
            }
       return true;
    }

    /**
     * 检查Token
     * @param token
     * @return
     * @throws
     */
    private SystemUser checkTokenAndRefresh(String token) throws  Exception{
        SystemUser systemUser = JwtUtils.parseJWT(token, SystemUser.class, secret);
        if (this.getSystemUserMap().get(systemUser.getUsername()) ==null ){
            return null;
        }
        TokenHelp origina = this.systemUserMap.get(systemUser.getUsername());
        if (origina!= null && origina.getLastRequestTime() != null){
            if (origina.getLastRequestTime().getTime() + (expire * 60 * 1000) > System.currentTimeMillis()){
                TokenHelp tokenHelp = new TokenHelp(systemUser.getUserId(),systemUser.getUsername(), new Date(),token);
                this.getSystemUserMap().put(systemUser.getUsername(),tokenHelp);
                return systemUser;
            }
        }
        systemUser = JwtUtils.parseJWTNotExpired(token, SystemUser.class, secret);
        return systemUser;
    }
    private RequiredPermission getPermission(Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // 获取方法上的注解
            RequiredPermission requiredPermission = handlerMethod.getMethod().getAnnotation(RequiredPermission.class);
            // 如果方法上的注解为空 则获取类的注解
            if (requiredPermission == null) {
                requiredPermission = handlerMethod.getMethod().getDeclaringClass().getAnnotation(RequiredPermission.class);
            }
            return  requiredPermission;
        }
        return null;
    }
}

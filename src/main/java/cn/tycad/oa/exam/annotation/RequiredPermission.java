package cn.tycad.oa.exam.annotation;

import java.lang.annotation.*;

/**
 * @Author: YY
 * @Date: 2019/3/14
 * @Description: 权限的注解
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RequiredPermission {
    String[] value();
}

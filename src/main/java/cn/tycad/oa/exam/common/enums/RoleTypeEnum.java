package cn.tycad.oa.exam.common.enums;

/**
 * @Author: YY
 * @Date: 2019/4/24
 * @Description:
 */
public enum RoleTypeEnum {
    NORMAL_USER(0,"普通用户"),
    TEACHER_USER(1,"阅卷老师"),
    ADMIN_USER(2,"超级管理员");

    RoleTypeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
    private Integer code;
    private String message;

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }}

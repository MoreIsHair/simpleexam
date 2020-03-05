package cn.tycad.oa.exam.common;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author: YY
 * @Date: 2019/3/14
 * @Description: 权限常量（每个权限的Name，配合权限注解使用）
 */
public class PermissionConstants {

    /**
     * 不需要验证的权限（登陆、注册、注销）
     */
    public static final String LOGIN = "login";
    public static final String REGISTER = "register";
    /**
     * 需要验证的权限
     */
    public static final String LOGOUT = "logout";
    public static final String LOGGED_IN = "logged_in";
    public static final String USER_DETAIL = "user_manager_detail";
    public static final String USER_ADD = "user_manager_add";
    public static final String USER_DELETE = "user_manager_delete";
    public static final String ADMIN_EXAM_EXCEL_IMPORT = "exam_manager_excel_import";
    public static final String EXAM_FUTURE_MARK_PAPERS_LIST = "exam_future_mark_papers_list";
    public static final String EXAM_SUPER_POWER = "exam_super_power";
    public static final String EXAM_STUDENT_POWER = "exam_student_power";
    public static final String EXAM_TEACHER_POWER = "exam_teacher_power";
    public static final String EXAM_REVIEW_POWER = "exam_review_power";
    public static final String ROLE_MANAGER = "role_manager";
    public static final String USER_MANAGER = "user_manager";


    public static final Set<String> getNotNeedControlPermissions(){
        HashSet<String> permissionSet = new HashSet<String>() {
            {
                add(LOGIN);
                add(REGISTER);
            }
        };
        return permissionSet;
    }
}

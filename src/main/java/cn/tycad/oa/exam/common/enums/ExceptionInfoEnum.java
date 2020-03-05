package cn.tycad.oa.exam.common.enums;

/**
 * @Author: YY
 * @Date: 2019/3/14
 * @Description: 自定义异常信息的枚举
 */
public enum ExceptionInfoEnum {
    SUCCESS(200,"操作成功"),
    ERROR(99,"操作失败"),
    ID_ILLEGAL_ERROR(99,"ID不合法"),
    TIME_NULL_ERROR(99,"时间不能为空"),
    INVALID_DELETE(99, "非法删除"),
    PUBLISH_TIME_BEFORE_NOW_ERROR(99,"考试已经发布，不允许修改考试相关信息"),
    NO_PRIVILEGE_OR_NO_TIME_RANGE(99,"无权限或者不在考试时间内"),
    PUBLISH_TIME_AFTER_DEADLINE_ERROR(99,"发布时间不能再截至时间之后"),
    NOW_AFTER_PUBLISH_TIME_ERROR(99,"试卷已经被发布不能删除"),
    USERNAME_OR_PASSWORD_NULL_ERROR(99,"用户名或者密码为空"),
    USERNAME_NO_EXIST(99,"用户不存在"),
    USERNAME_OR_PASSWORD_NO_VALID(99,"用户名或密码不正确"),
    USERNAME_ALREADY_EXIST(99,"用户名已经存在"),
    USER_ALREADY_LOGIN(99,"已登录，不可重复登录"),
    USER_NO_LOGIN(99,"未登录"),
    ROLE_NAME_DUPLICATE(99, "角色名称重复"),
    MENU_NAME_DUPLICATE(99, "菜单名称重复"),
    JWT_RESOLVE_ERROR(99,"Token解析错误"),
    JWT_TIME_EXPIRE_ERROR(99,"Token失效"),
    NONE_VALID_DATA(99, "无有效数据"),
    INVALID_TEMPLATE(99, "文件模板不合法"),
    INVALID_FILE_TYPE(99, "文件类型不合法"),
    PASSWORD_BLANK_ERROR(99,"密码中存在非法字符空格"),

    //考试业务
    EXAM_ALREADY_TAKEN(99, "不能重复提交答案"),
    PARAM_IS_NULL_ERROR(99,"请求参数为空"),
    EXAM_QR_CODE_CREATE_ERROR(99,"二维码创建失败"),
    EXAM_REPEAT_UPDATE_ERROR(99,"试题不可重复评改"),
    EXAM_IS_NOT_FINISH_ERROR(99,"考试还未完成"),
    BLANK_EXAM_NAME(99, "考试名称不能为空"),
    BLANK_EXAM_DURATION(99, "考试时长不能为空"),
    OVER_TIME_ERROR(99, "考试时长超时"),
    BLANK_EXAM_ID(99, "考试ID为空"),
    BLANK_EXAM_CREATOR_ID(99, "创建人ID为空"),
    CREATE_TIME_ERROR(99, "创建时间错误格式"),

    //导入试题
    EXAM_IMPORT_SUBTITLE_OVER_SIZE(99, "考试标题过长"),
    EXAM_IMPORT_EXAM_NAME_OVER_SIZE(99, "考试名称过长"),
    EXAM_IMPORT_ANSWER_IS_NULL_ERROR(99,"选择或者判断题未添加正确答案或者格式不对"),
    EXAM_IMPORT_QUESTION_TYPE_NOT_VALID(99,"试题问题类型不清晰"),
    INTERVIEW_IMPORT_QUESTION_FOLLOW_NOT_VALID(99,"问题类型所属组导入不清楚"),
    EXAM_IMPORT_TEACHER_IS_NOT_VALID(99,"导入试题的阅卷老师名称不合法或者不存在"),
    EXAM_IMPORT_SHEETS_ERROR(99,"试题数与题目数不对应，导入失败"),
    EXAM_IMPORT_EXAMTYPE_NOT_VALID(99,"考试类型不合法"),
    EXAM_IMPORT_DATA_ERROR(99,"数据输入不正确,题目是否为空"),
    EXAM_IMPORT_NAME_BLANK_ERROR(99,"试题名称为空"),
    EXAM_IMPORT_DURATION_INVALID_ERROR(99,"考试时长为空或者非法"),
    EXAM_IMPORT_PUBLISH_TIME_INVALID_ERROR(99,"考试时长非法"),
    EXAM_IMPORT_DEADLINE_INVALID_ERROR(99,"截止时间非法"),
    EXAM_IMPORT_TIME_NOT_NUM(99,"考试时长错误，不能存在非法字母"),
    EXAM_IMPORT_PUBLISH_TIME_ERROR(99,"发布时间格式错误"),
    EXAM_IMPORT_DEADLINE_TIME_ERROR(99,"截止时间格式错误"),

    //评分业务
    DUPLICATE_INSERT(99, "重复写入信息"),
    REVIEW_SCORE_NOT_VALID(99,"未做评价或者评价分数不正确"),

    //用户角色
    USERNAME_NULL(99,"用户名为空"),
    EMPTY_USER_ROLE(99, "用户角色为空");


    private Integer code;
    private String msg;

    ExceptionInfoEnum(Integer code, String message) {
        this.code = code;
        this.msg = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

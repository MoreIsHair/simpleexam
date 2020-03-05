package cn.tycad.oa.exam.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: YY
 * @Date: 2019/8/12
 * @Description: 试题以及问题所属的类型分组（JAVA，.Net,测试，CAD，前端，性格，安卓，IOS，通信工程相关）
 */

public enum InterviewQuestionFollowEnum {

    JAVA(0,"JAVA类型"),
    DOT_NET(1,".Net"),
    TEST(2,"测试"),
    CAD(3,"CAD"),
    NOSE(4,"前端"),
    ANDROID(5,"安卓"),
    IOS(6,"IOS"),
    TELECOMMUNICATION_ENGINEERING(7,"通信相关"),
    TEMPERAMENT(8,"性格"),
    CPLUSPLUS(9,"C++");

    private static final Map<Integer, InterviewQuestionFollowEnum> codeAndEnumMap =
            new HashMap<>();

    static {
        for(InterviewQuestionFollowEnum q: InterviewQuestionFollowEnum.values()) {
            codeAndEnumMap.put(q.getCode(), q);
        }
    }

    InterviewQuestionFollowEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static Map<Integer, InterviewQuestionFollowEnum> map() {
        return codeAndEnumMap;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    private Integer code;
    private String msg;
}

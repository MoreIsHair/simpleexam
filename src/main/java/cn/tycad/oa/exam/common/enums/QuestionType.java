package cn.tycad.oa.exam.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: shizf
 */
public enum QuestionType {

    /**
     * 多项选择 multiple choice question
     */
    MMCQ(-1),
    /**
     * multiple choice question
     */
    MCQ(0),
    /**
     * 判断题
     */
    YNQ(1),
    /**
     * 简答题
     */
    SAQ(2);

    private int code;

    QuestionType(int status) {
        this.code = status;
    }

    public int getCode() {
        return code;
    }

    private static final Map<Integer, QuestionType> code2enumMap =
            new HashMap<>();
    static {
        for(QuestionType qt: QuestionType.values()) {
            code2enumMap.put(qt.getCode(), qt);
        }
    }

    /**
     * 按code从映射中查找枚举
     * @param statusCode 枚举的数值
     * @return 枚举值
     */
    public static QuestionType getByCode(int statusCode) {
        return code2enumMap.get(statusCode);
    }
}

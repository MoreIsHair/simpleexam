package cn.tycad.oa.exam.exception;

import cn.tycad.oa.exam.common.enums.ExceptionInfoEnum;

/**
 * @Author: YY
 * @Date: 2019/3/13
 * @Description:
 */
public class BusinessException extends RuntimeException{
    private ExceptionInfoEnum exceptionInfoEnum;

    public BusinessException() { }

    public BusinessException(ExceptionInfoEnum exceptionInfoEnum) {
        this.exceptionInfoEnum = exceptionInfoEnum;
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ExceptionInfoEnum getExceptionInfoEnum() {
        return exceptionInfoEnum;
    }
}

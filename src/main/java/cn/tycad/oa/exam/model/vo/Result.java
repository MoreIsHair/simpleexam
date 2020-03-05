package cn.tycad.oa.exam.model.vo;

import cn.tycad.oa.exam.common.enums.ExceptionInfoEnum;

/**
 * @Author: YY
 * @Date: 2019/3/14
 * @Description: Restful类型的返回结果
 */
public class Result<T> {
    private static final Integer SUCCESS_CODE = 200;
    private static final Integer ERROR_CODE = 99;
    private static final String SUCCESS_MSG = "操作成功";
    private int code;
    private String msg;
    private T data;


    private Result(T data){
        this.code=SUCCESS_CODE;
        this.msg=SUCCESS_MSG;
        this.data=data;
    }
    private Result(){
        this.code=SUCCESS_CODE;
        this.msg=SUCCESS_MSG;
    }

    private Result(ExceptionInfoEnum infoEnum) {
        if (infoEnum==null){
            return;
        }
        this.code=infoEnum.getCode();
        this.msg=infoEnum.getMsg();
    }

    public Result(String msg) {
        this.code=ERROR_CODE;
        this.msg = msg;
    }

    /**
     * 成功时(带数据)
     * @param <T>
     * @return
     */
    public static <T>  Result<T>  success(T data){
        return new Result<T>(data);
    }

    /**
     * 成功时（无返回数据）
     * @param <T>
     * @return
     */
    public static <T>  Result<T>  success(){
        return new Result<T>();
    }

    /**
     * 自定义异常失败
     * @param <T>
     * @return
     */
    public static <T>  Result<T>  businessError(ExceptionInfoEnum infoEnum){
        return  new Result<T>(infoEnum);
    }

    /**
     * 未知异常错误
     * @param msg
     * @param <T>
     * @return
     */
    public static <T>  Result<T>  error(String msg){
        return  new Result<T>(msg);
    }

    public int getCode() {
        return code;
    }


    public String getMsg() {
        return msg;
    }


    public T getData() {
        return data;
    }
}

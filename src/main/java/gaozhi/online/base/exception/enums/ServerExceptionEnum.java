package gaozhi.online.base.exception.enums;


import gaozhi.online.base.result.Result;

/**
 * @description(3000 服务异常枚举)
 * @author: gaozhi.online
 * @createDate: 2021/3/15 0015
 * @version: 1.0
 */
public enum ServerExceptionEnum implements Result.ResultEnum {
    GENERAL_ERROR(-1,"未处理的全局服务器异常 Exception"),
    PROPERTY_VALIDATE_ERROR(-2,"属性校验未通过"),
    QPS_ERROR(-3,"异常访问")
    ;

    private final int  code;
    private final String message;

    ServerExceptionEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}

package gaozhi.online.base.exception.enums;


import gaozhi.online.base.result.Result;

/**
 * @description (1000 参数异常枚举)
 * @author: gaozhi.online
 * @createDate: 2021/3/15 0015
 * @version: 1.0
 */
public enum ParamExceptionEnum implements Result.ResultEnum {

    PARAM_IS_INVALID(-200,"参数异常：ConstraintViolationException"),
    INNER_PARAM_IS_INVALID(-201,"业务过程参数异常：IllegalArgumentException");
    private final int  code;
    private final String message;

    ParamExceptionEnum(int code, String message) {
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

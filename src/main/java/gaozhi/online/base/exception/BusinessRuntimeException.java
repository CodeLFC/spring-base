package gaozhi.online.base.exception;

import gaozhi.online.base.result.Result;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 运行时异常
 * @date 2022/3/4 12:20
 */
public class BusinessRuntimeException extends RuntimeException {
    private final Result.ResultEnum exception;

    public BusinessRuntimeException(Result.ResultEnum exception) {
        this(exception,null);
    }

    public BusinessRuntimeException(Result.ResultEnum exception, String msg) {
        super(msg);
        this.exception = exception;
    }

    public Result.ResultEnum getException() {
        return exception;
    }
}

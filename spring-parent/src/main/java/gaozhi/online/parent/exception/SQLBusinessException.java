package gaozhi.online.parent.exception;

import gaozhi.online.parent.result.Result;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 数据库业务异常
 * @date 2022/3/4 18:12
 */
public class SQLBusinessException extends BusinessRuntimeException {

    public SQLBusinessException(Result.ResultEnum exception) {
        super(exception);
    }

    public SQLBusinessException(Result.ResultEnum exception, String msg) {
        super(exception, msg);
    }
}

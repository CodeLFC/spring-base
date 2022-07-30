package gaozhi.online.base.exception.handler;

import gaozhi.online.base.exception.BusinessRuntimeException;
import gaozhi.online.base.exception.enums.ParamExceptionEnum;
import gaozhi.online.base.exception.enums.ServerExceptionEnum;
import gaozhi.online.base.result.Result;
import org.apache.commons.logging.Log;
import org.springframework.core.log.LogDelegateFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

/**
 * @description(拦截异常处理)
 * @author: gaozhi.online
 * @createDate: 2021/3/15 0015
 * @version: 1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Log log = LogDelegateFactory.getHiddenLog(GlobalExceptionHandler.class);
    @ResponseBody
    @ExceptionHandler(value = BusinessRuntimeException.class)
    public Result businessExceptionHandler(BusinessRuntimeException e) {
        Result result = Result.failure(e.getException(), e.getMessage());
        log.warn("businessExceptionHandler:"+result);
        return result;
    }
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Result exceptionHandler(Exception e) {
        Result result = Result.failure(ServerExceptionEnum.GENERAL_ERROR, e.getMessage());
        log.error("exceptionHandler:"+result);
        e.printStackTrace();
        return result;
    }

    //参数校验
    @ResponseBody
    @ExceptionHandler(value = ConstraintViolationException.class)
    public Result paramsExceptionHandler(ConstraintViolationException e) {
        Result result =Result.failure(ParamExceptionEnum.PARAM_IS_INVALID, e.getMessage());
        log.warn("ConstraintViolationException:"+result);
        return result;
    }

    //非法参数异常
    @ResponseBody
    @ExceptionHandler(value = IllegalArgumentException.class)
    public Result IllegalArgumentExceptionHandler(IllegalArgumentException e) {
        Result result =Result.failure(ParamExceptionEnum.INNER_PARAM_IS_INVALID, e.getMessage());
        log.warn("IllegalArgumentExceptionHandler:"+result);
        return result;
    }
}

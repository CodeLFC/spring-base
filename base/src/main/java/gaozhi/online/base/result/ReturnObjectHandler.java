package gaozhi.online.base.result;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.Resource;


/**
 * @description(拦截结果，将结果封装起来)
 * @author: gaozhi.online
 * @createDate: 2021/3/15 0015
 * @version: 1.0
 */
@ControllerAdvice
public class ReturnObjectHandler implements ResponseBodyAdvice<Object> {

    private final Gson gson = new Gson();

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (o instanceof Result) {
            return o;
        }
        //修复返回类型为String 是类型转换的错误
        if (o instanceof String) {
            return gson.toJson(Result.success(o));
        }
        return Result.success(o);
    }
}

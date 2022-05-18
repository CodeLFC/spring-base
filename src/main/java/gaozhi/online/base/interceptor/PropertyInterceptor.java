package gaozhi.online.base.interceptor;

import gaozhi.online.base.component.GetBeanHelper;
import gaozhi.online.base.exception.BusinessRuntimeException;
import gaozhi.online.base.exception.enums.ParamExceptionEnum;
import gaozhi.online.base.exception.enums.ServerExceptionEnum;
import gaozhi.online.base.util.IPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 权限拦截器
 * @date 2022/3/31 1:59
 */
@Component
public class PropertyInterceptor implements HandlerInterceptor {
    //获取bean
    private final GetBeanHelper getBeanHelper;

    @Autowired
    public PropertyInterceptor(GetBeanHelper getBeanHelper) {
        this.getBeanHelper = getBeanHelper;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 如果不是映射方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        // 如果方法没注明Auth，则不需要验证
        HeaderChecker annotation = method.getAnnotation(HeaderChecker.class);
        if (annotation == null) {
            return true;
        }
        HeaderPropertyChecker<?> headerPropertyChecker = getBeanHelper.getBean(annotation.property(), HeaderPropertyChecker.class);
        if (headerPropertyChecker == null) {
            throw new BusinessRuntimeException(ParamExceptionEnum.INNER_PARAM_IS_INVALID, "HeaderPropertyChecker=null, property:" + annotation.property());
        }
        //预处理
        headerPropertyChecker.preHandle(request, response, handlerMethod);

        String url = annotation.rpc() ? request.getHeader(HeaderChecker.rpcURLKey) : request.getRequestURL().toString();
        String ip =annotation.rpc()?request.getHeader(HeaderChecker.rpcClientIp): IPUtil.getRemoteHost(request);
        //校验
        Object obj = headerPropertyChecker.check(request.getHeader(annotation.property()), url,ip, annotation.rpc(),request,response);
        request.setAttribute(annotation.property(), obj);
        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    }
}

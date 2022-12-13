package gaozhi.online.base.interceptor;

import gaozhi.online.base.component.GetBeanHelper;
import gaozhi.online.base.exception.BusinessRuntimeException;
import gaozhi.online.base.exception.enums.ParamExceptionEnum;
import gaozhi.online.base.privilege.Privilege;
import gaozhi.online.base.util.IPUtil;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class PropertyInterceptor implements HandlerInterceptor {
    //获取bean
    private final GetBeanHelper getBeanHelper;
    private static final String TRUE = "true";
    private static final String FALSE = "false";

    @Autowired
    public PropertyInterceptor(GetBeanHelper getBeanHelper) {
        this.getBeanHelper = getBeanHelper;
    }

    @Override
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

        String ip = request.getHeader(HeaderChecker.rpcClientIp);
        if (ip == null) {
            ip = IPUtil.getRemoteHost(request);
        }
        String url = request.getHeader(HeaderChecker.rpcURLKey);
        if (url == null) {
            url = request.getServletPath();
        }
        String privilege = request.getHeader(HeaderChecker.rpcPrivilege);
        if (privilege == null) {
            privilege = FALSE;
        }
        //放置header内容
        EditableHttpServletRequestWrapper editableHttpServletRequestWrapper = new EditableHttpServletRequestWrapper(request);
        editableHttpServletRequestWrapper.addHeader(HeaderChecker.rpcURLKey, url);
        editableHttpServletRequestWrapper.addHeader(HeaderChecker.rpcClientIp, ip);
        editableHttpServletRequestWrapper.addHeader(HeaderChecker.rpcPrivilege, privilege);
        String property = request.getHeader(annotation.property());
        //校验头部属性
        Object obj = headerPropertyChecker.propertyCheck(property, request);
        request.setAttribute(annotation.property(), obj);
        //校验方法权限
        if (method.isAnnotationPresent(Privilege.class) || TRUE.equals(privilege)) {
            headerPropertyChecker.privilegeCheck(url, ip, request);
        }
        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    }
}

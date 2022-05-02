package gaozhi.online.base.interceptor;

import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * @description: TODO header参数的校验
 * @author LiFucheng
 * @date 2022/5/2 9:57
 * @version 1.0
 */
public interface HeaderPropertyChecker<T> {
    /**
     * @description: 预处理方法 ，默认不处理，特殊需求使用
     * @param: request HttpServletRequest
     * @param: response HttpServletResponse
     * @param: handler HandlerMethod
     * @return: void
     * @author LiFucheng
     * @date: 2022/5/2 10:09
     */
    default void preHandle(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler){}
    /** 
     * @description: 检查被HeaderChecker注解标记的请求中的header中key==HeaderChecker.property的value
     * @param: grade 检查等级 HeaderChecker.grade
     * @param: value header中key==HeaderChecker.property的value
     * @param: url 被处理的接口url
     * @return: T 被check方法处理后返回的值，将通过HttpServletRequest.setAttribute(HeaderChecker.property(), T)方法放入请求中，可通过参数注解 @RequestAttribute(property)注入
     * @author LiFucheng
     * @date: 2022/5/2 10:04
     */ 
    T check(String value,String url);

}

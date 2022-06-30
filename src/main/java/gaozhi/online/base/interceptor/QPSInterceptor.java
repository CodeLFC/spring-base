package gaozhi.online.base.interceptor;

import gaozhi.online.base.exception.SQLBusinessException;
import gaozhi.online.base.exception.enums.ServerExceptionEnum;
import gaozhi.online.base.util.IPUtil;
import org.apache.commons.logging.Log;
import org.springframework.core.log.LogDelegateFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO QPS访问拦截器
 * @date 2022/6/30 16:29
 */
@Component
public class QPSInterceptor implements HandlerInterceptor {
    private final Log log = LogDelegateFactory.getHiddenLog(QPSInterceptor.class);

    /**
     * @author LiFucheng
     * @version 1.0
     * @description: TODO 访问者信息
     * @date 2022/6/30 18:33
     */
    public static class Visitor {
        private final long period = 1000;
        private long qpsMax;
        private String ip;
        private final Map<String, PriorityQueue<Long>> countMap = new HashMap<>();

        public Visitor(String ip, long qpsMax) {
            this.ip = ip;
            this.qpsMax = qpsMax;
        }

        public String getIp() {
            return ip;
        }

        private boolean requestCheck(String url) {
            PriorityQueue<Long> request = countMap.getOrDefault(url, new PriorityQueue<>());
            countMap.put(url, request);
            long current = System.currentTimeMillis();
            while (request.size() > 0) {
                if (current - request.peek() > period) {
                    request.poll();
                } else {
                    break;
                }
            }
            request.offer(current);

            return getQPS(url) < qpsMax;
        }

        public long getQPS(String url) {
            if (!countMap.containsKey(url)) {
                return 0;
            }
            return (long) (countMap.get(url).size() / (period / 1000.0));
        }

        @Override
        public String toString() {
            return "Visitor{" +
                    "qpsMax=" + qpsMax +
                    ", ip='" + ip + '\'' +
                    '}';
        }
    }

    //ip访问缓存 <ip+method,count>
    private final Map<String, Visitor> visitorCountCathe = new WeakHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 如果不是映射方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        // 如果方法没注明Auth，则不需要验证
        QPSChecker annotation = method.getAnnotation(QPSChecker.class);
        if (annotation == null) {
            return true;
        }
        String ip = request.getHeader(HeaderChecker.rpcClientIp);
        if (ip == null) {
            ip = IPUtil.getRemoteHost(request);
        }
        String url = request.getHeader(HeaderChecker.rpcURLKey);
        if (url == null) {
            url = request.getRequestURL().toString();
        }
        //放置header内容
        EditableHttpServletRequestWrapper editableHttpServletRequestWrapper = new EditableHttpServletRequestWrapper(request);
        editableHttpServletRequestWrapper.addHeader(HeaderChecker.rpcURLKey, url);
        editableHttpServletRequestWrapper.addHeader(HeaderChecker.rpcClientIp, ip);
        //校验QPS
        String key = ip + url;
        Visitor visitor = visitorCountCathe.getOrDefault(key, new Visitor(ip, annotation.max()));

        //qps超过最大值
        if (!visitor.requestCheck(url)) {
            throw new SQLBusinessException(ServerExceptionEnum.QPS_ERROR, "访问频繁, QPS>" + annotation.max());
        }
        visitorCountCathe.put(key, visitor);
        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    }

}

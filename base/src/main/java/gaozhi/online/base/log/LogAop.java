package gaozhi.online.base.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author http://gaozhi.online
 * @version 1.0
 * @description: TODO controller 访问日志切面
 * @date 2022/8/4 16:11
 */
@Slf4j
public abstract class LogAop {
    //日志token串,用于日志aop工具
    public static final String logCustomProperty = "logCustomProperty";
    public static final String logURL = "logURL";
    public static final String logIP = "logIP";

    private ILogService sysLogService;

    public void setSysLogService(ILogService sysLogService) {
        this.sysLogService = sysLogService;
    }

    private long id = 0;

    /**
     * @description: 切入点表达式  实际开发常用： @Pointcut("execution(* gaozhi.online.user.controller.*.*(..))")
     * @author http://gaozhi.online
     * @date: 2022/8/4 17:09
     */
    @Pointcut("execution(* gaozhi.online.base.result.*.*(..))")
    public void pointcut() {

    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint jp) throws Throwable {
        //获取当前访问的类
        Class<?> clazz = jp.getTarget().getClass();

        //获取访问方法的名称
        String methodName = jp.getSignature().getName();
        //获取当前访问的方法的参数
        Object[] args = jp.getArgs();

        //获取具体执行的方法的Method对象
        Method method;
        if (args == null || args.length == 0) {
            //只能获取无参数的类
            method = clazz.getMethod(methodName);
        } else {
            Class<?>[] classArgs = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                classArgs[i] = args[i].getClass();
            }
            method = clazz.getMethod(methodName, classArgs);
        }

        long startTime = System.currentTimeMillis();
        Object object = jp.proceed();
        long endTime = System.currentTimeMillis();
        //将日志相关信息封装到SysLog对象
        SysLog sysLog = new SysLog(++id, getClass().getName(), null, null, startTime, endTime - startTime, null);
        //填充一些特殊信息
        fillSysLog(clazz, method, sysLog);
        //如果是日志服务类，就直接返回，避免造成递归调用
        if (jp.getTarget() instanceof ILogService) {
            log.info("recycler : "+ methodName);
            return object;
        }
        //通过service处理日志
        sysLogService.handle(sysLog);
        return object;
    }

    protected void fillSysLog(Class<?> clazz, Method method, SysLog sysLog) {
        //获取url
        if (clazz != null && method != null && clazz != LogAop.class) {
            //1.获取类上的的@RequestMapping
            RequestMapping classAnnotation = clazz.getAnnotation(RequestMapping.class);
            if (classAnnotation != null) {
                /**
                 * 1.获取request信息
                 * 2.根据request获取session
                 * 3.从session中取出日志信息
                 */
                RequestAttributes ra = RequestContextHolder.getRequestAttributes();
                ServletRequestAttributes sra = (ServletRequestAttributes) ra;
                HttpServletRequest request = sra.getRequest();
                String url = (String) request.getAttribute(logURL);
                sysLog.setUri(url);
                String property = (String) request.getAttribute(logCustomProperty);
                sysLog.setProperty(property);
                String ip = (String) request.getAttribute(logIP);
                sysLog.setIp(ip);
                //log.info("aop log info url:{} property:{} ip:{}",url,property,ip);
                return;
            }
        }
        //填充一般信息
        sysLog.setUri(clazz.getName() + ":" + method.getName() + Arrays.toString(method.getParameters()));
    }
}

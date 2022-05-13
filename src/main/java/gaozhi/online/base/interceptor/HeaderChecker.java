package gaozhi.online.base.interceptor;

import java.lang.annotation.*;

/**
 * @description: TODO  被注解后
 * @author LiFucheng
 * @date 2022/3/31 2:05
 * @version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HeaderChecker {
    //远程过程调用的键
    String rpcURLKey = "rpcURLKey";
    //向发起远程过程调用方发起请求的客户端Ip
    String rpcClientIp = "rpcClientIp";
    boolean rpc() default false;
    String property() default "token";
}

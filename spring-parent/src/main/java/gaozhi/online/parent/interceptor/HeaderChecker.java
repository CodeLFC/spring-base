package gaozhi.online.parent.interceptor;

import java.lang.annotation.*;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO  被注解后
 * @date 2022/3/31 2:05
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HeaderChecker {
    /**远程过程调用的URL*/
    String rpcURLKey = "rpcURLKey";
     /**向发起远程过程调用方发起请求的客户端Ip*/
    String rpcClientIp = "rpcClientIp";
    /**权限校验*/
    String rpcPrivilege = "rpcPrivilege";
    /**权限校验*/
    String accessToken = "token";

    /**需要检查的头部属性*/
    String property() default accessToken;
}

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
    String property();
}

package gaozhi.online.base.privilege;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
* @description: TODO 权限的注解
* @author http://gaozhi.online
* @date 2022/11/21 19:57
* @version 1.0
*/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Privilege {
    /**API的名字*/
    String name();
    /**API的描述*/
    String description();
}

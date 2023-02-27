package gaozhi.online.parent.privilege;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
* @description: TODO 权限的注解，被HeaderChecker注解后再添加此注解的方法会被扫描
* @author http://gaozhi.online
* @date 2022/11/21 19:57
* @version 1.0
*/
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Privilege {
    /**API的名字*/
    String name();
    /**API的描述*/
    String description();
}

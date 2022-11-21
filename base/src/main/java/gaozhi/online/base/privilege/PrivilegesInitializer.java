package gaozhi.online.base.privilege;

import java.util.Set;

/**
* @description: TODO 权限初始化
* @author http://gaozhi.online
* @date 2022/11/21 19:52
* @version 1.0
*/
public interface PrivilegesInitializer {
    /**
    * @description: 处理权限
    * @param name 权限名
     * @param description 权限的描述
     * @param urlSet 一个方法可能对应多个url
    * @author http://gaozhi.online
    * @date: 2022/11/21 19:54
    */
    void handlePrivilege(String name, String description, Set<String> urlSet);
}

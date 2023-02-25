package gaozhi.online.base.privilege;


/**
* @description: TODO 权限初始化,被Privilege标注的方法都会在启动时被处理一次
* @author http://gaozhi.online
* @date 2022/11/21 19:52
* @version 1.0
*/
public interface PrivilegesInitializer {
    /**
    * @description: 处理权限
     * @param fullUrl 一个方法可能对应多个url
    * @author http://gaozhi.online
    * @date: 2022/11/21 19:54
    */
    void handlePrivilege(Privilege klass,Privilege method, String[] fullUrl,String[]methodUrl);
}

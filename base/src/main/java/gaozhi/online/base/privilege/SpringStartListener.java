package gaozhi.online.base.privilege;

import gaozhi.online.base.component.GetBeanHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import java.util.Map;
import java.util.Set;

/**
* @description: TODO
* @author http://gaozhi.online
* @date 2022/11/21 20:00
* @version 1.0
*/
@Slf4j
@Component
public class SpringStartListener implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private PrivilegesInitializer privilegesInitializer;
    @Autowired
    private GetBeanHelper getBeanHelper;
    /**初始化标志用于解决初始化两次的问题*/
    private boolean initialFlag = false;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if(!initialFlag){
            initialFlag =true;
            return;
        }
        log.info("扫描需要鉴权的Api");
        RequestMappingHandlerMapping mapping = getBeanHelper.getBean(RequestMappingHandlerMapping.class);
        // 拿到Handler适配器中的全部方法
        Map<RequestMappingInfo, HandlerMethod> methodMap = mapping.getHandlerMethods();

        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : methodMap.entrySet()){
            Set<String> urlSet = entry.getKey().getPatternsCondition().getPatterns();
            HandlerMethod handlerMethod = entry.getValue();
            Privilege privilegeAnnotation =handlerMethod.getMethodAnnotation(Privilege.class);
            if(privilegeAnnotation==null||privilegesInitializer==null){
                return;
            }
            //处理权限
            privilegesInitializer.handlePrivilege(privilegeAnnotation.name(),privilegeAnnotation.description(),urlSet);
        }
    }
}

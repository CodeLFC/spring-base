package gaozhi.online.base.privilege;

import gaozhi.online.base.component.GetBeanHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
* @description: TODO  启动监听，用于扫描权限
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
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        log.info("扫描需要鉴权的Api-------------------------------------------------------------------------------");
        RequestMappingHandlerMapping mapping = getBeanHelper.getBean(RequestMappingHandlerMapping.class);
        // 拿到Handler适配器中的全部方法
        Map<RequestMappingInfo, HandlerMethod> methodMap = mapping.getHandlerMethods();

        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : methodMap.entrySet()){
            HandlerMethod handlerMethod = entry.getValue();
            Privilege classPrivilege = handlerMethod.getBeanType().getAnnotation(Privilege.class);
            if(classPrivilege==null){
                log.info("类没有注解---{}",handlerMethod.getBeanType().getName());
                continue;
            }
            Privilege methodPrivilege =handlerMethod.getMethodAnnotation(Privilege.class);
            if(methodPrivilege==null){
                log.info("方法没有注解---{}",handlerMethod.getMethod().getName());
                continue;
            }
            //url
            Set<String> urlSet = entry.getKey().getPatternsCondition().getPatterns();
            //获取方法所有注解
            String[] methodUrl={};
            Annotation[] annotations = handlerMethod.getMethod().getAnnotations();
            for (Annotation annotation : annotations) {
                log.info("注解：{}",annotation.annotationType().getName());
                //请求
                if(annotation instanceof RequestMapping){
                    methodUrl = ((RequestMapping) annotation).value();
                    break;
                }else{
                    if (annotation instanceof GetMapping) {
                        methodUrl = ((GetMapping) annotation).value();
                        break;
                    }
                    if (annotation instanceof PostMapping) {
                        methodUrl = ((PostMapping) annotation).value();
                        break;
                    }
                    if (annotation instanceof PutMapping) {
                        methodUrl = ((PutMapping) annotation).value();
                        break;
                    }
                    if (annotation instanceof DeleteMapping) {
                        methodUrl = ((DeleteMapping) annotation).value();
                        break;
                    }
                }

            }
            log.info("methodUrl：{}", Arrays.toString(methodUrl));
            if(privilegesInitializer==null){
                continue;
            }
            //处理权限
            privilegesInitializer.handlePrivilege(classPrivilege,methodPrivilege,urlSet.toArray(new String[]{}),methodUrl);
        }
    }
}

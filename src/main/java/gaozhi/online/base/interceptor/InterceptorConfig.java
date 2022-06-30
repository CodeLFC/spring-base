package gaozhi.online.base.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 拦截器注册
 * @date 2022/3/31 2:41
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    private PropertyInterceptor authInterceptor;
    private QPSInterceptor qpsInterceptor;

    @Autowired
    public void setAuthInterceptor(PropertyInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    @Autowired
    public void setQpsInterceptor(QPSInterceptor qpsInterceptor) {
        this.qpsInterceptor = qpsInterceptor;
    }

    /**
     * 注册鉴权拦截器
     *
     * @param
     * @return
     */
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor).addPathPatterns("/**").excludePathPatterns("/error");
        registry.addInterceptor(qpsInterceptor).addPathPatterns("/**").excludePathPatterns("/error");
    }
}

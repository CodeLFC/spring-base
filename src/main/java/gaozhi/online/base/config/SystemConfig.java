package gaozhi.online.base.config;
/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO  请求中带有 特殊字符的 配置
 * @date 2021/10/26 18:53
 */
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置Spring boot支持在查询参数中加{}[]字符。
 * @author elon
 * @version 2019年1月6日
 */
@Configuration
public class SystemConfig {
    @Bean
    public ServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory fa = new TomcatServletWebServerFactory();
        //json 串中的特殊字符
        fa.addConnectorCustomizers(connector -> connector.setProperty("relaxedQueryChars", "[]{}"));
        return fa;
    }
}


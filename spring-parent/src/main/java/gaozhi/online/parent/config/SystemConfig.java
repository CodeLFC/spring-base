package gaozhi.online.parent.config;
/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO  请求中带有 特殊字符的 配置
 * @date 2021/10/26 18:53
 */
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 配置Spring boot支持在查询参数中加{}[]字符。
 * 开启 AspectJ 的自动代理
 * @author elon
 * @version 2019年1月6日
 */
@Configuration
@EnableAspectJAutoProxy
public class SystemConfig {
    @Bean
    public ServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory fa = new TomcatServletWebServerFactory();
        //json 串中的特殊字符
        fa.addConnectorCustomizers(connector -> {
            connector.setProperty("relaxedQueryChars", "(),/:;<=>?@[\\]{}");
            connector.setProperty("rejectIllegalHeader", "false");
        });
        return fa;
    }
}


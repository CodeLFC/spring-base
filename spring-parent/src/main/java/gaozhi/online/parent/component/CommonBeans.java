package gaozhi.online.parent.component;

import com.google.gson.Gson;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author gaozhi.online
 * @date 2023年02月27日 15:29
 * 通用Beans
 */
@Component
public class CommonBeans {
    @Bean
    public Gson gson() {
        return new Gson();
    }
}

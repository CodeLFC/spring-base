package gaozhi.online.parent.util;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * @author gaozhi.online
 * @date 2023年02月27日 14:56
 */
public class HttpUtil {
    /**
     * 获取post数据
     * @param request
     * @return
     */
    public static String getPostData(HttpServletRequest request) {
        StringBuilder data = new StringBuilder();
        String line;
        BufferedReader reader;
        try {
            reader = request.getReader();
            while (null != (line = reader.readLine())) {
                data.append(line);
            }
        } catch (IOException e) {
            return null;
        }
        return data.toString();
    }
}

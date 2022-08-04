package gaozhi.online.base.util;


import java.text.SimpleDateFormat;
import java.util.Date;
/**
* @description: TODO 格式化日期时间工具
* @author http://gaozhi.online
* @date 2022/8/4 15:09
* @version 1.0
*/
public class FormatDateTimeUtil {
    private static final SimpleDateFormat formatDate = new SimpleDateFormat("yyyy年MM月dd日");

    private static final SimpleDateFormat formatDateTime = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");

    private static final SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");

    public static String getFormatDate(long time) {
        return formatDate.format(new Date(time));
    }

    public static String getFormatDateTime(long time) {
        return formatDateTime.format(new Date(time));
    }

    public static String getFormatTime(long time) {
        return formatTime.format(new Date(time));
    }
}

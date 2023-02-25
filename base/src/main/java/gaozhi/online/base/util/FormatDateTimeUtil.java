package gaozhi.online.base.util;


import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    private static final long _1h = 3600000;

    public static boolean isToday(long time) {
        //Calendar使用单例，多次调用不重复创建对象
        Calendar calendar = Calendar.getInstance();
        //使用System获取当前时间
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long today = calendar.getTimeInMillis();
        if (time - today < _1h*24&&time-today>0)
            return true;
        return false;
    }
}

package gaozhi.online.base.log;

import gaozhi.online.base.util.FormatDateTimeUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author http://gaozhi.online
 * @version 1.0
 * @description: TODO 日志
 * @date 2022/8/4 15:21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysLog {
    private long id;
    private String type;
    private String ip;
    private String uri;
    private long time;
    private long executeTime;
    private String property;

    @Override
    public String toString() {
        return "SysLog{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", ip='" + ip + '\'' +
                ", uri='" + uri + '\'' +
                ", time=" + FormatDateTimeUtil.getFormatDateTime(time) +
                ", executeTime=" + executeTime +
                "s, remark='" + property + '\'' +
                '}';
    }
}
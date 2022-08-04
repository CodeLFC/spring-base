package gaozhi.online.base.log;

import org.apache.commons.logging.Log;
import org.springframework.core.log.LogDelegateFactory;

/**
 * @author http://gaozhi.online
 * @version 1.0
 * @description: TODO 日志类，请将实现类设置到
 * @date 2022/8/4 15:11
 */
public interface ILogService {
    Log logger = LogDelegateFactory.getHiddenLog(ILogService.class);

    /**
     * @param log 日志
     * @description: 处理传来的日志信息
     * @author http://gaozhi.online
     * @date: 2022/8/4 16:38
     */
    default void handle(SysLog log) {
        logger.info(log);
    }
}

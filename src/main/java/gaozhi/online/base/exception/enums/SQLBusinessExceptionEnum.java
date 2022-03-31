package gaozhi.online.base.exception.enums;

import gaozhi.online.base.result.Result;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 数据库的增删改查错误枚举
 * @date 2022/3/4 18:14
 */
public enum SQLBusinessExceptionEnum implements Result.ResultEnum {
    INSERT_ERROR(-110, "数据插入失败"),
    DELETE_ERROR(-111, "数据删除失败"),
    SELECT_ERROR(-112, "数据查找失败"),
    UPDATE_ERROR(-113, "数据更新失败");
    private final int code;
    private final String message;

    SQLBusinessExceptionEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}

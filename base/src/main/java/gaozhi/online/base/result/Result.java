package gaozhi.online.base.result;

import com.google.gson.Gson;

/**
 * @description(结果)
 * @author: gaozhi.online
 * @createDate: 2021/3/4 0004
 * @version: 1.0
 */
public class Result {
    private static final Gson gson = new Gson();

    /**
     * 成功
     */
    public enum SUCCESSResultEnum implements ResultEnum {
        SUCCESS(200, "请求成功");
        private final int code;
        private final String message;

        SUCCESSResultEnum(int code, String message) {
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

    int code;
    String message="";
    String data="";

    public int getCode() {
        return code;
    }

    public void setResultCode(ResultEnum resultEnum) {
        this.code = resultEnum.code();
        this.message = resultEnum.message();
    }

    public String getMessage() {
        return message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data='" + data + '\'' +
                '}';
    }

    public static Result success() {
        Result result = new Result();
        result.setResultCode(SUCCESSResultEnum.SUCCESS);
        return result;
    }

    public static <T> Result success(T data) {
        Result result = new Result();
        result.setResultCode(SUCCESSResultEnum.SUCCESS);
        result.setData(gson.toJson(data));
        return result;
    }

    public static Result failure(ResultEnum resultEnum) {
        Result result = new Result();
        result.setResultCode(resultEnum);
        return result;
    }

    public static <T> Result failure(ResultEnum resultEnum, T data) {
        Result result = new Result();
        result.setResultCode(resultEnum);
        result.setData(gson.toJson(data));
        return result;
    }

    /**
     * 结果枚举
     */
    public interface ResultEnum {
        int code();

        String message();
    }

}

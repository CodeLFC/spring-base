package gaozhi.online.base.interceptor;

public interface HeaderPropertyChecker<T> {
    T check(int grade,String value);
}

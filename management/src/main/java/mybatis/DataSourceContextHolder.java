package mybatis;

/**
 * Created by hushe on 2018/1/20.
 */
public class DataSourceContextHolder {
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

    public static void setDbType(String dbType) {
        contextHolder.set(dbType);
    }

    public static String getDbType() {
        return (contextHolder.get());
    }

    public static void clearDbType() {
        contextHolder.remove();
    }
}

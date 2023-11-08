package org.zuel.innovation.auth.qywx.cache;

/**
 * @author fye
 * @email fh941005@163.com
 * @date 2022-01-08 23:14
 * @description
 */
public interface IAccessTokenCache {
    /**
     * 默认超时时间7200秒 9秒用于程序执行误差
     */
    int DEFAULT_TIME_OUT = 7200 - 9;

    String get(String key);

    void set(String key, String jsonValue);

    void remove(String key);
}

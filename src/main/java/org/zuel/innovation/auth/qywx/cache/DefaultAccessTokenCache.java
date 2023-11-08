package org.zuel.innovation.auth.qywx.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author fye
 * @email fh941005@163.com
 * @date 2022-01-08 23:27
 * @description
 */
public class DefaultAccessTokenCache implements IAccessTokenCache {

    private Map<String, String> map = new ConcurrentHashMap<String, String>();

    @Override
    public String get(String key) {
        return map.get(key);
    }

    @Override
    public void set(String key, String jsonValue) {
        map.put(key, jsonValue);
    }

    @Override
    public void remove(String key) {
        map.remove(key);
    }

}
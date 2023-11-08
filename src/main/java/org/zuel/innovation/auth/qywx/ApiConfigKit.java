package org.zuel.innovation.auth.qywx;

import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;
import org.zuel.innovation.auth.qywx.cache.DefaultAccessTokenCache;
import org.zuel.innovation.auth.qywx.cache.IAccessTokenCache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author fye
 * @email fh941005@163.com
 * @date 2022-01-08 00:22
 * @description 借鉴微信公众号开发接口
 */
public class ApiConfigKit {
    private static final Log log = Log.getLog(ApiConfigKit.class);

    private static final ThreadLocal<String> TL = new ThreadLocal<String>();

    private static final Map<String, ApiConfig> CFG_MAP = new ConcurrentHashMap<String, ApiConfig>();
    private static final String DEFAULT_CFG_KEY = "_default_cfg_key_";

    /**
     * 添加API配置，每个apiKey只需添加一次，相同apiKey将被覆盖。
     * 第一个添加的将作为默认的api配置
     *
     * @param apiConfig 数据请求配置
     * @return ApiConfig 数据请求配置
     */
    public static ApiConfig putApiConfig(ApiConfig apiConfig) {
        if (CFG_MAP.size() == 0) {
            CFG_MAP.put(DEFAULT_CFG_KEY, apiConfig);
        }
        return CFG_MAP.put(apiConfig.getCorpid(), apiConfig);
    }

    public static ApiConfig removeApiConfig(ApiConfig apiConfig) {
        return removeApiConfig(apiConfig.getCorpid());
    }

    public static ApiConfig removeApiConfig(String corpid) {
        ApiConfig removedConfig = CFG_MAP.remove(corpid);
        if (removedConfig != null) {
            ApiConfig defaultApiConfig = CFG_MAP.get(DEFAULT_CFG_KEY);

            if (defaultApiConfig.getCorpid().equals(removedConfig.getCorpid())) {

                CFG_MAP.remove(DEFAULT_CFG_KEY);

                ApiConfig firstApiConfig = null;
                if (CFG_MAP.size() > 0) {
                    for (Map.Entry<String, ApiConfig> entry : CFG_MAP.entrySet()) {
                        firstApiConfig = entry.getValue();
                        break;
                    }
                }

                if (firstApiConfig != null) {
                    CFG_MAP.put(DEFAULT_CFG_KEY, firstApiConfig);
                }
            }
        }

        return removedConfig;
    }

    public static void setThreadLocalCorpid(String corpid) {
        if (StrKit.isBlank(corpid)) {
            corpid = CFG_MAP.get(DEFAULT_CFG_KEY).getCorpid();
        }
        TL.set(corpid);
    }

    public static void removeThreadLocalAppId() {
        TL.remove();
    }

    public static String getAppId() {
        String appId = TL.get();
        if (StrKit.isBlank(appId)) {
            appId = CFG_MAP.get(DEFAULT_CFG_KEY).getCorpid();
        }
        return appId;
    }

    public static ApiConfig getApiConfig() {
        String appId = getAppId();
        return getApiConfig(appId);
    }

    public static ApiConfig getApiConfig(String appId) {
        log.debug("appId: " + appId);
        ApiConfig cfg = CFG_MAP.get(appId);
        if (cfg == null) {
            throw new IllegalStateException("需事先调用 ApiConfigKit.putApiConfig(apiConfig) 将 appId对应的 ApiConfig 对象存入，" +
                    "如JFinalConfig.afterJFinalStart()中调用, 才可以使用 ApiConfigKit.getApiConfig() 系列方法");
        }
        return cfg;
    }

    static IAccessTokenCache accessTokenCache = new DefaultAccessTokenCache();

    public static void setAccessTokenCache(IAccessTokenCache accessTokenCache) {
        ApiConfigKit.accessTokenCache = accessTokenCache;
    }

    public static IAccessTokenCache getAccessTokenCache() {
        return ApiConfigKit.accessTokenCache;
    }
}

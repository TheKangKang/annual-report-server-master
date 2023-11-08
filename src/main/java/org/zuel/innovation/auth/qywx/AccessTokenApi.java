package org.zuel.innovation.auth.qywx;

import cn.fabrice.kit.common.RetryKit;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.StrKit;
import org.zuel.innovation.auth.qywx.cache.IAccessTokenCache;

/**
 * @author fye
 * @email fh941005@163.com
 * @date 2022-01-08 00:18
 * @description 借鉴微信公众号开发接口
 */
public class AccessTokenApi {

    /**
     * 用于手动设置的 accessToken
     */
    private static AccessToken accessToken = null;

    public static void setAccessToken(AccessToken ak) {
        accessToken = ak;
    }

    public static void removeAccessToken() {
        accessToken = null;
    }

    /**
     * 从缓存中获取 access token，如果未取到或者 access token 不可用则先更新再获取
     *
     * @return AccessToken accessToken
     */
    public static AccessToken getAccessToken() {
        if (accessToken != null) {
            return accessToken;
        }
        ApiConfig ac = ApiConfigKit.getApiConfig();
        AccessToken result = getAvailableAccessToken(ac);
        if (result == null) {
            synchronized (ac) {
                result = getAvailableAccessToken(ac);
                if (result == null) {
                    result = refreshAccessToken(ac);
                }
            }
        }
        return result;
    }

    private static AccessToken getAvailableAccessToken(ApiConfig ac) {
        // 利用 appId 与 accessToken 建立关联，支持多账户
        IAccessTokenCache accessTokenCache = ApiConfigKit.getAccessTokenCache();

        String accessTokenJson = accessTokenCache.get(ac.getCorpid());
        if (StrKit.notBlank(accessTokenJson)) {
            AccessToken result = new AccessToken(accessTokenJson);
            if (result.isAvailable()) {
                return result;
            }
        }
        return null;
    }

    /**
     * 直接获取 accessToken 字符串，方便使用
     *
     * @return String accessToken
     */
    public static String getAccessTokenStr() {
        return getAccessToken().getAccessToken();
    }

    /**
     * 无条件强制更新 access token 值，不再对 cache 中的 token 进行判断
     *
     * @param ac ApiConfig
     * @return AccessToken
     */
    public static AccessToken refreshAccessToken(ApiConfig ac) {
        String corpid = ac.getCorpid();
        String corpsecret = ac.getCorpsecret();
        String url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=${key}&corpsecret=${secret}";
        String requestUrl = url.replace("${key}", corpid).replace("${secret}", corpsecret);

        // 最多三次请求
        AccessToken result = RetryKit.retryOnException(3, () -> {
            String json = HttpKit.get(requestUrl);
            return new AccessToken(json);
        });

        // 三次请求如果仍然返回了不可用的 access token 仍然 put 进去，便于上层通过 AccessToken 中的属性判断底层的情况
        if (null != result) {
            // 利用 appId 与 accessToken 建立关联，支持多账户
            IAccessTokenCache accessTokenCache = ApiConfigKit.getAccessTokenCache();
            accessTokenCache.set(ac.getCorpid(), result.getCacheJson());
        }
        return result;
    }
}

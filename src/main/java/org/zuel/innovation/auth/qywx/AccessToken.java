package org.zuel.innovation.auth.qywx;

import cn.fabrice.kit.common.RetryKit;
import com.jfinal.kit.JsonKit;

import java.io.Serializable;
import java.util.Map;

/**
 * @author fye
 * @email fh941005@163.com
 * @date 2022-01-08 23:46
 * @description 借鉴微信公众号开发接口
 */
public class AccessToken implements RetryKit.ResultCheck, Serializable {
    /**
     * 正确获取到 access_token 时有值
     */
    private String accessToken;
    /**
     * 正确获取到 access_token 时有值
     */
    private Integer expiresIn;
    /**
     * 返回代码
     */
    private Integer errcode;
    /**
     * 返回说明
     */
    private String errmsg;
    /**
     * 正确获取到 access_token 时有值，存放过期时间
     */
    private Long expiredTime;
    /**
     * 获取到的json字符串
     */
    private String json;

    public AccessToken(String jsonStr) {
        this.json = jsonStr;

        try {
            Map<String, Object> temp = JsonKit.parse(jsonStr, Map.class);
            this.errcode = (Integer) temp.get("errcode");
            this.errmsg = (String) temp.get("errmsg");
            this.accessToken = (String) temp.get("access_token");
            this.expiresIn = (Integer) temp.get("expires_in");
            if (this.expiresIn != null) {
                // expires_in - 9  用于控制在 access token 过期之前 9 秒就 "主动" 再次获取 access token
                // 避免大并发场景下多线程同时获取 access token，造成公众平台 api 调用额度的浪费
                expiredTime = System.currentTimeMillis() + ((this.expiresIn - 9) * 1000L);
            }

            // 用户缓存时还原
            if (temp.containsKey("expiredTime")) {
                Object tempExpiredTime = temp.get("expiredTime");
                if (tempExpiredTime != null) {
                    expiredTime = Long.valueOf(tempExpiredTime.toString());
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getJson() {
        return json;
    }

    public String getCacheJson() {
        Map<String, Object> temp = JsonKit.parse(json, Map.class);
        temp.put("expiredTime", expiredTime);
        temp.remove("expires_in");
        return JsonKit.toJson(temp);
    }

    public boolean isAvailable() {
        if (expiredTime == null) {
            return false;
        }
        if (this.errcode != 0) {
            return false;
        }
        if (expiredTime < System.currentTimeMillis()) {
            return false;
        }
        return this.accessToken != null;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    public Integer getErrcode() {
        return errcode;
    }

    public void setErrcode(Integer errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public Long getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Long expiredTime) {
        this.expiredTime = expiredTime;
    }

    public void setJson(String json) {
        this.json = json;
    }

    @Override
    public boolean matching() {
        return isAvailable();
    }
}

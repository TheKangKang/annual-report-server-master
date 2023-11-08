package org.zuel.innovation.auth.qywx;

import java.io.Serializable;

/**
 * @author fye
 * @email fh941005@163.com
 * @date 2022-01-08 00:20
 * @description 借鉴微信公众号开发接口
 */
public class ApiConfig implements Serializable {

    private static final long serialVersionUID = 5243926308290263767L;

    private String corpid = null;
    private String corpsecret = null;

    public ApiConfig() {

    }

    public ApiConfig(String corpid, String corpsecret) {
        setCorpid(corpid);
        setCorpsecret(corpsecret);
    }

    public String getCorpid() {
        if (corpid == null) {
            throw new IllegalStateException("corpid 未被赋值");
        }
        return corpid;
    }

    public void setCorpid(String corpid) {
        if (corpid == null) {
            throw new IllegalArgumentException("corpid 值不能为 null");
        }
        this.corpid = corpid;
    }

    public String getCorpsecret() {
        if (corpsecret == null) {
            throw new IllegalStateException("corpsecret 未被赋值");
        }
        return corpsecret;
    }

    public void setCorpsecret(String corpsecret) {
        if (corpsecret == null) {
            throw new IllegalArgumentException("corpsecret 值不能为 null");
        }
        this.corpsecret = corpsecret;
    }
}

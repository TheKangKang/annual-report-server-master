package org.zuel.innovation.common.module;

import java.math.BigInteger;

/**
 * @author lxree
 */
public class Tool {
    private String accessToken;
    private BigInteger sessionId;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public BigInteger getSessionId() {
        return sessionId;
    }

    public void setSessionId(BigInteger sessionId) {
        this.sessionId = sessionId;
    }
}

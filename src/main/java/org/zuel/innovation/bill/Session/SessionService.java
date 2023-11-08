package org.zuel.innovation.bill.Session;

import cn.fabrice.jfinal.service.BaseService;
import cn.fabrice.kit.Kits;
import com.jfinal.kit.Kv;
import org.zuel.innovation.common.module.Session;

import java.math.BigInteger;

public class SessionService extends BaseService<Session> {
    public SessionService(){
        super("session.", Session.class,"session");
    }

    /**
     * 添加用户登陆账号session存储
     *
     * @param userId 登录用户学号
     * @return 操作成功-session实体类/操作失败-null
     */
    public Session add(String userId,int type,int gender) {
        Session userSession = new Session();
        userSession.setUserId(userId);
        userSession.setSessionId(Kits.getUuid());
        userSession.setType(type);
        userSession.setSex(gender);
        return userSession.save()?userSession:null;

    }

    /**
     * 通过token获取用户session信息
     *
     * @param token token信息
     * @return 存在-返回对应实体类/不存在-null
     */
    public Session getByToken(String token) {
        return get(Kv.by("sessionId", token), "getByToken");
    }

    /**
     * 通过账号ID获取用户session信息
     *
     * @param accountId 账号ID
     * @return 存在-返回对应实体类/不存在-null
     */
    public Session getByAccount(long accountId) {
        return get(Kv.by("accountId", accountId), "getByAccount");
    }

    /**
     * 根据token删除登录信息
     *
     * @param token 对应数据表的session_id
     * @return 操作成功-true/操作失败-false
     */
    public boolean deleteByToken(String token) {
        return update(Kv.by("token", token), "deleteByToken") == 1;
    }
}
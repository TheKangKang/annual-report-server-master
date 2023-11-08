package org.zuel.innovation.common.interceptor;

import cn.fabrice.common.constant.BaseConstants;
import cn.fabrice.common.pojo.BaseResult;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.ehcache.CacheKit;
import org.zuel.innovation.common.module.Session;
import org.zuel.innovation.bill.Session.SessionService;

public class AuthInterceptor implements Interceptor  {

    @Override
    public void intercept(Invocation invocation) {
        Controller controller = invocation.getController();
        if (StrKit.notBlank(controller.getHeader(BaseConstants.TOKEN_NAME))) {
            String token = controller.getHeader(BaseConstants.TOKEN_NAME);
            SessionService sessionService = Aop.get(SessionService.class);
            //获取session实体
            Session userSession =  sessionService.getByToken(token);
            if (userSession == null) {
                controller.renderJson(BaseResult.fail("查询不到登录信息"));
                return;
            }
            controller.setAttr(BaseConstants.ACCOUNT, userSession);
            controller.setAttr(BaseConstants.ACCOUNT_ID, userSession.getUserId());
            invocation.invoke();
            return;
        }
        controller.renderJson(BaseResult.fail("token为空"));
    }

}

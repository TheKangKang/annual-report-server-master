package org.zuel.innovation.auth.cas;

import cn.fabrice.common.constant.BaseConstants;
import cn.fabrice.common.pojo.BaseResult;
import cn.fabrice.common.pojo.DataResult;
import cn.fabrice.kit.reg.RegKit;
import com.jfinal.aop.Clear;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.zuel.innovation.bill.BillService;
import org.zuel.innovation.bill.Session.LoginInfoService;
import org.zuel.innovation.common.interceptor.AuthInterceptor;
import org.zuel.innovation.common.module.LoginInfo;
import org.zuel.innovation.common.module.Tool;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Principal;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author fye
 * @email fh941005@163.com
 * @date 2021-01-11 14:29
 * @description
 */
@Clear(AuthInterceptor.class)
@Path("/")
public class CasController extends Controller {
    @Inject
    BillService billService;
    @Inject
    LoginInfoService loginInfoService;

    private final Log logger = Log.getLog(CasController.class);
    private final Pattern pattern = Pattern.compile("[0-9]+");

    /**
     * 判断是不是数字
     *
     * @param str
     * @return
     */
    private boolean isNumeric(String str) {
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    public void index(String redirect) throws ServletException, UnsupportedEncodingException {
        String webUrl = PropKit.get("webUrl").trim();

        HttpServletRequest request = getRequest();
        //cas-client-3.2.1版本集成
        //uid即学生学号
        String uid = request.getRemoteUser();
        logger.info("当前登录的账号为：{}", uid);
        if (StrKit.isBlank(uid) || !isNumeric(uid)) {
            request.logout();
            request.getSession().invalidate();
            renderJson(BaseResult.fail("当前用户无登录权限"));
            return;
        }
        int gender = 0;
        Principal principal = request.getUserPrincipal();
        if (principal instanceof AttributePrincipal) {
            AttributePrincipal aPrincipal = (AttributePrincipal) principal;
            //获取用户信息中公开的Attributes部分
            Map<String, Object> map = aPrincipal.getAttributes();
            // 获取姓名,可以根据属性名称获取其他属性
            logger.info("获取到的数据值：");
            gender = Integer.parseInt((String) map.getOrDefault("USER_SEX","1"));
            map.forEach((k, v) -> {
                logger.info(k + "：{}", v);
            });
        }
        LoginInfo loginInfo = loginInfoService.getLoginInfo(getRequest(), uid);
        BaseResult result = billService.login(uid, 2, gender);
        if (result.isOk()) {
            loginInfo.setStatus(BaseConstants.Status.NORMAL.ordinal());
            Tool user = (Tool) ((DataResult) result).getData();
            loginInfo.setSessionId(user.getSessionId());
            loginInfo.save();
            webUrl += "/#/login/token?token=" + user.getAccessToken();
            if (StrKit.notBlank(redirect)) {
                webUrl += "&redirect=" + redirect;
            }
            redirect(webUrl);
            return;
        }
        loginInfo.setStatus(BaseConstants.Status.ABNORMAL.ordinal());
        loginInfo.save();
        renderJson(result);
    }

    public void logout() throws UnsupportedEncodingException {
        String webUrl = PropKit.get("webUrl").trim();
        String idsLogoutUrl = PropKit.get("idsLogoutUrl").trim();
        redirect(idsLogoutUrl + "?service=" + URLEncoder.encode(webUrl, "utf8"));
    }
}

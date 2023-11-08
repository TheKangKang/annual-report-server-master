package org.zuel.innovation.bill;

import cn.fabrice.common.constant.BaseConstants;
import cn.fabrice.common.pojo.BaseResult;
import cn.fabrice.common.pojo.DataResult;
import cn.fabrice.jfinal.annotation.Param;
import cn.fabrice.jfinal.constant.ValidateRuleConstants;
import com.jfinal.aop.Clear;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.Path;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.Kv;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.ehcache.CacheKit;
import org.zuel.innovation.bill.Session.LoginInfoService;
import org.zuel.innovation.bill.Session.SessionService;
import org.zuel.innovation.common.interceptor.AuthInterceptor;
import org.zuel.innovation.common.module.Bill;
import org.zuel.innovation.common.module.LoginInfo;
import org.zuel.innovation.common.module.Session;
import org.zuel.innovation.common.module.Tool;
import org.zuel.innovation.common.module.base.BaseLoginInfo;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;

/**
 * @author ly
 * @since 5/11
 */
@Path("/bill")
public class BillController extends Controller {
    @Inject
    BillService billService;
    @Inject
    SessionService sessionService;
    @Inject
    LoginInfoService infoService;


    //    缺少展示bill总表的所有信息的方法
//    @Clear(AuthInterceptor.class)
//    @Param(name="account", required = true, customRule = "[1-9][0-9]{11}")
    public void annualReport() {
        String account = getAttr(BaseConstants.ACCOUNT_ID);
        Session session = getAttr(BaseConstants.ACCOUNT);
        Bill bill = billService.getByAccount(account);
        bill.put("sex", session.getSex());
        System.out.println("billService:" + bill);
        renderJson(DataResult.data(bill));
    }
//    //分页获取数据
////    @Clear(AuthInterceptor.class)
//    @Param(name="account", required = true, customRule = "[1-9][0-9]{11}")
//    public void pageEnd(String account) {
//        System.out.println("billService:"+billService.getPageEnd(account));
//        renderJson(DataResult.data(billService.getPageEnd(account)));
//    }
//
////    @Clear(AuthInterceptor.class)
//    @Param(name="account", required = true, customRule = "[1-9][0-9]{11}")
//    public void pageOne(String account) {
//        System.out.println("billService:"+billService.getPageOne(account));
//        renderJson(DataResult.data(billService.getPageOne(account)));
//    }
//
////    @Clear(AuthInterceptor.class)
//    @Param(name="account", required = true, customRule = "[1-9][0-9]{11}")
//    public void pageTwo(String account) {
//        System.out.println("billService:"+billService.getPageTwo(account));
//        renderJson(DataResult.data(billService.getPageTwo(account)));
//    }
//
////    @Clear(AuthInterceptor.class)
//    @Param(name="account", required = true, customRule = "[1-9][0-9]{11}")
//    public void pageThree(String account) {
//        System.out.println("billService:"+billService.getPageThree(account));
//        renderJson(DataResult.data(billService.getPageThree(account)));
//    }
//
////    @Clear(AuthInterceptor.class)
//    @Param(name="account", required = true, customRule = "[1-9][0-9]{11}")
//    public void pageFour(String account) {
//        System.out.println("billService:"+billService.getPageFour(account));
//        renderJson(DataResult.data(billService.getPageFour(account)));
//    }
//
////    @Clear(AuthInterceptor.class)
//    @Param(name="account", required = true, customRule = "[1-9][0-9]{11}")
//    public void pageFive(String account) {
//        System.out.println("billService:"+billService.getPageFive(account));
//        renderJson(DataResult.data(billService.getPageFive(account)));
//    }
//
////    @Clear(AuthInterceptor.class)
//    @Param(name="account", required = true, customRule = "[1-9][0-9]{11}")
//    public void pageSix(String account) {
//        System.out.println("billService:"+billService.getPageSix(account));
//        renderJson(DataResult.data(billService.getPageSix(account)));
//    }
//
////    @Clear(AuthInterceptor.class)
//    @Param(name="account", required = true, customRule = "[1-9][0-9]{11}")
//    public void pageSeven(String account) {
//        System.out.println("billService:"+billService.getPageSeven(account));
//        renderJson(DataResult.data(billService.getPageSeven(account)));
//    }
//
////    @Clear(AuthInterceptor.class)
//    @Param(name="account", required = true, customRule = "[1-9][0-9]{11}")
//    public void pageEight(String account) {
//        System.out.println("billService:"+billService.getPageEight(account));
//        renderJson(DataResult.data(billService.getPageEight(account)));
//    }
//
////    @Clear(AuthInterceptor.class)
//    @Param(name="account", required = true, customRule = "[1-9][0-9]{11}")
//    public void pageNine(String account) {
//        System.out.println("billService:"+billService.getPageNine(account));
//        renderJson(DataResult.data(billService.getPageNine(account)));
//    }

    /**
     * 该方法用于学生的模拟登录
     *
     * @param account 学生的学号
     */
    @Clear(AuthInterceptor.class)
    @Param(name = "account", required = true, customRule = "[1-9][0-9]{11}")
    @Param(name = "type", required = true, rule = ValidateRuleConstants.Key.ID)
    public void login(String account, int type) {
        LoginInfo loginInfo = infoService.getLoginInfo(getRequest(), account);
        BaseResult result = billService.login(account, type, 0);
        if (result.isOk()) {
            loginInfo.setStatus(BaseConstants.Status.NORMAL.ordinal());
            Tool user = (Tool) ((DataResult) result).getData();
            loginInfo.setSessionId(user.getSessionId());
            loginInfo.save();
        } else {
            System.out.println("进入else分支");
            loginInfo.setStatus(BaseConstants.Status.ABNORMAL.ordinal());
            loginInfo.save();
        }
        renderJson(result);

    }

    /**
     * 根据token获取用户信息
     */
    public void getInfo() {
        System.out.println("account_id:" + BaseConstants.ACCOUNT_ID);
        String account = getAttr(BaseConstants.ACCOUNT_ID);
        Kv cond = Kv.by("account", account);
        Bill user = billService.get(cond, "getName");
        renderJson(DataResult.data(user));
    }

    /**
     * 退出登录
     */
    public void logout() {
        Session session = getAttr(BaseConstants.ACCOUNT);
        String token = session.getSessionId();
        if (sessionService.deleteByToken(token)) {
            //同时删除缓存token
            CacheKit.remove(BaseConstants.ACCOUNT_CACHE_NAME, token);
            renderJson(BaseResult.ok());
            return;
        }
        renderJson(BaseResult.fail());
    }
}

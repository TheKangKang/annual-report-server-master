package org.zuel.innovation.bill;

import cn.fabrice.common.pojo.BaseResult;
import cn.fabrice.common.pojo.DataResult;
import cn.fabrice.jfinal.service.BaseService;
import com.jfinal.kit.Kv;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log4jLog;
import com.jfinal.plugin.activerecord.Record;
import org.zuel.innovation.bill.Session.SessionService;
import org.zuel.innovation.common.module.Bill;
import org.zuel.innovation.common.module.Session;
import org.zuel.innovation.common.module.Tool;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static java.lang.Double.parseDouble;

/**
 * @author ly
 * @since 5/11
 */
public class BillService extends BaseService<Bill> {
    private SessionService sessionService = new SessionService();
    private static final Log4jLog log = Log4jLog.getLog(BillService.class);

    public BillService() {
        // 第一个参数名需要与all.sql中配置的#namespace("product")保持一致
        // 第二个参数是用户类module，最后一个数据库表名
        super("bill.", Bill.class, "bill");
    }

    //    缺少展示bill总表的所有信息的方法
    public Bill getByAccount(String account) {
        if (log.isDebugEnabled()) {
            log.debug("Parameters: account = " + account);
        }
        Kv cond = Kv.by("account", account);
        Bill bill = get(cond, "getBillByAccount");
        if (log.isDebugEnabled()) {
            log.debug("Return: " + bill);
        }
//        System.out.println("bill:"+bill.toString());
        String nullStr = "#N/A";
        //修改数据格式
        String mostConsumeCanteenMoney = bill.getMostConsumeCanteenMoney();
        if (!mostConsumeCanteenMoney.equals(nullStr)) {
            bill.setMostConsumeCanteenMoney(changeFormatInt(mostConsumeCanteenMoney));
        }

        String sumCharge = bill.getSumCharge();
        if (!sumCharge.equals(nullStr)) {
            bill.setSumCharge(changeFormatInt(sumCharge));
        }

        String mostConsumeMoneyCanteenDate = bill.getMostConsumeMoneyCanteenDate();
        if (!mostConsumeMoneyCanteenDate.equals(nullStr)) {
            bill.setMostConsumeMoneyCanteenDate(changeFormatDate(mostConsumeMoneyCanteenDate));
        }

        String sumWebTime = bill.getSumWebTime();
        if (!sumWebTime.equals(nullStr)) {
            bill.setSumWebTime(changeFormatInt(sumWebTime));
        }

        String sumWebFlow = bill.getSumWebFlow();
        if (!sumWebTime.equals(nullStr)) {
            bill.setSumWebFlow(changeFormatG(sumWebFlow));
        }

        String longestWebUse = bill.getLongestWebUse();
        if (!longestWebUse.equals(nullStr)) {
            bill.setLongestWebUse(changeFormatInt(longestWebUse));
        }

        String longestWebUseTimestamp = bill.getLongestWebUseTimestamp();
        if (!longestWebUse.equals(nullStr)) {
            bill.setLongestWebUseTimestamp(changeFormatTimeStamp(longestWebUseTimestamp));
        }

        String latestWebLogin = bill.getLastestWebLogin();
        if (!longestWebUse.equals(nullStr)) {
            bill.setLastestWebLogin(changeFormatTimeStamp(latestWebLogin));
        }
        // System.out.println("bill:"+bill.toString());
        //endPage 运动篇不予考虑
        String library = "4";
        Double libraryPoint = 11.0;
        String web = "3";
        Double webPoint = 1507.649167;
        String canteen = "2";
        Double canteenPoint = 2817.27;
        String classPage = "1";
        //图书馆篇
        String libraryAppointmentTime = bill.getLibraryAppointmentTime();
        if (!libraryAppointmentTime.equals(nullStr) && Double.parseDouble(libraryAppointmentTime) > libraryPoint) {
            bill.setEndAchievement(library);
        }
        //信息化篇
        if (!sumWebTime.equals(nullStr) && Double.parseDouble(sumWebTime) > webPoint) {
            bill.setEndAchievement(web);
        }
        //食堂篇
        if (!sumCharge.equals(nullStr) && Double.parseDouble(sumCharge) > canteenPoint) {
            bill.setEndAchievement(canteen);
        }
        bill.setEndAchievement(classPage);
        return bill;
    }

//    public Bill getPageEnd(String account) {
//        Kv cond = Kv.by("account", account);
//        Bill bill= get(cond,"getEndData");
//        String endAchievement = bill.getEndAchievement();
//        String nullStr = "#N/A";
//        String library = "4";
//        int libraryPoint = 11;
//        String web = "3";
//        int webPoint = 1507;
//        String canteen = "2";
//        int canteenPoint = 2817;
//        //图书馆篇
//        String libraryAppointmentTime = bill.getLibraryAppointmentTime();
//        if(!libraryAppointmentTime.equals(nullStr) && parseInt(libraryAppointmentTime)>libraryPoint){
//            bill.setEndAchievement(library);
//        }
//        //信息化篇
//        String sumWebTime = bill.getSumWebTime();
//        if(!sumWebTime.equals(nullStr) && parseInt(sumWebTime)>webPoint){
//            bill.setEndAchievement(web);
//        }
//        //食堂篇
//        String sumCharge = bill.getSumCharge();
//        if(!sumCharge.equals(nullStr) && parseInt(sumCharge)>canteenPoint){
//            bill.setEndAchievement(canteen);
//        }
//        System.out.println("bill:"+bill.toString());
//        return bill;
//    }
//
//    public Bill getPageOne(String account) {
//        Kv cond = Kv.by("account", account);
//        Bill bill= get(cond,"getClassOneData");
//        System.out.println("bill:"+bill.toString());
//        return bill;
//    }
//
//    public Bill getPageTwo(String account) {
//        Kv cond = Kv.by("account", account);
//        Bill bill= get(cond,"getClassTwoData");
//        System.out.println("bill:"+bill.toString());
//        return bill;
//    }
//
//    public Bill getPageThree(String account) {
//        Kv cond = Kv.by("account", account);
//        Bill bill= get(cond,"getCanteenOneData");
//        System.out.println("bill:"+bill.toString());
//        return bill;
//    }
//
//    public Bill getPageFour(String account) {
//        Kv cond = Kv.by("account", account);
//        Bill bill= get(cond,"getCanteenTwoData");
//        System.out.println("bill:"+bill.toString());
//        return bill;
//    }
//
//    public Bill getPageFive(String account) {
//        Kv cond = Kv.by("account", account);
//        Bill bill= get(cond,"getNetworkOneData");
//        System.out.println("bill:"+bill.toString());
//        return bill;
//    }
//
//    public Bill getPageSix(String account) {
//        Kv cond = Kv.by("account", account);
//        Bill bill= get(cond,"getNetworkTwoData");
//        System.out.println("bill:"+bill.toString());
//        return bill;
//    }
//
//    public Bill getPageSeven(String account) {
//        Kv cond = Kv.by("account", account);
//        Bill bill= get(cond,"getLibraryOneData");
//        System.out.println("bill:"+bill.toString());
//        return bill;
//    }
//
//    public Bill getPageEight(String account) {
//        Kv cond = Kv.by("account", account);
//        Bill bill= get(cond,"getLibraryTwoData");
//        System.out.println("bill:"+bill.toString());
//        return bill;
//    }
//
//    public Bill getPageNine(String account) {
//        Kv cond = Kv.by("account", account);
//        Bill bill= get(cond,"getSportData");
//        System.out.println("bill:"+bill.toString());
//        return bill;
//    }

    public String changeFormatInt(String floatString) {
        StringBuilder builder = new StringBuilder(String.valueOf(floatString));
        if (builder.toString().contains(".")) {
            builder.delete(builder.indexOf("."), builder.length());
            System.out.println("builder:" + builder.toString());
            return builder.toString();
        }
        return builder.toString();
    }

    public String changeFormatDate(String dateString) {
        StringBuilder builder = new StringBuilder(String.valueOf(dateString));
        builder.insert(4, '年');
        builder.insert(7, '月');
        builder.insert(10, '日');
        System.out.println("builder:" + builder.toString());
        return builder.toString();
    }

    public String changeFormatG(String floatString) {
        StringBuilder builder = new StringBuilder(String.valueOf(floatString));
        if (builder.toString().contains(",")) {
            builder.deleteCharAt(builder.indexOf(","));
            System.out.println("builder:" + builder.toString());
            return builder.toString();
        }
        return builder.toString();
    }

    public String changeFormatTimeStamp(String timestampString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        String sd = sdf.format(new Date(Long.parseLong(timestampString) * 1000));
        return sd;
    }


    public BaseResult login(String account, int type, int gender) {
        Session session = sessionService.add(account, type, gender);
        Tool data = new Tool();
        if (StrKit.notNull(session)) {
            //token值
            data.setAccessToken(session.getSessionId());
            // session表的自增id
            data.setSessionId(session.getId());
            return DataResult.data(data);
        }
        return BaseResult.fail("登录信息保存失败");
    }
}

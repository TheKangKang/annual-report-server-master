package org.zuel.innovation.common;

import cn.fabrice.common.constant.BaseConstants;
import cn.fabrice.jfinal.ext.cros.interceptor.CrossInterceptor;
import cn.fabrice.jfinal.interceptor.ParaValidateInterceptor;
import cn.fabrice.jfinal.plugin.ValidationPlugin;
import cn.fabrice.kit.json.FJsonFactory;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.config.*;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.server.undertow.UndertowServer;
import com.jfinal.template.Engine;
import org.zuel.innovation.common.interceptor.AuthInterceptor;
import org.zuel.innovation.common.module._MappingKit;
//import org.zuel.innovation.member.MemberController;

public class App extends JFinalConfig  {
    public static  void main(String[]args){
        Prop prop = PropKit.use("base_config.properties");
        if (!prop.getBoolean("ids")) {
            UndertowServer.start(App.class);
            return;
        }
        UndertowServer.create(App.class)
                .configWeb(builder -> {
                    // 单点登录开始
                    // 用于单点退出，该监听器和过滤器用于实现单点登出功能，可选配置
                    builder.addListener("org.jasig.cas.client.session.SingleSignOutHttpSessionListener");
                    builder.addFilter("CAS Single Sign Out Filter", "org.jasig.cas.client.session.SingleSignOutFilter");
                    builder.addFilterUrlMapping("CAS Single Sign Out Filter", "/");
                    // 该过滤器负责用户的认证工作，必须启用它
                    builder.addFilter("CASFilter", "org.jasig.cas.client.authentication.AuthenticationFilter");
                    builder.addFilterInitParam("CASFilter", "casServerLoginUrl", "http://ids.zuel.edu.cn/authserver/login");
                    builder.addFilterInitParam("CASFilter", "serverName", "http://ndzd.zuel.edu.cn/api");
                    builder.addFilterUrlMapping("CASFilter", "/");
                    // 该过滤器负责对Ticket的校验工作，必须启用它
                    builder.addFilter("CAS Validation Filter", "org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter");
                    builder.addFilterInitParam("CAS Validation Filter", "casServerUrlPrefix", "http://ids.zuel.edu.cn/authserver");
                    builder.addFilterInitParam("CAS Validation Filter", "serverName", "http://ndzd.zuel.edu.cn/api");
                    builder.addFilterInitParam("CAS Validation Filter", "encoding", "UTF-8");
                    builder.addFilterUrlMapping("CAS Validation Filter", "/");
                    // 该过滤器负责实现HttpServletRequest请求的包裹，比如允许开发者通过HttpServletRequest的getRemoteUser()方法获得SSO登录用户的登录名。
                    builder.addFilter("CAS HttpServletRequest Wrapper Filter", "org.jasig.cas.client.util.HttpServletRequestWrapperFilter");
                    builder.addFilterUrlMapping("CAS HttpServletRequest Wrapper Filter", "/");
                    // 单点登录结束
                })
                .start();
    }

    @Override
    public void configConstant(Constants me) {
        /**
         * 1.使用JFinal里面的PropKit进行基本设置
         * 2.直接进行设置,设置成true表示进入开发模式，此时路径就比较的
         * me.setDevMode(true);
         */

        PropKit.use("base_config.properties");
        me.setDevMode(PropKit.getBoolean("devMode",true));
        //设置日志
        me.setToSlf4jLogFactory();
        //允许AOP注入
        me.setInjectDependency(true);
        // 配置文件上传下载路径，没有配置的话就是upload和download文件夹
        me.setBaseUploadPath("src/file");
        me.setBaseDownloadPath("file/download");
        // 设置上传文件的最大大小
        me.setMaxPostSize(20*1024*1024);
        // 前后端以驼峰的形式传递数据
        me.setJsonFactory(new FJsonFactory());

    }

    @Override
    public void configRoute(Routes me) {
        me.scan("org.zuel.innovation.");
        //me.add("/", MemberController.class);
//        me.setBaseViewPath("/api");

    }

    @Override
    public void configEngine(Engine me) {

    }

    @Override
    public void configPlugin(Plugins me) {
        // 读取数据库配置文件
        Prop prop = PropKit.use("db_config.properties");
        // 使用druid数据库连接池进行操作
        DruidPlugin dp= new DruidPlugin(prop.get("db_url").trim(),
                prop.get("db_user").trim(), prop.get("db_password").trim());
        // 加强数据库安全
        WallFilter wallFilter = new WallFilter();
        wallFilter.setDbType("mysql");
        dp.addFilter(wallFilter);
        // 添加 StatFilter 才会有统计数据
        dp.addFilter(new StatFilter());

        dp.setMaxActive(20);
        dp.setMinIdle(5);
        dp.setInitialSize(5);
        dp.setConnectionInitSql("set names utf8mb4");
        dp.setValidationQuery("select 1 from dual");
        me.add(dp);
        // 配置数据库活动记录插件
        ActiveRecordPlugin arp=new ActiveRecordPlugin(dp);
        Engine engine = arp.getEngine();
        // 上面的代码获取到了用于 sql 管理功能的 Engine 对象，接着就可以开始配置了
        engine.setToClassPathSourceFactory();
        engine.addSharedMethod(StrKit.class);
        //设置是否显示sql文件
        arp.setShowSql(PropKit.getBoolean("dev", true));
        //设置sql文件存储的基础路径，此段代码表示设置为classpath目录
        arp.setBaseSqlTemplatePath(null);
        arp.addSqlTemplate("sql/all.sql");
        _MappingKit.mapping(arp);
        me.add(arp);

        //添加规则校验插件
        me.add(new ValidationPlugin());
        //增加缓存插件
        me.add(new EhCachePlugin());


    }

    @Override
    public void configInterceptor(Interceptors me) {
//         首先添加跨域拦截器
        me.add(new CrossInterceptor(BaseConstants.TOKEN_NAME,true));
        // 然后添加全局拦截器
        me.add(new AuthInterceptor());
//        // 最后添加参数校验拦截器
        me.add(new ParaValidateInterceptor());

    }

    @Override
    public void onStart() {
        System.out.println("app starting......");
    }

    @Override
    public void onStop() {
        System.out.println("app stopping......");
    }

    @Override
    public void configHandler(Handlers me) {

    }
}

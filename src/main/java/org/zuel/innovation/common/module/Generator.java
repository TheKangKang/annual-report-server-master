package org.zuel.innovation.common.module;

import cn.fabrice.kit.jfinal.GeneratorModelKit;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;

/**
 * @author fye
 * @team occam
 * @email fh941005@163.com
 * @date 2019-04-20 19:35
 * @description 数据库模块生成类
 */
public class Generator {
    public static void main(String[] args) {
        Prop prop = PropKit.use("db_config.properties");
        // GeneratorModelKit 师兄库里的
        GeneratorModelKit.generate(prop.get("db_url").trim(), prop.get("db_user").trim(),
                prop.get("db_password").trim(), "org.zuel.innovation.common.module");
    }
}


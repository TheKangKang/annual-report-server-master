package org.zuel.innovation.common.module;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;

/**
 * Generated by JFinal, do not modify this file.
 * <pre>
 * Example:
 * public void configPlugin(Plugins me) {
 *     ActiveRecordPlugin arp = new ActiveRecordPlugin(...);
 *     _MappingKit.mapping(arp);
 *     me.add(arp);
 * }
 * </pre>
 */
public class _MappingKit {
	
	public static void mapping(ActiveRecordPlugin arp) {
		arp.addMapping("bill", "id", Bill.class);
		arp.addMapping("login_info", "id", LoginInfo.class);
		arp.addMapping("session", "id", Session.class);
		arp.addMapping("student", "id", Student.class);
	}
}


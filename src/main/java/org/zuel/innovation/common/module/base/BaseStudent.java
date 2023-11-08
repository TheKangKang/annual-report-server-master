package org.zuel.innovation.common.module.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseStudent<M extends BaseStudent<M>> extends Model<M> implements IBean {

	/**
	 * 自增主键
	 */
	public void setId(java.math.BigInteger id) {
		set("id", id);
	}
	
	/**
	 * 自增主键
	 */
	public java.math.BigInteger getId() {
		return get("id");
	}
	
	/**
	 * 创建时间
	 */
	public void setCreatedTime(java.util.Date createdTime) {
		set("created_time", createdTime);
	}
	
	/**
	 * 创建时间
	 */
	public java.util.Date getCreatedTime() {
		return get("created_time");
	}
	
	/**
	 * 更新时间
	 */
	public void setUpdatedTime(java.util.Date updatedTime) {
		set("updated_time", updatedTime);
	}
	
	/**
	 * 更新时间
	 */
	public java.util.Date getUpdatedTime() {
		return get("updated_time");
	}
	
	/**
	 * 是否删除（0-没有删除；1-已经删除）
	 */
	public void setIsDeleted(java.lang.Integer isDeleted) {
		set("is_deleted", isDeleted);
	}
	
	/**
	 * 是否删除（0-没有删除；1-已经删除）
	 */
	public java.lang.Integer getIsDeleted() {
		return getInt("is_deleted");
	}
	
	/**
	 * 学号
	 */
	public void setAccount(java.lang.String account) {
		set("account", account);
	}
	
	/**
	 * 学号
	 */
	public java.lang.String getAccount() {
		return getStr("account");
	}
	
}
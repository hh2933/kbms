package com.shuxin.model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.shuxin.commons.utils.JsonUtils;

/**
 *
 * 资源
 *
 */
@TableName("T_MENU")
public class Menu implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@TableId(type = IdType.UUID)
	private String id;

	/** 资源名称 */
	private String name;

	/** 资源路径 */
	private String url;

	/** 打开方式 ajax,iframe */
	@TableField(value = "open_mode")
	private String openMode;

	/** 资源介绍 */
	private String description;

	/** 资源图标 */
	@JsonProperty("iconCls")
	private String icon;

	/** 父级资源id */
	@TableField(value = "p_id")
	private String pid;

	/** 排序 */
	@TableField(value = "order_seq")
	private Integer seq;

	/** 状态 */
	private Integer status;
	
	//levels字段，刘叔预留
	private Integer	levels;
	
	//ishasChildrfen字段,是否有子菜单
	private String	ishaschildren;

	public String getIshaschildren() {
		return ishaschildren;
	}

	public void setIshaschildren(String ishaschildren) {
		this.ishaschildren = ishaschildren;
	}

	public Integer getLevels() {
		return levels;
	}

	public void setLevels(Integer levels) {
		this.levels = levels;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getOpenMode() {
		return openMode;
	}

	public void setOpenMode(String openMode) {
		this.openMode = openMode;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIcon() {
		return this.icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getPid() {
		return this.pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public Integer getSeq() {
		return this.seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}
}

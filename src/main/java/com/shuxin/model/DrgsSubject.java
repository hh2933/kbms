package com.shuxin.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

@TableName("T_BZSFXMXXB")
public class DrgsSubject implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;
	
	
	/** 主键id */
	@TableId(type = IdType.UUID)
	private String id;
	
	
	
	/**病种序号 */	
	private String bzxh;
	
	
	/**病种名称*/
	private String bzmc;
	
	/**项目类别*/
	private String xmlb;
	
	/**项目编码*/
	private String xmbm;
	/**项目名称*/
	private String xmmc;
	/**医药品限定用量*/
	private String ypxdyl;
	/**限定次数*/
	private String xdcs;
	/**大额审批标识*/
	private String despbs;
	
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getBzxh() {
		return bzxh;
	}
	public void setBzxh(String bzxh) {
		this.bzxh = bzxh;
	}

	public String getBzmc() {
		return bzmc;
	}
	public void setBzmc(String bzmc) {
		this.bzmc = bzmc;
	}
	public String getXmlb() {
		return xmlb;
	}
	public void setXmlb(String xmlb) {
		this.xmlb = xmlb;
	}
	public String getXmbm() {
		return xmbm;
	}
	public void setXmbm(String xmbm) {
		this.xmbm = xmbm;
	}
	public String getXmmc() {
		return xmmc;
	}
	public void setXmmc(String xmmc) {
		this.xmmc = xmmc;
	}
	public String getYpxdyl() {
		return ypxdyl;
	}
	public void setYpxdyl(String ypxdyl) {
		this.ypxdyl = ypxdyl;
	}
	public String getXdcs() {
		return xdcs;
	}
	public void setXdcs(String xdcs) {
		this.xdcs = xdcs;
	}
	public String getDespbs() {
		return despbs;
	}
	public void setDespbs(String despbs) {
		this.despbs = despbs;
	}
	
	
	
	
	
	
}

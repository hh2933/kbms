package com.shuxin.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;


/**
 *
 * 工作首页
 *
 */
@TableName("t_dm_gzsy_ks_m")
public class WorkIndex implements Serializable{
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;
	/** 系统年月 */
	private String  syearmonth ;
	/** 这年 */
	private String  syear ;
	/** 这月*/
	private String  smonth ;
	/**科室编码 */
	private String  ksbm ;
	/** 科室名称 */
	private String  ksmc ;
	
	/** 指标编码*/
	private String  zbbm ;
	/** 指标名称 */
	private String  zbmc ;
	/** 医保类型编码 */
	private String  yblxbm ;
	/**医保类型名称 */
	private String  yblxmc ;	
	/**诊断类型编码 */
	private String  jzlxbm ;
	/**诊断类型名称*/
	private String  jzlxmc ;
	/**本月累计*/
	private Double  bylj ;
	public String getSyearmonth() {
		return syearmonth;
	}
	public void setSyearmonth(String syearmonth) {
		this.syearmonth = syearmonth;
	}
	public String getSyear() {
		return syear;
	}
	public void setSyear(String syear) {
		this.syear = syear;
	}
	public String getSmonth() {
		return smonth;
	}
	public void setSmonth(String smonth) {
		this.smonth = smonth;
	}
	public String getKsbm() {
		return ksbm;
	}
	public void setKsbm(String ksbm) {
		this.ksbm = ksbm;
	}
	public String getKsmc() {
		return ksmc;
	}
	public void setKsmc(String ksmc) {
		this.ksmc = ksmc;
	}
	public String getZbbm() {
		return zbbm;
	}
	public void setZbbm(String zbbm) {
		this.zbbm = zbbm;
	}
	public String getZbmc() {
		return zbmc;
	}
	public void setZbmc(String zbmc) {
		this.zbmc = zbmc;
	}
	public String getYblxbm() {
		return yblxbm;
	}
	public void setYblxbm(String yblxbm) {
		this.yblxbm = yblxbm;
	}
	public String getYblxmc() {
		return yblxmc;
	}
	public void setYblxmc(String yblxmc) {
		this.yblxmc = yblxmc;
	}
	public String getJzlxbm() {
		return jzlxbm;
	}
	public void setJzlxbm(String jzlxbm) {
		this.jzlxbm = jzlxbm;
	}
	public String getJzlxmc() {
		return jzlxmc;
	}
	public void setJzlxmc(String jzlxmc) {
		this.jzlxmc = jzlxmc;
	}
	public Double getBylj() {
		return bylj;
	}
	public void setBylj(Double bylj) {
		this.bylj = bylj;
	}
	
	
	
	
}

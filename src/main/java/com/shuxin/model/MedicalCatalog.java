package com.shuxin.model;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableName;

/**
 * 医保目录跟新监控信息
 * @author admin
 *
 */
@TableName("ODS_KEY_RESULT")
public class MedicalCatalog {
	  
	// 医院项目编码
	private String yyxmbm;
	
	// 医保项目编码
	private String ybxmbm;
	
	// 项目名称
	private String xmmc;
	
	// 医保类型
	private String yblx;
	
	// 备注信息
	private String bzxx;
	
	// 自付比例
	private BigDecimal zfbl;
	
	// 操作类型   '0：新增；1：修改；2：删除';
	private String czlx;
	
	// 处理状态  '0:未处理；1：处理';
	private String clzt;
	
	// 同步时间
	private Date tbsj;
	
	// 处理时间
	private Date clsj;

	public String getYyxmbm() {
		return yyxmbm;
	}

	public void setYyxmbm(String yyxmbm) {
		this.yyxmbm = yyxmbm;
	}

	public String getYbxmbm() {
		return ybxmbm;
	}

	public void setYbxmbm(String ybxmbm) {
		this.ybxmbm = ybxmbm;
	}

	public String getXmmc() {
		return xmmc;
	}

	public void setXmmc(String xmmc) {
		this.xmmc = xmmc;
	}

	public String getYblx() {
		return yblx;
	}

	public void setYblx(String yblx) {
		this.yblx = yblx;
	}

	public String getBzxx() {
		return bzxx;
	}

	public void setBzxx(String bzxx) {
		this.bzxx = bzxx;
	}

	public BigDecimal getZfbl() {
		return zfbl;
	}

	public void setZfbl(BigDecimal zfbl) {
		this.zfbl = zfbl;
	}

	public String getCzlx() {
		return czlx;
	}

	public void setCzlx(String czlx) {
		this.czlx = czlx;
	}

	public String getClzt() {
		return clzt;
	}

	public void setClzt(String clzt) {
		this.clzt = clzt;
	}

	public Date getTbsj() {
		return tbsj;
	}

	public void setTbsj(Date tbsj) {
		this.tbsj = tbsj;
	}

	public Date getClsj() {
		return clsj;
	}

	public void setClsj(Date clsj) {
		this.clsj = clsj;
	}
}

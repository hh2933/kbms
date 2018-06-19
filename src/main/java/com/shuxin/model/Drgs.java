package com.shuxin.model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;


/**
*
* 规则表信息
*
*/
@TableName("t_bzzdb")
public class Drgs implements Serializable{
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;
	
	
	/** 主键id */
	@TableId(type = IdType.UUID)
	private String id;
	
	/**病种分类*/
	private String bzfl;
	
	/**病种序号 */	
	private String bzxh;
	
	/**病种编码 */
	private String bzbm;
	/**病种名称*/
	private String bzmc;
	
	/**病种类型*/
	private String bzlx;
	
	/**操作编码*/
	private String zlfsbm;
	/**操作名称*/
	private String zlfsmc;
	/**医疗总费用限额*/
	private String ylzfyxe;
	/**医保统筹支付限额*/
	private String ybtczfxe;
	/**限定住院时间*/
	private String xdzysj;
	/**报销比例*/
	private String bxbl;
	/**开始时间*/
	private Date starttime;
	/**结束时间*/
	private Date endtime;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBzfl() {
		return bzfl;
	}
	public void setBzfl(String bzfl) {
		this.bzfl = bzfl;
	}
	public String getBzxh() {
		return bzxh;
	}
	public void setBzxh(String bzxh) {
		this.bzxh = bzxh;
	}
	public String getBzbm() {
		return bzbm;
	}
	public void setBzbm(String bzbm) {
		this.bzbm = bzbm;
	}
	public String getBzmc() {
		return bzmc;
	}
	public void setBzmc(String bzmc) {
		this.bzmc = bzmc;
	}
	public String getBzlx() {
		return bzlx;
	}
	public void setBzlx(String bzlx) {
		this.bzlx = bzlx;
	}
	public String getZlfsbm() {
		return zlfsbm;
	}
	public void setZlfsbm(String zlfsbm) {
		this.zlfsbm = zlfsbm;
	}
	public String getZlfsmc() {
		return zlfsmc;
	}
	public void setZlfsmc(String zlfsmc) {
		this.zlfsmc = zlfsmc;
	}
	public String getYlzfyxe() {
		return ylzfyxe;
	}
	public void setYlzfyxe(String ylzfyxe) {
		this.ylzfyxe = ylzfyxe;
	}
	public String getYbtczfxe() {
		return ybtczfxe;
	}
	public void setYbtczfxe(String ybtczfxe) {
		this.ybtczfxe = ybtczfxe;
	}
	public String getXdzysj() {
		return xdzysj;
	}
	public void setXdzysj(String xdzysj) {
		this.xdzysj = xdzysj;
	}
	public String getBxbl() {
		return bxbl;
	}
	public void setBxbl(String bxbl) {
		this.bxbl = bxbl;
	}
	public Date getStarttime() {
		return starttime;
	}
	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}
	public Date getEndtime() {
		return endtime;
	}
	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}
	

	
	
	
	
	

}

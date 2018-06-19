package com.shuxin.model.ruleengine;

import java.io.Serializable;

public class RespInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2548536829968457552L;
	
	
	//返回码(0000,0001,0002,0003,0004)
	private String resultCode;
	//返回信息(正常,请求数据格式异常,验签失败,业务数据逻辑处理异常,数据库检索异常)
	private String resultMsg;
	//返回状态(S,F)
	private String resultStatus;
	
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultMsg() {
		return resultMsg;
	}
	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}
	public String getResultStatus() {
		return resultStatus;
	}
	public void setResultStatus(String resultStatus) {
		this.resultStatus = resultStatus;
	}
	
	
}

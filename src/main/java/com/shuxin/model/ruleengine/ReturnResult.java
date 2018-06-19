package com.shuxin.model.ruleengine;

import java.io.Serializable;

/**
 * 返回结果
 * @author shuxin
 *
 */
public class ReturnResult implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4056997983688527792L;
	//通用返回对象
	private RespInfo respInfo;
	//返回数据
	private Object respData;
	
	
	public RespInfo getRespInfo() {
		return respInfo;
	}
	public void setRespInfo(RespInfo respInfo) {
		this.respInfo = respInfo;
	}
	public Object getRespData() {
		return respData;
	}
	public void setRespData(Object respData) {
		this.respData = respData;
	}
	
	
}

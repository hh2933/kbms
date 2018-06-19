package com.shuxin.model.vo;

import java.util.List;

import com.shuxin.model.Dictionary;

public class DictionaryListVo {
	 /**
	  * 字典编码
	  */
	private  String  dictType 	;
	private  String  dictTypeCode	;
	
	private  List<DictionaryVo>  list;

	public String getDictType() {
		return dictType;
	}

	public void setDictType(String dictType) {
		this.dictType = dictType;
	}

	public List<DictionaryVo> getList() {
		return list;
	}

	public void setList(List<DictionaryVo> list) {
		this.list = list;
	}

	public String getDictTypeCode() {
		return dictTypeCode;
	}

	public void setDictTypeCode(String dictTypeCode) {
		this.dictTypeCode = dictTypeCode;
	}
		 
	
	
}

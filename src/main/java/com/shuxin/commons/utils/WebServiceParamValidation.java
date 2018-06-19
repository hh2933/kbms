package com.shuxin.commons.utils;

import java.util.Iterator;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;

import com.shuxin.model.ruleengine.WSReturnResult;

public class WebServiceParamValidation {
	
	/**
	 * 检验参数
	 * @param requestVO
	 * @param returnResult
	 * @return
	 */
	public static  WSReturnResult baseCheckInputVO(Object requestVO,WSReturnResult returnResult)
	{		
		if (null == requestVO) {
			returnResult.setResultCode("0001");
			returnResult.setResultMsg("信息不能为空！");
			returnResult.setResultStatus("F");
		    return returnResult;
		}
		Set<ConstraintViolation<Object>> validResult = Validation.buildDefaultValidatorFactory().getValidator()
		         .validate(requestVO);
		if (null != validResult && validResult.size() > 0) {
		    StringBuilder sb = new StringBuilder();
		    for (Iterator<ConstraintViolation<Object>> iterator = validResult.iterator(); iterator.hasNext();) {
		        ConstraintViolation<Object> constraintViolation = (ConstraintViolation<Object>) iterator.next();
		         //这里只取了字段名，如果需要其他信息可以自己处理
		        sb.append(constraintViolation.getMessage()).append("、");
		    }
			if(sb.lastIndexOf("、") == sb.length()-1){
			    sb.delete(sb.length()-1, sb.length());
			}
			returnResult.setResultCode("0001");
			returnResult.setResultMsg("请检查传入的数据格式异常:"+sb.toString());
			returnResult.setResultStatus("F");
			return returnResult;
		}
		returnResult.setResultCode("0000");
		returnResult.setResultMsg("正常");
		returnResult.setResultStatus("S");
		return returnResult;
	}

}

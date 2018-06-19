package com.shuxin.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shuxin.commons.utils.JsonUtils;
import com.shuxin.model.Parameter;
import com.shuxin.service.IParamterService;


@Controller
@RequestMapping("/parameter")
public class ParamterController {

	@Autowired
	private IParamterService paramterService;
	

	/**
	 * 
	 * @param depart
	 * @param page
	 * @param rows
	 * @param sort
	 * @param order
	 * @return
	 */
    @RequestMapping("/getType")
    @ResponseBody
    public Object getType(String clum) {
       
         List<Parameter> paramterType=paramterService.findDepartType(clum);
         
         
         String json= JsonUtils.toJson(paramterType);
		
        
        return json;
        
    }
    /**
     * 
     * @param depart
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     */
    @RequestMapping("/getType2")
    @ResponseBody
    public Object getType2(String clum) {
    	
    	List<Parameter> paramterType=paramterService.findDepartType(clum);
    	List<Parameter> changeType=new ArrayList<Parameter>();   
    	 Parameter  param=new Parameter();
    	 param.setpName("-请选择-");
    	 param.setpValue("");
    	 param.setpType("ruleType");
    	 param.setRemark("医保规则类型");
    	changeType.add(param);
    	for (int i = 0; i < paramterType.size(); i++) {
    		changeType.add(paramterType.get(i))	;
		}
    	
    	String json= JsonUtils.toJson(changeType);
    	
    	
    	return json;
    	
    }
	
	
	
}

package com.shuxin.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shuxin.commons.base.BaseController;
import com.shuxin.commons.shiro.ShiroUser;
import com.shuxin.commons.utils.JsonUtils;
import com.shuxin.commons.utils.PageInfo;
import com.shuxin.commons.utils.StringUtils;
import com.shuxin.model.Drgs;
import com.shuxin.service.IDrgsService;

@Controller
@RequestMapping("/drgs")
public class DrgsController extends BaseController{

	@Autowired
	private IDrgsService drgsService;
	

	@RequestMapping("/drgsmanager")
	public String  drgsmanager(HttpServletRequest request,  Model model){
		  model=getMenuId(request, model);
		return "subSetting/drgsSetting";
	}
	@RequestMapping("/mtdrgsmanager")
	public String  mtdrgsmanager(HttpServletRequest request,  Model model){
		model=getMenuId(request, model);
		return "subSetting/mTDrgsSetting";
	}
	
	
	

    @PostMapping("/drgsDataGrid")
    @ResponseBody
    public Object drgsDataGrid(Drgs drgs,String type, Integer page, Integer rows, String sort, String order) {
        PageInfo pageInfo = new PageInfo(page, rows, "", "");
        Map<String, Object> condition = new HashMap<String, Object>();

        if (StringUtils.isNotBlank(type)) {
            condition.put("type", type);
        }
        pageInfo.setCondition(condition);

        drgsService.drgsDataGrid(pageInfo);
        return pageInfo;
        
    }
    
    @RequestMapping("/editDrgs")
	@ResponseBody
	public Object editDrgs(Drgs drgs,String type){
	ShiroUser user = getShiroUser();
	  List<Drgs> list = drgsService.selectByxh(drgs);
      if (list.size()>0) {
          return renderError("该病种已存在!");
      }
      drgs.setBzlx(type);
      drgsService.editDrgs(drgs,user);
      return renderSuccess("操作成功");

	

	}
    
    /**
     * 删除病种
     * @param id
     * @return
     */
    @RequestMapping("/deleteDrgs")
    @ResponseBody
    public Object deleteDrgs(  Drgs drgs ) {
    	ShiroUser user = getShiroUser();
    	 String  ids=drgs.getId();
    		String[]  allIds=ids.split(",");
   		 List<String>  Ids=new  ArrayList<String>();
   		  for (int j = 0; j < allIds.length; j++) {
   			Ids.add(allIds[j]);
   		}
   		drgsService.deleteDrgs(Ids,user);
        return renderSuccess("删除成功！");
    }
	
	
	
	
    
    /**
     *得到病种分类
     * @param id
     * @return
     */
    @RequestMapping("/getType")
    @ResponseBody
    public Object getType(  HttpServletRequest request) {
    	 List<Map<String, Object>> paramterType=drgsService.drgsType();    
         String json= JsonUtils.toJson(paramterType);
        return json;
    }
    
    
    
    
	
	
	
}

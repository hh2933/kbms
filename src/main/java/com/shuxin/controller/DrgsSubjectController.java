package com.shuxin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shuxin.commons.base.BaseController;
import com.shuxin.commons.shiro.ShiroUser;
import com.shuxin.commons.utils.PageInfo;
import com.shuxin.commons.utils.StringUtils;
import com.shuxin.model.DrgsSubject;
import com.shuxin.model.vo.DrgsSubjectListVo;
import com.shuxin.service.IDrgsSubjectService;

@Controller
@RequestMapping("/drgsSubject")
public class DrgsSubjectController extends BaseController{
   @Autowired
   private IDrgsSubjectService drgsSubjectService;
	
   
	
   @PostMapping("/drgsSubjectDataGrid")
   @ResponseBody
   public Object drgsSubjectDataGrid(String bzxh, Integer page, Integer rows, String sort, String order) {
       PageInfo pageInfo = new PageInfo(page, rows, "", "");
       Map<String, Object> condition = new HashMap<String, Object>();

       if (bzxh!=null&&StringUtils.isNotBlank(bzxh)) {
           condition.put("bzxh", bzxh);
       }
       pageInfo.setCondition(condition);
       drgsSubjectService.drgsSubjectDataGrid(pageInfo);
       return pageInfo;
       
   }
	
   
   
	@RequestMapping("/add")
	@ResponseBody
	public Object addDrgs(@RequestBody DrgsSubjectListVo vo){
	ShiroUser user = getShiroUser();
	if (vo.getList().size()>0&&vo.getList().get(0).getId() != null
			&& !vo.getList().get(0).getId().equals("")) {
		
		List<DrgsSubject> finddic = drgsSubjectService.selectByBm(vo.getList().get(0));
		if(finddic.size()>0){
			return renderError("该项目类型已存在");			
		}
		drgsSubjectService.addDrgs(user, vo);

	} else {
		if(vo.getList().size()>0){
			int p=0;
			for (int i = 0; i < vo.getList().size(); i++) {
				List<DrgsSubject> finddic = drgsSubjectService.selectByBm(vo.getList().get(i));
				if (finddic.size() > 0) {
					p=1;
					break;
				}
			}
			if(p==1){
				return renderError("该项目类型已存在");
			}
			drgsSubjectService.addDrgs(user, vo);
		}
		
	}

	if (StringUtils.isNotBlank(vo.getList().get(0).getId())) {
		return renderSuccess("修改成功");
	} else {
		return renderSuccess("添加成功");
	}

	}
   
   
	@RequestMapping("/delete")
	@ResponseBody
	public Object deleteDrgsSubject(@RequestBody DrgsSubjectListVo vo){
		
		ShiroUser user=getShiroUser();
		drgsSubjectService.deleteDrgsSubject(user,vo);
			
		return renderSuccess("删除成功");

	}
   
   
   
}

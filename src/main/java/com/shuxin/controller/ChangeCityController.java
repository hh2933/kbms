package com.shuxin.controller;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shuxin.commons.base.BaseController;
import com.shuxin.commons.utils.StringUtils;
import com.shuxin.service.IRoleService;
import com.shuxin.service.ISysUserService;

@Controller
@RequestMapping("/changeCity")
public class ChangeCityController extends BaseController{

	@Autowired
	private ISysUserService sysUserService;
	
    @Autowired 
    private IRoleService roleService;
	
	 @RequestMapping("/changeCity")
	 @ResponseBody
	public Object changeCity(HttpSession httpSession)
	{
		 String userId=sysUserService.selectUserIdByLoginName(getShiroUser().getLoginName());
		 if(StringUtils.isNotBlank(userId))
		 {
			 Map<String, Set<String>> resourceMap = roleService.selectResourceMapByUserId(userId);
		     Set<String> roles = resourceMap.get("roles");
		     getShiroUser().setRoles(roles);
		     getShiroUser().setId(userId);
		     getShiroUser().setCity((String)httpSession.getAttribute("city"));
			 httpSession.removeAttribute("tree");
			 return renderSuccess("ok");
		 }
		 return renderError("fail");
	}
}

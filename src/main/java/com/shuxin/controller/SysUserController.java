/**
 *	Develop by summer at  2017-3-10 下午04:10:54
 *	Copyright ? 2017济南数昕医疗信息技术有限公司
 *	注意：SysUserController.java不是开源产品，任何未经许可的源码修改、重新发布、反编译，都会被追究法律责任
 *
 */
package com.shuxin.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shuxin.commons.base.BaseController;
import com.shuxin.commons.utils.DigestUtils;
import com.shuxin.commons.utils.JsonUtils;
import com.shuxin.commons.utils.PageInfo;
import com.shuxin.commons.utils.StringUtils;
import com.shuxin.model.Role;
import com.shuxin.model.SysUser;
import com.shuxin.model.User;
import com.shuxin.model.vo.SysUserVo;
import com.shuxin.model.vo.UserVo;
import com.shuxin.service.ISysUserService;

/**
 * 用户管理
 * @author summeryuan
 * @email  905532187qq.com
 */
@Controller
@RequestMapping("/user")
public class SysUserController extends BaseController  {
	@Autowired
	private ISysUserService sysUserService;
	
	
	
	   /**
     * 用户管理页
     *
     * @return
     */
    @RequestMapping("/manager")
  //  @RequiresPermissions("/user/manager")
    public String manager(HttpServletRequest request,  Model model){
		  model=getMenuId(request, model);
	//	   model.addAttribute("flag", 0);
		   model.addAttribute("roleIds", "");
		   
        return "admin/user";
    }

    /**
     * 用户管理列表
     *@author summer
     * @param userVo
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     */
    @PostMapping("/dataGrid")
    @ResponseBody
    public Object dataGrid(SysUserVo userVo, Integer page, Integer rows, String sort, String order) {
        PageInfo pageInfo = new PageInfo(page, rows, sort, order);
        Map<String, Object> condition = new HashMap<String, Object>();

        if (StringUtils.isNotBlank(userVo.getName())) {
            condition.put("name", userVo.getName());
        }
       /* if (userVo.getOrganizationId() != null) {
            condition.put("organizationId", userVo.getOrganizationId());
        }*/
        if (userVo.getCreatedateStart() != null) {
            condition.put("startTime", userVo.getCreatedateStart());
        }
        if (userVo.getCreatedateEnd() != null) {
            condition.put("endTime", userVo.getCreatedateEnd());
        }
        pageInfo.setCondition(condition);
        sysUserService.selectDataGrid(pageInfo);
        return pageInfo;
    }

    /**
     * 添加用户页
     *
     * @return
     */
    @GetMapping("/addPage")
    public String addPage() {
        return "admin/userAdd";
    }

    /**
     * 添加用户
     *
     * @param userVo
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public Object add(SysUserVo userVo,HttpSession httpSession) {
        List<SysUser> list = sysUserService.selectByLoginName(userVo);
        if (list != null && !list.isEmpty()) {
            return renderError("用户名已存在!");
        }
        userVo.setUserPassword(DigestUtils.md5Hex(userVo.getUserPassword()));
        userVo.setCity((String)httpSession.getAttribute("city"));
        sysUserService.insertByVo(userVo);
   /*     insertOperationLog(getShiroUser().getLoginName(),
    			"add","T_USER",userVo.toString());*/
        return renderSuccess("添加成功");
    }

    /**
     * 编辑用户页
     *
     * @param id
     * @param model
     * @return
     */
    @PostMapping("/editPage")
    @ResponseBody
    public String editPage(Model model, String userId,HttpServletRequest request){
		  model=getMenuId(request, model);

    	SysUserVo userVo = sysUserService.selectVoById(userId);
        List<Role> rolesList = userVo.getRolesList();
        List<String> ids = new ArrayList<String>();
   //     StringBuffer ids = new StringBuffer("'");
        if( rolesList.size()>0){
        for (int i = 0; i < rolesList.size(); i++) {
        	     if(rolesList.get(0).getId()!=null){
        	    	 ids.add(rolesList.get(0).getId());
        	     }
        	
			/* if(i==0){
				 ids=ids.append(rolesList.get(0).getId());
			 }else{
				 ids=ids.append(",");
				 ids=ids.append(rolesList.get(i).getId());
			 }*/
        	
		}
        }
      //  ids=ids.append("'");
//        for (Role role : rolesList) {
//            ids.add(role.getId());
//        }
        HashMap<String, Object> datamap=new HashMap<String, Object>();
        datamap.put("user", userVo);
        datamap.put("roleIds", ids);
    	String jsonStr = JsonUtils.toJson(datamap); 
		return jsonStr;
    //    model.addAttribute("roleIds", ids);
    //    model.addAttribute("user", userVo);
    //    model.addAttribute("flag", 1);
     //   return "admin/user";
    }

    /**
     * 编辑用户
     *
     * @param userVo
     * @return
     */
    @RequestMapping("/edit")
    @ResponseBody
    public Object edit(SysUserVo userVo) {
        List<SysUser> list = sysUserService.selectByLoginName(userVo);
        if (list != null && !list.isEmpty()) {
            return renderError("用户名已存在!");
        }
        if (StringUtils.isNotBlank(userVo.getUserPassword())) {
            userVo.setUserPassword(DigestUtils.md5Hex(userVo.getUserPassword()));
        }
        sysUserService.updateByVo(userVo);
     /*   insertOperationLog(getShiroUser().getLoginName(),
    			"update","T_USER",userVo.toString());*/
        return renderSuccess("修改成功！");
    }

    /**
     * 修改密码页
     *
     * @return
     */
    @GetMapping("/editPwdPage")
    public String editPwdPage() {
        return "admin/userEditPwd";
    }

    /**
     * 修改密码
     *
     * @param oldPwd
     * @param pwd
     * @return
     */
    @RequestMapping("/editUserPwd")
    @ResponseBody
    public Object editUserPwd(String oldPwd, String pwd) {
    	SysUser user = sysUserService.selectById(getUserId());
        if (!user.getUserPassword().equals(DigestUtils.md5Hex(oldPwd))) {
            return renderError("老密码不正确!");
        }
        sysUserService.updatePwdByUserId(getUserId(), DigestUtils.md5Hex(pwd));
        return renderSuccess("密码修改成功！");
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Object delete(  HttpServletRequest request) {
    	 String  ids=request.getParameter("id");
    		String[]  allIds=ids.split(",");
   		 List<String>  userIds=new  ArrayList<String>();
   		  for (int j = 0; j < allIds.length; j++) {
   			userIds.add(allIds[j]);
   		}
        sysUserService.deleteUserById(userIds);
   /*     insertOperationLog(getShiroUser().getLoginName(),
    			"delete","T_USER",userIds.toString());*/
        return renderSuccess("删除成功！");
    }
	

}

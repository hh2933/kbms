package com.shuxin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shuxin.commons.base.BaseController;
import com.shuxin.commons.utils.JsonUtils;
import com.shuxin.commons.utils.PageInfo;
import com.shuxin.model.Role;
import com.shuxin.model.WorkIndex;
import com.shuxin.service.IRoleMenuService;
import com.shuxin.service.IRoleService;
import com.shuxin.service.IRoleUserService;

/**
 * @description：权限管理
 */
@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {

    @Autowired
    private IRoleService roleService;
    @Autowired
    private IRoleMenuService roleMenuService;
    @Autowired
    private IRoleUserService roleUserService;

    /**
     * 权限管理页
     *
     * @return
     */
    //@RequiresPermissions("/role/manager")
    @GetMapping("/manager")
    public String manager(WorkIndex workIndex,HttpServletRequest request,  Model model){
		  model=getMenuId(request, model);
        return "admin/role";
    }

    /**
     * 权限列表
     *
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     */

    @PostMapping("/dataGrid")
    @ResponseBody
    public Object dataGrid(Integer page, Integer rows, String sort, String order) {
        PageInfo pageInfo = new PageInfo(page, rows, sort, order);
        roleService.selectDataGrid(pageInfo);
        return pageInfo;
    }

    /**
     * 权限树
     *
     * @return
     */
    @PostMapping("/tree")
    @ResponseBody
    public Object tree() {
        return roleService.selectTree();
    }

    /**
     * 添加权限页
     *
     * @return
     */
    
    @GetMapping("/addPage")
    //@RequiresPermissions("/role/manager")
    public String addPage() {
        return "admin/roleAdd";
    }

    /**
     * 添加权限
     *
     * @param role
     * @return
     */
    //@RequiresPermissions("/role/manager")
    @PostMapping("/add")
    @ResponseBody
    public Object add(Role role) {
    	//在添加之前验证用户名是否重复
    	String name= role.getName();
    	Map<String, Object> map=new HashMap<String,Object>();
    	map.put("name",name);
    	List<Role> list= roleService.selectByMap(map);
        if(list.size()!=0){
        	//验证失败
        	return renderError("添加失败:用户名重复了！");
        }
        //验证成功
		//insertOperationLog(getShiroUser().getLoginName(),
		//		"add","t_role",role.toString());
        roleService.insert(role);
        return renderSuccess("添加成功！");
    }

    /**
     * 删除权限
     *
     * @param id
     * @return
     */
    //@RequiresPermissions("/role/manager")
    @RequestMapping("/delete")
    @ResponseBody
    public Object delete(String id) {
        roleService.deleteById(id);
    	//删除角色之后把roleMenu删除
    	Map<String, Object> map=new HashMap<String,Object>();
    	map.put("role_id", id);
    	roleMenuService.deleteByMap(map);
    	//删除角色之后把roleUser删除
    	roleUserService.deleteByMap(map);
		//insertOperationLog(getShiroUser().getLoginName(),
		//		"delete","t_role",id);
    	//两张关联表删除后即可
        return renderSuccess("删除成功！");
    }

    /**
     * 编辑权限页
     *
     * @param model
     * @param id
     * @return
     */
    
    @RequestMapping("/editPage")
    //@RequiresPermissions("/role/manager")
    public String editPage(Model model, String id) {
        Role role = roleService.selectById(id);
        model.addAttribute("role", role);
        return "admin/roleEdit";
    }
    //@RequiresPermissions("/role/manager")
    @RequestMapping("/editPageNew")
    @ResponseBody
    public Object editPageNew(String id) {
        Role role = roleService.selectById(id);
        return JsonUtils.toJson(role);
    }

    /**
     * 删除权限
     *
     * @param role
     * @return
     */
    //@RequiresPermissions("/role/manager")
    @RequestMapping("/edit")
    @ResponseBody
    public Object edit(Role role) {
    	//根据要修改的角色的ID获取到要修改的角色
    	Role roletobeupdate= roleService.selectById(role.getId());
    	//在修改之前验证用户名是否重复
    	String name= role.getName();
    	Map<String, Object> map=new HashMap<String,Object>();
    	map.put("name",name);
    	List<Role> list= roleService.selectByMap(map);
        if(list.size()!=0){
        	//进入了这个if这个时候说明在修改自己
        	if(role.getName().equals(roletobeupdate.getName())){
                roleService.updateById(role);
                return renderSuccess("编辑成功！");
        	}
        	//验证失败
        	return renderError("添加失败:用户名重复了！");
        }
        //验证成功
        roleService.updateById(role);
		//insertOperationLog(getShiroUser().getLoginName(),
		//		"update","t_role",role.toString());
        return renderSuccess("编辑成功！");
    }

    /**
     * 授权页面
     *
     * @param id
     * @param model
     * @return
     */
    
    @GetMapping("/grantPage")
    //@RequiresPermissions("/role/manager")
    public String grantPage(Model model, String id) {
        model.addAttribute("id", id);
        return "admin/roleGrant";
    }

    /**
     * 授权页面页面根据角色查询资源
     *
     * @param id
     * @return
     */
    @RequestMapping("/findResourceIdListByRoleId")
    @ResponseBody
    public Object findResourceByRoleId(String id) {
        List<String> resources = roleService.selectResourceIdListByRoleId(id);
        return renderSuccess(resources);
    }

    /**
     * 授权
     *
     * @param id
     * @param resourceIds
     * @return
     */
    //@RequiresPermissions("/role/manager")
    @RequestMapping("/grant")
    @ResponseBody
    public Object grant(String id, String resourceIds) {
        roleService.updateRoleResource(id, resourceIds);
		//insertOperationLog(getShiroUser().getLoginName(),
		//		"update","t_role_menu","id:"+id+",resourceIds:"+resourceIds);
        return renderSuccess("授权成功！");
    }

}

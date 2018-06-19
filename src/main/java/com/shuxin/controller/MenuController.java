package com.shuxin.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shuxin.commons.base.BaseController;
import com.shuxin.commons.result.Tree;
import com.shuxin.commons.shiro.ShiroUser;
import com.shuxin.commons.utils.PageInfo;
import com.shuxin.model.Menu;
import com.shuxin.service.IMenuService;

/**
 * @description：菜单管理
 */
@Controller
@RequestMapping("/menu")
public class MenuController extends BaseController {
	
    @Autowired
    private IMenuService menuService;
     
    /**
     * 菜单树
     *
     * @return
     */
    @PostMapping("/tree")
    @ResponseBody
    public Object tree(HttpSession httpSession) {
        ShiroUser shiroUser = getShiroUser();
        List<Tree> treeList= null;
        if(httpSession.getAttribute("tree")==null)
        {
        	treeList=menuService.selectTree(shiroUser);
        	httpSession.setAttribute("tree", treeList);
        }
        else
        {
        	treeList = (List<Tree>)httpSession.getAttribute("tree");
        }
        
        return treeList;
    }    
    
    
    @RequestMapping("/allTree")
    @ResponseBody
    public Object allMenu() {
        return menuService.selectAllMenu();
    }
    @RequestMapping("/allTrees")
    @ResponseBody
    public Object allTree() {
        return menuService.selectAllTree();
    }
}

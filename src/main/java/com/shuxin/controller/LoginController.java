package com.shuxin.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shuxin.commons.base.BaseController;
import com.shuxin.commons.csrf.CsrfToken;
import com.shuxin.commons.result.Tree;
import com.shuxin.commons.shiro.ShiroUser;
import com.shuxin.commons.utils.StringUtils;
import com.shuxin.service.IMenuService;

/**
 * @description：登录退出
 */
@Controller
public class LoginController extends BaseController {
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private HttpSession session;
	
    @Autowired
    private IMenuService menuService;
     
    /**
     * 首页
     *
     * @return
     */
    @GetMapping("/")
    public String index() {
        return "redirect:/index";
    }

    /**
     * 首页
     *
     * @param model
     * @return
     */
    @GetMapping("/index")
    public String index(Model model) {
        return "redirect:"+getFirstMenu();
    
    }

    /**
     * GET 登录
     * @return {String}
     */
    @GetMapping("/login")
    @CsrfToken(create = true)
    public String login() {
        logger.info("GET请求登录");
        if (SecurityUtils.getSubject().isAuthenticated()) {
            return "redirect:"+getFirstMenu();
        }
        //ShiroHelper.clearCurrentUserCache();
        return "login";
    }

    /**
     * POST 登录 shiro 写法
     *
     * @param username 用户名
     * @param password 密码
     * @return {Object}
     */
    @PostMapping("/login")
    @CsrfToken(remove = true)
    @ResponseBody
    public Object loginPost(HttpServletRequest request, String username, String password,String city, String captcha, 
            @RequestParam(value = "rememberMe", defaultValue = "0") Integer rememberMe) {
        logger.info("POST请求登录");
        // 改为全部抛出异常，避免ajax csrf token被刷新
        if (StringUtils.isBlank(username)) {
        	return renderError("用户名不能为空");
            //throw new RuntimeException("用户名不能为空");
        }
        if (StringUtils.isBlank(password)) {
        	return renderError("密码不能为空");
            //throw new RuntimeException("密码不能为空");
        }
        
        if (StringUtils.isBlank(city)) {
        	return renderError("城市不能为空");
            //throw new RuntimeException("密码不能为空");
        }
//        if (StringUtils.isBlank(captcha)) {
//            throw new RuntimeException("验证码不能为空");
//        }
//        if (!CaptchaUtils.validate(request, captcha)) {
//            throw new RuntimeException("验证码错误");
//        }
        Subject user = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        // 设置记住密码
        token.setRememberMe(1 == rememberMe);
        try {
            user.login(token);
            //User asd = ShiroHelper.getCurrentUser();
            return renderSuccess();
        } catch (UnknownAccountException e) {
        	return renderError("账号不存在");
            //throw new RuntimeException("账号不存在！", e);	
        } catch (DisabledAccountException e) {
        	return renderError("账号未启用");
        	
           // throw new RuntimeException("账号未启用！", e);
        } catch (IncorrectCredentialsException e) {
        	return renderError("密码错误");
        	//request.setAttribute("loginmsg", "err");
            //throw new RuntimeException("密码错误！", e);
        } catch (Throwable e) {
        	return renderError("未知错误");
           // throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 未授权
     * @return {String}
     */
    @GetMapping("/unauth")
    public String unauth() {
        if (SecurityUtils.getSubject().isAuthenticated() == false) {
            return "redirect:/login";
        }
        return "unauth";
    }

    /**
     * 退出
     * @return {Result}
     */
    @RequestMapping("/logout")
    @ResponseBody
    public Object logout() {
        logger.info("登出");
        Subject subject = SecurityUtils.getSubject();
        //ShiroHelper.clearCurrentUserCache();
        subject.logout();
        return renderSuccess();
    }

    public String getFirstMenu(){
        ShiroUser shiroUser = getShiroUser();
        List<Tree> treeList= null;
        treeList=menuService.selectTree(shiroUser);
        session.setAttribute("tree", treeList);
        
        
//        List<Tree> list=menuService.selectTree(shiroUser);
        if(treeList==null || treeList.size()==0){
        	return "err";
        }
    	Tree t1= treeList.get(0);
    	Tree t2=null;
    	Tree t3=null;
    	if(t1.getAttributes()!=null){
    		return t1.getAttributes().toString()+"?id="+t1.getId();
    	}else{
    		String t1id= t1.getId();
    		for (Tree childt : treeList) {
				if(childt.getPid().equals(t1id)){
					t2=childt;
					break;
				}
			}
    		if(t2==null){
    			return "err";
    		}else{
    			if(t2.getAttributes()!=null){
    				return t2.getAttributes().toString()+"?pid="+t1.getId()+"&id="+t2.getId();
    			}else{
    				String t2id= t2.getId();
    	    		for (Tree childchildt : treeList) {
    					if(childchildt.getPid().equals(t2id)){
    						t3=childchildt;
    						break;
    					}
    				}
    	    		if(t3==null){
    	    			return "err";
    	    		}else{
    	    			if(t3.getAttributes()==null){
    	    				return "err";
    	    			}else{
    	    				return t3.getAttributes().toString()+"?ppid="+t1.getId()+"&pid="+t2.getId()+"&id="+t3.getId();
    	    			}
    	    		}
    			}
    		}
    	}
    }
}

package com.shuxin.commons.scan;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.shuxin.commons.datasource.DynamicDataSourceHolder;
import com.shuxin.commons.shiro.ShiroUser;
import com.shuxin.commons.utils.StringUtils;


/**
 * @description：AOP 日志
 */
@Aspect
@Component
public class DataSourceAspect {
    private static final Logger logger = LogManager.getLogger(DataSourceAspect.class);


    @Pointcut("!execution(* com.shuxin.controller.ruleengine.*.*(..)) && execution(* com.shuxin.controller.*.*(..))")
    public void cutController() {}

    @Before("cutController()")
    public void changeDataSource() throws Throwable {
                
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();    
        HttpSession session = request.getSession(); 
        String city="";  
        ShiroUser shiroUser= (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        if(request.getParameter("city")!=null)
        {
        	session.setAttribute("city", request.getParameter("city"));
        	city = request.getParameter("city");
        }
        else if(session.getAttribute("city")!=null)
        {
        	city = String.valueOf(session.getAttribute("city"));        	
        }
        else if(shiroUser != null)
        {
        	city = shiroUser.getCity();
        	session.setAttribute("city", city);
        }
        
        if(StringUtils.isNotBlank(city))
        {        	
        	DynamicDataSourceHolder.setDataSource("dataSource_"+city);
        }
       
    }  
   
}

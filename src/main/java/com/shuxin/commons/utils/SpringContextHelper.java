package com.shuxin.commons.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringContextHelper implements ApplicationContextAware {
	
	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext=applicationContext;
	}

	public static ApplicationContext getApplicationContext()
	{
		return applicationContext;
	}
	
	public static Object getBean(String name)
	{
		return applicationContext.getBean(name);
	}
}

package com.shuxin.commons.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.xml.ws.Endpoint;

import com.shuxin.commons.utils.PropertiesLoader;
import com.shuxin.webservice.impl.RuleEngineWebServiceImpl;
@WebListener
public class RuleEngineListener implements ServletContextListener {
	

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		PropertiesLoader propertiesLoader = new PropertiesLoader("application.properties");
		Endpoint.publish("http://"+propertiesLoader.getProperty("webservice.ip")+":"+propertiesLoader.getProperty("webservice.port")+"/kbms/examine", new RuleEngineWebServiceImpl());  
        System.out.println("publish success...");
	}

}

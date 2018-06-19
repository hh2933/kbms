package com.shuxin.TimerTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.shuxin.commons.utils.SpringContextHelper;
import com.shuxin.service.ruleengine.IRuleEngineRecovService;

public class DataRecoverTimerTask extends TimerTask{
	
	 private Logger logger =LogManager.getLogger(this.getClass());

	 private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    @Override
	    public void run() {
	        try {
	        	logger.info("执行当前时间"+formatter.format(Calendar.getInstance().getTime()));
	        	IRuleEngineRecovService ruleEngineRecovService =(IRuleEngineRecovService) SpringContextHelper.getBean("ruleEngineRecovServiceImpl");
//	        	ruleEngineRecovService.examine();
	        	
	        } catch (Exception e) {
	        	logger.info("-------------解析信息发生异常--------------");
	        }
	    }

}

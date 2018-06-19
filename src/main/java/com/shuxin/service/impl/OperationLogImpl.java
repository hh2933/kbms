package com.shuxin.service.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.shuxin.mapper.OperationLogMapper;
import com.shuxin.model.OperationLog;
import com.shuxin.service.IOperationLogService;

@Service
public class OperationLogImpl extends ServiceImpl<OperationLogMapper,OperationLog> implements IOperationLogService {
	private ExecutorService logThreadPool = Executors.newFixedThreadPool(1);

	private final OperationLogImpl operationLogImpl = this;
	
	private Logger logger = LogManager.getLogger(getClass());
	
	@Override
	public void insertLog(final OperationLog operationLog) {
		
		
		
		logThreadPool.execute(new Runnable() {
			
			@Override
			public void run() {
				try
				{
					operationLogImpl.insert(operationLog);
				}
				catch(Exception e)
				{
					e.printStackTrace();
					logger.error(e.getMessage());
				}
				
				
			}
		});
	}
	
	

}

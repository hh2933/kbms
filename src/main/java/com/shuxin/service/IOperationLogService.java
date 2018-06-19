package com.shuxin.service;

import com.baomidou.mybatisplus.service.IService;
import com.shuxin.model.OperationLog;

public interface IOperationLogService extends IService<OperationLog>{

	
	public void insertLog(OperationLog operationLog);
}

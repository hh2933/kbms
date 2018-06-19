package com.shuxin.service.impl.ruleengine;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shuxin.mapper.ruleengine.HISRequestInfoMapper;
import com.shuxin.service.ruleengine.IHISRequestInfoService;
@Service("hISRequestInfoServiceImpl")
public class HISRequestInfoServiceImpl implements IHISRequestInfoService {

	@Autowired
	private HISRequestInfoMapper hISRequestInfoMapper;
	
	@Override
	public void saveHISRequestInfo(Map<String, String> paramMap) {
		hISRequestInfoMapper.saveHISRequestInfo(paramMap);
	}

	@Override
	public void updateHis(Map<String, String> paramMap) {
		hISRequestInfoMapper.updateHis(paramMap);
		
	}

}

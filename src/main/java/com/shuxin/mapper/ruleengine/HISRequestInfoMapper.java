package com.shuxin.mapper.ruleengine;

import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.shuxin.model.CommonModel;

public interface HISRequestInfoMapper  extends BaseMapper<CommonModel>{
	
	public void saveHISRequestInfo(Map<String, String> paramMap);
	
	public void updateHis (Map<String, String> paramMap);

}

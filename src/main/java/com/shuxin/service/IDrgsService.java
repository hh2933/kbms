package com.shuxin.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.shuxin.commons.shiro.ShiroUser;
import com.shuxin.commons.utils.PageInfo;
import com.shuxin.model.Drgs;

public interface IDrgsService extends IService<Drgs>{

public	void drgsDataGrid(PageInfo pageInfo);

public List<Drgs> selectByxh(Drgs drgs);

public void editDrgs(Drgs drgs, ShiroUser user);

public void deleteDrgs(List<String> ids, ShiroUser user);

public List<Map<String, Object>> drgsType();

	
	
	
	
	
}

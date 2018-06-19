package com.shuxin.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.shuxin.commons.shiro.ShiroUser;
import com.shuxin.commons.utils.PageInfo;
import com.shuxin.model.MIStandardMap;

public interface IMIStandardMapService  extends IService<MIStandardMap>{

	public void findmiStandardDataGrid(PageInfo pageInfo);

	public List<MIStandardMap> selectBycode(MIStandardMap standardMap);

	public void editDiagnosis(MIStandardMap standardMap, ShiroUser user);

	public void deleteMiStandardMap(List<String> ids, ShiroUser user);

	public void importData(List<Map<String, String>> exportList, ShiroUser user);

	public List<Map<String, Object>> selectDetailMIStandardMap();

	public List<Map<String, Object>> selectMIStandardMapHistory();

	
	
	
	
}

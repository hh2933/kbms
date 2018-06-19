package com.shuxin.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.shuxin.commons.shiro.ShiroUser;
import com.shuxin.commons.utils.PageInfo;
import com.shuxin.model.MIDiagnosis;
import com.shuxin.model.MIStandardMap;

public interface IMIDiagnosisService extends IService<MIDiagnosis>{

	public void findMIDiagnosisDataGrid(PageInfo pageInfo);

	public List<MIDiagnosis> selectBycode(MIDiagnosis diagnosis);

	public void editDiagnosis(MIDiagnosis diagnosis, ShiroUser user);

	public void deleteMIDiagnosis(List<String> ids, ShiroUser user);

	public void importData(List<Map<String, String>> exportList, ShiroUser user);

	public List<Map<String, Object>> selectDetailMIDiagnosis();

	public List<Map<String, Object>> selectMIDiagnosisHistory();

	
	
	
	
	
	
	
	
	
	
	
	
	
}

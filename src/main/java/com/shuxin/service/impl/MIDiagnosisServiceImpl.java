package com.shuxin.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.shuxin.commons.shiro.ShiroUser;
import com.shuxin.commons.utils.BeanUtils;
import com.shuxin.commons.utils.PageInfo;
import com.shuxin.commons.utils.StringUtils;
import com.shuxin.mapper.MIDiagnosisMapper;
import com.shuxin.mapper.MIDiagnosisOptMapper;
import com.shuxin.model.MIDiagnosis;
import com.shuxin.model.MIDiagnosisOpt;
import com.shuxin.model.MIStandardMap;
import com.shuxin.model.ProjectCatalogOpt;
import com.shuxin.service.IMIDiagnosisService;
@Service
public class MIDiagnosisServiceImpl  extends ServiceImpl<MIDiagnosisMapper, MIDiagnosis> implements IMIDiagnosisService{
  @Autowired
  private MIDiagnosisMapper miDiagnosisMapper;
  @Autowired
  private MIDiagnosisOptMapper miDiagnosisOptMapper;

  
@Override
public void findMIDiagnosisDataGrid(PageInfo pageInfo) {
	// TODO Auto-generated method stub
	 Page<MIDiagnosis> page = new Page<MIDiagnosis>(pageInfo.getNowpage(), pageInfo.getSize());
     String orderField = com.baomidou.mybatisplus.toolkit.StringUtils.camelToUnderline(pageInfo.getSort());
     page.setOrderByField(orderField);
     page.setAsc(pageInfo.getOrder().equalsIgnoreCase("asc"));
     List<MIDiagnosis> list = miDiagnosisMapper.findmiDiagnosisMapperDataGrid(page, pageInfo.getCondition());
     pageInfo.setRows(list);
     pageInfo.setTotal(page.getTotal());	
}

@Override
public List<MIDiagnosis> selectBycode(MIDiagnosis diagnosis) {
	// TODO Auto-generated method stub
	MIDiagnosis newdiagnosis = new 	MIDiagnosis();
	newdiagnosis.setDiagnosisCode(diagnosis.getDiagnosisCode());
	newdiagnosis.setAdaptionPackageCode(diagnosis.getAdaptionPackageCode());
	
    EntityWrapper<MIDiagnosis> wrapper = new EntityWrapper<MIDiagnosis>(newdiagnosis);
    if(diagnosis.getId()!=null){
    	 wrapper.where("id != {0}", diagnosis.getId());
	}
    
    return this.selectList(wrapper);

}

@Override
public void editDiagnosis(MIDiagnosis diagnosis, ShiroUser user) {
	// TODO Auto-generated method stub
	diagnosis.setUpdateTime(new Date());
	diagnosis.setUpdateUser(user.getLoginName());
	String optType="添加";
	if(diagnosis.getId()!=null&&StringUtils.isNotBlank(diagnosis.getId())){
		optType="修改";
		addMIDiagnosisHistory(diagnosis.getId(), user.getLoginName(),optType );
		this.updateById(diagnosis);
		
	}else{
		diagnosis.setCreateTime(new Date());
		diagnosis.setCreateUser(user.getLoginName());;
        this.insert(diagnosis);
        MIDiagnosisOpt miDiagnosisOpt=BeanUtils.copy(diagnosis,MIDiagnosisOpt.class);
        miDiagnosisOpt.setOptType("添加");
        miDiagnosisOptMapper.insert(miDiagnosisOpt);
	}
	
}

@Override
public void deleteMIDiagnosis(List<String> ids, ShiroUser user) {
	 
	 for (int i = 0; i < ids.size(); i++) {
		 addMIDiagnosisHistory(ids.get(i), user.getLoginName(),"删除" );
	}
	 this.deleteBatchIds(ids);
}
private void addMIDiagnosisHistory(String id,String loginName,String optType)
{
	
	Map<String, Object> params = new HashMap<String, Object>();

	params.put("id", id);
	params.put("optType", optType);
	params.put("loginName", loginName);
	miDiagnosisMapper.addMIDiagnosisHistory(params);
}
@Override
public void importData(List<Map<String, String>> exportList, ShiroUser user) {
	// TODO Auto-generated method stub
	Map<String, Object> map = new HashMap<String, Object>();
	if(exportList.size()==0)
	{
		return;
	}
	else if(exportList.size()<101)
	{			
		map.put("list", exportList);
		map.put("loginName", user.getLoginName());
		miDiagnosisMapper.daoruData(map);
	}
	else
	{
		int count= exportList.size()/100==0?exportList.size()/100:exportList.size()/100+1;
		int startIndex=0;
		int endIndex=100;
		for(int i=1;i<=count;i++)
		{
			map.put("list", exportList.subList(startIndex, endIndex));
			map.put("loginName", user.getLoginName());
			miDiagnosisMapper.daoruData(map);
			startIndex=endIndex;
			endIndex=100*(i+1);
			if(endIndex>exportList.size())
			{
				endIndex=exportList.size();
			}
		}
	}
}

@Override
public List<Map<String, Object>> selectDetailMIDiagnosis() {
	// TODO Auto-generated method stub
		List<Map<String, Object>> detailList = miDiagnosisMapper.selectDetailMIDiagnosis();
		return detailList;
}

@Override
public List<Map<String, Object>> selectMIDiagnosisHistory() {
	// TODO Auto-generated method stub
	 List<Map<String, Object>> historyList = miDiagnosisMapper.selectMIDiagnosisHistory();
		return historyList;
}
	
	
	
	
	
	
	
	
	
}

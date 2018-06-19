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
import com.shuxin.mapper.MIStandardMapMapper;
import com.shuxin.mapper.MIStandardMapOptMapper;
import com.shuxin.model.DiagnosticCatalog;
import com.shuxin.model.MIDiagnosisOpt;
import com.shuxin.model.MIStandardMap;
import com.shuxin.model.MIStandardMapOpt;
import com.shuxin.service.IMIStandardMapService;
@Service
public class MIStandardMapServiceImpl extends ServiceImpl<MIStandardMapMapper, MIStandardMap> implements IMIStandardMapService{
 @Autowired
 private MIStandardMapMapper miStandardMapMapper;
 @Autowired
 private MIStandardMapOptMapper miStandardMapOptMapper;
@Override
public void findmiStandardDataGrid(PageInfo pageInfo) {
	// TODO Auto-generated method stub
	 Page<MIStandardMap> page = new Page<MIStandardMap>(pageInfo.getNowpage(), pageInfo.getSize());
     String orderField = com.baomidou.mybatisplus.toolkit.StringUtils.camelToUnderline(pageInfo.getSort());
     page.setOrderByField(orderField);
     page.setAsc(pageInfo.getOrder().equalsIgnoreCase("asc"));
     List<MIStandardMap> list = miStandardMapMapper.findmiStandardDataGrid(page, pageInfo.getCondition());
     pageInfo.setRows(list);
     pageInfo.setTotal(page.getTotal());	
}

@Override
public List<MIStandardMap> selectBycode(MIStandardMap standardMap) {
	// TODO Auto-generated method stub
	MIStandardMap newstandardMap = new MIStandardMap();
	newstandardMap.setProjectCode(standardMap.getProjectCode());
	
    EntityWrapper<MIStandardMap> wrapper = new EntityWrapper<MIStandardMap>(newstandardMap);
    if(standardMap.getId()!=null){
    	 wrapper.where("id != {0}", standardMap.getId());
	}
    
    return this.selectList(wrapper);
}

@Override
public void editDiagnosis(MIStandardMap standardMap, ShiroUser user) {
	// TODO Auto-generated method stub
	standardMap.setUpdateTime(new Date());
	standardMap.setUpdateUser(user.getLoginName());
	String optType="添加";
	if(standardMap.getId()!=null&&StringUtils.isNotBlank(standardMap.getId())){
		optType="修改";
		addMIStandardMapHistory(standardMap.getId(), user.getLoginName(),optType );
		this.updateById(standardMap);
		
	}else{
		standardMap.setCreateTime(new Date());
		standardMap.setCreateUser(user.getLoginName());;
        this.insert(standardMap);
        MIStandardMapOpt miStandardMapOpt=BeanUtils.copy(standardMap,MIStandardMapOpt.class);
        miStandardMapOpt.setOptType("添加");
        miStandardMapOptMapper.insert(miStandardMapOpt);
	}
	
}

@Override
public void deleteMiStandardMap(List<String> ids, ShiroUser user) {
	 for (int i = 0; i < ids.size(); i++) {
		 addMIStandardMapHistory(ids.get(i), user.getLoginName(),"删除" );
	}
	 this.deleteBatchIds(ids);
}
private void addMIStandardMapHistory(String id,String loginName,String optType)
{
	
	Map<String, Object> params = new HashMap<String, Object>();

	params.put("id", id);
	params.put("optType", optType);
	params.put("loginName", loginName);
	miStandardMapMapper.addMIStandardMapHistory(params);
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
		miStandardMapMapper.daoruData(map);
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
			miStandardMapMapper.daoruData(map);
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
public List<Map<String, Object>> selectDetailMIStandardMap() {
	// TODO Auto-generated method stub
	List<Map<String, Object>> detailList = miStandardMapMapper.selectDetailMIStandardMap();
	return detailList;
}

@Override
public List<Map<String, Object>> selectMIStandardMapHistory() {
	// TODO Auto-generated method stub
	 List<Map<String, Object>> historyList = miStandardMapMapper.selectMIStandardMapHistory();
		return historyList;
}
	
	
	
	
	
}

package com.shuxin.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.shuxin.commons.shiro.ShiroUser;
import com.shuxin.commons.utils.BeanUtils;
import com.shuxin.commons.utils.PageInfo;
import com.shuxin.commons.utils.StringUtils;
import com.shuxin.mapper.TumourDiagnosisMapper;
import com.shuxin.mapper.TumourDiagnosisOptMapper;
import com.shuxin.model.TreatSubjectTransOpt;
import com.shuxin.model.TumourDiagnosis;
import com.shuxin.model.TumourDiagnosisOpt;
import com.shuxin.service.ITumourDiagnosisService;

@Service
public class TumourDiagnosisServiceImpl extends ServiceImpl<TumourDiagnosisMapper, TumourDiagnosis> implements  ITumourDiagnosisService{
	
  @Autowired
  private  TumourDiagnosisMapper tumourDiagnosisMapper	;
  @Autowired
  private  TumourDiagnosisOptMapper tumourDiagnosisOptMapper;

@Override
public void findTumourDiagnosisDataGrid(PageInfo pageInfo) {
	// TODO Auto-generated method stub
	 Page<TumourDiagnosis> page = new Page<TumourDiagnosis>(pageInfo.getNowpage(), pageInfo.getSize());
     String orderField = com.baomidou.mybatisplus.toolkit.StringUtils.camelToUnderline(pageInfo.getSort());
     page.setOrderByField(orderField);
     page.setAsc(pageInfo.getOrder().equalsIgnoreCase("asc"));
     List<TumourDiagnosis> list = tumourDiagnosisMapper.findTumourDiagnosisDataGrid(page, pageInfo.getCondition());
     pageInfo.setRows(list);
     pageInfo.setTotal(page.getTotal());	
}

@Override
public List<TumourDiagnosis> selectBycode(TumourDiagnosis tumourDiagnosis) {
	TumourDiagnosis newtumourDiagnosis = new TumourDiagnosis();
	newtumourDiagnosis.setDiagnosisCode(tumourDiagnosis.getDiagnosisCode());
	
    EntityWrapper<TumourDiagnosis> wrapper = new EntityWrapper<TumourDiagnosis>(newtumourDiagnosis);
    if(tumourDiagnosis.getId()!=null){
    	 wrapper.where("id != {0}", tumourDiagnosis.getId());
	}
    
    return this.selectList(wrapper);
}

@Override
public void editDiagnosis(TumourDiagnosis tumourDiagnosis, ShiroUser user) {
	// TODO Auto-generated method stub
	tumourDiagnosis.setUpdateTime(new Date());
	tumourDiagnosis.setUpdateUser(user.getLoginName());
	String optType="添加";
	if(tumourDiagnosis.getId()!=null&&StringUtils.isNotBlank(tumourDiagnosis.getId())){
		optType="修改";
		addTumourDiagnosisHistory(tumourDiagnosis.getId(), user.getLoginName(),optType );

		this.updateById(tumourDiagnosis);
		
	}else{
		tumourDiagnosis.setCreateTime(new Date());
		tumourDiagnosis.setCreateUser(user.getLoginName());;
        this.insert(tumourDiagnosis);
        TumourDiagnosisOpt tumourdiagnosisopt=BeanUtils.copy(tumourDiagnosis,TumourDiagnosisOpt.class);
        tumourdiagnosisopt.setOptType("添加");
        tumourDiagnosisOptMapper.insert(tumourdiagnosisopt);
        
	}
	}

@Override
public void deleteTreatsubjectTrans(List<String> ids, ShiroUser user) {
	 for (int i = 0; i < ids.size(); i++) {
		 addTumourDiagnosisHistory(ids.get(i), user.getLoginName(),"删除" );
	}
	this.deleteBatchIds(ids);
}
private void addTumourDiagnosisHistory(String id,String loginName,String optType)
{
	
	Map<String, Object> params = new HashMap<String, Object>();

	params.put("id", id);
	params.put("optType", optType);
	params.put("loginName", loginName);
	tumourDiagnosisMapper.addTumourDiagnosisHistory(params);
}
@Override
public void importData(List<Map<String, String>> exportList, ShiroUser user) {
	Map<String, Object> map = new HashMap<String, Object>();
	if(exportList.size()==0)
	{
		return;
	}
	else if(exportList.size()<101)
	{			
		map.put("list", exportList);
		map.put("loginName", user.getLoginName());
		tumourDiagnosisMapper.daoruData(map);
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
			tumourDiagnosisMapper.daoruData(map);
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
public List<Map<String, Object>> selectDetailTumourDiagnosis() {
	 List<Map<String, Object>> detailList = tumourDiagnosisMapper.selectDetailTumourDiagnosis();
		
		
		return detailList;
}

@Override
public List<Map<String, Object>> selectTumourDiagnosisHistory() {
	// TODO Auto-generated method stub
	 List<Map<String, Object>> detailList = tumourDiagnosisMapper.selecTumourDiagnosisHistory();
		
		
		return detailList;
}
	
	
	
	
	
	
	
	
	
	
}

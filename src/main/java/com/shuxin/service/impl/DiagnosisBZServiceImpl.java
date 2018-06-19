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
import com.shuxin.mapper.DiagnosisBZMapper;
import com.shuxin.mapper.DiagnosticmaBZMppingOptMapper;
import com.shuxin.model.DiagnosticmaBZMpping;
import com.shuxin.model.DiagnosticmaBZMppingOpt;
import com.shuxin.service.IDiagnosisBZService;

@Service
public class DiagnosisBZServiceImpl extends ServiceImpl<DiagnosisBZMapper, DiagnosticmaBZMpping> implements IDiagnosisBZService{
   @Autowired
   private DiagnosisBZMapper bzMapper;
	@Autowired
	private  DiagnosticmaBZMppingOptMapper diagnosticmaBZMppingOptMapper;
	@Override
	public void findbzDiagnosisDataGrid(PageInfo pageInfo) {
		// TODO Auto-generated method stub
		 Page<DiagnosticmaBZMpping> page = new Page<DiagnosticmaBZMpping>(pageInfo.getNowpage(), pageInfo.getSize());
	        String orderField = com.baomidou.mybatisplus.toolkit.StringUtils.camelToUnderline(pageInfo.getSort());
	        page.setOrderByField(orderField);
	        page.setAsc(pageInfo.getOrder().equalsIgnoreCase("asc"));
	        List<DiagnosticmaBZMpping> list = bzMapper.findbzDiagnosisDataGrid(page, pageInfo.getCondition());
	        pageInfo.setRows(list);
	        pageInfo.setTotal(page.getTotal());	
		
	}

	@Override
	public List<DiagnosticmaBZMpping> selectBycode(DiagnosticmaBZMpping diagnosisBZ) {
		// TODO Auto-generated method stub
		DiagnosticmaBZMpping newdiagnosisBZ = new DiagnosticmaBZMpping();
		newdiagnosisBZ.setDiagnosisCode(diagnosisBZ.getDiagnosisCode());
		
        EntityWrapper<DiagnosticmaBZMpping> wrapper = new EntityWrapper<DiagnosticmaBZMpping>(newdiagnosisBZ);
        if(diagnosisBZ.getId()!=null){
        	 wrapper.where("id != {0}", diagnosisBZ.getId());
		}
        
        return this.selectList(wrapper);
	}

	@Override
	public void editDiagnosis(DiagnosticmaBZMpping diagnosisBZ,ShiroUser user) {
		// TODO Auto-generated method stub
		diagnosisBZ.setUpdateTime(new Date());
		diagnosisBZ.setUpdateUser(user.getLoginName());
		String optType="添加";
		if(diagnosisBZ.getId()!=null&&StringUtils.isNotBlank(diagnosisBZ.getId())){
			optType="修改";
			addDiagnosisBZHistory(diagnosisBZ.getId(), user.getLoginName(),optType );
			this.updateById(diagnosisBZ);
			
			
		}else{
			diagnosisBZ.setCreateTime(new Date());
			diagnosisBZ.setCreateUser(user.getLoginName());;
	        this.insert(diagnosisBZ);
	        DiagnosticmaBZMppingOpt diagnosticamBZMppingopt =BeanUtils.copy(diagnosisBZ,DiagnosticmaBZMppingOpt.class);
	        diagnosticamBZMppingopt.setOptType("添加");
	        diagnosticmaBZMppingOptMapper.insert(diagnosticamBZMppingopt);
		}
		
	}

	@Override
	public void deleteDiagnosisBZ(List<String> ids,ShiroUser user) {
		 
		 for (int i = 0; i < ids.size(); i++) {
			 addDiagnosisBZHistory(ids.get(i), user.getLoginName(),"删除" );
		}
		 
		 this.deleteBatchIds(ids);
		
	}

	
	
	private void addDiagnosisBZHistory(String id,String loginName,String optType)
	{
		
		Map<String, Object> params = new HashMap<String, Object>();
	
		params.put("id", id);
		params.put("optType", optType);
		params.put("loginName", loginName);
		bzMapper.addDiagnosisBZHistory(params);
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
			bzMapper.daoruData(map);
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
				bzMapper.daoruData(map);
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
	public List<Map<String, Object>> selectDetailDiagnosisBZ() {
		
		 List<Map<String, Object>> detailList = bzMapper.selectDetailDiagnosisBZ();
		
		
		return detailList;
	}

	@Override
	public List<Map<String, Object>> selectDiagnosisBZHistory() {
		 List<Map<String, Object>> detailList = bzMapper.selectDiagnosisBZHistory();						
			return detailList;
	}

	
	
	
	
	
	

	
	
}

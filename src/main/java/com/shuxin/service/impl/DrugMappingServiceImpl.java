package com.shuxin.service.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.shuxin.commons.shiro.ShiroUser;
import com.shuxin.commons.utils.BeanUtils;
import com.shuxin.commons.utils.PageInfo;
import com.shuxin.commons.utils.StringUtils;
import com.shuxin.mapper.DrugMappingMapper;
import com.shuxin.mapper.DrugMappingOptMapper;
import com.shuxin.model.DrugCatalogOpt;
import com.shuxin.model.DrugMapping;
import com.shuxin.model.DrugMappingOpt;
import com.shuxin.model.vo.DrugMappingVo;
import com.shuxin.service.IDrugMappingService;

@Service
public class DrugMappingServiceImpl extends ServiceImpl<DrugMappingMapper,DrugMapping> implements IDrugMappingService{

	@Autowired
	private DrugMappingMapper drugMappingMapper;
	
	@Autowired
	private DrugMappingOptMapper drugCatalogOptMapper;

	@Override
	public void selectDrugMappingVoPage(PageInfo pageInfo) {
		Page page = new Page(pageInfo.getNowpage(), pageInfo.getSize());       
        List<Map<String,Object>> list = drugMappingMapper.selectDrugMappingVoPage(page, pageInfo.getCondition());
        pageInfo.setRows(list);
        pageInfo.setTotal(page.getTotal());		
	}

	@Override
	public int selectExistDrugMapping(DrugMappingVo drugMappingVo) {
		return drugMappingMapper.selectExistDrugMapping(drugMappingVo);
	}

	@Override
	public void editDrugMapping(DrugMappingVo drugMappingVo, ShiroUser user) {
		DrugMapping drugMapping = BeanUtils.copy(drugMappingVo,DrugMapping.class);
		Date date = new Date();
		drugMapping.setUpdateTime(date);
		drugMapping.setUpdateUser(user.getLoginName());
		
		if(StringUtils.isNotBlank(drugMapping.getId()))
		{
			List<String> list = new ArrayList<String>();
			list.add(drugMapping.getId());
			addDrugMappingHistory(list,user.getLoginName(),"修改");
			this.updateById(drugMapping);
		}
		else
		{
			drugMapping.setCreateTime(date);
			drugMapping.setCreateUser(user.getLoginName());
			this.insert(drugMapping);
			DrugMappingOpt drugMappingOpt=BeanUtils.copy(drugMapping,DrugMappingOpt.class);
			drugMappingOpt.setOptType("添加");
			drugCatalogOptMapper.insert(drugMappingOpt);
		}
	}	
	
	private void addDrugMappingHistory(List<String> list,String loginName,String optType)
	{
		Map<String, Object> map = new HashMap<String, Object>();		
		map.put("list", list);
		map.put("loginName", loginName);
		map.put("optType", optType);
		drugMappingMapper.addDrugMappingHistory(map);
	}

	@Override
	public DrugMappingVo selectDrugMappingVo(String id) {
		DrugMapping drugMapping = this.selectById(id);
		DrugMappingVo drugMappingVo = BeanUtils.copy(drugMapping,DrugMappingVo.class);
		return drugMappingVo;
	}

	@Override
	public void deleteDrugMapping(List<String> list, ShiroUser user) {
		addDrugMappingHistory(list,user.getLoginName(),"删除");
		this.deleteBatchIds(list);
		
	}

	@Override
	public List<Map<String, Object>> selectDrugMappingVoPage() {
		// TODO Auto-generated method stub
		return drugMappingMapper.selectDrugMappingVoPage();
	}

	@Override
	public List<Map<String, Object>> searchDrugMappingHistroy() {
		// TODO Auto-generated method stub
		return drugMappingMapper.searchDrugMappingHistroy();
	}

	@Override
	public void importDrugMapping(List<Map<String, String>> list, String loginName) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(list.size()==0)
		{
			return;
		}
		else if(list.size()<101)
		{			
			map.put("list", list);
			map.put("loginName", loginName);
			drugMappingMapper.importDrugMapping(map);
		}
		else
		{
			int count= list.size()/100==0?list.size()/100:list.size()/100+1;
			int startIndex=0;
			int endIndex=100;
			for(int i=1;i<=count;i++)
			{
				map.put("list", list.subList(startIndex, endIndex));
				map.put("loginName", loginName);
				drugMappingMapper.importDrugMapping(map);
				startIndex=endIndex;
				endIndex=100*(i+1);
				if(endIndex>list.size())
				{
					endIndex=list.size();
				}
			}
		}
		
	}
	

}

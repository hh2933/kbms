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
import com.shuxin.mapper.MinDrugTypeMapper;
import com.shuxin.mapper.MinDrugTypeOptMapper;
import com.shuxin.model.DrugMappingOpt;
import com.shuxin.model.MinDrugType;
import com.shuxin.model.MinDrugTypeOpt;
import com.shuxin.model.vo.DrugMappingVo;
import com.shuxin.model.vo.MinDrugTypeVo;
import com.shuxin.service.IMinDrugTypeService;

@Service
public class MinDrugTypeServiceImpl extends ServiceImpl<MinDrugTypeMapper,MinDrugType> implements IMinDrugTypeService{

	@Autowired
	private MinDrugTypeMapper minDrugTypeMapper;
	
	@Autowired
	private MinDrugTypeOptMapper minDrugTypeOptMapper;

	@Override
	public void selectMinDrugTypeVoPage(PageInfo pageInfo) {
		Page page = new Page(pageInfo.getNowpage(), pageInfo.getSize());       
        List<Map<String,Object>> list = minDrugTypeMapper.selectMinDrugTypeVoPage(page, pageInfo.getCondition());
        pageInfo.setRows(list);
        pageInfo.setTotal(page.getTotal());		
	}

	@Override
	public void editMinDrugType(MinDrugTypeVo minDrugTypeVo, ShiroUser user) {
		MinDrugType minDrugType = BeanUtils.copy(minDrugTypeVo,MinDrugType.class);
		Date date = new Date();
		minDrugType.setUpdateTime(date);
		minDrugType.setUpdateUser(user.getLoginName());
		
		if(StringUtils.isNotBlank(minDrugType.getId()))
		{
			List<String> list = new ArrayList<String>();
			list.add(minDrugType.getId());
			addMinDrugTypeHistory(list,user.getLoginName(),"修改");
			this.updateById(minDrugType);
		}
		else 
		{
			minDrugType.setCreateTime(date);
			minDrugType.setCreateUser(user.getLoginName());
			this.insert(minDrugType);
			MinDrugTypeOpt minDrugTypeOpt=BeanUtils.copy(minDrugType,MinDrugTypeOpt.class);
			minDrugTypeOpt.setOptType("添加");
			minDrugTypeOptMapper.insert(minDrugTypeOpt);
		}
		
	}	
	
	private void addMinDrugTypeHistory(List<String> list,String loginName,String optType)
	{
		Map<String, Object> map = new HashMap<String, Object>();		
		map.put("list", list);
		map.put("loginName", loginName);
		map.put("optType", optType);
		minDrugTypeMapper.addMinDrugTypeHistory(map);
	}

	@Override
	public int selectExistMinDrugType(MinDrugTypeVo minDrugTypeVo) {
		
		return minDrugTypeMapper.selectExistMinDrugType(minDrugTypeVo);
	}

	@Override
	public MinDrugTypeVo selectMinDrugTypeVo(String id) {
		MinDrugType minDrugType = this.selectById(id);
		MinDrugTypeVo minDrugTypeVo = BeanUtils.copy(minDrugType,MinDrugTypeVo.class);
		return minDrugTypeVo;
	}

	@Override
	public void deleteMinDrugType(List<String> list, ShiroUser user) {
		addMinDrugTypeHistory(list,user.getLoginName(),"删除");
		this.deleteBatchIds(list);
		
	}

	@Override
	public List<Map<String, Object>> searchMinDrugTypeHistroy() {
		return minDrugTypeMapper.searchMinDrugTypeHistroy();
	}

	@Override
	public List<Map<String, Object>> selectMinDrugTypeVoPage() {
		return minDrugTypeMapper.selectMinDrugTypeVoPage();
	}

	@Override
	public void importMinDrugType(List<Map<String, String>> list, String loginName) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(list.size()==0)
		{
			return;
		}
		else if(list.size()<101)
		{			
			map.put("list", list);
			map.put("loginName", loginName);
			minDrugTypeMapper.importMinDrugType(map);
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
				minDrugTypeMapper.importMinDrugType(map);
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

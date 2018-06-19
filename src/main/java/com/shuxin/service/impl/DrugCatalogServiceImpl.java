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
import com.shuxin.mapper.DrugCatalogMapper;
import com.shuxin.mapper.DrugCatalogOptMapper;
import com.shuxin.model.DrugCatalog;
import com.shuxin.model.DrugCatalogOpt;
import com.shuxin.model.vo.DrugCatalogVo;
import com.shuxin.service.IDrugCatalogService;

@Service
public class DrugCatalogServiceImpl extends ServiceImpl<DrugCatalogMapper,DrugCatalog> implements IDrugCatalogService{

	@Autowired
	private DrugCatalogMapper drugCatalogMapper;
	
	@Autowired
	private DrugCatalogOptMapper drugCatalogOptMapper;

	@Override
	public void selectDrugCatalogVoPage(PageInfo pageInfo) {
		Page page = new Page(pageInfo.getNowpage(), pageInfo.getSize());       
        List<Map<String,Object>> list = drugCatalogMapper.selectDrugCatalogVoPage(page, pageInfo.getCondition());
        pageInfo.setRows(list);
        pageInfo.setTotal(page.getTotal());
		
	}

	@Override
	public int selectExistDrugCatalog(DrugCatalogVo drugCatalogVo) {
		
		return drugCatalogMapper.selectExistDrugCatalog(drugCatalogVo);
	}

	@Override
	public void editDrugCatalog(DrugCatalogVo drugCatalogVo, ShiroUser user) {
		DrugCatalog drugCatalog =BeanUtils.copy(drugCatalogVo,DrugCatalog.class);
		Date date = new Date();
		drugCatalog.setUpdateTime(date);
		drugCatalog.setUpdateUser(user.getLoginName());
		if(StringUtils.isNotBlank(drugCatalog.getId()))
		{
			List<String> list = new ArrayList<String>();
			list.add(drugCatalog.getId());
			addDrugCatalogHistory(list,user.getLoginName(),"修改");
			this.updateById(drugCatalog);
		}
		else
		{
			drugCatalog.setCreateTime(date);
			drugCatalog.setCreateUser(user.getLoginName());
			this.insert(drugCatalog);
			DrugCatalogOpt drugCatalogOpt=BeanUtils.copy(drugCatalog,DrugCatalogOpt.class);
			drugCatalogOpt.setOptType("添加");
			drugCatalogOptMapper.insert(drugCatalogOpt);
		}
	}
	
	private void addDrugCatalogHistory(List<String> list,String loginName,String optType)
	{
		Map<String, Object> map = new HashMap<String, Object>();		
		map.put("list", list);
		map.put("loginName", loginName);
		map.put("optType", optType);
		drugCatalogMapper.addDrugCatalogHistory(map);
	}

	@Override
	public DrugCatalogVo selectDrugCatalogVo(String id) {
		DrugCatalog drugCatalog =this.selectById(id);
		DrugCatalogVo drugCatalogVo = BeanUtils.copy(drugCatalog,DrugCatalogVo.class); 
		return drugCatalogVo;
	}

	@Override
	public void deleteDrugCatalog(List<String> list, ShiroUser user) {
		addDrugCatalogHistory(list,user.getLoginName(),"删除");
		this.deleteBatchIds(list);
	}

	@Override
	public List<Map<String, Object>> selectDrugCatalogVoPage() {
		return drugCatalogMapper.selectDrugCatalogVoPage();
	}

	@Override
	public List<Map<String, Object>> searchDrugCatalogHistroy() {
		return drugCatalogMapper.searchDrugCatalogHistroy();
	}

	@Override
	public void importDrugCatalog(List<Map<String, String>> list, String loginName) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(list.size()==0)
		{
			return;
		}
		else if(list.size()<101)
		{			
			map.put("list", list);
			map.put("loginName", loginName);
			drugCatalogMapper.importDrugCatalog(map);
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
				drugCatalogMapper.importDrugCatalog(map);
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

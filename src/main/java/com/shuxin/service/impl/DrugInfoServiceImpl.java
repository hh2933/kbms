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
import com.shuxin.mapper.DrugInfoMapper;
import com.shuxin.mapper.DrugInfoOptMapper;
import com.shuxin.model.DiagnosticCatalog;
import com.shuxin.model.DrugInfo;
import com.shuxin.model.DrugInfoOpt;
import com.shuxin.model.vo.DrugInfoVo;
import com.shuxin.model.vo.StepDrugVo;
import com.shuxin.service.IDrugInfoService;
@Service
public class DrugInfoServiceImpl extends ServiceImpl<DrugInfoMapper,DrugInfo> implements IDrugInfoService{

	@Autowired
	private DrugInfoMapper drugInfoMapper;
	
	@Autowired
	private DrugInfoOptMapper drugInfoOptMapper;
	
	@Override
	public void selectDrugInfoVoPage(PageInfo pageInfo) {
		Page<DrugInfo> page = new Page<DrugInfo>(pageInfo.getNowpage(), pageInfo.getSize());
        List<Map<String,Object>> list = drugInfoMapper.selectDrugInfoVoPage(page, pageInfo.getCondition());
        pageInfo.setRows(list);
        pageInfo.setTotal(page.getTotal());	
		
	}

	@Override
	public int selectExistDrugInfo(DrugInfoVo drugInfoVo) {
		return drugInfoMapper.selectExistDrugInfo(drugInfoVo);
	}

	@Override
	public void editDrugInfo(DrugInfoVo drugInfoVo, ShiroUser user) {
		DrugInfo drugInfo=BeanUtils.copy(drugInfoVo,DrugInfo.class);
		Date date = new Date();
		drugInfo.setUpdateTime(date);
		drugInfo.setUpdateUser(user.getLoginName());
		if(StringUtils.isNotBlank(drugInfo.getId()))
		{
			List<String> list = new ArrayList<String>();
			list.add(drugInfo.getId());
			addStepDrugHistory(list,user.getLoginName(),"修改");
			this.updateById(drugInfo);
		}
		else
		{
			drugInfo.setCreateTime(date);
			drugInfo.setCreateUser(user.getLoginName());
			this.insert(drugInfo);
			DrugInfoOpt drugInfoOpt=BeanUtils.copy(drugInfo,DrugInfoOpt.class);
			drugInfoOpt.setOptType("添加");
			drugInfoOptMapper.insert(drugInfoOpt);
		}
	}
	
	private void addStepDrugHistory(List<String> list,String loginName,String optType)
	{
		Map<String, Object> map = new HashMap<String, Object>();		
		map.put("list", list);
		map.put("loginName", loginName);
		map.put("optType", optType);
		drugInfoMapper.addDrugInfoHistory(map);
	}

	@Override
	public DrugInfoVo selectDrugInfoVo(String id) {
		DrugInfo drugInfo=this.selectById(id);
		DrugInfoVo drugInfoVo=BeanUtils.copy(drugInfo,DrugInfoVo.class);
		return drugInfoVo;
	}

	@Override
	public void deleteDrugInfo(List<String> list, ShiroUser user) {
		addStepDrugHistory(list,user.getLoginName(),"删除");
		this.deleteBatchIds(list);
	}

	@Override
	public List<Map<String, Object>> selectDrugInfoVoPage(DrugInfoVo drugInfoVo) {
		return drugInfoMapper.selectDrugInfoVoPage(drugInfoVo);
	}

	@Override
	public List<Map<String, Object>> selectDrugInfoHistory(DrugInfoVo drugInfoVo) {
		return drugInfoMapper.selectDrugInfoHistory(drugInfoVo);
	}

	@Override
	public void importDrugInfo(List<Map<String, String>> list,String loginName) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		if(list.size()==0)
		{
			return;
		}
		else if(list.size()<101)
		{			
			map.put("list", list);
			map.put("loginName", loginName);
			drugInfoMapper.importDrugInfo(map);
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
				drugInfoMapper.importDrugInfo(map);
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

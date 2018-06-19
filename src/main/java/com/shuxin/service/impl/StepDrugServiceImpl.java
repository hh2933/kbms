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
import com.shuxin.mapper.StepDrugMapper;
import com.shuxin.mapper.StepDrugOptMapper;
import com.shuxin.model.DiagnosticCatalog;
import com.shuxin.model.StepDrug;
import com.shuxin.model.StepDrugOpt;
import com.shuxin.model.vo.StepDrugVo;
import com.shuxin.service.IStepDrugService;
@Service
public class StepDrugServiceImpl extends ServiceImpl<StepDrugMapper, StepDrug> implements IStepDrugService {

	@Autowired
	private StepDrugMapper stepDrugMapper;
	
	@Autowired
	private StepDrugOptMapper stepDrugOptMapper;
	
	@Override
	public void selectStepDrugVoPage(PageInfo pageInfo) {
		Page<DiagnosticCatalog> page = new Page<DiagnosticCatalog>(pageInfo.getNowpage(), pageInfo.getSize());
        List<Map<String,Object>> list = stepDrugMapper.selectStepDrugVoPage(page, pageInfo.getCondition());
        pageInfo.setRows(list);
        pageInfo.setTotal(page.getTotal());	
		
	}

	@Override
	public int selectExistStepDrug(StepDrugVo stepDrugVo) {
		return stepDrugMapper.selectExistStepDrug(stepDrugVo);
	}

	@Override
	public void editStepDrug(StepDrugVo stepDrugVo, ShiroUser user) {
		StepDrug stepDrug = BeanUtils.copy(stepDrugVo,StepDrug.class);
		Date date = new Date();
		stepDrug.setUpdateTime(date);
		stepDrug.setUpdateUser(user.getLoginName());
		if(StringUtils.isNotBlank(stepDrug.getId()))
		{
			List<String> list = new ArrayList<String>();
			list.add(stepDrug.getId());
			addStepDrugHistory(list,user.getLoginName(),"修改");
			this.updateById(stepDrug);
		}
		else
		{
			stepDrug.setCreateTime(date);
			stepDrug.setCreateUser(user.getLoginName());
			this.insert(stepDrug);
			StepDrugOpt stepDrugOpt=BeanUtils.copy(stepDrug,StepDrugOpt.class);
			stepDrugOpt.setOptType("添加");
			stepDrugOptMapper.insert(stepDrugOpt);
		}
	}

	private void addStepDrugHistory(List<String> list,String loginName,String optType)
	{
		Map<String, Object> map = new HashMap<String, Object>();		
		map.put("list", list);
		map.put("loginName", loginName);
		map.put("optType", optType);
		stepDrugMapper.addStepDrugHistory(map);
	}

	@Override
	public StepDrugVo selectStepDrugVo(String id) {
		StepDrug stepDrug=this.selectById(id);
		StepDrugVo stepDrugVo = BeanUtils.copy(stepDrug,StepDrugVo.class);
		return stepDrugVo;
	}

	@Override
	public void deleteStepDrug(List<String> list, ShiroUser user) {
		addStepDrugHistory(list,user.getLoginName(),"删除");
		this.deleteBatchIds(list);
	}

	@Override
	public List<Map<String, Object>> selectStepDrugVoPage() {
		return stepDrugMapper.selectStepDrugVoPage();
	}

	@Override
	public List<Map<String, Object>> selectStepDrugHistory() {
		return stepDrugMapper.selectStepDrugHistory();
	}

	@Override
	public void importStepDrug(List<Map<String, String>> list, String loginName) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(list.size()==0)
		{
			return;
		}
		else if(list.size()<101)
		{			
			map.put("list", list);
			map.put("loginName", loginName);
			stepDrugMapper.importStepDrug(map);
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
				stepDrugMapper.importStepDrug(map);
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

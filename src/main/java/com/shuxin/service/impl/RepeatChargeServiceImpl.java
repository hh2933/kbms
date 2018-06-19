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
import com.shuxin.mapper.RepeatChargeMapper;
import com.shuxin.mapper.RepeatChargeOptMapper;
import com.shuxin.model.ProjectsMapping;
import com.shuxin.model.RepeatCharge;
import com.shuxin.model.RepeatChargeOpt;
import com.shuxin.model.vo.RepeatChargeVo;
import com.shuxin.service.IRepeatChargeService;
@Service
public class RepeatChargeServiceImpl extends ServiceImpl<RepeatChargeMapper,RepeatCharge> implements IRepeatChargeService
{
	@Autowired
	private RepeatChargeMapper repeatChargeMapper;
	
	@Autowired
	private RepeatChargeOptMapper repeatChargeOptMapper;
	
	@Override
	public void selectRepeatChargeVoPage(PageInfo pageInfo) {
		Page<RepeatCharge> page = new Page<RepeatCharge>(pageInfo.getNowpage(), pageInfo.getSize());
        List<Map<String,Object>> list = repeatChargeMapper.selectRepeatChargeVoPage(page, pageInfo.getCondition());
        pageInfo.setRows(list);
        pageInfo.setTotal(page.getTotal());	
		
	}

	@Override
	public int selectExistRepeatCharge(RepeatChargeVo repeatChargeVo) {
		return repeatChargeMapper.selectExistRepeatCharge(repeatChargeVo);
	}

	@Override
	public void editRepeatCharge(RepeatChargeVo repeatChargeVo, ShiroUser user) {
		RepeatCharge repeatCharge=BeanUtils.copy(repeatChargeVo,RepeatCharge.class);
		Date date = new Date();
		repeatCharge.setUpdateTime(date);
		repeatCharge.setUpdateUser(user.getLoginName());
		if(StringUtils.isNotBlank(repeatCharge.getId()))
		{
			List<String> list = new ArrayList<String>();
			list.add(repeatCharge.getId());
			addRepeatChargeHistroy(list,user.getLoginName(),"修改");
			this.updateById(repeatCharge);
		}
		else
		{
			repeatCharge.setCreateTime(date);
			repeatCharge.setCreateUser(user.getLoginName());
			this.insert(repeatCharge);
			RepeatChargeOpt repeatChargeOpt=BeanUtils.copy(repeatCharge,RepeatChargeOpt.class);
			repeatChargeOpt.setOptType("添加");
			repeatChargeOptMapper.insert(repeatChargeOpt);
		}
	}
	
	private void addRepeatChargeHistroy(List<String> list,String loginName,String optType)
	{
		Map<String, Object> map = new HashMap<String, Object>();		
		map.put("list", list);
		map.put("loginName", loginName);
		map.put("optType", optType);
		repeatChargeMapper.addRepeatChargeHistory(map);
	}

	@Override
	public RepeatChargeVo selectEditRepeatCharge(String id) {
		RepeatCharge repeatCharge= this.selectById(id);
		RepeatChargeVo repeatChargeVo=BeanUtils.copy(repeatCharge,RepeatChargeVo.class);
		return repeatChargeVo;
	}

	@Override
	public void deleteRepeatCharge(List<String> list, ShiroUser user) {
		addRepeatChargeHistroy(list,user.getLoginName(),"删除");
		this.deleteBatchIds(list);
		
	}

	@Override
	public List<Map<String, Object>> selectRepeatChargeVoPage() {
		return repeatChargeMapper.selectRepeatChargeVoPage();
	}

	@Override
	public List<Map<String, Object>> selectRepeatChargeHistory() {
		return repeatChargeMapper.selectRepeatChargeHistory();
	}

	@Override
	public void importRepeatCharge(List<Map<String, String>> list, String loginName) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(list.size()==0)
		{
			return;
		}
		else if(list.size()<101)
		{			
			map.put("list", list);
			map.put("loginName", loginName);
			repeatChargeMapper.importRepeatChargeMapping(map);
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
				repeatChargeMapper.importRepeatChargeMapping(map);
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

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
import com.shuxin.mapper.LimitNumberMapper;
import com.shuxin.mapper.LimitNumberOptMapper;
import com.shuxin.model.LimitNumber;
import com.shuxin.model.LimitNumberOpt;
import com.shuxin.model.vo.LimitNumberVo;
import com.shuxin.service.ILimitNumberService;
@Service
public class LimitNumberServiceImpl extends ServiceImpl<LimitNumberMapper,LimitNumber> implements ILimitNumberService {
	@Autowired
	private LimitNumberMapper limitNumberMapper;
	
	@Autowired
	private LimitNumberOptMapper limitNumberOptMapper;
	
	@Override
	public void selectLimitNumberVoPage(PageInfo pageInfo) {
		Page<LimitNumber> page = new Page<LimitNumber>(pageInfo.getNowpage(), pageInfo.getSize());
        List<Map<String,Object>> list = limitNumberMapper.selectLimitNumberVoPage(page, pageInfo.getCondition());
        pageInfo.setRows(list);
        pageInfo.setTotal(page.getTotal());	
		
	}

	@Override
	public int selectExistLimitNumber(LimitNumberVo limitNumberVo) {
		return limitNumberMapper.selectExistLimitNumber(limitNumberVo);
	}

	@Override
	public void editLimitNumber(LimitNumberVo limitNumberVo, ShiroUser user) {
		LimitNumber limitNumber=BeanUtils.copy(limitNumberVo,LimitNumber.class);
		Date date = new Date();
		limitNumber.setUpdateTime(date);
		limitNumber.setUpdateUser(user.getLoginName());
		if(StringUtils.isNotBlank(limitNumber.getId()))
		{
			List<String> list = new ArrayList<String>();
			list.add(limitNumber.getId());
			addLimitNumberHistroy(list,user.getLoginName(),"修改");
			this.updateById(limitNumber);
		}
		else
		{
			limitNumber.setCreateTime(date);
			limitNumber.setCreateUser(user.getLoginName());
			this.insert(limitNumber);
			LimitNumberOpt limitNumberOpt=BeanUtils.copy(limitNumber,LimitNumberOpt.class);
			limitNumberOpt.setOptType("添加");
			limitNumberOptMapper.insert(limitNumberOpt);
		}
	}

	private void addLimitNumberHistroy(List<String> list,String loginName,String optType)
	{
		Map<String, Object> map = new HashMap<String, Object>();		
		map.put("list", list);
		map.put("loginName", loginName);
		map.put("optType", optType);
		limitNumberMapper.addLimitNumberHistory(map);
	}

	@Override
	public LimitNumberVo selectEditLimitNumber(String id) {
		LimitNumber limitNumber=this.selectById(id);
		LimitNumberVo limitNumberVo=BeanUtils.copy(limitNumber,LimitNumberVo.class);
		return limitNumberVo;
	}

	@Override
	public void deleteLimitNumber(List<String> list, ShiroUser user) {
		addLimitNumberHistroy(list,user.getLoginName(),"删除");
		this.deleteBatchIds(list);
	}

	@Override
	public List<Map<String, Object>> selectLimitNumberVoPage() {
		return limitNumberMapper.selectLimitNumberVoPage();
	}

	@Override
	public List<Map<String, Object>> selectLimitNumberHistory() {
		return limitNumberMapper.selectLimitNumberHistory();
	}

	@Override
	public void importLimitNumber(List<Map<String, String>> list, String loginName) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(list.size()==0)
		{
			return;
		}
		else if(list.size()<101)
		{			
			map.put("list", list);
			map.put("loginName", loginName);
			limitNumberMapper.importLimitNumber(map);
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
				limitNumberMapper.importLimitNumber(map);
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

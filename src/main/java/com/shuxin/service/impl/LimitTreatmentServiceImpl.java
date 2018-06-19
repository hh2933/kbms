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
import com.shuxin.mapper.LimitTreatmentMapper;
import com.shuxin.mapper.LimitTreatmentOptMapper;
import com.shuxin.model.LimitTreatment;
import com.shuxin.model.LimitTreatmentOpt;
import com.shuxin.model.vo.LimitTreatmentVo;
import com.shuxin.service.ILimitTreatmentService;
@Service
public class LimitTreatmentServiceImpl extends ServiceImpl<LimitTreatmentMapper,LimitTreatment> implements ILimitTreatmentService{
	@Autowired
	private LimitTreatmentMapper limitTreatmentMapper;
	
	@Autowired
	private LimitTreatmentOptMapper limitTreatmentOptMapper;
	
	@Override
	public void selectLimitTreatmentVoPage(PageInfo pageInfo) {
		Page<LimitTreatment> page = new Page<LimitTreatment>(pageInfo.getNowpage(), pageInfo.getSize());
        List<Map<String,Object>> list = limitTreatmentMapper.selectLimitTreatmentVoPage(page, pageInfo.getCondition());
        pageInfo.setRows(list);
        pageInfo.setTotal(page.getTotal());	
		
	}

	@Override
	public int selectExistLimitTreatment(LimitTreatmentVo limitTreatmentVo) {
		return limitTreatmentMapper.selectExistLimitTreatment(limitTreatmentVo);
	}

	@Override
	public void editLimitTreatment(LimitTreatmentVo limitTreatmentVo, ShiroUser user) {
		LimitTreatment limitTreatment=BeanUtils.copy(limitTreatmentVo,LimitTreatment.class);
		
		Date date = new Date();
		limitTreatment.setUpdateTime(date);
		limitTreatment.setUpdateUser(user.getLoginName());
		if(StringUtils.isNotBlank(limitTreatment.getId()))
		{
			List<String> list = new ArrayList<String>();
			list.add(limitTreatment.getId());
			addLimitTreatmentHistroy(list,user.getLoginName(),"修改");
			this.updateById(limitTreatment);
		}
		else
		{
			limitTreatment.setCreateTime(date);
			limitTreatment.setCreateUser(user.getLoginName());
			this.insert(limitTreatment);
			LimitTreatmentOpt limitTreatmentOpt=BeanUtils.copy(limitTreatment,LimitTreatmentOpt.class);
			limitTreatmentOpt.setOptType("添加");
			limitTreatmentOptMapper.insert(limitTreatmentOpt);
		}
	}
	
	private void addLimitTreatmentHistroy(List<String> list,String loginName,String optType)
	{
		Map<String, Object> map = new HashMap<String, Object>();		
		map.put("list", list);
		map.put("loginName", loginName);
		map.put("optType", optType);
		limitTreatmentMapper.addLimitTreatmentHistory(map);
	}

	@Override
	public LimitTreatmentVo selectEditLimitTreatment(String id) {
		LimitTreatment limitTreatment=this.selectById(id);
		LimitTreatmentVo limitTreatmentVo=BeanUtils.copy(limitTreatment,LimitTreatmentVo.class);
		return limitTreatmentVo;
	}

	@Override
	public void deleteLimitTreatment(List<String> list, ShiroUser user) {
		addLimitTreatmentHistroy(list,user.getLoginName(),"删除");
		this.deleteBatchIds(list);
	}

	@Override
	public List<Map<String, Object>> selectLimitTreatmentVoPage() {
		return limitTreatmentMapper.selectLimitTreatmentVoPage();
	}

	@Override
	public List<Map<String, Object>> selectLimitTreatmentHistory() {
		return limitTreatmentMapper.selectLimitTreatmentHistory();
	}

	@Override
	public void importLimitTreatment(List<Map<String, String>> list, String loginName) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(list.size()==0)
		{
			return;
		}
		else if(list.size()<101)
		{			
			map.put("list", list);
			map.put("loginName", loginName);
			limitTreatmentMapper.importLimitTreatment(map);
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
				limitTreatmentMapper.importLimitTreatment(map);
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

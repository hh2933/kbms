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
import com.shuxin.mapper.DecomposeHospitalMapper;
import com.shuxin.mapper.DecomposeHospitalOptMapper;
import com.shuxin.model.DecomposeHospital;
import com.shuxin.model.DecomposeHospitalOpt;
import com.shuxin.model.vo.DecomposeHospitalVo;
import com.shuxin.service.IDecomposeHospitalService;
@Service
public class DecomposeHospitalServiceImpl extends ServiceImpl<DecomposeHospitalMapper,DecomposeHospital> implements IDecomposeHospitalService{
	@Autowired
	private DecomposeHospitalMapper decomposeHospitalMapper;
	@Autowired
	private DecomposeHospitalOptMapper decomposeHospitalOptMapper;
	
	@Override
	public void selectDecomposeHospitalVoPage(PageInfo pageInfo) {
		Page<DecomposeHospital> page = new Page<DecomposeHospital>(pageInfo.getNowpage(), pageInfo.getSize());
        List<Map<String,Object>> list = decomposeHospitalMapper.selectDecomposeHospitalVoPage(page, pageInfo.getCondition());
        pageInfo.setRows(list);
        pageInfo.setTotal(page.getTotal());	
		
	}

	@Override
	public int selectExistDecomposeHospital(DecomposeHospitalVo decomposeHospitalVo) {
		return decomposeHospitalMapper.selectExistDecomposeHospital(decomposeHospitalVo);
	}

	@Override
	public void editDecomposeHospital(DecomposeHospitalVo decomposeHospitalVo, ShiroUser user) {
		DecomposeHospital decomposeHospital=BeanUtils.copy(decomposeHospitalVo,DecomposeHospital.class);
		Date date = new Date();
		decomposeHospital.setUpdateTime(date);
		decomposeHospital.setUpdateUser(user.getLoginName());
		if(StringUtils.isNotBlank(decomposeHospital.getId()))
		{
			List<String> list = new ArrayList<String>();
			list.add(decomposeHospital.getId());
			addDecomposeHospitalHistroy(list,user.getLoginName(),"修改");
			this.updateById(decomposeHospital);
		}
		else
		{
			decomposeHospital.setCreateTime(date);
			decomposeHospital.setCreateUser(user.getLoginName());
			this.insert(decomposeHospital);
			DecomposeHospitalOpt decomposeHospitalOpt=BeanUtils.copy(decomposeHospital,DecomposeHospitalOpt.class);
			decomposeHospitalOpt.setOptType("添加");
			decomposeHospitalOptMapper.insert(decomposeHospitalOpt);
		}
	}
	
	private void addDecomposeHospitalHistroy(List<String> list,String loginName,String optType)
	{
		Map<String, Object> map = new HashMap<String, Object>();		
		map.put("list", list);
		map.put("loginName", loginName);
		map.put("optType", optType);
		decomposeHospitalMapper.addDecomposeHospitalHistory(map);
	}

	@Override
	public DecomposeHospitalVo selectEditDecomposeHospital(String id) {
		DecomposeHospital decomposeHospital=this.selectById(id);
		DecomposeHospitalVo decomposeHospitalVo=BeanUtils.copy(decomposeHospital,DecomposeHospitalVo.class);
		return decomposeHospitalVo;
	}

	@Override
	public void deleteDecomposeHospital(List<String> list, ShiroUser user) {
		addDecomposeHospitalHistroy(list,user.getLoginName(),"删除");
		this.deleteBatchIds(list);
	}

	@Override
	public List<Map<String, Object>> selectDecomposeHospitalVoPage() {
		return decomposeHospitalMapper.selectDecomposeHospitalVoPage();
	}

	@Override
	public List<Map<String, Object>> selectDecomposeHospitalHistory() {
		return decomposeHospitalMapper.selectDecomposeHospitalHistory();
	}

	@Override
	public void importDecomposeHospital(List<Map<String, String>> list, String loginName) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(list.size()==0)
		{
			return;
		}
		else if(list.size()<101)
		{			
			map.put("list", list);
			map.put("loginName", loginName);
			decomposeHospitalMapper.importDecomposeHospital(map);
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
				decomposeHospitalMapper.importDecomposeHospital(map);
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

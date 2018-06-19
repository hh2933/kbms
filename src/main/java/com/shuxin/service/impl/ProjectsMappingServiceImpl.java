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
import com.shuxin.mapper.ProjectsMappingMapper;
import com.shuxin.mapper.ProjectsMappingOptMapper;
import com.shuxin.model.ProjectsMapping;
import com.shuxin.model.ProjectsMappingOpt;
import com.shuxin.model.vo.ProjectsMappingVo;
import com.shuxin.service.IProjectsMappingService;
@Service
public class ProjectsMappingServiceImpl extends ServiceImpl<ProjectsMappingMapper,ProjectsMapping> implements IProjectsMappingService{

	@Autowired
	private ProjectsMappingMapper projectsMappingMapper;
	
	@Autowired
	private ProjectsMappingOptMapper projectsMappingOptMapper;
	
	@Override
	public void selectProjectsMappingVoPage(PageInfo pageInfo) {
		Page<ProjectsMapping> page = new Page<ProjectsMapping>(pageInfo.getNowpage(), pageInfo.getSize());
        List<Map<String,Object>> list = projectsMappingMapper.selectProjectsMappingVoPage(page, pageInfo.getCondition());
        pageInfo.setRows(list);
        pageInfo.setTotal(page.getTotal());	
	}

	@Override
	public int selectExistProjectsMapping(ProjectsMappingVo projectsMappingVo) {
		return projectsMappingMapper.selectExistProjectsMapping(projectsMappingVo);
	}

	@Override
	public void editProjectsMapping(ProjectsMappingVo projectsMappingVo, ShiroUser user) {
		ProjectsMapping projectsMapping=BeanUtils.copy(projectsMappingVo,ProjectsMapping.class);
		Date date = new Date();
		projectsMapping.setUpdateTime(date);
		projectsMapping.setUpdateUser(user.getLoginName());
		if(StringUtils.isNotBlank(projectsMapping.getId()))
		{
			List<String> list = new ArrayList<String>();
			list.add(projectsMapping.getId());
			addProjectsMappingHistroy(list,user.getLoginName(),"修改");
			this.updateById(projectsMapping);
		}
		else
		{
			projectsMapping.setCreateTime(date);
			projectsMapping.setCreateUser(user.getLoginName());
			this.insert(projectsMapping);
			ProjectsMappingOpt projectsMappingOpt=BeanUtils.copy(projectsMapping,ProjectsMappingOpt.class);
			projectsMappingOpt.setOptType("添加");
			projectsMappingOptMapper.insert(projectsMappingOpt);
		}
	}
	
	private void addProjectsMappingHistroy(List<String> list,String loginName,String optType)
	{
		Map<String, Object> map = new HashMap<String, Object>();		
		map.put("list", list);
		map.put("loginName", loginName);
		map.put("optType", optType);
		projectsMappingMapper.addProjectsMappingHistory(map);
	}

	@Override
	public ProjectsMappingVo selectEditProjectsMapping(String id) {
		ProjectsMapping projectsMapping = this.selectById(id);
		ProjectsMappingVo projectsMappingVo=BeanUtils.copy(projectsMapping,ProjectsMappingVo.class);
		return projectsMappingVo;
	}

	@Override
	public void deleteProjectsMapping(List<String> list, ShiroUser user) {
		addProjectsMappingHistroy(list,user.getLoginName(),"删除");
		this.deleteBatchIds(list);
		
	}

	@Override
	public List<Map<String, Object>> selectProjectsMappingVoPage() {
		return projectsMappingMapper.selectProjectsMappingVoPage();
	}

	@Override
	public List<Map<String, Object>> selectProjectsMappingHistory() {
		return projectsMappingMapper.selectProjectsMappingHistory();
	}

	@Override
	public void importProjectsMapping(List<Map<String, String>> list, String loginName) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(list.size()==0)
		{
			return;
		}
		else if(list.size()<101)
		{			
			map.put("list", list);
			map.put("loginName", loginName);
			projectsMappingMapper.importProjectsMapping(map);
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
				projectsMappingMapper.importProjectsMapping(map);
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

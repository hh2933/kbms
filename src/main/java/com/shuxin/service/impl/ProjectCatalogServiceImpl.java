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
import com.shuxin.mapper.ProjectCatalogMapper;
import com.shuxin.mapper.ProjectCatalogOptMapper;
import com.shuxin.model.ProjectCatalog;
import com.shuxin.model.ProjectCatalogOpt;
import com.shuxin.model.vo.ProjectCatalogVo;
import com.shuxin.service.IProjectCatalogService;

@Service
public class ProjectCatalogServiceImpl extends ServiceImpl<ProjectCatalogMapper,ProjectCatalog> implements IProjectCatalogService{

	@Autowired
	private ProjectCatalogMapper projectCatalogMapper;
	
	@Autowired
	private ProjectCatalogOptMapper projectCatalogOptMapper;
	
	@Override
	public void selectProjectCatalogVoPage(PageInfo pageInfo) {
		Page page = new Page(pageInfo.getNowpage(), pageInfo.getSize());       
        List<Map<String,Object>> list = projectCatalogMapper.selectProjectCatalogVoPage(page, pageInfo.getCondition());
        pageInfo.setRows(list);
        pageInfo.setTotal(page.getTotal());
	}

	@Override
	public int selectExistProjectCatalog(ProjectCatalogVo projectCatalogVo) {
		// TODO Auto-generated method stub
		return projectCatalogMapper.selectExistProjectCatalog(projectCatalogVo);
	}

	@Override
	public void editProjectCatalog(ProjectCatalogVo projectCatalogVo,ShiroUser user) {
		ProjectCatalog projectCatalog=BeanUtils.copy(projectCatalogVo,ProjectCatalog.class);
		Date date = new Date();
		projectCatalog.setUpdateTime(date);
		projectCatalog.setUpdateUser(user.getLoginName());
		if(StringUtils.isNotBlank(projectCatalog.getId()))
		{
			List<String> list = new ArrayList<String>();
			list.add(projectCatalog.getId());
			addProjectCatalogHistory(list,user.getLoginName(),"修改");
			this.updateById(projectCatalog);
		}
		else
		{
			projectCatalog.setCreateTime(date);
			projectCatalog.setCreateUser(user.getLoginName());
			this.insert(projectCatalog);
			ProjectCatalogOpt projectCatalogOpt=BeanUtils.copy(projectCatalog,ProjectCatalogOpt.class);
			projectCatalogOpt.setOptType("添加");
			projectCatalogOptMapper.insert(projectCatalogOpt);
		}
		
	}

	@Override
	public ProjectCatalogVo selectProjectCatalogVo(String id) {
		ProjectCatalog projectCatalog=this.selectById(id);
		ProjectCatalogVo projectCatalogVo=BeanUtils.copy(projectCatalog,ProjectCatalogVo.class);
		return projectCatalogVo;
	}
	
	private void addProjectCatalogHistory(List<String> list,String loginName,String optType)
	{
		Map<String, Object> map = new HashMap<String, Object>();		
		map.put("list", list);
		map.put("loginName", loginName);
		map.put("optType", optType);
		projectCatalogMapper.addProjectCatalogHistory(map);
	}

	@Override
	public void deleteProjectCatalog(List<String> list, ShiroUser user) {
		
		addProjectCatalogHistory(list,user.getLoginName(),"删除");
		this.deleteBatchIds(list);
		
	}

	@Override
	public List<Map<String, Object>> selectProjectCatalogVoPage() {
		return projectCatalogMapper.selectProjectCatalogVoPage();
	}

	@Override
	public List<Map<String, Object>> searchProjectCatalogHistroy() {
		return projectCatalogMapper.searchProjectCatalogHistroy();
	}

	@Override
	public void importProjectCatalog(List<Map<String, String>> list, String loginName)
	{		
		
		Map<String, Object> map = new HashMap<String, Object>();
		if(list.size()==0)
		{
			return;
		}
		else if(list.size()<101)
		{			
			map.put("list", list);
			map.put("loginName", loginName);
			projectCatalogMapper.importProjectCatalog(map);
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
				projectCatalogMapper.importProjectCatalog(map);
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

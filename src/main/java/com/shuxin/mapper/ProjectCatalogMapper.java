
package com.shuxin.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.shuxin.model.ProjectCatalog;
import com.shuxin.model.vo.ProjectCatalogVo;

public interface ProjectCatalogMapper  extends BaseMapper<ProjectCatalog>{

	public List<Map<String, Object>> selectProjectCatalogVoPage(Pagination page, Map<String, Object> params);
	
	public int selectExistProjectCatalog(ProjectCatalogVo projectCatalogVo);
	
	public void addProjectCatalogHistory(Map<String, Object> map);
	
	public List<Map<String, Object>> selectProjectCatalogVoPage();
	
	public List<Map<String, Object>> searchProjectCatalogHistroy();
	
	public void importProjectCatalog(Map<String, Object> map);
}

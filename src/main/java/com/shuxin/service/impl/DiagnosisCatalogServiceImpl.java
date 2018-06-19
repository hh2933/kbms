package com.shuxin.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.shuxin.commons.shiro.ShiroUser;
import com.shuxin.commons.utils.BeanUtils;
import com.shuxin.commons.utils.PageInfo;
import com.shuxin.commons.utils.StringUtils;
import com.shuxin.mapper.DiagnosisCatalogMapper;
import com.shuxin.mapper.DiagnosticCatalogOptMapper;
import com.shuxin.model.DiagnosticCatalog;
import com.shuxin.model.DiagnosticCatalogOpt;
import com.shuxin.model.DiagnosticmaBZMpping;
import com.shuxin.model.ProjectCatalogOpt;
import com.shuxin.service.IDiagnosisCatalogService;
@Service
public class DiagnosisCatalogServiceImpl extends ServiceImpl<DiagnosisCatalogMapper, DiagnosticCatalog> implements IDiagnosisCatalogService{
    
	@Autowired
	private  DiagnosisCatalogMapper catalogMapper;
	  @Autowired
	  private DiagnosticCatalogOptMapper catalogOptMapper;
	 
	@Override
	public void findDiagnosticCatalogDataGrid(PageInfo pageInfo) {
		// TODO Auto-generated method stub
		 Page<DiagnosticCatalog> page = new Page<DiagnosticCatalog>(pageInfo.getNowpage(), pageInfo.getSize());
	        String orderField = com.baomidou.mybatisplus.toolkit.StringUtils.camelToUnderline(pageInfo.getSort());
	        page.setOrderByField(orderField);
	        page.setAsc(pageInfo.getOrder().equalsIgnoreCase("asc"));
	        List<DiagnosticCatalog> list = catalogMapper.findDiagnosisCatalogDataGrid(page, pageInfo.getCondition());
	        pageInfo.setRows(list);
	        pageInfo.setTotal(page.getTotal());	
	}

	@Override
	public List<DiagnosticCatalog> selectBycode(
			DiagnosticCatalog diagnosticCatalog) {
		DiagnosticCatalog newdiagnosticCatalog = new DiagnosticCatalog();
		newdiagnosticCatalog.setDiagnosisCode(diagnosticCatalog.getDiagnosisCode());
		
        EntityWrapper<DiagnosticCatalog> wrapper = new EntityWrapper<DiagnosticCatalog>(newdiagnosticCatalog);
        if(diagnosticCatalog.getId()!=null){
        	 wrapper.where("id != {0}", diagnosticCatalog.getId());
		}
        
        return this.selectList(wrapper);
	}

	@Override
	public void editDiagnosis(DiagnosticCatalog diagnosticCatalog, ShiroUser user) {
		// TODO Auto-generated method stub
		diagnosticCatalog.setUpdateTime(new Date());
		diagnosticCatalog.setUpdateUser(user.getLoginName());
		String optType="添加";
		if(diagnosticCatalog.getId()!=null&&StringUtils.isNotBlank(diagnosticCatalog.getId())){
			optType="修改";
			addDiagnosticCatalogHistory(diagnosticCatalog.getId(), user.getLoginName(),optType );
			this.updateById(diagnosticCatalog);
			
		}else{
			diagnosticCatalog.setCreateTime(new Date());
			diagnosticCatalog.setCreateUser(user.getLoginName());;
	        this.insert(diagnosticCatalog);
	        DiagnosticCatalogOpt diagnosticCatalogOpt=BeanUtils.copy(diagnosticCatalog,DiagnosticCatalogOpt.class);
	        diagnosticCatalogOpt.setOptType("添加");
	        catalogOptMapper.insert(diagnosticCatalogOpt);
		}
		
	}

	@Override
	public void deleteDiagnosticCatalog(List<String> ids, ShiroUser user) {
		 for (int i = 0; i < ids.size(); i++) {
			 addDiagnosticCatalogHistory(ids.get(i), user.getLoginName(),"删除" );
		}
		 this.deleteBatchIds(ids);
	}
	private void addDiagnosticCatalogHistory(String id,String loginName,String optType)
	{
		
		Map<String, Object> params = new HashMap<String, Object>();
	
		params.put("id", id);
		params.put("optType", optType);
		params.put("loginName", loginName);
		catalogMapper.addDiagnosticCatalogHistory(params);
	}
	@Override
	public  void  importData(List<Map<String, String>> exportList, ShiroUser user) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		if(exportList.size()==0)
		{
			return;
		}
		else if(exportList.size()<101)
		{			
			map.put("list", exportList);
			map.put("loginName", user.getLoginName());
			catalogMapper.daoruData(map);
		}
		else
		{
			int count= exportList.size()/100==0?exportList.size()/100:exportList.size()/100+1;
			int startIndex=0;
			int endIndex=100;
			for(int i=1;i<=count;i++)
			{
				map.put("list", exportList.subList(startIndex, endIndex));
				map.put("loginName", user.getLoginName());
				catalogMapper.daoruData(map);
				startIndex=endIndex;
				endIndex=100*(i+1);
				if(endIndex>exportList.size())
				{
					endIndex=exportList.size();
				}
			}
		}
	}

	@Override
	public List<Map<String, Object>> selectDetailDiagnosisCatalog() {
		// TODO Auto-generated method stub
		 List<Map<String, Object>> detailList = catalogMapper.selectDetailDiagnosisCatalog();
			
			
			return detailList;
	}

	@Override
	public List<Map<String, Object>> selectDiagnosisCatalogHistory() {
		// TODO Auto-generated method stub
		 List<Map<String, Object>> detailList = catalogMapper.selectDiagnosisCatalogHistory();
			
			
			return detailList;
	}

	
}

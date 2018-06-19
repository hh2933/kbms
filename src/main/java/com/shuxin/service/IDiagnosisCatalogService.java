package com.shuxin.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;

import com.baomidou.mybatisplus.service.IService;
import com.shuxin.commons.shiro.ShiroUser;
import com.shuxin.commons.utils.PageInfo;
import com.shuxin.model.DiagnosticCatalog;
import com.shuxin.model.DiagnosticmaBZMpping;

public interface IDiagnosisCatalogService  extends IService<DiagnosticCatalog>{

	public	void findDiagnosticCatalogDataGrid(PageInfo pageInfo);

	public	List<DiagnosticCatalog> selectBycode(DiagnosticCatalog diagnosticCatalog);

	public	void editDiagnosis(DiagnosticCatalog diagnosticCatalog, ShiroUser user);

	public	void deleteDiagnosticCatalog(List<String> ids, ShiroUser user);

	public	void importData(List<Map<String, String>> exportList, ShiroUser user);

	public	List<Map<String, Object>> selectDetailDiagnosisCatalog();

	public	List<Map<String, Object>> selectDiagnosisCatalogHistory();

	
	
	
}

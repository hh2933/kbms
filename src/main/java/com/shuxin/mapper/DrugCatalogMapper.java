
package com.shuxin.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.shuxin.model.DrugCatalog;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.model.vo.DrugCatalogVo;

public interface DrugCatalogMapper  extends BaseMapper<DrugCatalog>{

	public List<Map<String, Object>> selectDrugCatalogVoPage(Pagination page, Map<String, Object> params);
	
	public int  selectExistDrugCatalog(DrugCatalogVo drugCatalogVo);
	
	public void addDrugCatalogHistory(Map<String, Object> map);
	
	public List<Map<String, Object>> selectDrugCatalogVoPage();
	
	public List<Map<String, Object>> searchDrugCatalogHistroy();
	
	public void importDrugCatalog(Map<String, Object> map);
	
	public List<Map<String, String>> selectDrugType(List<HospitalClaimDetail> list);
}

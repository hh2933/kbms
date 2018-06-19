package com.shuxin.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.shuxin.model.MedicalCatalog;

public interface MedicalCatalogMapper extends BaseMapper<MedicalCatalog>{

	List<MedicalCatalog> getCatalogTableInfo(Page<MedicalCatalog> page, Map<String, Object> condition);

	Boolean updateClztIn1(List<String> ids);
	
	Boolean updateClztIn0(List<String> ids);

	List<Map<String, String>> getCatalogExportInfo(MedicalCatalog catalog);

	List<Map<String, String>> getYpzdDetails(List<String> yyxmbmList);

	List<Map<String, String>> getYlzdDetails(List<String> yyxmbmList);
	

}

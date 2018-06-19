package com.shuxin.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.shuxin.commons.utils.PageInfo;
import com.shuxin.model.MedicalCatalog;

public interface IMedicalCatalogService  extends IService<MedicalCatalog>{

	void getCatalogTableInfo(PageInfo pageInfo);

	boolean updateClzt(String clzt, String ids);

	List<Map<String, String>> getCatalogExportInfo(MedicalCatalog catalog);
}

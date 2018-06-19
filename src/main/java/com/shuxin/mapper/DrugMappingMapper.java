
package com.shuxin.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.shuxin.model.DrugMapping;
import com.shuxin.model.vo.DrugMappingVo;

public interface DrugMappingMapper  extends BaseMapper<DrugMapping>{

	public List<Map<String, Object>> selectDrugMappingVoPage(Pagination page, Map<String, Object> params);
	
	public int  selectExistDrugMapping(DrugMappingVo drugMappingVo);
	
	public void  addDrugMappingHistory(Map<String, Object> map);
	
	public List<Map<String, Object>> selectDrugMappingVoPage();
	
	public List<Map<String, Object>> searchDrugMappingHistroy();
	
	public void importDrugMapping(Map<String, Object> map);
}

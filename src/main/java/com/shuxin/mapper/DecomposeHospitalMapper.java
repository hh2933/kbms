package com.shuxin.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.shuxin.model.DecomposeHospital;
import com.shuxin.model.vo.DecomposeHospitalVo;

public interface DecomposeHospitalMapper extends BaseMapper<DecomposeHospital>{

	public List<Map<String, Object>>  selectDecomposeHospitalVoPage(Pagination page, Map<String, Object> params);
	
	public int selectExistDecomposeHospital(DecomposeHospitalVo decomposeHospitalVo);
	
	public void addDecomposeHospitalHistory(Map<String, Object> map);
	
	public List<Map<String, Object>>  selectDecomposeHospitalVoPage();
	
	public List<Map<String, Object>>  selectDecomposeHospitalHistory();
	
	public void importDecomposeHospital(Map<String, Object> map);
}

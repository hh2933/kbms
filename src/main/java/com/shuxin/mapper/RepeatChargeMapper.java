package com.shuxin.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.shuxin.model.RepeatCharge;
import com.shuxin.model.vo.RepeatChargeVo;

public interface RepeatChargeMapper extends BaseMapper<RepeatCharge>{
	
	public List<Map<String, Object>>  selectRepeatChargeVoPage(Pagination page, Map<String, Object> params);

	public int selectExistRepeatCharge(RepeatChargeVo repeatChargeVo);
	
	public void addRepeatChargeHistory(Map<String, Object> map);
	
	public List<Map<String, Object>>  selectRepeatChargeVoPage();
	
	public List<Map<String, Object>>  selectRepeatChargeHistory();
	
	public void importRepeatChargeMapping(Map<String, Object> map);
}

package com.shuxin.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.shuxin.model.MIStandardMap;

public interface MIStandardMapMapper extends BaseMapper<MIStandardMap> {

 public	List<MIStandardMap> findmiStandardDataGrid(Page<MIStandardMap> page,
			Map<String, Object> condition);

public void addMIStandardMapHistory(Map<String, Object> params);

public void daoruData(Map<String, Object> map);

public List<Map<String, Object>> selectDetailMIStandardMap();

public List<Map<String, Object>> selectMIStandardMapHistory();


}

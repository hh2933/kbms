package com.shuxin.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.shuxin.model.Dictionary;


public interface DictionaryMapper extends BaseMapper<Dictionary> {

 	public List<Dictionary> findDictionaryDataGrid(Page<Dictionary> page,
			Map<String, Object> condition);

	public List<Dictionary> groupByDictType();

	
	
	
	
	
}

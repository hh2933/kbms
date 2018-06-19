package com.shuxin.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.shuxin.model.DrgsSubject;

public interface DrgsSubjectMapper extends BaseMapper<DrgsSubject>{

public	List<Map<String, Object>> drgsSubjectDataGrid(Page<DrgsSubject> page,
			Map<String, Object> condition);

	
	
	
	
}

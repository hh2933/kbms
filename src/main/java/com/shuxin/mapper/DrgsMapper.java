package com.shuxin.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.shuxin.model.Drgs;

public interface DrgsMapper extends BaseMapper<Drgs> {


public List<Map<String, Object>> selectDrgsByCondition(Page<Drgs> page,
		Map<String, Object> condition);

public List<Map<String, Object>> drgsType();

}

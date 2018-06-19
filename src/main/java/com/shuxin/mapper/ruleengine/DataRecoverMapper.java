package com.shuxin.mapper.ruleengine;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.shuxin.model.CommonModel;
import com.shuxin.model.Dictionary;

public interface DataRecoverMapper extends BaseMapper<CommonModel> {

	public List<Object> selectFromTimeOut();

	public String selectFromYbkf();

	public Object selectFromHis();

	public String selectFrom64();

}

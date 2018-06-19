package com.shuxin.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import com.shuxin.mapper.ParameterMapper;

import com.shuxin.model.Parameter;

import com.shuxin.service.IParamterService;

@Service
public class ParameterServiceImpl extends ServiceImpl<ParameterMapper,Parameter>   implements IParamterService {
	
 
	@Autowired
	private ParameterMapper paramterMapper;
	 
	@Override
	public List<Parameter> findDepartType(String clum) {
		// TODO Auto-generated method stub
		 EntityWrapper<Parameter> wrapper = new EntityWrapper<Parameter>();
		
		  wrapper.where("p_type={0}", clum);
	       return paramterMapper.selectList(wrapper);
	
	}


	
	
}

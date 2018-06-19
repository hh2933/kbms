package com.shuxin.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.shuxin.commons.shiro.ShiroUser;
import com.shuxin.commons.utils.PageInfo;
import com.shuxin.commons.utils.StringUtils;
import com.shuxin.mapper.DrgsMapper;
import com.shuxin.model.Drgs;
import com.shuxin.service.IDrgsService;
@Service
public class DrgsServiceImpl extends ServiceImpl<DrgsMapper, Drgs> implements IDrgsService {

	@Autowired
	private DrgsMapper drgsMapper;

	@Override
	public void drgsDataGrid(PageInfo pageInfo) {
		// TODO Auto-generated method stub
		 Page<Drgs> page = new Page<Drgs>(pageInfo.getNowpage(), pageInfo.getSize());
	        String orderField = com.baomidou.mybatisplus.toolkit.StringUtils.camelToUnderline(pageInfo.getSort());
	        page.setOrderByField(orderField);
	        page.setAsc(pageInfo.getOrder().equalsIgnoreCase("asc"));
	        List<Map<String, Object>> list = drgsMapper.selectDrgsByCondition(page, pageInfo.getCondition());
	        pageInfo.setRows(list);
	        pageInfo.setTotal(page.getTotal());
	}

	@Override
	public List<Drgs> selectByxh(Drgs drgs) {
		Drgs newdrgs = new Drgs();
		newdrgs.setBzxh(drgs.getBzxh());
		
        EntityWrapper<Drgs> wrapper = new EntityWrapper<Drgs>(newdrgs);
        if(drgs.getId()!=null){
        	 wrapper.where("id != {0}", drgs.getId());
		}
        
        return this.selectList(wrapper);
	}

	
	@Override
	public void editDrgs(Drgs drgs, ShiroUser user) {
		// TODO Auto-generated method stub
		if(drgs.getId()!=null&&StringUtils.isNotBlank(drgs.getId())){		
			this.updateById(drgs);						
		}else{		
	        this.insert(drgs);
	      
		}
		
	}

	@Override
	public void deleteDrgs(List<String> ids, ShiroUser user) {
		// TODO Auto-generated method stub
		 this.deleteBatchIds(ids);
	}

	@Override
	public List<Map<String, Object>> drgsType() {
		// TODO Auto-generated method stub
		List<Map<String, Object>> list=drgsMapper.drgsType();
		return list;
	}
	
	
	
	
	
	
	
	
	
	
}

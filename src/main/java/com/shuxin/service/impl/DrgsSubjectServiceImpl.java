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
import com.shuxin.mapper.DrgsSubjectMapper;
import com.shuxin.model.Dictionary;
import com.shuxin.model.Drgs;
import com.shuxin.model.DrgsSubject;
import com.shuxin.model.vo.DictionaryListVo;
import com.shuxin.model.vo.DictionaryVo;
import com.shuxin.model.vo.DrgsSubjectListVo;
import com.shuxin.service.IDrgsSubjectService;
@Service
public class DrgsSubjectServiceImpl extends ServiceImpl<DrgsSubjectMapper, DrgsSubject>  implements IDrgsSubjectService {

	@Autowired
	private   DrgsSubjectMapper drgsSubjectMapper;

	@Override
	public void drgsSubjectDataGrid(PageInfo pageInfo) {
		// TODO Auto-generated method stub
		 Page<DrgsSubject> page = new Page<DrgsSubject>(pageInfo.getNowpage(), pageInfo.getSize());
	        String orderField = com.baomidou.mybatisplus.toolkit.StringUtils.camelToUnderline(pageInfo.getSort());
	        page.setOrderByField(orderField);
	        page.setAsc(pageInfo.getOrder().equalsIgnoreCase("asc"));
	        List<Map<String, Object>> list = drgsSubjectMapper.drgsSubjectDataGrid(page, pageInfo.getCondition());
	        pageInfo.setRows(list);
	        pageInfo.setTotal(page.getTotal());
		
		
	}

	@Override
	public List<DrgsSubject> selectByBm(DrgsSubject drgsSubject) {
		// TODO Auto-generated method stub
		DrgsSubject  sub = new DrgsSubject();
		sub.setXmbm(drgsSubject.getXmbm());
        EntityWrapper<DrgsSubject> wrapper = new EntityWrapper<DrgsSubject>(sub);
        if(drgsSubject.getId()!=null){
       	 wrapper.where("id != {0}", drgsSubject.getId());
		}
        return this.selectList(wrapper);
	}

	@Override
	public void addDrgs(ShiroUser user, DrgsSubjectListVo vo) {
		// TODO Auto-generated method stub
		List<DrgsSubject> list=vo.getList();
		if(list.size()>0 && StringUtils.isNotBlank(list.get(0).getId()) ){
			for (int i = 0; i < list.size(); i++) {
				drgsSubjectMapper.updateById(list.get(i));
			}
		
		}else if(list.size()>0 && StringUtils.isBlank(list.get(0).getId())){
                     for (int i = 0; i < list.size(); i++) {								
                    	 drgsSubjectMapper.insert(list.get(i));
                    	 }
		}
		
		
	}

	@Override
	public void deleteDrgsSubject(ShiroUser user, DrgsSubjectListVo vo) {
		// TODO Auto-generated method stub
		List<DrgsSubject> list=vo.getList();
		if(list.size()>0 && StringUtils.isNotBlank(list.get(0).getId()) ){
			for (int i = 0; i < list.size(); i++) {
			
				drgsSubjectMapper.deleteById(list.get(i).getId());
			}
			
		}
	}
	
	
	

}

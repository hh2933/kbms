package com.shuxin.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.shuxin.commons.shiro.ShiroUser;
import com.shuxin.commons.utils.BeanUtils;
import com.shuxin.commons.utils.PageInfo;
import com.shuxin.commons.utils.StringUtils;
import com.shuxin.mapper.DictionaryMapper;
import com.shuxin.model.Dictionary;
import com.shuxin.model.Parameter;
import com.shuxin.model.RuleTableInfo;
import com.shuxin.model.vo.DictionaryListVo;
import com.shuxin.model.vo.DictionaryVo;
import com.shuxin.service.IDictionaryService;

@Service
public class DictionaryServiceImpl extends ServiceImpl<DictionaryMapper, Dictionary> implements IDictionaryService{

	@Autowired
	private DictionaryMapper dictionaryMapper;
	
	@Override
	public void findDictionaryDataGrid(PageInfo pageInfo) {
		// TODO Auto-generated method stub
		 Page<Dictionary> page = new Page<Dictionary>(pageInfo.getNowpage(), pageInfo.getSize());
	        String orderField = com.baomidou.mybatisplus.toolkit.StringUtils.camelToUnderline(pageInfo.getSort());
	        page.setOrderByField(orderField);
	        page.setAsc(pageInfo.getOrder().equalsIgnoreCase("asc"));
	        List<Dictionary> list = dictionaryMapper.findDictionaryDataGrid(page, pageInfo.getCondition());
	        pageInfo.setRows(list);
	        pageInfo.setTotal(page.getTotal());	
	}

	@Override
	public List<Dictionary> findDepartType(String clum) {

				 EntityWrapper<Dictionary> wrapper = new EntityWrapper<Dictionary>();
				if(clum !=null && !StringUtils.isEmpty(clum) ){
					 wrapper.where("dict_type_code={0}", clum);
				}
				 
			       return dictionaryMapper.selectList(wrapper);	
	}

	@Override
	public void editDictionary(ShiroUser user, DictionaryListVo vo) {
		// TODO Auto-generated method stub
		List<DictionaryVo> list=vo.getList();
		if(vo.getDictTypeCode()==null||vo.getDictTypeCode().equals("")){
			vo.setDictTypeCode(UUID.randomUUID().toString().replace("-","").trim());
		}
		if(list.size()>0 ){
			for (int i = 0; i < list.size(); i++) {
				Dictionary dic=BeanUtils.copy(list.get(i),Dictionary.class);
				dic.setCreateTime(new Date());
				dic.setCreateUser(user.getLoginName());
				dic.setDictType(vo.getDictType());
				dic.setUpdateTime(new Date());
				dic.setUpdateUser(user.getLoginName());
				dic.setDictTypeCode(vo.getDictTypeCode());
				dictionaryMapper.insert(dic);
			}
			
		}
		  
		
	}

	@Override
	public List<Dictionary> selectByType(DictionaryListVo vo) {
		// TODO Auto-generated method stub
		Dictionary  dict = new Dictionary();
		dict.setDictType(vo.getDictType());
        EntityWrapper<Dictionary> wrapper = new EntityWrapper<Dictionary>(dict);
       
        return this.selectList(wrapper);
	}

	@Override
	public void deleteDictionary(ShiroUser user, DictionaryListVo vo) {
		// TODO Auto-generated method stub
		List<DictionaryVo> list=vo.getList();
		if(list.size()>0 && StringUtils.isNotBlank(list.get(0).getId()) ){
			for (int i = 0; i < list.size(); i++) {
				
				
				dictionaryMapper.deleteById(list.get(i).getId());
			}
			
		}
		
	}

	@Override
	public List<Dictionary> groupByDictType() {
		// TODO Auto-generated method stub
		List<Dictionary> dictList=dictionaryMapper.groupByDictType();
		       return  dictList;
	}

	@Override
	public void deleteDictionaryByType(ShiroUser user, DictionaryListVo vo) {
		// TODO Auto-generated method stub
		Dictionary  dict = new Dictionary();
		dict.setDictTypeCode(vo.getDictTypeCode());
        EntityWrapper<Dictionary> wrapper = new EntityWrapper<Dictionary>(dict); 
        	dictionaryMapper.delete(wrapper);
  
	}

	@Override
	public List<Dictionary> selectByType(String type) {
		Dictionary  dict = new Dictionary();
		dict.setDictType(type);
        EntityWrapper<Dictionary> wrapper = new EntityWrapper<Dictionary>(dict);
        wrapper.orderBy("dict_code");
        return this.selectList(wrapper);
	}
}

package com.shuxin.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.shuxin.commons.shiro.ShiroUser;
import com.shuxin.commons.utils.PageInfo;
import com.shuxin.model.Dictionary;
import com.shuxin.model.Parameter;
import com.shuxin.model.vo.DictionaryListVo;

public interface IDictionaryService  extends IService<Dictionary>{

    public	 void findDictionaryDataGrid(PageInfo pageInfo);

	public List<Dictionary> findDepartType(String clum);

	public void editDictionary(ShiroUser user, DictionaryListVo vo);

	public List<Dictionary> selectByType(DictionaryListVo vo);
	public List<Dictionary> selectByType(String type);

	public void deleteDictionary(ShiroUser user, DictionaryListVo vo);

	public List<Dictionary> groupByDictType();

	public void deleteDictionaryByType(ShiroUser user, DictionaryListVo vo);

}

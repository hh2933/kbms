package com.shuxin.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.shuxin.commons.shiro.ShiroUser;
import com.shuxin.commons.utils.PageInfo;
import com.shuxin.model.Drgs;
import com.shuxin.model.DrgsSubject;
import com.shuxin.model.vo.DictionaryListVo;
import com.shuxin.model.vo.DrgsSubjectListVo;

public interface IDrgsSubjectService extends IService<DrgsSubject>{

 public	void drgsSubjectDataGrid(PageInfo pageInfo);

public List<DrgsSubject> selectByBm(DrgsSubject vo);

public void addDrgs(ShiroUser user, DrgsSubjectListVo vo);

public void deleteDrgsSubject(ShiroUser user, DrgsSubjectListVo vo);

}

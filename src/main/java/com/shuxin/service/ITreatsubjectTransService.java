package com.shuxin.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;

import com.baomidou.mybatisplus.service.IService;
import com.shuxin.commons.shiro.ShiroUser;
import com.shuxin.commons.utils.PageInfo;
import com.shuxin.model.TreatSubjectTrans;

public interface ITreatsubjectTransService extends IService<TreatSubjectTrans>{

 public	void findTreatSubjectTransDataGrid(PageInfo pageInfo);

 public	List<TreatSubjectTrans> selectBycode(TreatSubjectTrans treatSubjectTrans);

 public	void editTreatSubjectTrans(TreatSubjectTrans treatSubjectTrans, ShiroUser user);

 public	void deleteTreatsubjectTrans(List<String> ids, ShiroUser user);

 public	void importData(List<Map<String, String>> exportList, ShiroUser user);

 public	List<Map<String, Object>> selectDetailTreatSubjectTrans();

 public	List<Map<String, Object>> selectTreatSubjectTransHistory();

}

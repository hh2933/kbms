package com.shuxin.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;

import com.baomidou.mybatisplus.service.IService;
import com.shuxin.commons.shiro.ShiroUser;
import com.shuxin.commons.utils.PageInfo;
import com.shuxin.model.TumourDiagnosis;

public interface ITumourDiagnosisService  extends IService<TumourDiagnosis>{

public	void findTumourDiagnosisDataGrid(PageInfo pageInfo);

public	List<TumourDiagnosis> selectBycode(TumourDiagnosis tumourDiagnosis);

public	void editDiagnosis(TumourDiagnosis tumourDiagnosis, ShiroUser user);

public	void deleteTreatsubjectTrans(List<String> ids, ShiroUser user);

public	void importData(List<Map<String, String>> exportList, ShiroUser user);

public	List<Map<String, Object>> selectDetailTumourDiagnosis();

public	List<Map<String, Object>> selectTumourDiagnosisHistory();

}

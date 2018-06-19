package com.shuxin.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;

import com.baomidou.mybatisplus.service.IService;
import com.shuxin.commons.shiro.ShiroUser;
import com.shuxin.commons.utils.PageInfo;
import com.shuxin.model.DiagnosticmaBZMpping;

public interface IDiagnosisBZService extends IService<DiagnosticmaBZMpping>{

	public	void findbzDiagnosisDataGrid(PageInfo pageInfo);

	public	List<DiagnosticmaBZMpping> selectBycode(DiagnosticmaBZMpping diagnosisBZ);

	public	void editDiagnosis(DiagnosticmaBZMpping diagnosisBZ, ShiroUser user);

	public	void deleteDiagnosisBZ(List<String> ids,ShiroUser user);

	public	void importData(List<Map<String, String>> exportList, ShiroUser user);

	public	List<Map<String, Object>> selectDetailDiagnosisBZ();

	public	List<Map<String, Object>> selectDiagnosisBZHistory();

}

package com.shuxin.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.shuxin.model.MIDiagnosis;

public interface MIDiagnosisMapper extends BaseMapper<MIDiagnosis> {

public	List<MIDiagnosis> findmiDiagnosisMapperDataGrid(Page<MIDiagnosis> page,
			Map<String, Object> condition);

public	void addMIDiagnosisHistory(Map<String, Object> params);

public void daoruData(Map<String, Object> map);

public List<Map<String, Object>> selectDetailMIDiagnosis();

public List<Map<String, Object>> selectMIDiagnosisHistory();

public List<String> selectAdaptionPackageCode(List<String> list);

public Integer selectBZ31DiagnosisCodeCount(List<String> list);

public List<String> selectBZ31DiagnosisCodeInfo(List<String> list);

}

package com.shuxin.service.impl.ruleengine;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shuxin.mapper.ruleengine.ViolationDetailMapper;
import com.shuxin.model.ruleengine.HospitalClaim;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.model.ruleengine.ViolationDetail;
import com.shuxin.service.ruleengine.IViolationDetailService;

@Service
public class ViolationDetailServiceImpl implements IViolationDetailService {

	@Autowired
	private ViolationDetailMapper violationDetailMapper;

	@Override
	public void editViolationDetail(HospitalClaim hospitalClaim,List<HospitalClaimDetail> hospitalClaimDetails,
			List<ViolationDetail> violationDetails) {
		
		violationDetailMapper.deleteViolationDetailById(hospitalClaim.getId());
		String violationCode = UUID.randomUUID().toString().replace("-", "");
		for(ViolationDetail violationDetail:violationDetails)
		{
			violationDetail.setViolationCode(violationCode);
			violationDetailMapper.addViolationDetail(violationDetail);
		}
	}

	@Override
	public void deleteViolationDetail(String id) {
		violationDetailMapper.deleteViolationDetailById(id);
		
	}

	@Override
	public void addViolationResult(Map<String, String> paramMap) {
		violationDetailMapper.addViolationResult(paramMap);
		
	}
	
	

}

package com.shuxin.service.ruleengine;

import com.baomidou.mybatisplus.service.IService;
import com.shuxin.model.ruleengine.HospitalClaimDetail;

public interface IHospitalClaimDetailService extends IService<HospitalClaimDetail> {

//	public void deleteHospitalClaimDetail(String diaSerialCode);
	public void deleteHospitalClaimDetail(String id);
	
}

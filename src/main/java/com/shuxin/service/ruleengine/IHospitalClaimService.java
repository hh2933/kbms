package com.shuxin.service.ruleengine;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.shuxin.model.ruleengine.HospitalClaim;
import com.shuxin.model.ruleengine.HospitalClaimDetail;

public interface IHospitalClaimService  extends IService<HospitalClaim> {
	
	public boolean handleHospitalClaimInfo(HospitalClaim hospitalClaim,List<HospitalClaimDetail> hospitalClaimDetails);
	
//	public boolean delHospitalClaimInfo(String diaSerialCode);
	
	/*
	 * 1.根据id查询T_PATIENT_INFORMATION表diaserialcode，在根据diaserialcode删除T_CHARGE_DETAILS表数据
	 * 2.根据id删除T_PATIENT_INFORMATION表数据
	 */
	public boolean delHospitalClaimInfo(String id);

}

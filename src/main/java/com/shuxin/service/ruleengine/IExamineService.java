package com.shuxin.service.ruleengine;

import java.util.List;

import com.shuxin.model.ruleengine.HospitalClaim;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.model.ruleengine.ViolationDetail;
/**
 * 审核服务接口
 * @author shuxin
 *
 */
public interface IExamineService{

	/**
	 * 审核门诊
	 * @param hospitalClaim
	 * @param hospitalClaimDetail
	 * @return
	 */
	public List<ViolationDetail> examineOutpatient(HospitalClaim hospitalClaim,List<HospitalClaimDetail> hospitalClaimDetails);
	
	/**
	 * 审核住院
	 * @param hospitalClaim
	 * @param hospitalClaimDetail
	 * @return
	 */
	public List<ViolationDetail> examineHospitalization(HospitalClaim hospitalClaim,List<HospitalClaimDetail> hospitalClaimDetails);
}

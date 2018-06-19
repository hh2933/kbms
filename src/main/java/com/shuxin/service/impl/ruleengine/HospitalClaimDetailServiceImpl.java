package com.shuxin.service.impl.ruleengine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.shuxin.mapper.ruleengine.HospitalClaimDetailMapper;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.service.ruleengine.IHospitalClaimDetailService;

@Service
public class HospitalClaimDetailServiceImpl extends ServiceImpl<HospitalClaimDetailMapper, HospitalClaimDetail> implements IHospitalClaimDetailService {

	@Autowired
	private HospitalClaimDetailMapper hospitalClaimDetailMapper;
	
	@Override
	public void deleteHospitalClaimDetail(String diaSerialCode) {
		hospitalClaimDetailMapper.deleteHospitalClaimDetail(diaSerialCode);
		
	}
	
}

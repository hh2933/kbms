package com.shuxin.service.impl.ruleengine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.shuxin.mapper.ruleengine.HospitalClaimDetailMapper;
import com.shuxin.mapper.ruleengine.HospitalClaimMapper;
import com.shuxin.model.ruleengine.HospitalClaim;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.service.ruleengine.IHospitalClaimDetailService;
import com.shuxin.service.ruleengine.IHospitalClaimService;

@Service
public class HospitalClaimServiceImpl extends ServiceImpl<HospitalClaimMapper, HospitalClaim> implements IHospitalClaimService  {

	@Autowired
	private IHospitalClaimDetailService hospitalClaimDetailService;
	
	@Autowired
	private HospitalClaimMapper hospitalClaimMapper;
	
	@Autowired
	private HospitalClaimDetailMapper hospitalClaimDetailMapper;
	
	protected Logger logger = LogManager.getLogger(getClass());
	
	private static ExecutorService threadPool = Executors.newFixedThreadPool(20);
	
	@Override
	public boolean handleHospitalClaimInfo(HospitalClaim hospitalClaim, List<HospitalClaimDetail> hospitalClaimDetails) {
		final List<String> errorList=new ArrayList<String>();
		try
		{
			
			delHospitalClaimInfo(hospitalClaim.getId());
			this.insertOrUpdate(hospitalClaim);
			Iterator<HospitalClaimDetail> iterator =hospitalClaimDetails.iterator();
			while (iterator.hasNext()) {
				HospitalClaimDetail hospitalClaimDetail =  iterator.next();
				
				if("3".equals(hospitalClaimDetail.getDetailOperationType()))
				{
					iterator.remove();
				}
//				else
//				{
//					hospitalClaimDetailService.insert(hospitalClaimDetail);
//				}
//				
				
			}
			
			if(hospitalClaimDetails.size()==0)
			{
				return true;
			}
			
			int startIndex=0;
			int endIndex=0;
			int batchCount=100;
			int cycleCount=hospitalClaimDetails.size()%batchCount==0?hospitalClaimDetails.size()/100:hospitalClaimDetails.size()/batchCount+1;
			final List<HospitalClaimDetail> list1=Collections.synchronizedList(new ArrayList<HospitalClaimDetail>());
			
			for(int i=0;i<cycleCount;i++)
			{
				startIndex=endIndex;
				endIndex=endIndex+batchCount;
				if(endIndex>hospitalClaimDetails.size())
				{
					endIndex=hospitalClaimDetails.size();
				}
				final List<HospitalClaimDetail> list=hospitalClaimDetails.subList(startIndex, endIndex);
				
				threadPool.execute(new Runnable() {
					@Override
					public void run() {
//						for(HospitalClaimDetail hospitalClaimDetail:list)
//						{
//							hospitalClaimDetailService.insert(hospitalClaimDetail);
//							list1.add(hospitalClaimDetail);
//						}
						try 
						{
							hospitalClaimDetailMapper.insertBathHospitalClaimDetail(list);
						} 
						catch (Exception e)
						{
							errorList.add("error");
							logger.error(e.getMessage());
						}
						finally
						{
							list1.addAll(list);
						}
						
						
					}
				});
				
//				hospitalClaimDetailService.insertBatch(hospitalClaimDetails.subList(startIndex, endIndex));
			}
			
			while(list1.size()<hospitalClaimDetails.size());
			
			
//			hospitalClaimDetailService.insertBatch(hospitalClaimDetails);
		}
		catch (Exception e) {
			logger.error(e.getMessage(),e);
			return false;
		}
		
		if(errorList.size()>0)
		{
			return false;
		}
		
		return true;
	}
	

	@Override
	public boolean delHospitalClaimInfo(String id) {
		
		try
		{
			hospitalClaimDetailService.deleteHospitalClaimDetail(id);
			
			hospitalClaimMapper.deleteHospitalClaim(id);
			
		}
		catch (Exception e) {
			logger.error(e.getMessage(),e);
			return false;
		}
		
		return true;
	}


}

package com.shuxin.commons.utils;

import com.shuxin.model.ruleengine.*;
import com.shuxin.service.impl.ruleengine.RedisCacheUtil;
import com.shuxin.service.ruleengine.IHISRequestInfoService;
import com.shuxin.service.ruleengine.IHospitalClaimDetailOptService;
import com.shuxin.service.ruleengine.IHospitalClaimOptService;
import com.shuxin.service.ruleengine.IViolationDetailService;
import oracle.jdbc.internal.OracleTypes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolUtil {

	private static ExecutorService threadPool = Executors.newFixedThreadPool(10);

	private static Logger logger = LogManager.getLogger(ThreadPoolUtil.class);

	/**
	 * 保存请求的历史记录
	 * 
	 * @param hospitalClaim
	 * @param hospitalClaimDetails
	 */
	public static void handleHospitalClaimOpt(final HospitalClaim hospitalClaim,
			final List<HospitalClaimDetail> hospitalClaimDetails) {
		final List<HospitalClaimDetail> tempList = new ArrayList<HospitalClaimDetail>();
		tempList.addAll(hospitalClaimDetails);
		threadPool.execute(new Runnable() {
			@Override
			public void run() {

				HospitalClaimOpt hospitalClaimOpt = BeanUtils.copy(hospitalClaim, HospitalClaimOpt.class);

				Date date = new Date();

				IHospitalClaimOptService hospitalClaimOptService = (IHospitalClaimOptService) SpringContextHelper
						.getBean("hospitalClaimOptServiceImpl");
				IHospitalClaimDetailOptService hospitalClaimDetailOptService = (IHospitalClaimDetailOptService) SpringContextHelper
						.getBean("hospitalClaimDetailOptServiceImpl");
				String optId = UUID.randomUUID().toString().replace("-", "");
				hospitalClaimOpt.setOptId(optId);
				hospitalClaimOpt.setOptDate(date);
				hospitalClaimOptService.insert(hospitalClaimOpt);
				for (HospitalClaimDetail hospitalClaimDetail : tempList) {
					HospitalClaimDetailOpt hospitalClaimDetailOpt = BeanUtils.copy(hospitalClaimDetail,
							HospitalClaimDetailOpt.class);
					hospitalClaimDetailOpt.setOptId(optId);
					hospitalClaimDetailOpt.setOptDate(date);
					hospitalClaimDetailOptService.insert(hospitalClaimDetailOpt);
				}
			}
		});
	}

	/**
	 * 只返回当天的违规信息
	 * 
	 * @param hospitalClaim
	 * @param violationDetails
	 */

	public static void handleViolationCurrentDay(HospitalClaim hospitalClaim, Object violationDetails) {

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			Date serviceDate = null;
			List<ViolationDetail> violationDetailList = (List<ViolationDetail>) violationDetails;
			
			Iterator<ViolationDetail> it = violationDetailList.iterator();
			while (it.hasNext()) {
				serviceDate = sdf.parse(it.next().getServiceDate());
				if (DateUtils.differentDays(serviceDate, date) != 0) {
					it.remove();
				}

			}
		} catch (ParseException e) {
			logger.error(e.getMessage());
		}

	}
	/**
	 * 
	 * @param hospitalClaim
	 * @param violationDetails
	 */
	public static void handleViolationCache(HospitalClaim hospitalClaim,Object violationDetails){
		 RedisCacheUtil<Object> redisCacheUtil=(RedisCacheUtil<Object>) SpringContextHelper.getBean("redisCacheUtil");
		 List<ViolationDetail> violationDetailList = (List<ViolationDetail>) violationDetails;
		 if(violationDetailList.size()==0){
			 return;
		 }
		 List<ViolationDetail> violationDetailListTemp=new ArrayList<ViolationDetail>();
		 violationDetailListTemp.addAll(violationDetailList);
		 //就诊流水号
		 String diaSerialCode=hospitalClaim.getDiaSerialCode();
		 List<ViolationDetail> violationDetailCacheList=redisCacheUtil.getCacheList(diaSerialCode);
		 if(violationDetailCacheList!=null){ 
		
			 	Iterator<ViolationDetail> it = violationDetailList.iterator();
			 	ViolationDetail violationDetail;
				while (it.hasNext()) {
					violationDetail=it.next();
					for(ViolationDetail violationDetailCache :violationDetailCacheList){
						if(violationDetailCache.getMessageCode().equals(violationDetail.getMessageCode())&&violationDetailCache
								.getProductCode().equals(violationDetail.getProductCode())&&
								violationDetailCache.getAgainstAmount().equals(violationDetail.getAgainstAmount())&&
								violationDetailCache.getAgainstNum().equals(violationDetail.getAgainstNum())){
							if("3004".equals(violationDetail.getMessageCode())||"3005".equals(violationDetail.getMessageCode())){
								continue;
							}
							if((!StringUtils.isEmpty(violationDetail.getDocSerialCode())&&violationDetailCache.getDocSerialCode()
									.equals(violationDetail.getDocSerialCode()))||StringUtils.isEmpty(violationDetail.getDocSerialCode())){
								
								it.remove();
								break;
							}
						}
					}
				
				}
		 }
		 redisCacheUtil.setCacheList(diaSerialCode, violationDetailListTemp);
		
	}
	
	/**
	 * 调用存储过程
	 * @param V_POINTORGID 就诊流水号
	 * @param T_TYPE  操作类型
	 */
	public static void UseProcedures(final String V_POINTORGID,final String T_TYPE){
		
		threadPool.execute(new Runnable() {
			
			@Override
			public void run() {
				
				Connection conn = JDBCUtils.getConnection();
				String sql ="{call PROC_WGMX_BRXM(?,?,?,?)}";
				CallableStatement call=null;
				try {
					call = conn.prepareCall(sql);
					call.setString(1, V_POINTORGID);
					call.setString(2, T_TYPE);
					
					call.registerOutParameter(3, OracleTypes.VARCHAR);
					call.registerOutParameter(4, OracleTypes.VARCHAR);
					
					call.execute();
					
					String message = call.getString(3);
					
					FileWriter fw =new FileWriter("D:/test_proce.txt");
					PrintWriter pw =new PrintWriter(fw);
					pw.println("test_proce:"+message);
					fw.close();
					pw.close();
					
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}finally {
					JDBCUtils.release(conn, call, null);
				}
				
				
			}
		});
		
		
	}
	

	/**
	 * 保存违规结果 ods
	 * 
	 * @param hospitalClaim
	 * @param hospitalClaimDetails
	 * @param violationDetails
	 */
	public static void handleViolationDetail(final HospitalClaim hospitalClaim,
			final List<HospitalClaimDetail> hospitalClaimDetails, final List<ViolationDetail> violationDetails) {
		final List<ViolationDetail> tempList = new ArrayList<ViolationDetail>();
		tempList.addAll(violationDetails);
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				IViolationDetailService violationDetailService = (IViolationDetailService) SpringContextHelper
						.getBean("violationDetailServiceImpl");
				violationDetailService.editViolationDetail(hospitalClaim, hospitalClaimDetails, tempList);
			}
		});
	}

	/**
	 * 保存返回给HIS结果信息 t_Violation_result
	 * 
	 * @param hospitalClaim
	 * @param result
	 */
	public static void handleViolationResult(final HospitalClaim hospitalClaim, final String result,final String resultId) {
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				Map<String, String> paramMap = new HashMap<String, String>();
				if (hospitalClaim != null) {
					paramMap.put("id", hospitalClaim.getDiaSerialCode());
				} else {
					paramMap.put("id", UUID.randomUUID().toString().replace("-", ""));
				}
				paramMap.put("result", result);
				paramMap.put("result_id", resultId);
				IViolationDetailService violationDetailService = (IViolationDetailService) SpringContextHelper
						.getBean("violationDetailServiceImpl");
				violationDetailService.addViolationResult(paramMap);
			}
		});
	}

	/**
	 * 保存HIS请求参数 t_his_request_info
	 * 
	 * @param requestInfo
	 */
	public static void handleHISRequestInfo(final String requestInfo,final String id) {
//		threadPool.execute(new Runnable() {
//			@Override
//			public void run() {
				Map<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("requestInfo", requestInfo);
				paramMap.put("id", id);
				IHISRequestInfoService hISRequestInfoService = (IHISRequestInfoService) SpringContextHelper
						.getBean("hISRequestInfoServiceImpl");
				hISRequestInfoService.saveHISRequestInfo(paramMap);
//			}
//		});
	}
	
	/**
	 * 
	 * @param diaSerialCode
	 * @param id
	 */
	
	public static void updateHis(final HospitalClaim hospitalClaim,final String id) {
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				
				Map<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("diaSerialCode", hospitalClaim.getDiaSerialCode());
				paramMap.put("patName", hospitalClaim.getPatName());
				paramMap.put("patIdCard", hospitalClaim.getPatIdCard());
				paramMap.put("id", id);
				IHISRequestInfoService hISRequestInfoService = (IHISRequestInfoService) SpringContextHelper
						.getBean("hISRequestInfoServiceImpl");
				hISRequestInfoService.updateHis(paramMap);
				
			}
		});
	
	}
	
	

	public static boolean baseCheckInputVOList(List<HospitalClaimDetail> hospitalClaimDetails,
			ReturnResult returnResult) {
		final List<Set<ConstraintViolation<HospitalClaimDetail>>> list = Collections
				.synchronizedList(new ArrayList<Set<ConstraintViolation<HospitalClaimDetail>>>());
		for (final HospitalClaimDetail hospitalClaimDetail : hospitalClaimDetails) {
			threadPool.submit(new Runnable() {
				@Override
				public void run() {
					Set<ConstraintViolation<HospitalClaimDetail>> validResult = Validation
							.buildDefaultValidatorFactory().getValidator().validate(hospitalClaimDetail);
					list.add(validResult);
				}

			});
		}
		while (list.size() < hospitalClaimDetails.size())
			;
		for (Set<ConstraintViolation<HospitalClaimDetail>> validResult : list) {
			if (null != validResult && validResult.size() > 0) {
				StringBuilder sb = new StringBuilder();
				for (Iterator<ConstraintViolation<HospitalClaimDetail>> iterator = validResult.iterator(); iterator
						.hasNext();) {
					ConstraintViolation<HospitalClaimDetail> constraintViolation = (ConstraintViolation<HospitalClaimDetail>) iterator
							.next();
					// 这里只取了字段名，如果需要其他信息可以自己处理
					sb.append(constraintViolation.getMessage()).append("、");
				}
				if (sb.lastIndexOf("、") == sb.length() - 1) {
					sb.delete(sb.length() - 1, sb.length());
				}
				RespInfo respInfo = new RespInfo();
				respInfo.setResultCode("0001");
				respInfo.setResultMsg("请检查传入的数据格式异常:" + sb.toString());
				respInfo.setResultStatus("F");
				returnResult.setRespInfo(respInfo);
				return false;
			}
		}

		return true;
	}
}

package com.shuxin.service.impl.ruleengine;

import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.shuxin.commons.datasource.DynamicDataSourceHolder;
import com.shuxin.commons.utils.PropertiesLoader;
import com.shuxin.commons.utils.StringUtils;
import com.shuxin.commons.utils.ThreadPoolUtil;
import com.shuxin.mapper.ruleengine.DataRecoverMapper;
import com.shuxin.mapper.ruleengine.HospitalClaimDetailMapper;
import com.shuxin.mapper.ruleengine.HospitalClaimMapper;
import com.shuxin.model.ruleengine.HospitalClaim;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.model.ruleengine.RespInfo;
import com.shuxin.model.ruleengine.ReturnResult;
import com.shuxin.model.ruleengine.ViolationDetail;
import com.shuxin.service.ruleengine.IExamineService;
import com.shuxin.service.ruleengine.IHospitalClaimDetailService;
import com.shuxin.service.ruleengine.IHospitalClaimService;
import com.shuxin.service.ruleengine.IRuleEngineRecovService;
import com.shuxin.service.ruleengine.IViolationDetailService;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

/**
 * 
 * @author shuxin
 *
 */
@Service
public class RuleEngineRecovServiceImpl implements IRuleEngineRecovService {

	// 规则审核service
	@Autowired
	private IExamineService examineService;
	// 就诊信息service
	@Autowired
	private IHospitalClaimService hospitalClaimService;
	// 明细service
	@Autowired
	private IHospitalClaimDetailService hospitalClaimDetailService;
	@Autowired
	private DataRecoverMapper dataRecoverMapper;
	// @Autowired
	// private HospitalClaimMapper hospitalClaimMapper;
	// @Autowired
	// private HospitalClaimDetailMapper hospitalClaimDetailMapper;

	@Autowired
	private IViolationDetailService violationDetailService;

	protected Logger logger = LogManager.getLogger(getClass());

	PropertiesLoader propertiesLoader;

	public RuleEngineRecovServiceImpl() {
		propertiesLoader = new PropertiesLoader("certID.properties");
		JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[] { "yyyy-MM-dd HH:mm:ss" }));
	}

	/**
	 * 审核
	 * 
	 * @param
	 * @return
	 */
	@Override
	public void examine() {
		// System.out.println("jsonData========="+jsonData);
		// logger.error("jsonData========="+jsonData);
		//切换数据源 从hisdb拿数据
//		DynamicDataSourceHolder.setDataSource("dataSource_his");
		List<Object> jsonDataList = dataRecoverMapper.selectFromTimeOut();
//		DynamicDataSourceHolder.clearDataSource();
		if (jsonDataList.size() == 0) {
			return;
		}
		String jsonData = "";
		for (Object obj : jsonDataList) {
			Blob blob = (Blob)obj;
			try {
//				jsonData=clob.getSubString((long)1,(int)clob.length()); 
				jsonData = new String(blob.getBytes((long) 1, (int) blob.length()));
			} catch (SQLException e) {

				logger.error(e.getMessage(), e);
			}
			String uuid = UUID.randomUUID().toString().replace("-", "");
			ThreadPoolUtil.handleHISRequestInfo(jsonData,uuid);
			ReturnResult returnResult = new ReturnResult();
			RespInfo respInfo = new RespInfo();

			JSONObject jsonObject = JSONObject.fromObject(jsonData);

			// 验证上传的json数据
			if (!checkJsonData(jsonObject, returnResult)) {
				ThreadPoolUtil.handleViolationResult(null, JSONObject.fromObject(returnResult).toString(),uuid);
			}

			// JSONObject body = (JSONObject)jsonObject.get("body");
			JSONObject reqData = (JSONObject) jsonObject.get("reqData");
			// 验证就诊信息数据格式
			if (!checkClaimFormatByJsonData(reqData, returnResult)) {
				ThreadPoolUtil.handleViolationResult(null, JSONObject.fromObject(returnResult).toString(),uuid);
			}
			// 验证明细列表数据格式
			// if(!checkClaimDetailListFormatByJsonData(reqData,returnResult)){
			// return JSONObject.fromObject(returnResult).toString();
			// }
			// 就诊信息
			HospitalClaim hospitalClaim = getHospitalClaimByJsonData(reqData);
			// 明细信息
			List<HospitalClaimDetail> hospitalClaimDetails = getHospitalClaimDetailListByJsonData(reqData);
			// 就医方式
			String medTreatmentMode = hospitalClaim.getMedTreatmentMode();
			// 住院状态
			String liveHospStatus = hospitalClaim.getLiveHospStatus();
			
			ThreadPoolUtil.updateHis(hospitalClaim, uuid);

			List<ViolationDetail> respDatas = new ArrayList<ViolationDetail>();

			boolean opreationResult = false;
			
			//传给存储过程的操作类型
			String t_type="";
			
			if("3".equals(hospitalClaim.getOperationType())){
				t_type = "2";
			}else {
				t_type = "1";
			}

			ThreadPoolUtil.handleHospitalClaimOpt(hospitalClaim, hospitalClaimDetails);

			// 如果删除主单信息，不需要审核直接返回结果
			if ("3".equals(hospitalClaim.getOperationType())) {
				violationDetailService.deleteViolationDetail(hospitalClaim.getId());
				opreationResult = hospitalClaimService.delHospitalClaimInfo(hospitalClaim.getId());
				if (opreationResult) {
					// ThreadPoolUtil.handleHospitalClaimOpt(hospitalClaim,
					// hospitalClaimDetails);
					respInfo.setResultCode("0000");
					respInfo.setResultMsg("正常");
					respInfo.setResultStatus("S");
				} else {
					respInfo.setResultCode("0002");
					respInfo.setResultMsg("业务数据逻辑处理异常");
					respInfo.setResultStatus("F");
				}
				returnResult.setRespInfo(respInfo);
				returnResult.setRespData(respDatas);
				ThreadPoolUtil.handleViolationResult(hospitalClaim, JSONObject.fromObject(returnResult).toString(),uuid);
			} else {
				opreationResult = hospitalClaimService.handleHospitalClaimInfo(hospitalClaim, hospitalClaimDetails);
			}
			// 保存单据信息
			// boolean insert =
			// hospitalClaimService.insertOrUpdate(hospitalClaim);
			// //保存明细信息
			// boolean insertBatch =
			// hospitalClaimDetailService.insertOrUpdateBatch(hospitalClaimDetails);

			// 判断就医方式是门诊
			if (medTreatmentMode.equals("11") || 
					medTreatmentMode.equals("13") || 
					medTreatmentMode.equals("15") || 
					medTreatmentMode.equals("51") || 
					medTreatmentMode.equals("71")) {

				if (opreationResult) {
					List<ViolationDetail> violationDetails = examineService.examineOutpatient(hospitalClaim,
							hospitalClaimDetails);
					for (ViolationDetail violationDetail : violationDetails) {
						respDatas.add(violationDetail);
					}
					respInfo.setResultCode("0000");
					respInfo.setResultMsg("正常");
					respInfo.setResultStatus("S");
				} else {
					respInfo.setResultCode("0002");
					respInfo.setResultMsg("业务数据逻辑处理异常");
					respInfo.setResultStatus("F");
				}
				// 判断就医方式是住院
			} else if (medTreatmentMode.equals("21") || 
					medTreatmentMode.equals("22") || 
					medTreatmentMode.equals("25") || 
					medTreatmentMode.equals("52") || 
					medTreatmentMode.equals("72")) {
				// 保存单据信息
				// boolean insert = true;
				// boolean insert = hospitalClaimService.insert(hospitalClaim);
				// 保存明细信息
				// boolean insertBatch = true;
				// boolean insertBatch =
				// hospitalClaimDetailService.insertBatch(hospitalClaimDetails);
				if (opreationResult) {
					List<ViolationDetail> violationDetails = examineService.examineHospitalization(hospitalClaim,
							hospitalClaimDetails);
					for (ViolationDetail violationDetail : violationDetails) {
						respDatas.add(violationDetail);
					}
					respInfo.setResultCode("0000");
					respInfo.setResultMsg("正常");
					respInfo.setResultStatus("S");
				} else {
					respInfo.setResultCode("0002");
					respInfo.setResultMsg("业务数据逻辑处理异常");
					respInfo.setResultStatus("F");
				}
			} else {
				respInfo.setResultCode("0002");
				respInfo.setResultMsg("业务数据逻辑处理异常");
				respInfo.setResultStatus("F");
			}
			returnResult.setRespInfo(respInfo);
			returnResult.setRespData(respDatas);
			ThreadPoolUtil.handleViolationDetail(hospitalClaim, hospitalClaimDetails, respDatas);

			String result = JSONObject.fromObject(returnResult).toString();
			ThreadPoolUtil.handleViolationResult(hospitalClaim, result,uuid);
			
			ThreadPoolUtil.UseProcedures(hospitalClaim.getDiaSerialCode(), t_type);
			// System.out.println("result================"+result);
			// logger.error("result================"+result);
			// return result;
			// **/
			// return "ssss";

		}

	}

	/**
	 * 检查上传的json 数据
	 * 
	 * @param jsonData
	 * @param hospitalClaim
	 * @param hospitalClaimDetails
	 * @param returnResult
	 * @return
	 */
	private boolean checkJsonData(JSONObject jsonObject, ReturnResult returnResult) {
		RespInfo respInfo = new RespInfo();
		if (null == jsonObject || jsonObject.isEmpty()) {
			respInfo.setResultCode("0001");
			respInfo.setResultMsg("Json数据为空！");
			respInfo.setResultStatus("F");
			returnResult.setRespInfo(respInfo);
			return false;
		}
		// 接收传过来的json数据对json数据进行解析
		// JSONObject jsonObject = JSONObject.fromObject(jsonData);

		// JSONObject header = (JSONObject) jsonObject.get("header");
		// if(null == header || header.isEmpty()){
		// respInfo.setResultCode("0001");
		// respInfo.setResultMsg("header数据为空！");
		// respInfo.setResultStatus("F");
		// returnResult.setRespInfo(respInfo);
		// return false;
		// }
		// JSONObject body = (JSONObject)jsonObject.get("body");
		// if(null == body || body.isEmpty()){
		// respInfo.setResultCode("0001");
		// respInfo.setResultMsg("body数据为空！");
		// respInfo.setResultStatus("F");
		// returnResult.setRespInfo(respInfo);
		// return false;
		// }
		JSONObject reqData = (JSONObject) jsonObject.get("reqData");
		if (null == reqData || reqData.isEmpty()) {
			respInfo.setResultCode("0001");
			respInfo.setResultMsg("reqData数据为空！");
			respInfo.setResultStatus("F");
			returnResult.setRespInfo(respInfo);
			return false;
		}
		// 验证认证ID
		String certID = (String) reqData.get("certId");
		if (!checkCertId(certID, returnResult)) {
			return false;
		}
		return true;
	}

	public boolean checkCertId(String certId, ReturnResult returnResult) {
		String setCertID = propertiesLoader.getProperty("certID");
		if (!certId.equals(setCertID)) {
			RespInfo respInfo = new RespInfo();
			respInfo.setResultCode("0002");
			respInfo.setResultMsg("认证ID号验证失败！");
			respInfo.setResultStatus("F");
			returnResult.setRespInfo(respInfo);
			return false;
		}
		return true;
	}

	/**
	 * 获取json数据中的就诊信息
	 * 
	 * @param jsonData
	 * @return
	 */
	private HospitalClaim getHospitalClaimByJsonData(JSONObject reqData) {
		JSONObject hospitalClaimJson = (JSONObject) reqData.get("hospitalClaim");
		HospitalClaim hospitalClaim = (HospitalClaim) JSONObject.toBean(hospitalClaimJson, HospitalClaim.class);
		return hospitalClaim;
	}

	/**
	 * 获取json数据中的明细列表
	 * 
	 * @param jsonData
	 * @return
	 */
	private List<HospitalClaimDetail> getHospitalClaimDetailListByJsonData(JSONObject reqData) {
		JSONArray hospitalClaimDetailsJson = (JSONArray) reqData.get("hospitalClaimDetails");
		List<HospitalClaimDetail> hospitalClaimDetailList = (List<HospitalClaimDetail>) JSONArray
				.toCollection(hospitalClaimDetailsJson, HospitalClaimDetail.class);
		return hospitalClaimDetailList;
	}

	/**
	 * 检查json数据中的就诊信息格式
	 * 
	 * @param reqData
	 * @return
	 */
	private boolean checkClaimFormatByJsonData(JSONObject reqData, ReturnResult returnResult) {
		RespInfo respInfo = new RespInfo();
		// 获取json中的就诊数据
		JSONObject hospitalClaimJson = (JSONObject) reqData.get("hospitalClaim");
		if (null == hospitalClaimJson || hospitalClaimJson.isEmpty()) {
			respInfo.setResultCode("0001");
			respInfo.setResultMsg("hospitalClaim数据为空！");
			respInfo.setResultStatus("F");
			returnResult.setRespInfo(respInfo);
			return false;
		}

		// String outHospDate = hospitalClaimJson.getString("outHospDate");
		// hospitalClaimJson.put("outHospDate", outHospDate);
		// 出院状态必须验证出院时间
		if ("1".equals(hospitalClaimJson.getString("liveHospStatus"))
				|| !StringUtils.isEmpty(hospitalClaimJson.getString("outHospDate"))) {
			if (!isValidDate("outHospDate", hospitalClaimJson.getString("outHospDate"), returnResult)) {
				return false;
			}
		} else {
			hospitalClaimJson.remove("outHospDate");
		}

		// String settlementDate =
		// hospitalClaimJson.getString("settlementDate")+" 00:00:00";
		// hospitalClaimJson.put("settlementDate", settlementDate);
		if (StringUtils.isEmpty(hospitalClaimJson.getString("settlementDate"))) {
			hospitalClaimJson.remove("settlementDate");
		} else {
			if (!isValidDate("settlementDate", hospitalClaimJson.getString("settlementDate"), returnResult)) {
				return false;
			}
		}

		// 如果是删除主单信息只需要验证id
		if ("3".equals(hospitalClaimJson.getString("operationType"))) {
			if (StringUtils.isEmpty(hospitalClaimJson.getString("id"))) {
				respInfo.setResultCode("0002");
				respInfo.setResultMsg("id为空！");
				respInfo.setResultStatus("F");
				returnResult.setRespInfo(respInfo);
				return false;
			}
			return true;
		}

		// 验证就诊信息里面的日期格式
		// String patBirthday = hospitalClaimJson.getString("patBirthday")+"
		// 00:00:00";
		// hospitalClaimJson.put("patBirthday", patBirthday);
		if (!isValidDate("patBirthday", hospitalClaimJson.getString("patBirthday"), returnResult)) {
			return false;
		}
		// String inHospDate = hospitalClaimJson.getString("inHospDate");
		// hospitalClaimJson.put("inHospDate", inHospDate);
		if (!isValidDate("inHospDate", hospitalClaimJson.getString("inHospDate"), returnResult)) {
			return false;
		}

		HospitalClaim hospitalClaim = (HospitalClaim) JSONObject.toBean(hospitalClaimJson, HospitalClaim.class);

		return true;
	}

	/**
	 * 验证字符串是否是一个合法的日期格式
	 * 
	 * @param str
	 * @return
	 */
	private boolean isValidDate(String str, String strVal, ReturnResult returnResult) {
		boolean convertSuccess = true;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			// 设置lenient为false.
			// 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
			format.setLenient(false);
			format.parse(strVal);
		} catch (ParseException e) {
			// e.printStackTrace();
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			RespInfo respInfo = new RespInfo();
			respInfo.setResultCode("0001");
			respInfo.setResultMsg(str + "值无效");
			respInfo.setResultStatus("F");
			returnResult.setRespInfo(respInfo);
			convertSuccess = false;
		}
		return convertSuccess;
	}

}

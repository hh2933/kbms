package com.shuxin.controller.ruleengine;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shuxin.commons.base.BaseController;
import com.shuxin.commons.utils.PropertiesLoader;
import com.shuxin.commons.utils.StringUtils;
import com.shuxin.commons.utils.ThreadPoolUtil;
import com.shuxin.model.ruleengine.HospitalClaim;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.model.ruleengine.RespInfo;
import com.shuxin.model.ruleengine.ReturnResult;
import com.shuxin.model.ruleengine.ViolationDetail;
import com.shuxin.service.ruleengine.IExamineService;
import com.shuxin.service.ruleengine.IHospitalClaimDetailService;
import com.shuxin.service.ruleengine.IHospitalClaimService;
import com.shuxin.service.ruleengine.IViolationDetailService;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;
/**
 * 规则Controller
 * @author shuxin
 *
 */
@Controller
@RequestMapping("/request")
public class RuleEngineController extends BaseController {
	
	//规则审核service
	@Autowired
	private IExamineService examineService;
	//就诊信息service
	@Autowired
	private IHospitalClaimService hospitalClaimService;
	//明细service
	@Autowired
	private IHospitalClaimDetailService hospitalClaimDetailService;
	
	@Autowired
	private IViolationDetailService violationDetailService;
	
	PropertiesLoader propertiesLoader;
	
	protected Logger logger = LogManager.getLogger(getClass());
	
	public RuleEngineController(){
		super();
		propertiesLoader = new PropertiesLoader("certID.properties");
		JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[] { "yyyy-MM-dd HH:mm:ss" }));
	}
	
	/**
	 * 审核
	 * @param jsonData
	 * @return
	 */
	@PostMapping("/examine")
    @ResponseBody
	public String examine(@RequestBody String jsonData){
		//System.out.println("jsonData========="+jsonData);
		//logger.error("jsonData========="+jsonData);
		String uuid = UUID.randomUUID().toString().replace("-", "");
		ThreadPoolUtil.handleHISRequestInfo(jsonData,uuid);
		ReturnResult returnResult = new ReturnResult();
		RespInfo respInfo = new RespInfo();
		
		JSONObject jsonObject = JSONObject.fromObject(jsonData);
		
		//验证上传的json数据
		if(!checkJsonData(jsonObject,returnResult)){
			ThreadPoolUtil.handleViolationResult(null, JSONObject.fromObject(returnResult).toString(),uuid);
			return JSONObject.fromObject(returnResult).toString();
		}
	
//		JSONObject body = (JSONObject)jsonObject.get("body");
		JSONObject reqData = (JSONObject)jsonObject.get("reqData");
		//验证就诊信息数据格式
		if(!checkClaimFormatByJsonData(reqData,returnResult)){
			ThreadPoolUtil.handleViolationResult(null, JSONObject.fromObject(returnResult).toString(),uuid);
			return JSONObject.fromObject(returnResult).toString();
		}
		//验证明细列表数据格式
//		if(!checkClaimDetailListFormatByJsonData(reqData,returnResult)){
//			return JSONObject.fromObject(returnResult).toString();
//		}
		//就诊信息
		HospitalClaim hospitalClaim = getHospitalClaimByJsonData(reqData);
		//明细信息
		List<HospitalClaimDetail> hospitalClaimDetails = getHospitalClaimDetailListByJsonData(reqData);
		//就医方式
		String medTreatmentMode = hospitalClaim.getMedTreatmentMode();
		//住院状态
		String liveHospStatus=hospitalClaim.getLiveHospStatus();
		
		ThreadPoolUtil.updateHis(hospitalClaim, uuid);
		
		List<ViolationDetail> respDatas = new ArrayList<ViolationDetail>();
		//传给存储过程的操作类型
		String t_type="";
		
		boolean opreationResult = false;
		
		if("3".equals(hospitalClaim.getOperationType())){	//(1、新增2、修改3、删除)
			t_type = "2";
		}else {
			t_type = "1";
		}
		
		ThreadPoolUtil.handleHospitalClaimOpt(hospitalClaim, hospitalClaimDetails);
		
		//如果删除主单信息，不需要审核直接返回结果
		if("3".equals(hospitalClaim.getOperationType()))
		{
			violationDetailService.deleteViolationDetail(hospitalClaim.getId());
			opreationResult=hospitalClaimService.delHospitalClaimInfo(hospitalClaim.getId());
			if(opreationResult)
			{
				//ThreadPoolUtil.handleHospitalClaimOpt(hospitalClaim, hospitalClaimDetails);
				respInfo.setResultCode("0000");
				respInfo.setResultMsg("正常");
				respInfo.setResultStatus("S");
			}
			else
			{
				respInfo.setResultCode("0002");
				respInfo.setResultMsg("业务数据逻辑处理异常");
				respInfo.setResultStatus("F");
			}
			returnResult.setRespInfo(respInfo);
			returnResult.setRespData(respDatas);
			ThreadPoolUtil.handleViolationResult(hospitalClaim, JSONObject.fromObject(returnResult).toString(),uuid);
			return JSONObject.fromObject(returnResult).toString();
		}
		else
		{
			opreationResult=hospitalClaimService.handleHospitalClaimInfo(hospitalClaim, hospitalClaimDetails);
		}
		//保存单据信息
//		boolean insert = hospitalClaimService.insertOrUpdate(hospitalClaim);
//		//保存明细信息
//		boolean insertBatch = hospitalClaimDetailService.insertOrUpdateBatch(hospitalClaimDetails);
		
		
		//判断就医方式是门诊
		if(medTreatmentMode.equals("11") || 
			medTreatmentMode.equals("13") || 
			medTreatmentMode.equals("15") || 
			medTreatmentMode.equals("51") || 
			medTreatmentMode.equals("71")){
			
			if(opreationResult){
				List<ViolationDetail> violationDetails = examineService.examineOutpatient(hospitalClaim,hospitalClaimDetails);
				for(ViolationDetail violationDetail : violationDetails){
					respDatas.add(violationDetail);
				}
				respInfo.setResultCode("0000");
				respInfo.setResultMsg("正常");
				respInfo.setResultStatus("S");
			}else{
				respInfo.setResultCode("0002");
				respInfo.setResultMsg("业务数据逻辑处理异常");
				respInfo.setResultStatus("F");
			}
		//判断就医方式是住院
		}else if(medTreatmentMode.equals("21") || 
				medTreatmentMode.equals("22") || 
				medTreatmentMode.equals("25") || 
				medTreatmentMode.equals("52") || 
				medTreatmentMode.equals("72")){ 
			//保存单据信息
//			boolean insert = true;
			//boolean insert = hospitalClaimService.insert(hospitalClaim);
			//保存明细信息
//			boolean insertBatch = true;
			//boolean insertBatch = hospitalClaimDetailService.insertBatch(hospitalClaimDetails);
			if(opreationResult){
				List<ViolationDetail> violationDetails = examineService.examineHospitalization(hospitalClaim,hospitalClaimDetails);
				for(ViolationDetail violationDetail : violationDetails){
					respDatas.add(violationDetail);
				}
				respInfo.setResultCode("0000");
				respInfo.setResultMsg("正常");
				respInfo.setResultStatus("S");
			}else{
				respInfo.setResultCode("0002");
				respInfo.setResultMsg("业务数据逻辑处理异常");
				respInfo.setResultStatus("F");
			}
		}else{
			respInfo.setResultCode("0002");
			respInfo.setResultMsg("业务数据逻辑处理异常");
			respInfo.setResultStatus("F");
		}
		returnResult.setRespInfo(respInfo);
		returnResult.setRespData(respDatas);
		ThreadPoolUtil.handleViolationDetail(hospitalClaim, hospitalClaimDetails, respDatas);
		
		//住院
		if(medTreatmentMode.equals("21") || 
		   medTreatmentMode.equals("22") || 
		   medTreatmentMode.equals("25") || 
		   medTreatmentMode.equals("52") || 
		   medTreatmentMode.equals("72")){
			//非出院状态
			if(!"1".equals(liveHospStatus)){
				ThreadPoolUtil.handleViolationCurrentDay(hospitalClaim, returnResult.getRespData());
			}
		}
		//非出院状态
		if(!"1".equals(liveHospStatus)){
				ThreadPoolUtil.handleViolationCache(hospitalClaim,returnResult.getRespData());
		}
		
		String result = JSONObject.fromObject(returnResult).toString();
		ThreadPoolUtil.handleViolationResult(hospitalClaim, result,uuid);
		
		ThreadPoolUtil.UseProcedures(hospitalClaim.getDiaSerialCode(), t_type);
		//System.out.println("result================"+result);
		//logger.error("result================"+result);
		return result;
		//**/
		//return "ssss";
	}
	
	
	/**
	 * 删除主单信息
	 * @param jsonData
	 * @return
	 */
//	@PostMapping("/deleteClaim")
//    @ResponseBody
	public String deleteClaim(@RequestBody String jsonData){
		ReturnResult returnResult = new ReturnResult();
		RespInfo respInfo = new RespInfo();
		JSONObject jsonObject = JSONObject.fromObject(jsonData);
		//验证上传的json数据
		if(!checkJsonData(jsonObject,returnResult)){
			return JSONObject.fromObject(returnResult).toString();
		}
	
		JSONObject body = (JSONObject)jsonObject.get("body");
		JSONObject reqData = (JSONObject)body.get("reqData");
		String claimId = reqData.getString("claimId");
		boolean deleteById = hospitalClaimService.deleteById(claimId);
		if(deleteById){
			respInfo.setResultCode("0000");
			respInfo.setResultMsg("正常");
			respInfo.setResultStatus("S");
		}else{
			respInfo.setResultCode("0004");
			respInfo.setResultMsg("删除主单数据异常");
			respInfo.setResultStatus("F");
		}
		returnResult.setRespInfo(respInfo);
		return JSONObject.fromObject(returnResult).toString();
	}
	
	/**
	 * 修改主单信息
	 * @param jsonData
	 * @return
	 */
//	@PostMapping("/editClaim")
//    @ResponseBody
	public String editClaim(@RequestBody String jsonData){
		ReturnResult returnResult = new ReturnResult();
		RespInfo respInfo = new RespInfo();
		JSONObject jsonObject = JSONObject.fromObject(jsonData);
		//验证上传的json数据
		if(!checkJsonData(jsonObject,returnResult)){
			return JSONObject.fromObject(returnResult).toString();
		}
	
		JSONObject body = (JSONObject)jsonObject.get("body");
		JSONObject reqData = (JSONObject)body.get("reqData");
		
		//验证就诊信息数据格式
		if(!checkClaimFormatByJsonData(reqData,returnResult)){
			return JSONObject.fromObject(returnResult).toString();
		}
		//就诊信息
		HospitalClaim hospitalClaim = getHospitalClaimByJsonData(reqData);
		boolean updateById = hospitalClaimService.updateById(hospitalClaim);
		if(updateById){
			respInfo.setResultCode("0000");
			respInfo.setResultMsg("正常");
			respInfo.setResultStatus("S");
		}else{
			respInfo.setResultCode("0004");
			respInfo.setResultMsg("修改主单数据异常");
			respInfo.setResultStatus("F");
		}
		returnResult.setRespInfo(respInfo);
		return JSONObject.fromObject(returnResult).toString();
	}
	
	/**
	 * 删除明细信息
	 * @param jsonData
	 * @return
	 */
//	@PostMapping("/deleteDetail")
//    @ResponseBody
	public String deleteDetail(@RequestBody String jsonData){
		ReturnResult returnResult = new ReturnResult();
		RespInfo respInfo = new RespInfo();
		JSONObject jsonObject = JSONObject.fromObject(jsonData);
		//验证上传的json数据
		if(!checkJsonData(jsonObject,returnResult)){
			return JSONObject.fromObject(returnResult).toString();
		}
	
		JSONObject body = (JSONObject)jsonObject.get("body");
		JSONObject reqData = (JSONObject)body.get("reqData");
		String detailId = reqData.getString("detailId");
		boolean deleteById = hospitalClaimDetailService.deleteById(detailId);
		if(deleteById){
			respInfo.setResultCode("0000");
			respInfo.setResultMsg("正常");
			respInfo.setResultStatus("S");
		}else{
			respInfo.setResultCode("0004");
			respInfo.setResultMsg("删除明细数据异常");
			respInfo.setResultStatus("F");
		}
		returnResult.setRespInfo(respInfo);
		return JSONObject.fromObject(returnResult).toString();
	}
	
	/**
	 * 修改明细信息
	 * @param jsonData
	 * @return
	 */
//	@PostMapping("/editDetail")
//    @ResponseBody
	public String editDetail(@RequestBody String jsonData){
		ReturnResult returnResult = new ReturnResult();
		RespInfo respInfo = new RespInfo();
		JSONObject jsonObject = JSONObject.fromObject(jsonData);
		//验证上传的json数据
		if(!checkJsonData(jsonObject,returnResult)){
			return JSONObject.fromObject(returnResult).toString();
		}
	
		JSONObject body = (JSONObject)jsonObject.get("body");
		JSONObject reqData = (JSONObject)body.get("reqData");
		
		if(!checkClaimDetailFormatByJsonData(reqData,returnResult)){
			return JSONObject.fromObject(returnResult).toString();
		}
		HospitalClaimDetail hospitalClaimDetail = getHospitalClaimDetailByJsonData(reqData);
		boolean updateById = hospitalClaimDetailService.updateById(hospitalClaimDetail);
		if(updateById){
			respInfo.setResultCode("0000");
			respInfo.setResultMsg("正常");
			respInfo.setResultStatus("S");
		}else{
			respInfo.setResultCode("0004");
			respInfo.setResultMsg("修改明细数据异常");
			respInfo.setResultStatus("F");
		}
		returnResult.setRespInfo(respInfo);
		return JSONObject.fromObject(returnResult).toString();
	}
	
	/**
	 * 获取json数据中的就诊信息
	 * @param jsonData
	 * @return
	 */
	private HospitalClaim getHospitalClaimByJsonData(JSONObject reqData){
		JSONObject hospitalClaimJson = (JSONObject)reqData.get("hospitalClaim");
		HospitalClaim hospitalClaim = (HospitalClaim)JSONObject.toBean(hospitalClaimJson,HospitalClaim.class);
		return hospitalClaim;
	}
	
	/**
	 * 获取json数据中的明细列表
	 * @param jsonData
	 * @return
	 */
	private List<HospitalClaimDetail> getHospitalClaimDetailListByJsonData(JSONObject reqData){
		JSONArray hospitalClaimDetailsJson = (JSONArray)reqData.get("hospitalClaimDetails");
		List<HospitalClaimDetail> hospitalClaimDetailList = (List<HospitalClaimDetail>) JSONArray.toCollection(hospitalClaimDetailsJson,HospitalClaimDetail.class);
		return hospitalClaimDetailList;
	}
	
	/**
	 * 获取json数据中的明细数据
	 * @param jsonData
	 * @return
	 */
	private HospitalClaimDetail getHospitalClaimDetailByJsonData(JSONObject reqData){
		JSONObject hospitalClaimDetailJson = (JSONObject)reqData.get("hospitalClaimDetail");
		HospitalClaimDetail hospitalClaimDetail = (HospitalClaimDetail) JSONObject.toBean(hospitalClaimDetailJson,HospitalClaimDetail.class);
		return hospitalClaimDetail;
	}
	
	/**
	 * 检查上传的json 数据
	 * @param jsonData
	 * @param hospitalClaim
	 * @param hospitalClaimDetails
	 * @param returnResult
	 * @return
	 */
	private boolean checkJsonData(JSONObject jsonObject, ReturnResult returnResult){
		RespInfo respInfo = new RespInfo();
		if(null == jsonObject || jsonObject.isEmpty()){
			respInfo.setResultCode("0001");
			respInfo.setResultMsg("Json数据为空！");
			respInfo.setResultStatus("F");
			returnResult.setRespInfo(respInfo);
			return false;
		}
		//接收传过来的json数据对json数据进行解析
//		JSONObject jsonObject = JSONObject.fromObject(jsonData);
		
//		JSONObject header = (JSONObject) jsonObject.get("header");
//		if(null == header || header.isEmpty()){
//			respInfo.setResultCode("0001");
//			respInfo.setResultMsg("header数据为空！");
//			respInfo.setResultStatus("F");
//			returnResult.setRespInfo(respInfo);
//			return false;
//		}
//		JSONObject body = (JSONObject)jsonObject.get("body");
//		if(null == body || body.isEmpty()){
//			respInfo.setResultCode("0001");
//			respInfo.setResultMsg("body数据为空！");
//			respInfo.setResultStatus("F");
//			returnResult.setRespInfo(respInfo);
//			return false;
//		}
		JSONObject reqData = (JSONObject)jsonObject.get("reqData");
		if(null == reqData || reqData.isEmpty()){
			respInfo.setResultCode("0001");
			respInfo.setResultMsg("reqData数据为空！");
			respInfo.setResultStatus("F");
			returnResult.setRespInfo(respInfo);
			return false;
		}
		//验证认证ID
		String certID = (String) reqData.get("certId");
		if(!checkCertId(certID,returnResult)){
			return false; 
		}
		return true;
	}
	
	/**
	 * 检查json数据中的就诊信息格式
	 * @param reqData
	 * @return
	 */
	private boolean checkClaimFormatByJsonData(JSONObject reqData, ReturnResult returnResult){
		RespInfo respInfo = new RespInfo();
		//获取json中的就诊数据
		JSONObject hospitalClaimJson = (JSONObject)reqData.get("hospitalClaim");
		if(null == hospitalClaimJson || hospitalClaimJson.isEmpty()){
			respInfo.setResultCode("0001");
			respInfo.setResultMsg("hospitalClaim数据为空！");
			respInfo.setResultStatus("F");
			returnResult.setRespInfo(respInfo);
			return false;
		}
		
//		String outHospDate = hospitalClaimJson.getString("outHospDate");
//		hospitalClaimJson.put("outHospDate", outHospDate);
		//出院状态必须验证出院时间
		if("1".equals(hospitalClaimJson.getString("liveHospStatus"))
				|| !StringUtils.isEmpty(hospitalClaimJson.getString("outHospDate")))
		{
			if(!isValidDate("outHospDate",hospitalClaimJson.getString("outHospDate"),returnResult)){
				return false;
			}
		}
		else
		{
			hospitalClaimJson.remove("outHospDate");
		}
		
//		String settlementDate = hospitalClaimJson.getString("settlementDate")+" 00:00:00";
//		hospitalClaimJson.put("settlementDate", settlementDate);
		if(StringUtils.isEmpty(hospitalClaimJson.getString("settlementDate")))
		{
			hospitalClaimJson.remove("settlementDate");
		}
		else
		{
			if(!isValidDate("settlementDate",hospitalClaimJson.getString("settlementDate"),returnResult)){
				return false;
			}
		}
		
		//如果是删除主单信息只需要验证id
		if("3".equals(hospitalClaimJson.getString("operationType")))
		{
			if(StringUtils.isEmpty(hospitalClaimJson.getString("id")))
			{
				respInfo.setResultCode("0002");
				respInfo.setResultMsg("id为空！");
				respInfo.setResultStatus("F");
				returnResult.setRespInfo(respInfo);
				return false;
			}
			return true;
		}
		
		//验证就诊信息里面的日期格式
//		String patBirthday = hospitalClaimJson.getString("patBirthday")+" 00:00:00";
//		hospitalClaimJson.put("patBirthday", patBirthday);
		if(!isValidDate("patBirthday",hospitalClaimJson.getString("patBirthday"),returnResult)){
			return false;
		}
//		String inHospDate = hospitalClaimJson.getString("inHospDate");
//		hospitalClaimJson.put("inHospDate", inHospDate);
		if(!isValidDate("inHospDate",hospitalClaimJson.getString("inHospDate"),returnResult)){
			return false;
		}

		
		

		HospitalClaim hospitalClaim = (HospitalClaim)JSONObject.toBean(hospitalClaimJson,HospitalClaim.class);
		//验证传入的就诊信息数据
		if(!baseCheckInputVO(hospitalClaim,returnResult)){
			return false;
		}
		return true;
	}
	
	/**
	 * 检查json中的明细列表数据格式
	 * @param reqData
	 * @param returnResult
	 * @return
	 */
	private boolean checkClaimDetailListFormatByJsonData(JSONObject reqData, ReturnResult returnResult){
		RespInfo respInfo = new RespInfo();
		JSONObject hospitalClaimJson = (JSONObject)reqData.get("hospitalClaim");
		//如果是删除主单信息不需要验证明细信息
		if("3".equals(hospitalClaimJson.getString("operationType")))
		{
			return true;
		}
		
		//获取json数据中的明细信息
		JSONArray claimDetailListJson = (JSONArray)reqData.get("hospitalClaimDetails");
		if(null == claimDetailListJson || claimDetailListJson.isEmpty()){
//			respInfo.setResultCode("0001");
//			respInfo.setResultMsg("hospitalClaimDetails数据为空！");
//			respInfo.setResultStatus("F");
//			returnResult.setRespInfo(respInfo);
			return true;
		}		
		
		//验证单据明细信息中的日期格式
		for (int i = 0; i < claimDetailListJson.size(); i++) {
			JSONObject object = (JSONObject)claimDetailListJson.get(i);
//			String serviceDate = object.getString("serviceDate")+" 00:00:00";
//			object.put("serviceDate", serviceDate);
			if(!isValidDate("serviceDate",object.getString("serviceDate"),returnResult)){
				return false;
			}
		}
		List<HospitalClaimDetail> hospitalClaimDetails = (List<HospitalClaimDetail>) JSONArray.toCollection(claimDetailListJson,HospitalClaimDetail.class);
		//验证传入的明细信息数据		
//		for(HospitalClaimDetail hospitalClaimDetail : hospitalClaimDetails){
//			if(!baseCheckInputVO(hospitalClaimDetail,returnResult)){
//				return false;
//			}
//		}
		return ThreadPoolUtil.baseCheckInputVOList(hospitalClaimDetails, returnResult);
		//return true;
	}
	
	
	/**
	 * 检查json中的明细数据
	 * @param reqData
	 * @param returnResult
	 * @return
	 */
	private boolean checkClaimDetailFormatByJsonData(JSONObject reqData, ReturnResult returnResult){
		RespInfo respInfo = new RespInfo();
		//取明细数据
		JSONObject claimDetailJson = (JSONObject)reqData.get("hospitalClaimDetail");
		if(null == claimDetailJson || claimDetailJson.isEmpty()){
			respInfo.setResultCode("0001");
			respInfo.setResultMsg("hospitalClaimDetail数据为空！");
			respInfo.setResultStatus("F");
			returnResult.setRespInfo(respInfo);
			return false;
		}
		String serviceDate = claimDetailJson.getString("serviceDate");
		if(!isValidDate("serviceDate",serviceDate,returnResult)){
			return false;
		}
		HospitalClaimDetail hospitalClaimDetail = (HospitalClaimDetail)JSONObject.toBean(claimDetailJson,HospitalClaimDetail.class);
		if(!baseCheckInputVO(hospitalClaimDetail,returnResult)){
			return false;
		}
		return true;
	}
	
	/**
	 * 验证vo数据
	 * @param requestVO
	 * @param returnResult
	 * @return
	 */
	private boolean baseCheckInputVO(Object requestVO, ReturnResult returnResult) {
		if (null == requestVO) {
			RespInfo respInfo = new RespInfo();
			respInfo.setResultCode("0001");
			respInfo.setResultMsg("信息不能为空！");
			respInfo.setResultStatus("F");
			returnResult.setRespInfo(respInfo);
		    return false;
		}
		Set<ConstraintViolation<Object>> validResult = Validation.buildDefaultValidatorFactory().getValidator()
		         .validate(requestVO);
		if (null != validResult && validResult.size() > 0) {
		    StringBuilder sb = new StringBuilder();
		    for (Iterator<ConstraintViolation<Object>> iterator = validResult.iterator(); iterator.hasNext();) {
		        ConstraintViolation<Object> constraintViolation = (ConstraintViolation<Object>) iterator.next();
		         //这里只取了字段名，如果需要其他信息可以自己处理
		        sb.append(constraintViolation.getMessage()).append("、");
		    }
			if(sb.lastIndexOf("、") == sb.length()-1){
			    sb.delete(sb.length()-1, sb.length());
			}
			RespInfo respInfo = new RespInfo();
			respInfo.setResultCode("0001");
			respInfo.setResultMsg("请检查传入的数据格式异常:"+sb.toString());
			respInfo.setResultStatus("F");
			returnResult.setRespInfo(respInfo);
			return false;
		}
		return true;
	}
	
	/**
	 * 验证字符串是否是一个合法的日期格式
	 * @param str
	 * @return
	 */
	private boolean isValidDate(String str, String strVal, ReturnResult returnResult) { 
       boolean convertSuccess=true; 
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
        try {
        	// 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01 
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
           convertSuccess=false;
        }
        return convertSuccess;
	 }
	
	/**
	 * 验证认证ID
	 */
	public boolean checkCertId(String certId, ReturnResult returnResult){
		String setCertID = propertiesLoader.getProperty("certID");
		if(!certId.equals(setCertID)){
			RespInfo respInfo = new RespInfo();
			respInfo.setResultCode("0002");
			respInfo.setResultMsg("认证ID号验证失败！");
			respInfo.setResultStatus("F");
			returnResult.setRespInfo(respInfo);
			return false;
		}
		return true;
	}
}

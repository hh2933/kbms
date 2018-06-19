package com.shuxin.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.bind.annotation.RequestBody;

import com.baomidou.mybatisplus.service.IService;
import com.shuxin.commons.shiro.ShiroUser;
import com.shuxin.commons.utils.PageInfo;
import com.shuxin.model.CommonModel;
import com.shuxin.model.vo.KnowledgeBaseVo;

public interface IKnowledgeBaseService extends IService<CommonModel>{

	/**
	 * 查询知识库信息
	 * @param pageInfo
	 */
	public void selectKnowledgeBaseVoPage(PageInfo pageInfo,KnowledgeBaseVo knowledgeBaseVo);
	
	/**
	 * 查询记录是否已经存在
	 * @param knowledgeBaseVo
	 * @return
	 */
	public int selectExistKnowledgeBase(KnowledgeBaseVo knowledgeBaseVo);
	
	/**
	 * 修改或新增知识库信息
	 * @param knowledgeBaseVo
	 */
	public void editKnowledgeBase(KnowledgeBaseVo knowledgeBaseVo);
	
	/**
	 * 删除知识库信息
	 * @param id
	 * @param tableName
	 */
	public void delKnowledgeBase(List<String> list,String tableName,String loginName);

	/**
	 * 数据导入
	 * @param tableinfo
	 * @param workbook
	 * @param user
	 */
	public  String importData(List<Map<String, String>> tableinfo, Workbook workbook,
			ShiroUser user);
	
	/**
	 * 导出历史数据
	 * @param knowledgeBaseVo
	 * @param response
	 */
	public void exportKnowledgeBaseHistory(KnowledgeBaseVo knowledgeBaseVo,HttpServletResponse response);
}

package com.shuxin.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.shuxin.commons.shiro.ShiroUser;
import com.shuxin.commons.utils.BeanUtils;
import com.shuxin.commons.utils.PageInfo;
import com.shuxin.commons.utils.StringUtils;
import com.shuxin.mapper.TreatSubjectTransOptMapper;
import com.shuxin.mapper.TreatsubjectTransMapper;
import com.shuxin.model.DiagnosticCatalog;
import com.shuxin.model.MIStandardMapOpt;
import com.shuxin.model.TreatSubjectTrans;
import com.shuxin.model.TreatSubjectTransOpt;
import com.shuxin.service.ITreatsubjectTransService;

@Service
public class TreatsubjectTransServiceImpl extends ServiceImpl<TreatsubjectTransMapper, TreatSubjectTrans> implements  ITreatsubjectTransService{
  @Autowired
  private TreatsubjectTransMapper   treatsubjectTransMapper; 
  @Autowired
  private TreatSubjectTransOptMapper treatSubjectTransOptMapper;
	
	@Override
	public void findTreatSubjectTransDataGrid(PageInfo pageInfo) {
		// TODO Auto-generated method stub
		 Page<TreatSubjectTrans> page = new Page<TreatSubjectTrans>(pageInfo.getNowpage(), pageInfo.getSize());
	        String orderField = com.baomidou.mybatisplus.toolkit.StringUtils.camelToUnderline(pageInfo.getSort());
	        page.setOrderByField(orderField);
	        page.setAsc(pageInfo.getOrder().equalsIgnoreCase("asc"));
	        List<TreatSubjectTrans> list = treatsubjectTransMapper.findTreatSubjectTransDataGrid(page, pageInfo.getCondition());
	        pageInfo.setRows(list);
	        pageInfo.setTotal(page.getTotal());	
	
	}

	@Override
	public List<TreatSubjectTrans> selectBycode(
			TreatSubjectTrans treatSubjectTrans) {
		TreatSubjectTrans newtreatSubjectTrans = new TreatSubjectTrans();
		newtreatSubjectTrans.setSubjectCode(treatSubjectTrans.getSubjectCode());
		
        EntityWrapper<TreatSubjectTrans> wrapper = new EntityWrapper<TreatSubjectTrans>(newtreatSubjectTrans);
        if(treatSubjectTrans.getId()!=null){
        	 wrapper.where("id != {0}", treatSubjectTrans.getId());
		}
        
        return this.selectList(wrapper);
	}

	@Override
	public void editTreatSubjectTrans(TreatSubjectTrans treatSubjectTrans,
			ShiroUser user) {
		treatSubjectTrans.setUpdateTime(new Date());
		treatSubjectTrans.setUpdateUser(user.getLoginName());
		String optType="添加";
		if(treatSubjectTrans.getId()!=null&&StringUtils.isNotBlank(treatSubjectTrans.getId())){
			optType="修改";
			addTreatSubjectTransHistory(treatSubjectTrans.getId(), user.getLoginName(),optType );
			this.updateById(treatSubjectTrans);
			
		}else{
			treatSubjectTrans.setCreateTime(new Date());
			treatSubjectTrans.setCreateUser(user.getLoginName());;
	        this.insert(treatSubjectTrans);
	        TreatSubjectTransOpt treatsubjecttransopt=BeanUtils.copy(treatSubjectTrans,TreatSubjectTransOpt.class);
	        treatsubjecttransopt.setOptType("添加");
	        treatSubjectTransOptMapper.insert(treatsubjecttransopt);
		}
		
		
	}

	@Override
	public void deleteTreatsubjectTrans(List<String> ids, ShiroUser user) {
		
		 for (int i = 0; i < ids.size(); i++) {
			 addTreatSubjectTransHistory(ids.get(i), user.getLoginName(),"删除" );
		}
		 this.deleteBatchIds(ids);
	}
	private void addTreatSubjectTransHistory(String id,String loginName,String optType)
	{
		
		Map<String, Object> params = new HashMap<String, Object>();
	
		params.put("id", id);
		params.put("optType", optType);
		params.put("loginName", loginName);
		treatsubjectTransMapper.addTreatSubjectTransHistory(params);
	}
	@Override
	public void importData(List<Map<String, String>> exportList, ShiroUser user) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		if(exportList.size()==0)
		{
			return;
		}
		else if(exportList.size()<101)
		{			
			map.put("list", exportList);
			map.put("loginName", user.getLoginName());
			treatsubjectTransMapper.daoruData(map);
		}
		else
		{
			int count= exportList.size()/100==0?exportList.size()/100:exportList.size()/100+1;
			int startIndex=0;
			int endIndex=100;
			for(int i=1;i<=count;i++)
			{
				map.put("list", exportList.subList(startIndex, endIndex));
				map.put("loginName", user.getLoginName());
				treatsubjectTransMapper.daoruData(map);
				startIndex=endIndex;
				endIndex=100*(i+1);
				if(endIndex>exportList.size())
				{
					endIndex=exportList.size();
				}
			}
		}
	}

	

	@Override
	public List<Map<String, Object>> selectDetailTreatSubjectTrans() {
		// TODO Auto-generated method stub
		 List<Map<String, Object>> detailList = treatsubjectTransMapper.selectDetailTreatsubjectTrans();
			
			
			return detailList;
	}

	@Override
	public List<Map<String, Object>> selectTreatSubjectTransHistory() {
		// TODO Auto-generated method stub
		 List<Map<String, Object>> detailList = treatsubjectTransMapper.selecTeatsubjectTransHistory();
			
			
			return detailList;
	}
	
	
	
	
	
	

}

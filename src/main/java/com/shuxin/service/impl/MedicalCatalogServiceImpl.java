package com.shuxin.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.shuxin.commons.datasource.DynamicDataSourceHolder;
import com.shuxin.commons.utils.PageInfo;
import com.shuxin.commons.utils.StringUtils;
import com.shuxin.mapper.MedicalCatalogMapper;
import com.shuxin.model.MedicalCatalog;
import com.shuxin.service.IMedicalCatalogService;

@Service
public class MedicalCatalogServiceImpl extends ServiceImpl<MedicalCatalogMapper, MedicalCatalog> implements IMedicalCatalogService{
   @Autowired
   private MedicalCatalogMapper catalogMapper;

	@Override
	public void getCatalogTableInfo(PageInfo pageInfo) {
		Page<MedicalCatalog> page = new Page<MedicalCatalog>(pageInfo.getNowpage(), pageInfo.getSize());
        String orderField = com.baomidou.mybatisplus.toolkit.StringUtils.camelToUnderline(pageInfo.getSort());
        page.setOrderByField(orderField);
        page.setAsc(pageInfo.getOrder().equalsIgnoreCase("asc"));
        List<MedicalCatalog> list = catalogMapper.getCatalogTableInfo(page, pageInfo.getCondition());
        pageInfo.setRows(list);
        pageInfo.setTotal(page.getTotal());
	}

	@Override
	public boolean updateClzt(String clzt,String ids) {
		boolean flag = false;
		if(clzt.equals("1")){
			flag = catalogMapper.updateClztIn1(StringUtils.strTurnList(ids));
		}
		if(clzt.equals("0")){
			flag = catalogMapper.updateClztIn0(StringUtils.strTurnList(ids));
		}
		return flag;
	}

	@Override
	public List<Map<String, String>> getCatalogExportInfo(MedicalCatalog catalog) {
		// 监测跟新结果
		List<Map<String, String>> resultList = catalogMapper.getCatalogExportInfo(catalog);
		List<String> yyxmbmList = new ArrayList<>();
		List<Map<String, String>> ypzdList;
		List<Map<String, String>> ylzdList;
		
		if(resultList != null){
			for (Map<String, String> map : resultList) {
				yyxmbmList.add(map.get("YYXMBM"));
			}
			
			DynamicDataSourceHolder.setDataSource("dataSource_his");
			ypzdList = catalogMapper.getYpzdDetails(yyxmbmList);
			
			ylzdList = catalogMapper.getYlzdDetails(yyxmbmList);
			DynamicDataSourceHolder.clearDataSource();
			
			// 合并 his系统与64服务器上的表
			for (Map<String, String> map : resultList) {
				boolean flag = false;
				for (Map<String, String> mapYpzd : ypzdList) {
					if(map.get("YYXMBM").equals(mapYpzd.get("BM"))){
						map.putAll(mapYpzd);
						flag = true;
						break;
					}
				}
				
				if(flag){
					for (Map<String, String> mapYLzd : ylzdList) {
						if(map.get("YYXMBM").equals(mapYLzd.get("BM"))){
							map.putAll(mapYLzd);
							break;
						}
					}
				}
				
			}
		}else{
			return new ArrayList<Map<String,String>>();
		}
		return resultList;
	}
	
	
	
	
	
	
	

	
	
}

package com.shuxin.controller;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shuxin.commons.datasource.DynamicDataSourceHolder;
import com.shuxin.mapper.ruleengine.DataRecoverMapper;
import com.shuxin.mapper.ruleengine.HospitalClaimMapper;
import com.shuxin.model.ruleengine.RespInfo;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/request")
public class DatabaseLinkController {
	
	@Autowired
	private DataRecoverMapper dataRecoverMapper;
		
	@GetMapping("/zyw")
	@ResponseBody
	public String execute(){
		
		DynamicDataSourceHolder.setDataSource("dataSource_ybkf");
		String time = dataRecoverMapper.selectFromYbkf();
		DynamicDataSourceHolder.clearDataSource();
		
		DynamicDataSourceHolder.setDataSource("dataSource_his");
		Object name = dataRecoverMapper.selectFromHis();
		Blob blob = (Blob)name;
		String jsonfromhis ="";
			try {
				jsonfromhis = new String(blob.getBytes((long) 1, (int) blob.length()));
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		DynamicDataSourceHolder.clearDataSource();
		
		
		try {
			FileWriter fw =new FileWriter("D:/test_his.txt",true);
			PrintWriter pw =new PrintWriter(fw);
			pw.println("DataFromHis:"+jsonfromhis);
			pw.println("DataFromYbkf:"+time);
			fw.close();
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		RespInfo res =new RespInfo();
		res.setResultMsg("OK");
		
		return JSONObject.fromObject(res).toString();
		
	}
}

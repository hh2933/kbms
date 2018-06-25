package com.shuxin.controller.catalog;

import com.shuxin.model.catalog.YPML;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: zengj
 * @Date: Created on 2018-6-19 15:00.
 */
public class YPMLUtil {



    /*
     * @author zengj
     * @Description 转为限儿童
     * @Date 2018-6-22 14:42
     */
    public static Map<String,Object> toXet(YPML ypml,String gz){
        Map<String,Object> map = new HashMap<>();
        map.put("xmb项目编码m",ypml.getYbypbm());   //
        map.put("xdnlnt","14");             //限定年龄年天
        map.put("tsxx",gz);                 //提示信息
        map.put("etxdlx","1");              //儿童限定类型
        map.put("xdjbzd","N");              //限定疾病诊断
        map.put("cblxbm","N");              //参保类型编码
        map.put("xmmc",ypml.getYyypmc());   //项目名称
        map.put("bzxx",ypml.getSm());       //备注信息
        if (gz.matches("^限新生儿.*")){
            map.put("xdnlnt", "28");
            map.put("etxdlx","2");
            map.put("xdjbzd","A528");
        }
        return map;
    }


}

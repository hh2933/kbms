package com.shuxin.service.catalog;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.shuxin.commons.utils.PageInfo;
import com.shuxin.mapper.catalog.YPMLMapper;
import com.shuxin.model.catalog.YPML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zengj on 2018-6-6.
 */
@Service
public class YPMLService extends ServiceImpl<YPMLMapper,YPML> {

    @Autowired
    private YPMLMapper ypmlMapper;

    public int selectExist(YPML ypml) {
        return ypmlMapper.selectExist(ypml);
    }

    public List<YPML> selectAll(){
        YPML ypml = new YPML();
        EntityWrapper<YPML> wrapper= new EntityWrapper<>(ypml);
        return ypmlMapper.selectList(wrapper);
    }

    public List<YPML> selectWjx(){
        YPML ypml = new YPML();
        EntityWrapper<YPML> wrapper= new EntityWrapper<>(ypml);
        wrapper.in("jxzt",new Integer[]{1,4,5});
        return ypmlMapper.selectList(wrapper);
    }

    public Set<YPML> selectAllSet(){
        return ypmlMapper.selectAllSet();
    }

    public void selectPage(PageInfo pageInfo) {
        Page page = new Page(pageInfo.getNowpage(), pageInfo.getSize());
        List<Map<String,Object>> list = ypmlMapper.selectPage(page, pageInfo.getCondition());
        pageInfo.setRows(list);
        pageInfo.setTotal(page.getTotal());
    }

    public void importExcel(List<YPML> list) {
        for (YPML ypml: list) {
            ypml.setDrsj(new Date());
            ypml.setJxzt(1);
            ypmlMapper.insert(ypml);
        }
    }

    public YPML jx(YPML ypml){
        int jxzt;   //解析状态
        String sm = ypml.getSm();
        StringBuffer jxjg = new StringBuffer("");   //解析结果
        StringBuffer jxjgms = new StringBuffer(""); //解析结果描述
        StringBuffer yjxsm = new StringBuffer("");  //已解析说明
        StringBuffer wnjxsm = new StringBuffer(""); //未能解析说明

        String blsyfw = ""; //解析说明中的比例使用范围
        int tempIndex = sm.lastIndexOf("(");
        if (tempIndex != -1){
            String tempBlsyfw = sm.substring(tempIndex);
            if (tempBlsyfw.matches("^\\(比例适用范围:起始日期:.*\\)$")){ //(比例适用范围:起始日期:2000.01.01,终止日期:2050.12.31)，这部分不用解析
                blsyfw = tempBlsyfw;
                sm = sm.substring(0,tempIndex);
            }
        }

        if (!sm.equals("")){
            for (String gz : sm.split(";")) {
/*            if (gz.equals("医疗") || gz.equals("工伤")){

                yjxsm.append(gz).append(";");
                continue;
            }*/
                wnjxsm.append(gz).append(";");
            }
        }

        ypml.setJxjg(jxjg.length() > 0?jxjg.substring(0,jxjg.length()-1):"");
        ypml.setJxjgms(jxjgms.length() > 0?jxjgms.substring(0,jxjgms.length()-1):"");
        ypml.setYjxsm(yjxsm.length() > 0?yjxsm.substring(0,yjxsm.length()-1) + blsyfw:blsyfw);
        ypml.setWnjxsm(wnjxsm.length() > 0?wnjxsm.substring(0,wnjxsm.length()-1):"");

        yjxsm.append(blsyfw);
        if (yjxsm.length() == 0 && wnjxsm.length() > 0){    //(1：未开始，2已解析，3：全部解析，4：部分解析，5：全部未能解析)
            jxzt = 5;
        }else if (yjxsm.length() > 0 && wnjxsm.length() == 0){
            jxzt = 3;
        }else if(yjxsm.length() > 0 && wnjxsm.length() > 0){
            jxzt = 4;
        }else {
            jxzt = 2;
        }
        ypml.setJxzt(jxzt);
        ypmlMapper.updateById(ypml);
        return ypml;
    }

    public static void main(String[] args) {
        String sm = "dgdffgfd";
        String tempBlsyfw = sm.substring(sm.lastIndexOf("("));
        System.out.println(tempBlsyfw);
    }
}

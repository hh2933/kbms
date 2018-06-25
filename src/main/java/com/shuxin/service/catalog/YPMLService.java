package com.shuxin.service.catalog;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.shuxin.commons.utils.PageInfo;
import com.shuxin.controller.catalog.YPMLUtil;
import com.shuxin.mapper.DrugCatalogMapper;
import com.shuxin.mapper.KnowledgeBaseMapper;
import com.shuxin.mapper.catalog.YPMLMapper;
import com.shuxin.model.DrugCatalog;
import com.shuxin.model.catalog.YPML;
import com.shuxin.model.vo.DrugCatalogVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by zengj on 2018-6-6.
 */
@Service
public class YPMLService extends ServiceImpl<YPMLMapper,YPML> {

    @Autowired
    private YPMLMapper ypmlMapper;
    @Autowired
    private DrugCatalogMapper drugCatalogMapper;
    @Autowired
    private KnowledgeBaseMapper knowledgeBaseMapper;

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

    /*
     * @author zengj @Date 2018-6-22 16:44 @Description 导入excel
     */
    public void importExcel(List<YPML> list) {
        for (YPML ypml: list) {
            ypml.setDrsj(new Date());
            ypml.setJxzt(1);
            ypmlMapper.insert(ypml);
        }
    }

    /*
     * @author zengj @Date 2018-6-22 16:44 @Description 解析
     */
    public YPML jx(YPML ypml){
        ypml.setJxjg("");
        ypml.setJxjgms("");
        ypml.setYjxsm("");
        ypml.setWnjxsm("");

        String sm = ypml.getSm();   //说明
        String blsyfw = ""; //解析说明中的比例适用范围
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
                gz = gz.trim();
                if (gz.equals("医疗")){
                    ruleDrugCatalog(ypml,gz);
                    continue;
                }
                if (gz.matches("^限儿童.*") || gz.matches("^限新生儿.*")){
                    ruleXet(ypml,gz);
                    continue;
                }
                ypml.setWnjxsm(ypml.getWnjxsm() + gz + ";");
            }
        }else {
            if (ypml.getYbypbm() != null && !ypml.getYbypbm().equals("")){
                ruleDrugCatalog(ypml,"");
            }
        }

        ypml.setYjxsm(ypml.getYjxsm() + blsyfw + ";");
        if (ypml.getYjxsm().length() == 0 && ypml.getWnjxsm().length() > 0){    //(1：未开始，2已解析，3：全部解析，4：部分解析，5：全部未能解析)
            ypml.setJxzt(5);
        }else if (ypml.getYjxsm().length() > 0 && ypml.getWnjxsm().length() == 0){
            ypml.setJxzt(3);
        }else if(ypml.getYjxsm().length() > 0 && ypml.getWnjxsm().length() > 0){
            ypml.setJxzt(4);
        }else {
            ypml.setJxzt(2);
        }
        ypmlMapper.updateById(ypml);
        return ypml;
    }

    /*
     * @author zengj @Date 2018-6-22 16:41 @Description 药品目录t_drugs_catalog
     */
    private void ruleDrugCatalog(YPML ypml, String gz) {
        DrugCatalog drugCatalog = DrugCatalog.ypmlToDrugCatalog(ypml);
        drugCatalog.preInsert();
        DrugCatalogVo drugCatalogVo = new DrugCatalogVo();
        drugCatalogVo.setDrugCode(drugCatalog.getDrugCode());
        if (drugCatalogMapper.selectExistDrugCatalog(drugCatalogVo) == 0) {
            drugCatalogMapper.insert(drugCatalog);
            ypml.setJxjg(ypml.getJxjg() + "t_drugs_catalog." + drugCatalog.getId() + ";");
            ypml.setJxjgms(ypml.getJxjgms() + "药品目录;");
        }else {
            ypml.setJxjgms(ypml.getJxjgms() + "药品目录(重复);");
        }
        ypml.setYjxsm(ypml.getYjxsm() + gz + ";");
    }

    /*
     * @author zengj @Date 2018-6-22 16:43 @Description 限儿童t_xet
     */
    private void ruleXet(YPML ypml, String gz){
        Map map = YPMLUtil.toXet(ypml,gz);
        String tableName = "t_xet";

        List<String> exceptList = new ArrayList<>();    //不查重的字段
        exceptList.add("tsxx");
        exceptList.add("bzxx");
        if (selectExist(map, tableName, exceptList) == 0) {
            String id = insertMap(map,tableName);
            ypml.setJxjg(ypml.getJxjg() + tableName + "."+ id + ";");
            ypml.setJxjgms(ypml.getJxjgms() + "限儿童;");
        }else {
            ypml.setJxjgms(ypml.getJxjgms() + "限儿童(重复);");
        }
        ypml.setYjxsm(ypml.getYjxsm() + gz + ";");
    }

    private Integer selectExist(Map<String,Object> map,String tableName,List<String> exceptList){
        StringBuffer condition = new StringBuffer();
        for(Map.Entry<String,Object> entry: map.entrySet()) {
            if (!exceptList.contains(entry.getKey())){
                condition.append(entry.getKey()).append("='").append(entry.getValue()).append("'and ");
            }
        }
        System.out.println(condition);
        condition.delete(condition.length() - 4,condition.length());

        Map<String, Object> params = new HashMap<>();
        params.put("condition", condition.toString());
        params.put("tableName", tableName);
        int count = knowledgeBaseMapper.selectExistKnowledgeBase(params);
        return count;
    }


    /*
     * @author zengj @Date 2018-6-22 16:44 @Description map存入数据库对应表
     */
    private String insertMap(Map<String,Object> map,String tableName){
        String id = UUID.randomUUID().toString().replace("-", "");
        StringBuffer columnName = new StringBuffer("id");
        StringBuffer columnValue = new StringBuffer("'" + id + "'");
        columnName.append(",create_time,create_user,update_time,update_user");
        columnValue.append(",sysdate,'自动识别',sysdate,'自动识别'");

        for(Map.Entry<String,Object> entry: map.entrySet()){
            columnName.append(",").append(entry.getKey());
            columnValue.append(",'").append(entry.getValue()).append("'");
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("tableName", tableName);
        params.put("columnName", columnName.toString());
        params.put("columnValue", columnValue.toString());
        knowledgeBaseMapper.addKnowledgeBase(params);
        return id;
    }

    public static void main(String[] args) {
        StringBuffer buff = new StringBuffer("0123456789");
        System.out.println("buff="+buff);

        //删除下标从3到5的字符
        buff.delete(3,5);
        System.out.println("deletionBack="+ buff);

        buff = new StringBuffer("0123456789");
        //删除下标为8字符
        buff.deleteCharAt(8);
        System.out.println("delectCharAtBack="+buff);

        buff = new StringBuffer("0123456789");
        buff.delete(3,100);
        System.out.println("deletionBack="+ buff);

        buff = new StringBuffer("0123456789");
        buff.delete(3,buff.length());
        System.out.println("deletionBack="+ buff);

        buff = new StringBuffer("0123456789");
        System.out.println(buff.length());
        buff.delete(3,buff.length()-1);
        System.out.println("deletionBack="+ buff);
    }
}

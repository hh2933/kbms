package com.shuxin.controller.catalog;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.shuxin.commons.base.BaseController;
import com.shuxin.commons.result.Result;
import com.shuxin.commons.utils.*;
import com.shuxin.model.catalog.YPML;
import com.shuxin.service.catalog.YPMLService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @Description: M_YPML相关操作
 * Created by zengj on 2018-6-6.
 */
@Controller
@RequestMapping("/catalog/ypml")
public class YPMLController extends BaseController {
    @Autowired
    private YPMLService ypmlService;

    //重复数据map，因为不能同时向前端传递json并导出excel，所以重复数据先存储在这里。多人同时操作可能导致提示信息有误。
    private static Map repeatMap = new HashMap();

    @RequestMapping("/index")
    public String index(HttpServletRequest request, Model model){
        model=getMenuId(request, model);
        return "catalog/YPML";
    }

    /* 
     * @author zengj 
     * @Description 导出药品目录模板
     * @Date 2018-6-7 9:53
     */
    @RequestMapping("/exportTemp")
    @ResponseBody
    public void exportTemp(HttpServletResponse response){
        ExcelUtil.exportExcel(response, "M_YPMLImpTemp", "药品目录导入模版", new HashMap());
    }

    /*
     * @author zengj
     * @Description 导出全部
     * @Date 2018-6-13 9:21
     */
    @RequestMapping("/exportData")
    @ResponseBody
    public void exportData(HttpServletResponse response){
        List<YPML> list =ypmlService.selectAll();
        Map<String, Object> map = new HashMap<String,Object>();
        map.put("list", list);
        ExcelUtil.exportExcel(response, "M_YPMLExpTemp", "药品目录", map);
    }

    /*
 * @author zengj
 * @Description 导出未解析目录
 * @Date 2018-6-13 9:21
 */
    @RequestMapping("/exportWjxData")
    @ResponseBody
    public void exportWjxData(HttpServletResponse response){
        List<YPML> list =ypmlService.selectWjx();
        Map<String, Object> map = new HashMap<String,Object>();
        map.put("list", list);
        ExcelUtil.exportExcel(response, "M_YPMLExpTemp", "未解析药品目录", map);
    }

    /*
     * @author zengj
     * @Description 新增单条目录
     * @Date 2018-6-7 10:33
     */
    @PostMapping("/add")
    @ResponseBody
    public Object add(YPML ypml){
        int count = ypmlService.selectExist(ypml);
        if (count > 0){
            return renderError("该药品目录已经存在!");
        }
        ypml.setDrsj(new Date());
        ypml.setJxzt(1);
        ypmlService.insert(ypml);
        return renderSuccess("操作成功");
    }

    /*
     * @author zengj
     * @Description 修改单条目录
     * @Date 2018-6-7 9:54
     */
    @PostMapping("/edit")
    @ResponseBody
    public Object edit(YPML ypml){
        ypmlService.updateById(ypml);
        return renderSuccess("操作成功");
    }

    /*
     * @author zengj
     * @Description 查询列表
     * @Date 2018-6-8 9:06
     */
    @RequestMapping("/findPage")
    @ResponseBody
    public Object findPage( YPML ypml, Integer page, Integer rows, String sort, String order)
    {
        PageInfo pageInfo = new PageInfo(page, rows, sort, order);

        Map<String, Object> condition = new HashMap<String, Object>();

        if (StringUtils.isNotBlank(ypml.getYbypbm())) {
            condition.put("ybypbm", ypml.getYbypbm());
        }

        if (StringUtils.isNotBlank(ypml.getYyypmc())) {
            condition.put("yyypmc", ypml.getYyypmc());
        }

        pageInfo.setCondition(condition);
        ypmlService.selectPage(pageInfo);
        return pageInfo;
    }

    /*
     * @author zengj
     * @Description 导入excel
     * @Date 2018-6-11 16:24
     */
    @RequestMapping("/importExcel")
    @ResponseBody
    public Object importExcel(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletResponse response) {
        List<YPML> importList = new ArrayList<>();  //导入List
        List<Integer> repeatList = new ArrayList<>();  //重复的List
        try {
            String fileName = file.getOriginalFilename();
            String fileSuffix = fileName.substring(fileName.lastIndexOf(".") + 1);

            Workbook workbook;

            if("xlsx".equals(fileSuffix)||"xls".equals(fileSuffix)) {
                workbook=	new XSSFWorkbook(file.getInputStream());
            }
            else {
                return renderSuccess("导入文件格式不正确");
            }

            List<Map<String, String>> errorList = validateExportData(workbook,importList,repeatList);
            if(errorList.size()>0) {
                ImportErrorExcelUtils.creatErrorExcel(response, errorList);
                return renderError("导入失败");
            }

            ypmlService.importExcel(importList);

           if(repeatList.size() > 0){
                //repeatList传给repeatMap
                repeatMap.put("repeatList",repeatList);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return renderError("导入失败");
        }

        Result result = new Result();
        result.setSuccess(true);
        result.setMsg("导入成功["+importList.size()+"]条数据，此外该excel包含重复数据["+repeatList.size()+"]条。");
        result.setObj(repeatList.size());
        return result;
    }

    /*
     * @author zengj
     * @Description 导出重复数据
     * @Date 2018-6-12 12:12
     */
    @RequestMapping("/exportRepeat")
    @ResponseBody
    public void exportRepeat(HttpServletResponse response){
        ExcelUtil.exportExcel(response, "importRepeatTemp", "重复数据", repeatMap);
        repeatMap.clear();
    }

    /*
     * @author zengj
     * @Description 验证excel并提取数据
     * @Date 2018-6-19 9:30
     */
    private List<Map<String, String>> validateExportData(Workbook workbook,List<YPML> importList,List<Integer> repeatList) {
        Sheet sheet = workbook.getSheetAt(0);
        Row title = sheet.getRow(0);

        List<String> tempTitle = ExcelUtil.readExcelTempTitle("M_YPMLImpTemp");

        List<Map<String, String>> errorList = ExcelUtil.validateImpTempTitle(title,tempTitle);  //校验标题行

        if(errorList.size()>0) {
            return errorList;
        }

        Set<YPML> ypmlSet = ypmlService.selectAllSet();

        YPML ypml;
        String cellContent="";
        for (int rowNum = 1; rowNum <=sheet.getLastRowNum(); rowNum++) {  //遍历行
            Row rows = sheet.getRow(rowNum);
            if(!DataValidationUtils.isRowNotEmpty(rows)) {
                continue;
            }
            ypml = new YPML();
            for(int i=0;i<tempTitle.size();i++) {  //遍历列
                Cell cell = rows.getCell(i);
                if(cell != null ) {  //获取列信息
                    cellContent = ExcelUtil.getStringValueFromCell(cell).trim();
                }
                if(i < 11 ) { //0到10行不容许为空
                    if(StringUtils.isEmpty(cellContent)){
                        ExcelUtil.addErrorList(errorList,rowNum,i,tempTitle.get(i)+"不能为空");
                    }
                }
                switch (i){ //校验
                    case 0:case 1:
                        if(!DataValidationUtils.isCode(cellContent)){
                            ExcelUtil.addErrorList(errorList,rowNum,i,tempTitle.get(i)+"只能是字母数字和下划线");
                        }
                        if (cellContent.length() > 32) {
                            ExcelUtil.addErrorList(errorList,rowNum,i,tempTitle.get(i)+"不能超过32个字符");
                        }
                        break;
                    case 2:case 5:case 9:
                        if (cellContent.length() > 50) {
                            ExcelUtil.addErrorList(errorList,rowNum,i,tempTitle.get(i)+"不能超过50个汉字");
                        }
                        break;
                    case 3:case 7:case 8:case 10:
                        if (!DataValidationUtils.isNumber(cellContent)){
                            ExcelUtil.addErrorList(errorList,rowNum,i,tempTitle.get(i)+"只能是数字");
                        }
                        break;
                    case 4:case 11:
                        if (cellContent.length() > 25) {
                            ExcelUtil.addErrorList(errorList,rowNum,i,tempTitle.get(i)+"不能超过25个汉字");
                        }break;
                    case 6:
                        if (cellContent.length() > 5) {
                            ExcelUtil.addErrorList(errorList,rowNum,i,tempTitle.get(i)+"不能超过5个汉字");
                        }
                        break;
                    case 12:
                        if (cellContent.length() > 122) {
                            ExcelUtil.addErrorList(errorList,rowNum,i,tempTitle.get(i)+"不能超过122个汉字");
                        }
                        break;
                    default:
                        break;
                }
                if(!StringUtils.isEmpty(cellContent)){
                    setImportValue(ypml, i, cellContent);
                }
                cellContent="";
            }
            if (ypmlSet.add(ypml)){
                ypml.setId( UUID.randomUUID().toString().replace("-", ""));
                importList.add(ypml);
            }else {
                repeatList.add(rowNum+1);
            }
        }
        return errorList;
    }


    /*
     * @author zengj
     * @Description cell的值赋予ypml对象
     * @Date 2018-6-12 16:56
     */
    public void setImportValue( YPML ypml, int i , Object value){
        switch (i){
            case 0 : ypml.setYbypbm(value.toString());
                break;
            case 1 :  ypml.setYyypbm(value.toString());
                break;
            case 2 :  ypml.setYyypmc(value.toString());
                break;
            case 3 : ypml.setYplx(Integer.parseInt(value.toString()));
                break;
            case 4 : ypml.setJx(value.toString());
                break;
            case 5 : ypml.setGg(value.toString());
                break;
            case 6 : ypml.setDw(value.toString());
                break;
            case 7 :  ypml.setJg(BigDecimalUtil.getBigDecimal(value));
                break;
            case 8 : ypml.setZxbz(Integer.parseInt(value.toString()));
                break;
            case 9 : ypml.setSccs(value.toString());
                break;
            case 10 : ypml.setZfbl(Integer.parseInt(value.toString()));
                break;
            case 11 : ypml.setGytj(value.toString());
                break;
            case 12 : ypml.setSm(value.toString());
                break;
            default :
                break;
        }
    }

    /*
     * @author zengj
     * @Description 解析单条目录
     * @Date 2018-6-19 12:14
     */
    @RequestMapping("/explain")
    @ResponseBody
    public Object explain(String id){
        YPML ypml = ypmlService.selectById(id);
        String sm = ypml.getSm();
        if (ypml.getJxzt() != 1){
            return renderSuccess("该条目录已解析过，不能重复解析");
        }
        ypml = ypmlService.jx(ypml);
        return renderSuccess("解析状态：["+ypml.getJxztMs()+"],已解析说明:["+ypml.getYjxsm()+"],未能解析说明:["+ypml.getWnjxsm()+"]");
    }

    /*
     * @author zengj
     * @Description 全部解析
     * @Date 2018-6-19 12:14
     */
    @RequestMapping("/explainAll")
    @ResponseBody
    public Object explainAll(){
        YPML sYpml = new YPML();
        EntityWrapper<YPML> wrapper= new EntityWrapper<>(sYpml);
        wrapper.eq("jxzt",1);
        List<YPML> ypmlList = ypmlService.selectList(wrapper);
        Set<YPML> ypmlSet = ypmlService.selectAllSet();
        for (YPML ypml : ypmlList) {
            ypmlService.jx(ypml);
        }
        return renderSuccess("本次成功解析[" + ypmlList.size() + "]条药品目录");
    }

    /*
     * @author zengj
     * @Description 解析状态设为手动解析
     * @Date 2018-6-20 12:07
     */
    @RequestMapping("/setJxzt")
    @ResponseBody
    public Object setJxzt(String id){
        YPML ypml = ypmlService.selectById(id);
        ypml.setJxzt(6);
        ypmlService.updateById(ypml);
        return renderSuccess("操作成功");
    }
}

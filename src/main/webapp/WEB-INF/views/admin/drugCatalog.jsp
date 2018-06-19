<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<html>
<head>
<%@ include file="/commons/basejs.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style>
.sf-msg-table th{
    border-top: none !important;
}
.fw-target-div{
    height: 392px;
    margin-top: 4px;
    margin-bottom: 20px;
}
.fw-target-div:nth-child(odd){
    background: none;
    overflow-x: auto;
}
.input-table2 select{width: 66px !important;}
.input-table2 input{width: 66px !important;}
.header-div{min-height: 46px !important; height: auto !important;padding-bottom: 8px}
.panel-body-noheader {
    border-top-width: 0px;
}
.panel-header, .panel-body {
    border-width: 1px;
    border-style: solid;
}
.datagrid .datagrid-pager {
    display: block;
    margin: 0;
    border-width: 0px 0 0 1;
    border-style: solid;
}
.datagrid-header td, .datagrid-body td, .datagrid-footer td {
    border-width: 0 1px 0px 0;
    border-style: dotted;
    margin: 0;
    padding: 0;
}
</style>
<script type="text/javascript">
var drugCatalogDataGrid;
$(function(){
	$("#selfPay").bind('blur',function(){  
		this.value=this.value.replace(/[^0-9.]/g,'');  
	});
	$("#price").bind('blur',function(){  
		this.value=this.value.replace(/[^0-9.]/g,'');  
	});
	importExcel();
	drugCatalogDataGrid=$('#drugCatalogDataGrid').datagrid({
		url : '${path }/drugCatalog/searchDrugCatalog',
        fit : true,
        striped : true,
        pagination : true,
        singleSelect : false, 
        nowrap : false,	 
        pageSize : 20,
        pageList : [ 10, 20, 30, 40, 50, 100 ],
        columns : [ [
        	{
                checkbox : 'true',
                field : 'fc'
            },{
                    width : '4%',
                    title : '<font size="3px">序号</font>',
                    field : 'ROW_ID'
                },{
                    width : '10%',
                    title : '<font size="3px">医保药品编码</font>',
                    field : 'DRUG_CODE'
                },{
                    width : '16%',
                    title : '<font size="3px">医保药品新增编码</font>',
                    field : 'NEW_DRUG_CODE'
                },{
                    width : '10%',
                    title : '<font size="3px">医保药品名称</font>',
                    field : 'DRUG_NAME'
                },{
                    width : '10%',
                    title : '<font size="3px">药品类别</font>',
                    field : 'DRUG_TYPE'
                },{
                    width : '10%',
                    title : '<font size="3px">限定就医方式</font>',
                    field : 'LIMIT_TREATMENT'
                },{
                    width : '5%',
                    title : '<font size="3px">剂型</font>',
                    field : 'DOSAGE'
                },{
                    width : '5%',
                    title : '<font size="3px">规格</font>',
                    field : 'FORMATS'
                },{
                    width : '5%',
                    title : '<font size="3px">单位</font>',
                    field : 'UNIT'
                },{
                    width : '7%',
                    title : '<font size="3px">价格</font>',
                    field : 'PRICE'
                },{
                    width : '7%',
                    title : '<font size="3px">最小包装</font>',
                    field : 'MIN_PACKAGE'
                },{
                    width : '10%',
                    title : '<font size="3px">给药途径</font>',
                    field : 'ROUTE_MEDICATION'
                },{
                    width : '10%',
                    title : '<font size="3px">生产厂商</font>',
                    field : 'MANUFACTURER'
                },{
                    width : '7%',
                    title : '<font size="3px">自付比例</font>',
                    field : 'SELF_PAY'
                },{
                    width : '15%',
                    title : '<font size="3px">备注</font>',
                    field : 'REMARK'
                },{
                    width : '7%',
                    title : '<font size="3px">操作人</font>',
                    field : 'UPDATE_USER'
                },{
                    width : '12%',
                    title : '<font size="3px">操作时间</font>',
                    field : 'UPDATE_TIME'
                }
        	] ],
        	onBeforeLoad: function (param) {
            	updateDatagridHeader(this);           	
            }
	});	
	
	$('#drugCatalogEditForm').form({
		 url : '${path}/drugCatalog/editDrugCatalog',
		 onSubmit : function() {
              progressLoad();
              var isValid = $(this).form('validate');
              if (!isValid) {
                  progressClose();
              }
              return isValid;
          },
          success : function(result) {
              progressClose();
              result = $.parseJSON(result);
              if(result.success)
          	{
            	  closeWin();
            	  searchDrugCatalogFun();
          	}
              else
          	{
              	parent.$.messager.alert('提示', result.msg, 'warning');
          	}
          }
	 });
	
	$("#delDrugCatalog").click(function(){
		 
		var  drugCatalogChecked = drugCatalogDataGrid.datagrid('getSelections');
		 
		 if(drugCatalogChecked.length<1)
		 {
			 parent.$.messager.alert({
                  title : '提示',
                  msg : '请选择要删除的记录！'
              });
			 return;
		 }
		 parent.$.messager.confirm('询问', '您是否要删除当前记录？', function(flag) {
			 if(!flag)
			 {
			 	return;
			 }
			 
			 var id=[];
			 for(i=0;i<drugCatalogChecked.length;i++)
			 {
			 	id[i]=drugCatalogChecked[i].ID;
			 }
			 
			 progressLoad();
			 $.post('${path }/drugCatalog/deleteDrugCatalog', {
	            id : id
	        }, function(result) {
	            if (result.success) {
	                parent.$.messager.alert('提示', result.msg, 'info');
	                drugCatalogDataGrid.datagrid('reload');
	            }
	            progressClose();
	        }, 'JSON');
		 
	    });
	  });
	
	 $.post('${path }/dictionary/getType', {
		 clum:'daf7294103544be48b9e6ced3e909028'
	 }, function(result) {
		 $.each(result,function(i,r)
		  {			 
			 $('#limitTreatment').append("<option value='"+r.dictCode+"'>"+r.dictName+"</option>");
		  });
	 }, 'JSON');
	
});
function searchDrugCatalogFun() 
{
	drugCatalogDataGrid.datagrid('load', $.serializeObject($('#searchDrugCatalogForm')));
}
function cleanProjectCatalogFun()
{
   $('#searchDrugCatalogForm')[0].reset();
   drugCatalogDataGrid.datagrid('load', {});
}
function openWin(optName)
{
	$(".BgDiv3").css("z-index","100");
    $(".BgDiv3").css({ display: "block", height: $(document).height()});
    $(".DialogDiv3").css("top", "15%");
    $(".DialogDiv3").css("display", "block");
    document.documentElement.scrollTop = 0;
    $('#optName').text(optName);
}
function closeWin()
{
	 $(".BgDiv3").css("display", "none");
     $(".DialogDiv3").css("display", "none");
     $('#drugCatalogEditForm')[0].reset();
     $('#id').val('');
}
function editWin()
{
	var  drugCatalogChecked = drugCatalogDataGrid.datagrid('getSelections');
	if(drugCatalogChecked.length==0)
	 {
		 parent.$.messager.alert({
              title : '提示',
              msg : '请选择要修改的记录！'
          });
		 return;
	 }
	if(drugCatalogChecked.length>1)
	 {
		 parent.$.messager.alert({
              title : '提示',
              msg : '只能修改一条记录！'
          });
		 return;
	 }
	 $.post('${path }/drugCatalog/selectEditDrugCatalog', {
	        id : drugCatalogChecked[0].ID
	    }, function(result) {
	        $('#id').val(result.id);
	        $('#drugCode2').val(result.drugCode);
	        $('#drugName2').val(result.drugName);
	        $('#newdrugCode').val(result.newdrugCode);
	        $('#dosage').val(result.dosage);
	        $('#minPack').val(result.minPack);
	        $('#routeMedication').val(result.routeMedication);
	        $('#formats').val(result.formats);
	        $('#unit').val(result.unit);
	        $('#price').val(result.price);
	        $('#manufacturer').val(result.manufacturer);
	        $('#selfPay').val(result.selfPay);
	        $('#remark').val(result.remark);
	        $('#drugType').val(result.drugType);
	        $('#limitTreatment').val(result.limitTreatment);
	        openWin('修改'); 
	        
	    }, 'JSON');
}
function exportDrugCatalog()
{
	parent.$.messager.alert({
        title : '提示',
        msg : '正在导出数据... 请耐心等待！'
    });
	
	location.href='${path}/drugCatalog/exportDrugCatalog';
	
}
function exportDrugCatalogHistory()
{
	parent.$.messager.alert({
        title : '提示',
        msg : '正在导出数据... 请耐心等待！'
    });
	
	location.href='${path}/drugCatalog/exportDrugCatalogHistory';
}
function exportTemp()
{
	location.href='${path}/drugCatalog/exportTemp';
}
function importExcel()
{
	var imgobjs=document.getElementById("importExcel");
	   var excel=$('#excel').serialize();		   
		   
   	$(imgobjs).fileUpload(
		{
			uploadURL : '${path}/drugCatalog/importExcel',
			singleFileUploads: true,		
			callback : function(
					data) {
			
				if (data != undefined
						&& data!= null) {
					 $.messager.alert( '提示',data.msg, 'warning');
					 searchDrugCatalogFun();
				}; 
			}
		});
}
</script>
</head>
<body>
<div class="xPage">
<jsp:include page="../include/tophead.jsp" />
	<div class="xMain">
		<jsp:include page="../include/leftMenu.jsp" />
<div class="xRightbox">
<!--限适应症用药(违规)-->
<div class="body-width fs_18 bold header-color">药品目录管理</div>
<div class="header-div bg-color1 border-rgt border-lef">
  <form id="searchDrugCatalogForm">
    <table class="ys-table" style="width: 95%">
        <tbody>
        <tr>
            <td class="padding-left10 fs_16">
                	医保药品编码
            </td>
            <td>
                <input type="text" id="drugCode" name="drugCode" style="width: 124px">
            </td>
            <td class="padding-left10 fs_16">
                	医保药品名称
            </td>
            <td>
                <input type="text" id="drugName" name="drugName" style="width: 124px">
            </td>
			 
			
            <td class="td-width"></td>
            <td>
                <input type="button" value="查询" onclick="searchDrugCatalogFun()" class="btn-ok fs_16"/> <input type="button" value="重置" onclick="cleanProjectCatalogFun()" class="btn-cancel fs_16"/>
                
            </td>
            <td style="width: auto"></td>
        </tr>
		 
        </tbody>
    </table>
    </form>
</div>
<table id="btn-table" class="float margin-top10" style="width: 100%;">
    <tr>
        <td>
            <button class="btn-ok fs_16" id="increase" onclick="openWin('新增')">新增</button>
            <button class="btn-ok fs_16" onclick="editWin()">修改</button>
            <button class="btn-ok fs_16" id="delDrugCatalog">删除</button>
        </td>
        
        <td class="right" style="width: 72px">
            <button class="btn-ok fs_16" id="importExcel">导入</button>
        </td>
        <td class="right" style="width: 80px">
            <button class="btn-ok fs_16" onclick="exportTemp()">导出模版</button>
        </td>
        <td class="right" style="width: 80px">
            <button class="btn-ok fs_16" onclick="exportDrugCatalog()">导出明细</button>
        </td>
        <td class="right" style="width: 96px">
            <button class="btn-ok fs_16" style="width: 91px;" onclick="exportDrugCatalogHistory()">查看历史记录</button>
        </td>
    </tr>
</table>
<div class="BgDiv3"></div>
<div class="body-width clear">
    <div style="height: 340px">
        <table id="drugCatalogDataGrid" class="on-style">
            
        </table>
    </div>
    <div class="DialogDiv3" style="display: none">
        <div class="pop-up bg-color1">
            <span class="float pop-up-span width-color dis-block">药品目录管理<span id="optName"></span></span>
        </div>

        <!--table数据-->
        <div class="body-width border-val" style="height: 302px;margin-top: 20px !important;width: 90%;margin-left: 5%;">
             <form id="drugCatalogEditForm" method="post">
	    	<input type="hidden" id="id" name="id" />
            <table class="bz-zd-table ">
                <tbody>
               
                <tr>
                    <td colspan="6" style="text-align: right;color:#DF2828">
                        注：* 为必须项
                    </td>
                </tr>
                <tr>
                    <td>
                        <span>*</span>医保药品编码
                    </td>
                    <td>
                        <input type="text" id="drugCode2" name="drugCode" validateOnCreate="false"  class="easyui-validatebox" onkeyup="this.value=this.value.replace(/[^\u0000-\u00FF]/g,'')" validType="length[1,20]" data-options="required:true"/>
                    </td>
                    <td>
                        		医保药品新增编码
                    </td>
                    <td>
                        <input type="text" id="newdrugCode" name="newdrugCode" onkeyup="this.value=this.value.replace(/[^\u0000-\u00FF]/g,'')" validateOnCreate="false"  class="easyui-validatebox" validType="length[1,20]"/>
                    </td>
                    <td>
                        <span>*</span>医保药品名称
                    </td>
                    <td>
                        <input type="text" id="drugName2" name="drugName" validateOnCreate="false"  class="easyui-validatebox"  validType="length[1,30]" data-options="required:true"/>
                    </td>
                </tr>
                <tr>
                 	<td>
                        	<span>*</span>剂型
                    </td>
                    <td>
                        <input type="text" id="dosage" name="dosage" validateOnCreate="false"  class="easyui-validatebox"  validType="length[1,20]" data-options="required:true"/>
                    </td>
                    <td>
                        	<span>*</span>规格
                    </td>
                    <td>
                        <input type="text" id="formats" name="formats" validateOnCreate="false"  class="easyui-validatebox"  validType="length[1,10]" data-options="required:true"/>
                    </td>
                    <td>
                       	<span>*</span>单位
                    </td>
                    <td>
                        <input type="text" id="unit" name="unit" validateOnCreate="false"  class="easyui-validatebox"  validType="length[1,5]" data-options="required:true"/>
                    </td>                    
                </tr>
                <tr>
                	<td>
                       <span>*</span>价格
                    </td>
                    <td>
                        <input type="text" id="price" name="price" validateOnCreate="false" onkeyup="this.value=this.value.replace(/[^0-9.]/g,'')" class="easyui-validatebox"  data-options="required:true"/>
                    </td>
                    <td>
                       <span>*</span>最小包装
                    </td>
                    <td>
                        <input type="text" id="minPack" name="minPack" validateOnCreate="false"  class="easyui-validatebox" validType="length[1,10]"  data-options="required:true"/>
                    </td>
                    <td>
                       <span>*</span>给药途径
                    </td>
                    <td>
                        <input type="text" id="routeMedication" name="routeMedication" validateOnCreate="false"  class="easyui-validatebox" validType="length[1,10]"  data-options="required:true"/>
                    </td>
                </tr>
                <tr>
                   <td>
                        	<span>*</span>生产厂商
                    </td>
                    <td>
                        <input type="text" id="manufacturer" name="manufacturer" validateOnCreate="false"  class="easyui-validatebox" validType="length[1,50]"  data-options="required:true"/>
                    </td>
                    <td>
                       	<span>*</span>自付比例
                    </td>
                    <td>
                        <input type="text" id="selfPay" name="selfPay" validateOnCreate="false" onkeyup="this.value=this.value.replace(/[^0-9.]/g,'')" class="easyui-validatebox"  data-options="required:true"/>
                    </td>
                	<td>
                       	药品类别
                    </td>
                    <td>
                        <select id="drugType" name="drugType">
                        	<option value="1">西药</option>
                        	<option value="2">中成药</option>
                        	<option value="3">中药饮片</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>限定就医方式</td>
                
                    <td>
                        <select id="limitTreatment"  name="limitTreatment" />
                    </td>
                </tr>
                <tr>
                    <td>备注</td>
                </tr>
                <tr>
                    <td></td>
                    <td colspan="5">
                        <textarea id="remark" name="remark" cols="5" rows="10" style="height: 80px" validateOnCreate="false" class="easyui-validatebox" validType="length[1,100]"></textarea>
                    </td>
                </tr>
               
                <tr>
                    <td colspan="6" class="center-td" style="padding-top: 24px;">
                        <input  class="btn-ok" type="submit" value="确定" style="width: 76px;padding-left:5px;" />
                        <input class="back btn-cancel"  onclick="closeWin()" value="取消" style="width: 76px;padding-left:26px;cursor: pointer;"/>                        
                    </td>
                </tr>
                </tbody>
            </table>
             </form>
        </div>

    </div>
</div>
       </div>
    </div>
</div>
</body>
</html>
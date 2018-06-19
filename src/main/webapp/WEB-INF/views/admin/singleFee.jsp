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
var singleFeeDataGrid;
$(function(){
	importExcel();
	singleFeeDataGrid=$('#singleFeeDataGrid').datagrid({
		url : '${path }/singleFee/searchDrugInfo',
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
                    width : '46%',
                    title : '<font size="3px">药品编码</font>',
                    field : 'DRUG_CODE'
                },{
                    width : '48%',
                    title : '<font size="3px">药品名称</font>',
                    field : 'DRUG_NAME'
                }
        	] ],
        	onBeforeLoad: function (param) {
            	updateDatagridHeader(this);           	
            }
	});	
	
	$('#singleFeeEditForm').form({
		 url : '${path}/singleFee/editDrugInfo',
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
            	  searchSingleFeeFun();
          	}
              else
          	{
              	parent.$.messager.alert('提示', result.msg, 'warning');
          	}
          }
	 });
	
	$("#delSingleFee").click(function(){
		 
		var  singleFeeChecked = singleFeeDataGrid.datagrid('getSelections');
		 
		 if(singleFeeChecked.length<1)
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
			 for(i=0;i<singleFeeChecked.length;i++)
			 {
			 	id[i]=singleFeeChecked[i].ID;
			 }
			 
			 progressLoad();
			 $.post('${path }/singleFee/deleteDrugInfo', {
	            id : id
	        }, function(result) {
	            if (result.success) {
	                parent.$.messager.alert('提示', result.msg, 'info');
	                singleFeeDataGrid.datagrid('reload');
	            }
	            progressClose();
	        }, 'JSON');
		 
	    });
	  });
	
});
function searchSingleFeeFun() 
{
	singleFeeDataGrid.datagrid('load', $.serializeObject($('#searchSingleFeeForm')));
}
function cleanProjectCatalogFun()
{
   $('#searchSingleFeeForm')[0].reset();
   singleFeeDataGrid.datagrid('load', {});
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
     $('#singleFeeEditForm')[0].reset();
     $('#id').val('');
}
function editWin()
{
	var  stepDrugChecked = singleFeeDataGrid.datagrid('getSelections');
	if(stepDrugChecked.length==0)
	 {
		 parent.$.messager.alert({
              title : '提示',
              msg : '请选择要修改的记录！'
          });
		 return;
	 }
	if(stepDrugChecked.length>1)
	 {
		 parent.$.messager.alert({
              title : '提示',
              msg : '只能修改一条记录！'
          });
		 return;
	 }
	 $.post('${path }/singleFee/selectEditDrugInfo', {
	        id : stepDrugChecked[0].ID
	    }, function(result) {
	        $('#id').val(result.id);
	        $('#drugCode2').val(result.drugCode);
	        $('#drugName2').val(result.drugName);		       
	        
	        openWin('修改'); 
	        
	    }, 'JSON');
}
function exportSingleFee()
{
	parent.$.messager.alert({
        title : '提示',
        msg : '正在导出数据... 请耐心等待！'
    });
	
	location.href='${path}/singleFee/exportDrugInfo';
	
}
function exportSingleFeeHistory()
{
	parent.$.messager.alert({
        title : '提示',
        msg : '正在导出数据... 请耐心等待！'
    });
	
	location.href='${path}/singleFee/exportDrugInfoHistory';
}
function exportTemp()
{
	location.href='${path}/singleFee/exportTemp';
}
function importExcel()
{
	var imgobjs=document.getElementById("importExcel");
	   var excel=$('#excel').serialize();		   
		   
   	$(imgobjs).fileUpload(
		{
			uploadURL : '${path}/singleFee/importExcel',
			singleFileUploads: true,		
			callback : function(
					data) {
			
				if (data != undefined
						&& data!= null) {
					 $.messager.alert( '提示',data.msg, 'warning');
					 searchSingleFeeFun();
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
<div class="body-width fs_18 bold header-color">1012/1013_除外表</div>
<div class="header-div bg-color1 border-rgt border-lef">
  <form id="searchSingleFeeForm">
    <table class="ys-table" style="width: 95%">
        <tbody>
        <tr>
            <td class="padding-left10 fs_16">
                	药品编码
            </td>
            <td>
                <input type="text" id="drugCode" name="drugCode" style="width: 124px">
            </td>
            <td class="padding-left10 fs_16">
                	药品名称
            </td>
            <td>
                <input type="text" id="drugName" name="drugName" style="width: 124px">
            </td>			 
			
            <td class="td-width"></td>
            <td>
                <input type="button" value="查询" onclick="searchSingleFeeFun()" class="btn-ok fs_16"/> <input type="button" value="重置" onclick="cleanProjectCatalogFun()" class="btn-cancel fs_16"/>
                
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
            <button class="btn-ok fs_16" id="delSingleFee">删除</button>
        </td>
        
        <td class="right" style="width: 72px">
            <button class="btn-ok fs_16"   id="importExcel">导入</button>
        </td>
        <td class="right" style="width: 80px">
            <button class="btn-ok fs_16" onclick="exportTemp()">导出模版</button>
        </td>
        <td class="right" style="width: 80px">
            <button class="btn-ok fs_16" onclick="exportSingleFee()">导出明细</button>
        </td>
        <td class="right" style="width: 96px">
            <button class="btn-ok fs_16" style="width: 91px;" onclick="exportSingleFeeHistory()">查看历史记录</button>
        </td>
    </tr>
</table>
<div class="BgDiv3"></div>
<div class="body-width clear">
    <div style="height: 340px">
        <table id="singleFeeDataGrid" class="on-style">
            
        </table>
    </div>
    <div class="DialogDiv3" style="display: none">
        <div class="pop-up bg-color1">
            <span class="float pop-up-span width-color dis-block">1012/1013_除外表<span id="optName"></span></span>
        </div>

        <!--table数据-->
         <form id="singleFeeEditForm" method="post">
	    	<input type="hidden" id="id" name="id" />
        <div class="body-width border-val" style="margin-top: 20px !important;width: 90%;margin-left: 5%;">
            
            <table class="bz-zd-table ">
                <tbody>
               
                <tr>
                    <td colspan="6" style="text-align: right;color:#DF2828">
                        注：* 为必须项
                    </td>
                </tr>                
                <tr>
                    <td>
                        <span>*</span>药品编码
                    </td>
                    <td>
                        <input type="text" id="drugCode2" name="drugCode" validateOnCreate="false"  class="easyui-validatebox" onkeyup="this.value=this.value.replace(/[^\u0000-\u00FF]/g,'')" validType="length[1,20]" data-options="required:true"/>
                    </td>                    
                    <td>
                        <span>*</span>药品名称
                    </td>
                    <td>
                        <input type="text" id="drugName2" name="drugName" validateOnCreate="false"  class="easyui-validatebox"  validType="length[1,30]" data-options="required:true"/>
                    </td>
                </tr>                               
               
                <tr>
                   <td></td>
                </tr>
                </tbody>
            </table>
             
        </div>
		<div class="margin-top10"style="width: 94%;margin-left: 3%;" >
            <table class="bz-zd-table " style="width: 96.5% !important;">
                <tbody>
                <tr>
                    <td colspan="2" style="text-align:centre">
                      <input  class="btn-ok" type="submit" value="确定" style="width: 76px;padding-left:5px;" />
                        <input class="back btn-cancel"  onclick="closeWin()" value="取消" style="width: 76px;padding-left:26px;cursor: pointer;"/>                                                </td>
                    </td>
                    <td colspan="1">
                    
                                         
                     </td>
                </tr>
                </tbody>
            </table>
          
        </div>
        </form>
    </div>
</div>
       </div>
    </div>
</div>
</body>
</html>
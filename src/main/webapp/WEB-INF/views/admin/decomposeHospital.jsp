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
var decomposeHospitalDataGrid;
$(function(){
	importExcel();
	decomposeHospitalDataGrid=$('#decomposeHospitalDataGrid').datagrid({
		url : '${path }/decomposeHospital/searchDecomposeHospital',
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
                    width : '24%',
                    title : '<font size="3px">项目编码</font>',
                    field : 'PROJECT_CODE'
                },{
                    width : '24%',
                    title : '<font size="3px">项目名称</font>',
                    field : 'PROJECT_NAME'
                },{
                    width : '24%',
                    title : '<font size="3px">单位</font>',
                    field : 'UNIT'
                },{
                    width : '22%',
                    title : '<font size="3px">项目类别</font>',
                    field : 'PROJECT_TYPE'
                }
        	] ],
        	onBeforeLoad: function (param) {
            	updateDatagridHeader(this);           	
            }
	});	
	
	$('#decomposeHospitalEditForm').form({
		 url : '${path}/decomposeHospital/editDecomposeHospital',
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
            	  searchDecomposeHospitalFun();
          	}
              else
          	{
              	parent.$.messager.alert('提示', result.msg, 'warning');
          	}
          }
	 });
	
	$("#delDecomposeHospital").click(function(){
		 
		var  decomposeHospitalChecked = decomposeHospitalDataGrid.datagrid('getSelections');
		 
		 if(decomposeHospitalChecked.length<1)
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
			 for(i=0;i<decomposeHospitalChecked.length;i++)
			 {
			 	id[i]=decomposeHospitalChecked[i].ID;
			 }
			 
			 progressLoad();
			 $.post('${path }/decomposeHospital/deleteDecomposeHospital', {
	            id : id
	        }, function(result) {
	            if (result.success) {
	                parent.$.messager.alert('提示', result.msg, 'info');
	                decomposeHospitalDataGrid.datagrid('reload');
	            }
	            progressClose();
	        }, 'JSON');
		 
	    });
	  });
	
});
function searchDecomposeHospitalFun() 
{
	decomposeHospitalDataGrid.datagrid('load', $.serializeObject($('#searchDecomposeHospitalForm')));
}
function cleanProjectCatalogFun()
{
   $('#searchDecomposeHospitalForm')[0].reset();
   decomposeHospitalDataGrid.datagrid('load', {});
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
     $('#decomposeHospitalEditForm')[0].reset();
     $('#id').val('');
}
function editWin()
{
	var  decomposeHospitalChecked = decomposeHospitalDataGrid.datagrid('getSelections');
	if(decomposeHospitalChecked.length==0)
	 {
		 parent.$.messager.alert({
              title : '提示',
              msg : '请选择要修改的记录！'
          });
		 return;
	 }
	if(decomposeHospitalChecked.length>1)
	 {
		 parent.$.messager.alert({
              title : '提示',
              msg : '只能修改一条记录！'
          });
		 return;
	 }
	 $.post('${path }/decomposeHospital/selectEditDecomposeHospital', {
	        id : decomposeHospitalChecked[0].ID
	    }, function(result) {
	        $('#id').val(result.id);
	        $('#projectType2').val(result.projectType);
	        $('#projectCode2').val(result.projectCode);
	        $('#projectName2').val(result.projectName);		       
	        $('#unit').val(result.unit);		       
	        
	        openWin('修改'); 
	        
	    }, 'JSON');
}
function exportDecomposeHospital()
{
	parent.$.messager.alert({
        title : '提示',
        msg : '正在导出数据... 请耐心等待！'
    });
	
	location.href='${path}/decomposeHospital/exportDecomposeHospital';
	
}
function exportDecomposeHospitalHistory()
{
	parent.$.messager.alert({
        title : '提示',
        msg : '正在导出数据... 请耐心等待！'
    });
	
	location.href='${path}/decomposeHospital/exportDecomposeHospitalHistory';
}
function exportTemp()
{
	location.href='${path}/decomposeHospital/exportTemp';
}
function importExcel()
{
	var imgobjs=document.getElementById("importExcel");
	   var excel=$('#excel').serialize();		   
		   
   	$(imgobjs).fileUpload(
		{
			uploadURL : '${path}/decomposeHospital/importExcel',
			singleFileUploads: true,		
			callback : function(
					data) {
			
				if (data != undefined
						&& data!= null) {
					 $.messager.alert( '提示',data.msg, 'warning');
					 searchDecomposeHospitalFun();
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
<div class="body-width fs_18 bold header-color">3001_项目对应表</div>
<div class="header-div bg-color1 border-rgt border-lef">
  <form id="searchDecomposeHospitalForm">
    <table class="ys-table" style="width: 95%">
        <tbody>
        <tr>
            <td class="padding-left10 fs_16">
                	项目编码
            </td>
            <td>
                <input type="text" id="projectCode" name="projectCode" style="width: 124px">
            </td>
            <td class="padding-left10 fs_16">
                	项目名称
            </td>
            <td>
                <input type="text" id="projectName" name="projectName" style="width: 124px">
            </td>			 
			<td>
                 	项目类型
             </td>
             <td>
                 <select id="projectType" name="projectType" style="width:100px">
                 	<option value="">请选择</option>
		           	<option value="1">ICU</option>
		           	<option value="2">手术材料项目</option>
		          </select>
             </td>
            <td class="td-width"></td>
            <td>
                <input type="button" value="查询" onclick="searchDecomposeHospitalFun()" class="btn-ok fs_16"/> <input type="button" value="重置" onclick="cleanProjectCatalogFun()" class="btn-cancel fs_16"/>
                
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
            <button class="btn-ok fs_16" id="delDecomposeHospital">删除</button>
        </td>
        
        <td class="right" style="width: 72px">
            <button class="btn-ok fs_16"   id="importExcel">导入</button>
        </td>
        <td class="right" style="width: 80px">
            <button class="btn-ok fs_16" onclick="exportTemp()">导出模版</button>
        </td>
        <td class="right" style="width: 80px">
            <button class="btn-ok fs_16" onclick="exportDecomposeHospital()">导出明细</button>
        </td>
        <td class="right" style="width: 96px">
            <button class="btn-ok fs_16" style="width: 91px;" onclick="exportDecomposeHospitalHistory()">查看历史记录</button>
        </td>
    </tr>
</table>
<div class="BgDiv3"></div>
<div class="body-width clear">
    <div style="height: 340px">
        <table id="decomposeHospitalDataGrid" class="on-style">
            
        </table>
    </div>
    <div class="DialogDiv3" style="display: none">
        <div class="pop-up bg-color1">
            <span class="float pop-up-span width-color dis-block">3001_项目对应表<span id="optName"></span></span>
        </div>

        <!--table数据-->
         <form id="decomposeHospitalEditForm" method="post">
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
                        <span>*</span>项目编码
                    </td>
                    <td>
                        <input type="text" id="projectCode2" name="projectCode" validateOnCreate="false"  class="easyui-validatebox" onkeyup="this.value=this.value.replace(/[^\u0000-\u00FF]/g,'')" validType="length[1,40]" data-options="required:true"/>
                    </td>                    
                    <td>
                        <span>*</span>项目名称
                    </td>
                    <td>
                        <input type="text" id="projectName2" name="projectName" validateOnCreate="false"  class="easyui-validatebox"  validType="length[1,100]" data-options="required:true"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        	单位
                    </td>
                    <td>
                        <input type="text" id="unit" name="unit" validateOnCreate="false"  class="easyui-validatebox"  validType="length[1,10]"/>
                    </td>                    
                    <td>
                        	项目类型
                    </td>
                    <td>
                        <select id="projectType2" name="projectType">
				           	<option value="1">ICU</option>
				           	<option value="2">手术材料项目</option>
		                </select>
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
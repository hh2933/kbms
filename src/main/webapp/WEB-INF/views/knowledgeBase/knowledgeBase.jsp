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
    border-width: 0px;
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
	var filterObjcts=[];	
	var tableObjcts=[];
	var knowledgeBaseDataGrid;
	$(function() {
		$("a,button").focus(function(){this.blur()});
		getRuleTableInfo();
	});	
	
	 $(function(){
		  
		   var imgobjs=document.getElementById("daoru");
		   var excel=$('#excel').serialize();
		
			 
			   
			   
			    	$(imgobjs).fileUpload(
						{
							uploadURL : '${path}/knowledgeBase/importExcel?id='+pageId,
							singleFileUploads: true,		
							callback : function(
									data) {
							
								if (data != undefined
										&& data!= null) {
									 $.messager.alert( '提示',data.msg, 'warning');
									 loadKnowledgeBaseDataGrid();
								}; 
							}
						});
			  
			 
		   
		  
	   });  	
	
	
	function getRuleTableInfo()
	{
		$.post('${path }/rule/selectRuleTableInfoForMenuId', {
			menuId:pageId
		},function(result) {
			tableObjcts = result;
			$('#titleDiv').text(tableObjcts[0].MENU_NAME);
			$('#editTitle').text(tableObjcts[0].MENU_NAME);
			 $.each(result,function(index,d)
				{
					 if(d.IS_FILTER=="1")
					 {
						 filterObjcts.push(d);
					 }					 
			 });
			 createFilterCondition();
			 createEditCondition();
			 setSelectOption();
			 loadKnowledgeBaseDataGrid();
			 
		}, 'JSON');
	}
	function createFilterCondition()
	{		
		var tr="";
		$.each(filterObjcts,function(index,d)
		{
			if(index%3==0)
			{
			  tr="<tr>";
			}
			tr+=createFormControl(d,"");
			if((index+1)%3==0||(index+1)==filterObjcts.length)
			{				
				if(index<3)
				{
					tr+="<td class='td-width'></td>";
					tr+="<td><input type='button' value='查询' class='btn-ok fs_16' onclick='searchData()'/> <input type='button' class='btn-cancel fs_16' value='重置' onclick='cleanData()'/></td>";
					tr+="<td style='width: auto'></td>";
				}
				tr+="</tr>";
				$("#filterTable").append(tr);
			}
		});
	}
	function createEditCondition()
	{
		var tr="";
		$.each(tableObjcts,function(index,d)
		{
			if(index%3==0)
			{
			  tr="<tr>";
			}
			tr+=createFormControl(d,"_edit");
			if((index+1)%3==0||(index+1)==tableObjcts.length)
			{
				if((index+1)%3!=0)
				{
					tr+="<td></td><td></td>";
				}
				tr+="</tr>";
				$("#editTable").append(tr);
			}
		});
		$("#editTable").append("<tr><td></td></tr> ");
	}
	function createFormControl(obj,suffix)
	{
		
		var redFlag="";
		var tdStyle="";
		
		if(suffix!="")
		{
			if(obj.IS_UNIQUE=="1")
			{
				redFlag="<span style='color:red'>*</span>";
			}			
		}
		else
		{
			tdStyle=" class='padding-left10 fs_16'";
		}
		
		var td="<td"+tdStyle+">"+obj.TH_NAME+redFlag+"</td><td>";
		if(obj.HTML_INPUT=="text")
		{
			var numberFormate="";
			var maxLength="";
			if(obj.COLUMN_TYPE=="number")
			{
				numberFormate="onkeyup=\"this.value=this.value.replace(/[^0-9.]/g,'')\" onblur=\"this.value=this.value.replace(/[^0-9.]/g,'')\"";
			}
			td+="<input type='text' "+numberFormate+" id='"+obj.COLUMN_NAME+suffix+"'  maxlength='50' style='width: 124px'/>";
		}
		else if(obj.HTML_INPUT=="select")
		{
			td+="<select  id='"+obj.COLUMN_NAME+suffix+"' style='width: 124px'><option value=''></option></select>";			
		}
		else
		{
			td+="<input  id='"+obj.COLUMN_NAME+suffix+"'   placeholder='点击选择时间' onclick=WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd'}) readonly='readonly' style='width: 124px'/>";
		}
		td+="</td>";
		return td;
	}
	function searchData()
	{
		loadKnowledgeBaseDataGrid();
	}
	function loadKnowledgeBaseDataGrid()
	{
		var columns =[];
		var selectColumns=[];
		var columnsObj;
		var columnWidth= Math.ceil(100/tableObjcts.length)+1+"%";
		columnsObj = new Object();
		columnsObj.checkbox="true";
		columnsObj.field="fc";
		columns.push(columnsObj);
		$.each(tableObjcts,function(index,d)
		{
			columnsObj = new Object();
			columnsObj.width=columnWidth;
			 columnsObj.title=d.TH_NAME;
			 columnsObj.field=d.COLUMN_NAME.toUpperCase();
			 if(d.HTML_INPUT=="select")
			 {
				 columnsObj.formatter=function(value, row, index) {
					var options= $('#'+d.COLUMN_NAME+"_edit option");
					var showValue="";
					
				    for(var i=0;i<options.length;i++)
					{
						if(value==options[i].value)
						{
							showValue= options[i].text;
							break;
						}
					}
					 return showValue;
					 }
			 }
			 columns.push(columnsObj);
			 selectColumns.push(d.COLUMN_NAME);
		});		
		 
		knowledgeBaseDataGrid = $('#knowledgeBaseDataGrid').datagrid({			
			fit : true,
	        striped : true,
	        pagination : true,
	        remoteFilter: true,
	        nowrap : false,	    
	        loader : function(param,success,error){
	        	var columnName=[];
	     		var columnValue=[];
	     		var tableName=tableObjcts[0].TABLE_NAME;
	     		$.each(filterObjcts,function(index,d)
	     		{
	     			if($('#'+d.COLUMN_NAME).val()!="" && $('#'+d.COLUMN_NAME).val()!=null)
	     			{
	     				columnName.push(d.COLUMN_NAME);
	     				columnValue.push($('#'+d.COLUMN_NAME).val());
	     			}
	     		});
	        	
	        	var obj = new Object();
	        	obj.page=param.page;
	        	obj.rows=param.rows;
	        	obj.sort=param.sort;
	        	obj.order=param.order;
	        	obj.tableName = tableName;
	        	obj.columnName=columnName;
	        	obj.columnValue=columnValue;
	        	obj.selectColumns=selectColumns;
	        	
	        	 $.ajax({
		             url:"${path}/knowledgeBase/searchKnowledgeBase",
		             data:JSON.stringify(obj),
		             type:"post",
		             dataType:"json",
		             contentType:'application/json;charset=UTF-8',
		             success: function(data){
		            	 success(data);
		             }
		         }) 
	        },
	        pageSize : 10,
	        pageList : [ 10, 20, 30, 40, 50, 100 ],
	        columns : [columns],
            onBeforeLoad: function (param) {
            	  updateDatagridHeader(this);
            	}
		});	
	}
	function cleanData()
	{
		$('#searchKnowledgeBaseForm')[0].reset();
		loadKnowledgeBaseDataGrid();
	}
	function setSelectOption()
	{		
		
		$.each(tableObjcts,function(index,d)
		{
			if(d.HTML_INPUT=="select")
			{			
				 $.post('${path }/dictionary/getType', {
					 clum:d.DICT_TYPE
				 }, function(result) {
					 $.each(result,function(i,r)
					  {
						 if($('#'+d.COLUMN_NAME))
						 {
							 $('#'+d.COLUMN_NAME).append("<option value='"+r.dictCode+"'>"+r.dictName+"</option>");
						 }
						 $('#'+d.COLUMN_NAME+"_edit").append("<option value='"+r.dictCode+"'>"+r.dictName+"</option>");
					  });
				 }, 'JSON');
			}
			
		});		
		
	}
	
	function saveKnowledgeBase()
	{
		var validateFlag=true;
		var editObjcts=[];
		var obj;
		for(var i=0;i<tableObjcts.length;i++)
		{
			if(tableObjcts[i].IS_UNIQUE=="1" && $("#"+tableObjcts[i].COLUMN_NAME+"_edit").val()=="")
			{				
				validateFlag=false;
				parent.$.messager.alert({
		               title : '提示',
		               msg : tableObjcts[i].TH_NAME+'不能为空！'
		           });
				break;				
			}
			
			obj = new Object();
			obj.columnName=tableObjcts[i].COLUMN_NAME;
			obj.columnValue=$("#"+tableObjcts[i].COLUMN_NAME+"_edit").val();
			obj.isUnique=tableObjcts[i].IS_UNIQUE;
			editObjcts.push(obj);			
		}
		if(!validateFlag)
		{
			return;
		}
		var editKnowledgeBase = new Object();
		editKnowledgeBase.editKnowledgeBaseList=editObjcts;
		editKnowledgeBase.tableName=tableObjcts[0].TABLE_NAME;
		editKnowledgeBase.id=$('#id').val();
		progressLoad();
		$.ajax({
            url:"${path}/knowledgeBase/editKnowledgeBase",
            data:JSON.stringify(editKnowledgeBase),
            type:"post",
            dataType:"json",
            contentType:'application/json;charset=UTF-8',
            success: function(result){
            	progressClose();
            	if (result.success) {
	                parent.$.messager.alert('提示', result.msg, 'info');
	                knowledgeBaseDataGrid.datagrid('reload');
	                closeWin();
	            }
           	 	else
           	  	{
             	  parent.$.messager.alert('提示', result.msg, 'warning');
           	    }
            }
        });
		
	}
	function delKnowledgeBase()
	{
		var  knowledgeBaseChecked = knowledgeBaseDataGrid.datagrid('getSelections');
		if(knowledgeBaseChecked.length==0)
		 {
			 parent.$.messager.alert({
	              title : '提示',
	              msg : '请选择要删除的记录！'
	          });
			 return;
		 }
		
		 var id=[];
		 for(i=0;i<knowledgeBaseChecked.length;i++)
		 {
		 	id[i]=knowledgeBaseChecked[i].ID;
		 }

		parent.$.messager.confirm('询问', '您是否要删除当前记录？', function(flag) {
			if(!flag)
		 	{
		 		return;
		 	}
			
			progressLoad();
			$.post('${path }/knowledgeBase/delKnowledgeBase', {
	            id : id,
	            tableName : tableObjcts[0].TABLE_NAME
	        }, function(result) {
	        	progressClose();
	            if (result.success) {
	                parent.$.messager.alert('提示', result.msg, 'info');
	                knowledgeBaseDataGrid.datagrid('reload');
	                clearAndDisabled();
	            }
	            
	        }, 'JSON');
			
		});
		
	}
	function exportExcel() {
		location.href='${path}/knowledgeBase/exportExcel?id='+pageId;
		 
	}
	function searchHistory()
	{
		parent.$.messager.alert({
            title : '提示',
            msg : '正在导出数据... 请耐心等待！'
        });
		
		location.href='${path}/knowledgeBase/exportKnowledgeBaseHistory?menuId='+pageId;
		
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
     $('#editKnowledgeBaseForm')[0].reset();
	 $('#id').val("");
}
function editWin()
{
	var  knowledgeBaseChecked = knowledgeBaseDataGrid.datagrid('getSelections');
	if(knowledgeBaseChecked.length==0)
	 {
		 parent.$.messager.alert({
              title : '提示',
              msg : '请选择要修改的记录！'
          });
		 return;
	 }
	if(knowledgeBaseChecked.length>1)
	 {
		 parent.$.messager.alert({
              title : '提示',
              msg : '只能修改一条记录！'
          });
		 return;
	 }
	$("#id").val(knowledgeBaseChecked[0].ID);
	$.each(tableObjcts,function(index,d)
	{
		$('#'+d.COLUMN_NAME+"_edit").val(eval("knowledgeBaseChecked[0]."+d.COLUMN_NAME.toUpperCase()));
	});
	openWin("修改");
}
</script>
</head>
<body oncontextmenu="return false" onselectstart="return false" oncopy="return false">
<div class="xPage">
<jsp:include page="../include/tophead.jsp" />
	<div class="xMain">
		<jsp:include page="../include/leftMenu.jsp" />
<div class="xRightbox">
<!--限适应症用药(违规)-->
<div class="body-width fs_18 bold header-color" id="titleDiv"></div>
<div class="header-div bg-color1 border-rgt border-lef">
<form id="searchKnowledgeBaseForm">
    <table id="filterTable" class="ys-table" style="width: 95%">
        
    </table>
    </form>
</div>
<table id="btn-table" class="float margin-top10" style="width: 100%;">
    <tr>
        <td>
            <button onclick="openWin('新增')" class="btn-ok fs_16" id="increase">新增</button>
            <button onclick="editWin()" class="btn-ok fs_16" >修改</button>
            <button onclick="delKnowledgeBase()" class="btn-ok fs_16" >删除</button>
        </td>
        <td class="right" style="width: 80px"><button class="btn-ok fs_16" id="daoru"  name="daoru"  >导入</button></td>
        <td class="right"  style="width: 80px">            
            <button class="btn-ok fs_16" id="daochu"  onclick="exportExcel()" >导出模板</button>
        </td>
        <td class="right"  style="width: 98px">            
            <button class="btn-ok fs_16" id="searchHistory"  onclick="searchHistory()" style="width: 91px;">查看历史记录</button>
        </td>
    </tr>
</table>
<div class="BgDiv3"></div>
<div class="body-width clear">
    <div  style="height: 440px">
        <table id="knowledgeBaseDataGrid" class="on-style">
            
        </table>
    </div>
    <!--  
    <div class="fw-target-div" style="height: 440px;overflow-x: auto    ">
    <form id="editKnowledgeBaseForm">
      <input type="hidden" id="id" name="id"/>
        <table class="input-table" id="editTable">
            
        </table>
        </form>
        <div class="hr-div">
        	<input class="btn-ok fs_16" type="button" value="保存" onclick="saveKnowledgeBase()"/>
			<input class="btn-ok fs_16" type="button" value="删除" onclick="delKnowledgeBase()"/>
           
        </div>
    </div>
    -->
    <div class="DialogDiv3" style="display: none">
        <div class="pop-up bg-color1">
            <span class="float pop-up-span width-color dis-block"><span id="editTitle"></span><span id="optName"></span></span>
        </div>

        <!--table数据-->
        <form id="editKnowledgeBaseForm" method="post">
	    	<input type="hidden" id="id" name="id" />
        <div class="body-width border-val" style="margin-top: 20px !important;width: 90%;margin-left: 5%;">
             
            <table class="bz-zd-table " id="editTable">
                <tbody>
               
                <tr>
                    <td colspan="6" style="text-align: right;color:#DF2828">
                        注：* 为必须项
                    </td>
                </tr>
               
                </tbody>
            </table>
             
        </div>
		 <div class="margin-top10"style="width: 94%;margin-left: 3%;" >
            <table class="bz-zd-table " style="width: 96.5% !important;">
                <tbody>
                <tr>
                    <td colspan="2" style="text-align:centre">
                      <input  class="btn-ok" type="button" onclick="saveKnowledgeBase()" value="确定" style="width: 76px;padding-left:5px;" />
                        <input class="back btn-cancel"  onclick="closeWin()" value="取消" style="width: 76px;padding-left:26px;cursor: pointer;"/>                         </td>
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
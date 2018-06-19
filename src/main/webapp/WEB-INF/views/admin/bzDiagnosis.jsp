<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<!DOCTYPE html>
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
    var bzDiagnosisDataGrid;
   
   
    $(function() {
    	
        $.extend($.fn.validatebox.defaults.rules, {  
     		  account: {//param的值为[]中值  
     	        validator: function (value, param) {  
     	            if (value.length < param[0] || value.length > param[1]) {  
     	                $.fn.validatebox.defaults.rules.account.message = '该项长度必须在' + param[0] + '至' + param[1] + '范围';  
     	                return false;  
     	            } else {  
     	            	var reg = new RegExp("[\\u4E00-\\u9FFF]+","g");
     	                if (reg.test(value)) {  
     	                    $.fn.validatebox.defaults.rules.account.message = '该项不能输入中文';  
     	                    return false;  
     	                } else {  
     	                    return true;  
     	                }  
     	            }  
     	        }, message: ''  
     	    }  
          });
   
     
        function checkCombobox(comboboxId)
        {
        	var inputValue = $(comboboxId).combobox('getValue');
        	var _options = $(comboboxId).combobox('options');
        	var _data = $(comboboxId).combobox('getData');
        	var flag = false;
        	for (var i = 0; i < _data.length; i++)
        	{  
                if (_data[i][_options.valueField] == inputValue) 
                {  
                	flag=true;  
                    break;  
                }  
            } 
        	if(!flag)
        	{
        		$(comboboxId).combobox('setValue', '');
        	}
        }

        importExcel();
        bzDiagnosisDataGrid = $('#bzDiagnosisDataGrid').datagrid({
            url : '${path }/diagnosisBZ/bzDiagnosisDataGrid',
            fit : true,
            striped : true,
           // rownumbers : true,
            pagination : true,
           //singleSelect : true,
          //  idField : 'id',
            sortName : 'createTime',
	        sortOrder : 'asc',
            pageSize : 20,
            pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
            columns : [ [ {
            	field:'ck',
            	checkbox : true,
            	 width : ''
             },{
                width : '7%',
                title : '<font size="3px">序号</font>',
                field : 'ROW_ID'
             
            }, {
                width : '15%',
                title : '<font size="3px">医保诊断编码</font>',
                field : 'diagnosisCode'
            
            },{
                width : '15%',
                title : '<font size="3px">医保诊断名称</font>',
                field : 'diagnosisName'
          
            },  {
                width : '15%',
                title : '<font size="3px">标准诊断编码</font>',
                field : 'bzdiagnosisCode',
           
            }, {
                width : '15%',
                title : '<font size="3px">标准诊断名称</font>',
                field : 'bzdiagnosisName',
            
            },{
                width : '10%',
                title : '<font size="3px">备注</font>',
                field : 'remark',
               
            }, 
            {
                width : '10%',
                title : '<font size="3px">操作人</font>',
                field : 'updateUser',
             
            },{
                width : '12%',
                title : '<font size="3px">操作时间</font>',
                field : 'updateTime',
           
            }] ],
            onBeforeLoad: function (param) {
            	updateDatagridHeader(this);        	
            	
            },
           
        });
        
    
    	$('#diagnosisEditForm').form({
   		 url :'${path }/diagnosisBZ/updateDiagnosisBZ',
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
               	   hideDiv2();
               	bzDiagnosisDataGrid.datagrid('load', $.serializeObject($('#searchForm')));
           	 $.messager.alert( '提示',result.msg, 'warning');
              	}
                  else
              	{
                  	parent.$.messager.alert('提示', result.msg, 'warning');
              	}
              }
   	 });
     
    	$('#diagnosisAddForm').form({
   		 url :'${path }/diagnosisBZ/updateDiagnosisBZ',
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
                	
               	   hideDiv1();
               	bzDiagnosisDataGrid.datagrid('load', $.serializeObject($('#searchForm')));
           	 $.messager.alert( '提示',result.msg, 'warning');
              	}
                  else
              	{
                  	parent.$.messager.alert('提示', result.msg, 'warning');
              	}
              }
   	 });
     
    });
    

    
    function deleteFun() {
       /*  if (id == undefined) {//点击右键菜单才会触发这个
            var rows = bzDiagnosisDataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {//点击操作里面的删除图标会触发这个
            bzDiagnosisDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        } */
   	 var ss = "";
     var rows = $('#bzDiagnosisDataGrid').datagrid('getSelections');
     if(rows.length<1){
    	 $.messager.alert( '提示', "至少选一行", 'warning');
    	 return;
     }else{
     for(var i=0; i<rows.length; i++){
         var row = rows[i].id;
         if(i==0){
        	 
        	 ss=ss+row; 
         }else{
         ss=ss+","+row;
         }
     }
     }  
        parent.$.messager.confirm('询问', '您是否要删除当前记录？', function(b) {
            if (b) {
                var currentUserId = '${sessionInfo.id}';/*当前登录用户的ID*/
                for(var i=0; i<rows.length; i++){
                    var row = rows[i].id;
                    if (currentUserId == row) {
                    	 parent.$.messager.show({
                             title : '提示',
                             msg : '不可以删除自己！'
                         });
                    }
                }
                if (b) {
                    progressLoad();
                    $.post('${path }/diagnosisBZ/deleteDiagnosisBZ', {
                        id : ss
                    }, function(result) {
                        if (result.success) {
                            parent.$.messager.alert('提示', result.msg, 'info');
                            bzDiagnosisDataGrid.datagrid('reload');
                        }
                        progressClose();
                    }, 'JSON');
                }
            }
        });
    }
    
    function editDiagnosisFun() {
       	var id;
   	 var ss = [];
        var rows = $('#bzDiagnosisDataGrid').datagrid('getSelections');
        if(rows.length>1){
       	 $.messager.alert( '提示', "只能选一行 ", 'warning');
       	 return 
        }else if(rows.length<1){
       	 $.messager.alert( '提示', "请选一行", 'warning');
       	 return 
        }else{
       	 id=rows[0].id;
        }       
    	 $.post('${path }/diagnosisBZ/editPage', {
    		 Id : id
           }, function(result) {
          	 showDiv2(); 
               $('#id').val(result.id);
               $('#diagnosisCodeEdit').val(result.diagnosisCode);
               $('#diagnosisNameEdit').val(result.diagnosisName);
               $('#bzdiagnosisCodeEdit').val(result.bzdiagnosisCode);
               $('#bzdiagnosisNameEdit').val(result.bzdiagnosisName);
               $("#remarkEdit").val(result.remark);
           }, 'JSON');
        
    }
    
    function searchFun() {
        bzDiagnosisDataGrid.datagrid('load', $.serializeObject($('#searchForm')));
    }
    function cleanFun() {
        
        $('#searchForm')[0].reset();
        bzDiagnosisDataGrid.datagrid('load', {});
    }

    function showDiv2()
	 {
	 	$(".BgDiv2").css("z-index","100");
	     $(".BgDiv2").css({ display: "block", height: $(document).height()});
	     $(".DialogDiv2").css("top", "5%");
	     $(".DialogDiv2").css("display", "block");
	   //  document.documentElement.scrollTop = 0;
	 }
    function hideDiv2()
    {
    	 $(".BgDiv2").css("display", "none");
         $(".DialogDiv2").css("display", "none"); 
    }
    function showDiv1()
	 {
	 	$(".BgDiv1").css("z-index","100");
	     $(".BgDiv1").css({ display: "block", height: $(document).height()});
	     $(".DialogDiv1").css("top", "5%");
	     $(".DialogDiv1").css("display", "block");
	   //  document.documentElement.scrollTop = 0;
	      
	 }
    function hideDiv1()
    {
    	 $(".BgDiv1").css("display", "none");
         $(".DialogDiv1").css("display", "none"); 
    }
    function importExcel()
    {
    	var imgobjs=document.getElementById("daoru");
    	   var excel=$('#excel').serialize();		   
    		   
       	$(imgobjs).fileUpload(
    		{
    			uploadURL : '${path}/diagnosisBZ/importExcel',
    			singleFileUploads: true,		
    			callback : function(
    					data) {
    			
    				if (data != undefined
    						&& data!= null) {
    					 $.messager.alert( '提示',data.msg, 'warning');
    					 searchFun();
    				}; 
    			}
    		});
    };
    function exportBzDiagnosis()
    {
    	parent.$.messager.alert({
            title : '提示',
            msg : '正在导出数据... 请耐心等待！'
        });
    	
    	location.href='${path}/diagnosisBZ/exportBzDiagnosis';
    	
    }
    function exportBzDiagnosisHistory()
    {
    	parent.$.messager.alert({
            title : '提示',
            msg : '正在导出数据... 请耐心等待！'
        });
    	
    	location.href='${path}/diagnosisBZ/exportBzDiagnosisHistory';
    }
    function exportTemp()
    {
    	location.href='${path}/diagnosisBZ/exportTemp';
    }
</script>
</head>
<body>
<div class="xPage">
<jsp:include page="../include/tophead.jsp" />
	
	 <div class="xMain">
	<jsp:include page="../include/leftMenu.jsp" />
	 <div class="xRightbox">

	 
	<div class="body-width fs_18 bold header-color">诊断标准映射编码管理</div>
<div class="header-div bg-color1 border-rgt border-lef">
 <form action="" id="searchForm">
  <div  style="width: 80%;float: left">
    <table class="ys-table" style="width: 95%">
        <tbody>
        <tr>
            <td class="padding-left10 fs_16">
                         医保诊断编码
            </td>
            <td>
               <input name="diagnosisCode"  id="" style="width:124px;" />
            </td>

            <td class="td-width fs_16">
                             医保诊断名称
            </td>
            <td>
               <input name="diagnosisName"  id="" style="width:124px;" />
            </td>
           
      
            <td class="td-width"></td>
            <td>
        	<input type="button" value="查询" onclick="searchFun()" class="btn-ok fs_16"/> <input type="button" value="重置" onclick="cleanFun()" class="btn-cancel fs_16"/>
              
            </td>
            <td style="width: auto"></td>
        </tr>
        </tbody>
    </table>
    </div>
    </form>
</div>
   <table id="btn-table" class="float margin-top10"  style="width: 100%" >
    <tr>
        <td>
            <button onclick="" class="btn-ok fs_16" id="increase">新增</button>
              <button onclick="editDiagnosisFun()" class="btn-ok fs_16" >修改</button>
              <button onclick="deleteFun()" class="btn-ok fs_16" id="delete">删除</button>
        </td>
   
        <td class="right" style="width: 72px"><button class="btn-ok fs_16" id="daoru"  name="daoru"  >导入</button></td>
        <td class="right" style="width: 80px">
            <button class="btn-ok fs_16" onclick="exportTemp()">导出模版</button>
        </td>
        <td class="right" style="width: 80px">
            <button class="btn-ok fs_16" onclick="exportBzDiagnosis()">导出明细</button>
        </td>
        <td class="right" style="width: 96px">
            <button class="btn-ok fs_16" style="width: 91px;" onclick="exportBzDiagnosisHistory()">查看历史记录</button>
        </td>
        
    </tr>
</table>

	<div class="body-width   margin-top15" style="height: 340px; overflow-x: auto;">
		<table class="sf-msg-table border-val" id="bzDiagnosisDataGrid"style="width: 100%">
						
        </table>
	</div>


	<!--弹出层-->
    <div class="BgDiv1"></div>
    <div class="DialogDiv1" style="display: none">
        <div class="pop-up bg-color1">
            <span class="float pop-up-span width-color dis-block">诊断标准映射编码管理新增</span>
        </div>

        <!--table数据-->
        <form id="diagnosisAddForm" method="post">
        <div class="body-width border-val" style="height: 250px;margin-top: 20px !important;width: 94%;margin-left: 3%;">
            <table class="bz-zd-table">
                <tbody>
                <tr>
                    <td>
                        <span>*</span>医保诊断编码
                    </td>
                    <td>
                      <input name="diagnosisCode"  id="diagnosisCodeAdd" type="text" class="easyui-validatebox" validateOnCreate="false"
                        validType="account[1,40]"  data-options="required:true" >
                    </td>
                    <td>
                        <span >*</span>医保诊断名称
                    </td>
                    <td >
                       <input name="diagnosisName" type="text"  id="diagnosisNameAdd" class="easyui-validatebox" validateOnCreate="false"
                         validType="length[1,50]"   data-options="required:true" >
                    </td>
                  
                    
                </tr>
                <tr>
                    <td>
                        <span >*</span>标准诊断编码
                    </td>
                    <td>
                      <input name="bzdiagnosisCode"  id="bzdiagnosisCodeAdd"  type="text" validateOnCreate="false"
                       class="easyui-validatebox" validType="account[1,40]"  data-options="required:true">
                    </td>
                      <td>
                        <span>*</span>标准诊断名称
                    </td>
                    <td>
                      <input name="bzdiagnosisName"  id="bzdiagnosisNameAdd"   type="text" validateOnCreate="false"
                       class="easyui-validatebox" validType="length[1,50]"  data-options="required:true">
                    </td>
                </tr>
                 <tr >
                      <td >
                        <span></span>备注
                    </td>
                    <td colspan="5" >
                      <textarea   style="margin-top: -0px;"  style="height: 80px" maxlength="50"   id="remarkAdd" name="remark" cols="160" rows="5"></textarea>
                    </td>
                 </tr>
                <!-- <tr>
                <td>
                        <span class="bz-zd-table-span">*</span>操作人
                    </td>
                    <td>
                           <input name="updateUser"  id="updateUserAdd"   type="password" validateOnCreate="false"
                       class="easyui-validatebox" validType="length[1,16]"  data-options="required:true">   
                    </td>
                    <td>
                        <span class="bz-zd-table-span"></span>操作时间
                    </td>
                    <td>
                        <input type="text" name="updateTime"   id="updateTimeAdd"  validateOnCreate="false"
                           />
                    </td>
              
                    
                </tr> -->
    
                </tbody>
            </table>
        </div>
        
        
        <div class="margin-top10"style="width: 94%;margin-left: 3%;" >
            <table class="bz-zd-table " style="width: 96.5% !important;">
                <tbody>
                <tr>
                    <td style="text-align: left;color:#DF2828">
                        注：( * 为必须项)
                    </td>
                    <td colspan="2" class="center-td" style="text-align:center;">    
                         <input id="ok" type="submit"  value="确认" class="btn-ok"style="width: 76px;padding-left:5px;" />
                        <input id="back" type="button" value="取消" class="back btn-cancel"  style="width: 76px; cursor: pointer;background-color: #A3A2A2;"/>
                    
                    </td>
                    <td colspan="1">
                    
                       
                    </td>
                </tr>
                </tbody>
            </table>
          
        </div>
        </form>
    </div>
    
       <!--弹出层-->
    <div class="BgDiv2"></div>
    <div class="DialogDiv2" style="display: none">
        <div class="pop-up bg-color1">
            <span class="float pop-up-span width-color dis-block">诊断标准映射编码管理修改</span>
        </div>

        <!--table数据-->
        <form id="diagnosisEditForm" method="post">
        <div class="body-width border-val" style="height: 250px;margin-top: 20px !important;width: 94%;margin-left: 3%;">
               <table class="bz-zd-table">
                <tbody>
                <tr>
                    <td>
                        <span class="bz-zd-table-span">*</span>医保诊断编码
                    </td>
                    <td>
                    <input name="id"  id="id" type="hidden"  >
                      <input name="diagnosisCode"  id="diagnosisCodeEdit" type="text" class="easyui-validatebox" validateOnCreate="false"
                        validType="account[1,40]"  data-options="required:true" >
                    </td>
                    <td>
                        <span class="bz-zd-table-span">*</span>医保诊断名称
                    </td>
                    <td >
                       <input name="diagnosisName" type="text"  id="diagnosisNameEdit" class="easyui-validatebox" validateOnCreate="false"
                         validType="length[1,50]"   data-options="required:true" >
                    </td>
                  
                    
                </tr>
                <tr>
                    <td>
                        <span class="bz-zd-table-span">*</span>标准诊断编码
                    </td>
                    <td>
                      <input name="bzdiagnosisCode"  id="bzdiagnosisCodeEdit"  type="text" validateOnCreate="false"
                       class="easyui-validatebox" validType="account[1,40]"  data-options="required:true">
                    </td>
                      <td>
                        <span class="bz-zd-table-span">*</span>标准诊断名称
                    </td>
                    <td>
                      <input name="bzdiagnosisName"  id="bzdiagnosisNameEdit"   type="text" validateOnCreate="false"
                       class="easyui-validatebox" validType="length[1,50]"  data-options="required:true">
                    </td>
                </tr>
                 <tr >
                      <td >
                        <span class="bz-zd-table-span"></span>备注
                    </td>
                    <td colspan="5">
                      <textarea   style="margin-top: -0px;"   style="height: 80px" maxlength="50"   id="remarkEdit" name="remark" cols="160" rows="5"></textarea>
                    </td>
                 </tr>
                <!-- <tr>
                <td>
                        <span class="bz-zd-table-span">*</span>操作人
                    </td>
                    <td>
                           <input name="updateUser"  id="updateUserEdit"   type="password" validateOnCreate="false"
                       class="easyui-validatebox" validType="length[1,16]"  data-options="required:true">   
                    </td>
                    <td>
                        <span class="bz-zd-table-span"></span>操作时间
                    </td>
                    <td>
                        <input type="text" name="updateTime"   id="updateTimeEdit"  validateOnCreate="false"
                           />
                    </td>
              
                    
                </tr> -->
    
                </tbody>
            </table>
        </div>
        
        
        
        <div class="margin-top10"style="width: 94%;margin-left: 3%;" >
            <table class="bz-zd-table " style="width: 96.5% !important;">
                <tbody>
                <tr>
                    <td style="text-align: left;color:#DF2828">
                        注：( * 为必须项)
                    </td>
                    <td colspan="2"style="text-align:center;">    
                    
                         <input id="ok" type="submit"  value="确认"class="btn-ok"style="width: 76px;padding-left:5px;"/>
                        <input id="back" type="button" value="取消" class="back btn-cancel"  style="width: 76px; cursor: pointer;background-color: #A3A2A2;"/>
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
<script>
$(function () {
    //全选,取消全选
    $(document).on("click","#chk-qx",function(){
        if($(this).hasClass("checked")==false){
            $(this).closest(".yz-msg-table").find("input[type=checkbox]").prop("checked",true);
            $(this).addClass("checked");
        }else{
            $(this).closest(".yz-msg-table").find("input[type=checkbox]").prop("checked",false);
            $(this).removeClass("checked");
        }
    });

    <!--增加-弹出层-->
    $("#increase").click(function () {
        // var str = "我是弹出对话框";
        $(".BgDiv1").css("z-index","100");
        $(".BgDiv1").css({ display: "block", height: $(document).height()});
        $(".DialogDiv1").css("top", "5%");
        $(".DialogDiv1").css("display", "block");
        $('#diagnosisAddForm input').val('');
        $('#diagnosisAddForm textarea').val('');
      
        $("#ok").val("确定");
        $("#back").val("取消");
       
       
        document.documentElement.scrollTop = 0;
    });
   /*  $("#update").click(function () {
        // var str = "我是弹出对话框";
        $(".BgDiv2").css("z-index","100");
        $(".BgDiv2").css({ display: "block", height: $(document).height()});
        $(".DialogDiv2").css("top", "5%");
        $(".DialogDiv2").css("display", "block");
        $('#userAddForm input').val('');
      
        $("#ok").val("确定");
        $("#back").val("取消");
       
       
        document.documentElement.scrollTop = 0;
    }); */
    $(".BgDiv1,.back").click(function() {
        $(".BgDiv1").css("display", "none");
        $(".DialogDiv1").css("display", "none");  });

    <!--修改-弹出层-->
/*     $("#update").click(function () {
        $(".BgDiv2").css("z-index","100");
        $(".BgDiv2").css({ display: "block", height: $(document).height()});
        $(".DialogDiv2").css("top", "5%");
        $(".DialogDiv2").css("display", "block");
    }); */
    $(".BgDiv2,.back").click(function() {
        $(".BgDiv2").css("display", "none");
        $(".DialogDiv2").css("display", "none");
    });
});

$("a,button").focus(function(){this.blur()});
</script>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/commons/basejs.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style>
    #btn-table td{
        padding-right:20px;
    }
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
#btn-table .btn
{
    background: #4373FF;
    color: #ffffff;
    
}
#btn-table .btn:hover
{
    color: #1E1E1E;
}
.bz-zd-table .btn
{
	width: 20%
}
#back{
    background: #DFDFDF;
}
#ok{
    background: #4373FF;
    color: #ffffff;
}
#ok:hover{
    color: #1E1E1E;
}
.textbox-text,.textbox
{
	width: 99% !important;
	height: 30px !important;
}
</style>
<script type="text/javascript">
    var catalogDataGrid;
    var organizationTree;
   
    $(function() {
    	
        catalogDataGrid = $('#catalogDataGrid').datagrid({
            url : '${path }/catalog/dataGrid',
            fit : true,
            striped : true,
            pagination : true,
            sortName : 'clsj',
	        sortOrder : 'asc',
            pageSize : 20,
            pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
            columns : [ [ {
            	field:'ck',
            	checkbox : true,
            	 width : ''
             },{
                width : '10%',
                title : '<font size="3px">医保类型</font>',
                field : 'YBLX'
             
            }, {
                width : '10%',
                title : '<font size="3px">医院项目编码</font>',
                field : 'YYXMBM'
            
            },{
                width : '10%',
                title : '<font size="3px">医保项目编码</font>',
                field : 'YBXMBM'
          
            },  {
                width : '10%',
                title : '<font size="3px">项目名称</font>',
                field : 'XMMC'
            }, {
                width : '15%',
                title : '<font size="3px">备注信息</font>',
                field : 'BZXX',
            
            },{
                width : '10%',
                title : '<font size="3px">操作类型</font>',
                field : 'CZLX',
                formatter : function(value, row, index) {
                    switch (value) {
                    case '0':
                        return '新增';
                    case '1':
                        return '修改';
                    case '2':
                        return '删除';
                    }
                }
            }, 
            {
                width : '13%',
                title : '<font size="3px">同步时间</font>',
                field : 'TBSJ'
                
            },{
                width : '10%',
                title : '<font size="3px">处理状态</font>',
                field : 'CLZT',
                formatter : function(value, row, index) {
                    switch (value) {
                    case '0':
                        return '未处理';
                    case '1':
                        return '已处理';
                    }
                },
                styler: function(value,row,index){
    				if (value == '0'){
    					return 'color:red;';
    				}
    			}
            },{
                width : '13%',
                title : '<font size="3px">处理时间</font>',
                field : 'CLSJ'
            }] ],
            onBeforeLoad: function (param) {
            	updateDatagridHeader(this);        	
            	
            },
            onLoadSuccess: function (data) {  
            	console.log(data);
//                 var body = $(this).datagrid('getPanel').find('.datagrid-body');
//             	body.css({"overflow-x":"hidden"});
            }
        });
     
    });
    
    
    function searchUserFun() {	
        catalogDataGrid.datagrid('load', $.serializeObject($('#searchUserForm')));
    }
    function cleanUserFun() {
        $('#searchUserForm input').val('');
        $('#searchUserForm select').val(0);
        catalogDataGrid.datagrid('reload');
    }
</script>
</head>
<body>
<div class="xPage">
<jsp:include page="../include/tophead.jsp" />
	
	 <div class="xMain">
	<jsp:include page="../include/leftMenu.jsp" />
	 <div class="xRightbox">
	 
	 <div class="pop-up bg-color1">
        <span class="float pop-up-span width-color dis-block">医保目录监控</span>
    </div>

    <div class="header-div-tj bg-color3 margin-top21" style="height: 86px;">
        <form action="" id="searchUserForm">
        <div style="width: 80%;float: left">
            <table class="margin-left10 margin-top6">
                <tbody>
                <tr>
                    <td class="input-td">医保类型:</td>
                    <td>
                    	<select name="yblx" >
                    		<option value="0">全部</option>
                    		<option value="1">门诊统筹</option>
                    		<option value="2">市医保</option>
                    		<option value="3">省医保</option>
                    		<option value="4">工伤</option>
                    	</select>
                    </td>
                    <td class="input-td" style="padding-left: 18px">医院项目编码:</td>
                    <td>
                      <input type="text" name="yyxmbm"/>
                    </td>
                   
                    <td class="input-td" style="padding-left: 18px">医保项目编码:</td>
                    <td>
                      <input type="text"  name="ybxmbm"/>
                    </td>
                </tr>
                <tr>
                     <td class="input-td" style="padding-left: 18px">项目名称:</td>
                    <td>
                      <input type="text" name="xmmc"/>
                    </td>
                    
                    <td class="input-td" style="padding-left: 18px">操作类型:</td>
                    <td>
                      <select name="czlx" >
                    		<option value="00">全部</option>
                    		<option value="0">新增</option>
                    		<option value="1">修改</option>
                    		<option value="2">删除</option>
                    	</select>
                    </td>
                   
                    <td class="input-td" style="padding-left: 18px">处理状态:</td>
                    <td>
                      <select name="clzt" >
                    		<option value="00">全部</option>
                    		<option value="0">未处理</option>
                    		<option value="1">已处理</option>
                    	</select>
                    </td>
                
                </tr>
                
                </tbody>
            </table>
        </div>
        </form>
        <div style="float: right;margin-right:34px;margin-top: 40px ">
            <input type="text"  onclick="searchUserFun()"  value="查询" class="btn">
            <input type="text" onclick="cleanUserFun()" value="重置" class="btn">
        </div>
    </div>
	 <div class="header-div-tj bg-color3 margin-top21">
        <table id="btn-table"  class="margin-left10 float margin-top10" style="width: 25%;">
            <tr>
                <td>
                    <button   class="btn" id="handle" >处理</button>
                </td>
                <td>
                    <button id="noHandle"  class="btn" style="width:106px;">取消处理</button>
                </td>
               <td>
                    <button  onclick="excelExport()" type="button"  class="btn">导出</button>
                    
                </td> 
               <!--  <td>
                    <button class="btn">导出</button>
                </td> -->
            </tr>
        </table>
    </div>
	  <div class="body-width   margin-top15" style="height: 615px;overflow-x:auto;">
        <table id="catalogDataGrid" class="yz-msg-table border-val" style="width: 100%">
            
        </table>
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

    $("#handle").click(function () {
    	var param = {'clzt':1};
    	handleClzt(param);
    });
    // clzt   1 : 处理 ； 0 ： 未处理
    $("#noHandle").click(function(){
    	var param = {'clzt':0};
    	handleClzt(param);
    });
    
    function handleClzt(param){
    	var ids;
        var rows = $('#catalogDataGrid').datagrid('getSelections');
        if(rows.length==0){
       	 	$.messager.alert( '提示', "至少选择一行更新处理状态", 'warning');
       	 	return ;
        }else{
        	for(var i=0;i<rows.length;i++){
        		if(i==0){
        			ids = rows[0].R_ID;
        		}else{
        			ids += ','+rows[i].R_ID;
        		}
        	}
        }
        console.log(ids);
        $.ajax({
			type: "post",
	 		url: '${path }/catalog/handle',
	 	    cache: false,
	 	    async : false,
	 	    dataType: "json",
	 	    data: $.extend(param,{'ids':ids}), 
	 	    success: function (result) {
                if (result.success) {
                	 catalogDataGrid.datagrid('reload');
                	 $.messager.alert( '提示',result.msg, 'info');
                } else {
                	 $.messager.alert( '提示',result.msg, 'warning');
                }
	 	    }
		});
    }
});

//导出excel
function excelExport(flag){
	var url = '${path}/catalog/export';
    $("#searchUserForm").attr("action", url);
    $("#searchUserForm").submit();
}

$("a,button").focus(function(){this.blur()});
</script>
</body>
</html>
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
	width: 99%;
	height: 30px !important;
}

.datagrid-row-selected{
	color: black;
}
.validatebox-text{
	width: 99% !important;
	height: 30px !important;
}
.window-mask{
	background: #1E1E1E;
    position: fixed;
    width: 100%;
    height: 100%;
    z-index: 99;
    left: 0;
    top: 0;
    display: none;
    opacity: 0.5882352941176471;
    filter: alpha(opacity=50);
    -moz-opacity:0.5882352941176471;
}
.window{
	padding:0px;
}
.panel-header{
	width: 100%;
    height: 32px;
    box-shadow: 0px 2px 3px #A2A2A2;
    opacity:1.0;
    background: #E6E6E6;
}
.panel-title{
	color: #1E1E1E;
    padding-left: 10px;
    padding-top: 6px;
    font-weight: 500;
}
.window{
	-webkit-border-radius: 0;
    border-radius: 0;
}
.window-shadow{
	display:none !important;
}
</style>
<script type="text/javascript">
    var userDataGrid;
    var organizationTree;
    function changelable(i){
        i.addClass("active-radio");
        i.siblings().removeClass("active-radio");
    }
    $(function() {
    	
    	
        $.extend($.fn.validatebox.defaults.rules, {  
    		
     		 mobile: {//value值为文本框中的值  
     		        validator: function (value) {  
     		            var reg = /^1[3|4|5|8|9]\d{9}$/;  
     		            return reg.test(value);  
     		        },  
     		        message: '输入手机号码格式不准确.'  
     		    },
     		  account: {//param的值为[]中值  
     	        validator: function (value, param) {  
     	            if (value.length < param[0] || value.length > param[1]) {  
     	                $.fn.validatebox.defaults.rules.account.message = '用户名长度必须在' + param[0] + '至' + param[1] + '范围';  
     	                return false;  
     	            } else {  
     	                if (!/^[\w]+$/.test(value)) {  
     	                    $.fn.validatebox.defaults.rules.account.message = '用户名只能数字、字母、下划线组成.';  
     	                    return false;  
     	                } else {  
     	                    return true;  
     	                }  
     	            }  
     	        }, message: ''  
     	    }  
          });

     
        $('#ruleType').combobox({
            	url:'${path }/parameter/getType2?clum=ruleType',
        	  valueField:'pValue',
	          textField:'pName',
	          onLoadSuccess: function () { 
	        	  
 	      	     	 var val = $(this).combobox('getData');  
 	      	         for (var item in val[0]) 
 	      	         {  
 	      	             if (item == 'pValue') 
 	      	             {  
 	      	                 $(this).combobox('select', val[0][item]); 	
 	      	                 department = val[0][item];
 	      	             }  
 	      	         } 
 	      	      	         	         
 	      	     }
	      }); 
  
         $('#ruleType').combobox({
     		 onHidePanel: function () {
     			 checkCombobox(this);
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

        
     	ruleTableDataGrid = $('#ruleTableDataGrid').datagrid({
            url : '${path }/rule/ruleTableDataGrid',
            fit : true,
            striped : true,
           // rownumbers : true,
            pagination : true,
           //singleSelect : true,
          //  idField : 'id',
            sortName : 'rule_code',
	        sortOrder : 'asc',
            pageSize : 20,
            pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
            columns : [ [ {
            	field:'ck',
            	checkbox : true,
            	 width : ''
             }, {
                 width : '15%',
                 title : '<font size="3px">医保规则编号</font>',
                 field : 'ruleCode'
             
             }, {
                width : '35%',
                title : '<font size="3px">医保规则名称</font>',
                field : 'menuName'
            
            },{
                width :'25%',
                title : '<font size="3px">医保规则类型</font>',
                field : 'ruleTypeName'
          
            },  {
                width : '25%',
                title : '<font size="3px">医保规则启用标志</font>',
                field : 'isUsed',
           
                formatter : function(value, row, index) {
                    switch (value) {
                    case  "1":
                        return '启用';
                    case "0":
                        return '禁用';
                    }
                }
            }] ],
            onBeforeLoad: function (param) {
            	updateDatagridHeader(this);        	
            	
            },
            onLoadSuccess: function (data) {
            	parent.$.modalDialog.openner_dataGrid = ruleTableDataGrid;
                var body = $(this).datagrid('getPanel').find('.datagrid-body');
            	body.css({"overflow-x":"hidden"});
            }
        });
        

    
     
    });
    


    
     function ruleFun() {
       	var id;
   	 	var ss = [];
        var rows = $('#ruleTableDataGrid').datagrid('getSelections');
        if(rows.length>1){
       	 $.messager.alert( '提示', "只能选一行 ", 'warning');
       	 return 
        }else if(rows.length<1){
       	 $.messager.alert( '提示', "请选一行", 'warning');
       	 return 
        }else{
       	 id=rows[0].id;
        }
	
 		parent.$.modalDialog({
            title : '医保规则修改',
            width : 1092,
            height : 532,
            href : '${path }/rule/showUpdate?id=' + id,
            onSuccess:function(){

            },
            closable:false
        }); 
    } 
    
    function searchRuleFun() {
    	ruleTableDataGrid.datagrid('load', $.serializeObject($('#searchForm')));
    }
    function cleanRuleFun() {
      //  $('#searchForm input').val('');
        $('#isUsed').val('');
        $('#menuName').val('');
        $('#ruleType').combobox('setValue',"");
 /*   	 var val =   $('#ruleType').combobox('getData');  
     for (var item in val[0]) 
        {  
            if (item == 'pValue') 
            {  
            	  $('#ruleType').combobox('select', val[0]["-请选择-"]); 	
                department = val[0][item];
            }  
        }  */
            	
      
    //	$('#ruleType').combobox('clear');
        ruleTableDataGrid.datagrid('load', {});
    }


    function showInsert(){
        parent.$.modalDialog({
            title : '医保规则新增',
            width : 1092,
            height : 532,
            href : '${path }/rule/showInsert',
            closable:false
        });
    }

</script>
    <script type="text/javascript">
    
    	var flag=true;
        var editIndex = undefined;
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
        function endEditing(){
            if (editIndex == undefined){return true}
            if ($('#dg').datagrid('validateRow', editIndex)){
                $('#dg').datagrid('endEdit', editIndex);
                editIndex = undefined;
                return true;
            } else {
                return false;
            }
        }
        function onClickCell(index, field){
        	if(field=="delbtn"){
        		$('#dg').datagrid('deleteRow',index);
        	}
            if (editIndex != index){
                if (endEditing()){
                	editIndex = index;
                    $('#dg').datagrid('selectRow', index)
                            .datagrid('beginEdit', index);
                    var ed = $('#dg').datagrid('getEditor', {index:index,field:field});
                    if (ed){
                        ($(ed.target).data('textbox') ? $(ed.target).textbox('textbox') : $(ed.target)).focus();
                    }
                    //editIndex = index;
                } else {
                    setTimeout(function(){
                        $('#dg').datagrid('selectRow', editIndex);
                    },0);
                }
            }
        }
        function onEndEdit(index, row){
            var ed1 = $(this).datagrid('getEditor', {
                index: index,
                field: 'columnTypeShow'
            });
            row.columnTypeShow= $(ed1.target).combobox('getText');
            row.columnType = transType($(ed1.target).combobox('getText'))
            var ed2 = $(this).datagrid('getEditor', {
                index: index,
                field: 'htmlInputShow'
            });
            row.htmlInputShow = $(ed2.target).combobox('getText');
            row.htmlInput = transType($(ed2.target).combobox('getText'))
        }
        function onEndEdit1(index, row){
            var ed2 = $(this).datagrid('getEditor', {
                index: index,
                field: 'htmlInputShow'
            });
            row.htmlInputShow = $(ed2.target).combobox('getText');
            row.htmlInput = transType($(ed2.target).combobox('getText')) 
        }
        function transType(text){
        	if(text=='数字'){
        		return "number";
        	}
        	if(text=='文本'){
        		return "varchar2(100)";
        	}
        	if(text=='日期'){
        		return "varchar2(10)";
        	}
        	if(text=='文本框'){
        		return "text";
        	}
        	if(text=='下拉框'){
        		return "select";
        	}
        	if(text=='日期框'){
        		return "date";
        	}
        }
        function append(){
            if (endEditing()){
            	flag=false;
                $('#dg').datagrid('appendRow',{isUnique:'0',isFilter:'0',delbtn:'<input type="button" onclick="removeRow()" value="删这一行">'});
                editIndex = $('#dg').datagrid('getRows').length-1;
                $('#dg').datagrid('selectRow', editIndex)
                        .datagrid('beginEdit', editIndex);
            }
        }
        function removeit(){
            if (editIndex == undefined){return}
            flag=true;
            $('#dg').datagrid('cancelEdit', editIndex)
                    .datagrid('deleteRow', editIndex);
            editIndex = undefined;
        }
        
        function removeRow(){
        	if(editIndex==null){
        		return;
        	}
        	$('#dg').datagrid('deleteRow', editIndex);
        }
        
        function accept(){
            if (endEditing()){
            	flag=true;
                $('#dg').datagrid('acceptChanges');
            }
        }
        function submitForm(){
        	//其实不是submit，而是获取表单数据的假submit
        	if(!$('#ruleForm').form('validate')){
        		alert('表单中的必填项为填写正确');
        		return;
        	}
        	var rows= $('#dg').datagrid('getRows');
			if(rows.length==0){
				alert('规则表至少要有一列！');
    			return;
			}
			if(!flag){
				alert('有一行处于编辑状态,请点击保存后再提交');
				return;
			}
        	a={};
        	a.rows=rows;
        	var searchCondition=false;
        	var UniqueCondition=false;
        	for (var i = 0; i < rows.length; i++) {
				if(rows[i].isFilter=='是'){
					searchCondition=true;
					break;
				}
			}
        	for (var i = 0; i < rows.length; i++) {
				if(rows[i].isUnique=='是'){
					UniqueCondition=true;
					break;
				}
			}
        	if(!searchCondition){
        		alert('搜索条件至少应该有一个');
        		return;
        	}
        	if(!UniqueCondition){
        		alert('唯一标识至少应该有一个');
        		return;
        	}
        	a.menuName=$('#menuName').textbox('getValue');
        	a.ruleType=$('#ruleType').combobox('getValue');
        	a.isUsed=$('input[name="isUsed"]:checked').val();
        	a.remark=$('#remark').text();
            $.ajax({
           		type: "POST",
                   url: "${path }/rule/insert",
                   data:JSON.stringify(a),
              	   contentType:"application/json",
                   dataType: "json",
                   success: function(data){
                	   alert(data.msg);
                	   if(data.msg=='新增规则成功'){
                		   location.reload();
                	   }
                   }
               });
        }
        function submitForm1(){
        	//其实不是submit，而是获取表单数据的假submit
        	if(!$('#ruleForm').form('validate')){
        		alert('表单中的必填项为填写正确');
        		return;
        	}
        	var rows= $('#dg').datagrid('getRows');
			if(rows.length==0){
				alert('规则表至少要有一列！');
    			return;
			}
			if(!flag){
				alert('有一行处于编辑状态,请点击保存后再提交');
				return;
			}
        	a={};
        	a.id=$('#mid').val();
        	a.rows=rows;
        	var searchCondition=false;
        	var UniqueCondition=false;
        	for (var i = 0; i < rows.length; i++) {
				if(rows[i].isFilter=='是'){
					searchCondition=true;
					break;
				}
			}
        	for (var i = 0; i < rows.length; i++) {
				if(rows[i].isUnique=='是'){
					UniqueCondition=true;
					break;
				}
			}
        	if(!searchCondition){
        		alert('搜索条件至少应该有一个');
        		return;
        	}
        	if(!UniqueCondition){
        		alert('唯一标识至少应该有一个');
        		return;
        	}
        	a.menuName=$('#menuName').textbox('getValue');
        	a.ruleType=$('#ruleType').combobox('getValue');
        	a.isUsed=$('input[name="isUsed"]:checked').val();
        	a.remark=$('#remark').textbox('getValue');
            $.ajax({
           		type: "POST",
                   url: "${path }/rule/update",
                   data:JSON.stringify(a),
              	   contentType:"application/json",
                   dataType: "json",
                   success: function(data){
                   }
               });
        }
        function clearForm(){
            $('#ruleForm').form('clear');
        }
        function enableSelectDicType(newValue, oldValue){
			var ed = $('#dg').datagrid('getEditor', {
                index: editIndex,
                field: 'dictType'
            });
			if(newValue=='select'){
				$(ed.target).combobox('enable');
			}else{
				$(ed.target).combobox('clear');
				$(ed.target).combobox('disable');
			}
		}
        function changeHtmlInput(newValue, oldValue){
			var ed = $('#dg').datagrid('getEditor', {
                index: editIndex,
                field: 'dictType'
            });
			var edc = $('#dg').datagrid('getEditor', {
                index: editIndex,
                field: 'htmlInputShow'
            });
			var data=[];
			if(newValue!=undefined){
				if(newValue=='varchar2(10)' || newValue=='日期'){
					data=[{
						label: '日期框',
						value: 'date'
					}];
				}else{
					data=[{
						label: '文本框',
						value: 'text'
					},{
						label: '下拉框',
						value: 'select'
					}];
				}
			}
			$(edc.target).combobox({
				data:data
			});
			
			$('#dg').datagrid('getColumnOption','htmlInputShow').editor.options.data=data;
			var i= $('#dg').datagrid('getColumnOption','htmlInputShow').editor.options.data;
        }
        function clearForm(){
        	parent.$.modalDialog.handler.dialog('close');
        }  
    </script>
</head>
<body>
<div class="xPage">
<jsp:include page="../include/tophead.jsp" />
	
	 <div class="xMain">
	    <div class="xLeftbox">
	<jsp:include page="../include/leftMenu.jsp" />
	</div>
	 <div class="xRightbox">
	 
	<div class="body-width fs_18 bold header-color">医保规则</div>
<div class="header-div bg-color1 border-rgt border-lef">
 <form action="" id="searchForm">
  <div  style="width: 80%;float: left">
    <table class="ys-table" style="width: 95%">
        <tbody>
        <tr>
            <td class="padding-left10 fs_16">
                医保规则名称
            </td>
            <td>
               <input name="menuName"  id="menuName" style="width:120px;height: 30px;" />
            </td>

            <td class="td-width fs_16">
                医保规则类型
            </td>
            <td>
               <input name="ruleType"  id="ruleType" style="width:120px;height: 30px;" />
            </td>
            <td class="td-width fs_16">
                医保规则启用标志
            </td>
            <td>
                <select name="isUsed"  id="isUsed" style="width:120px;height: 30px;"  >
                         <option value="" >-请选择-</option>
                         <option value="0" >禁用</option>
                         <option value="1" >启用</option>
                      
                      </select>
            </td>
            <td class="td-width"></td>
            <td>
        
              
            </td>
            <td style="width: auto"></td>
        </tr>
        </tbody>
    </table>
    </div>
    </form>
      <div style="float: right;margin-right:34px;margin-top: 10px ">
        <button class="btn-ok fs_16"  onclick="searchRuleFun()">查询</button>
                <button class="btn-cancel fs_16"  onclick="cleanRuleFun()" >重置</button>
            
        </div>
</div>

 <!--    <div class="header-div-tj bg-color3 margin-top21">
        <form action="" id="searchForm">
        <div style="width: 80%;float: left">
            <table class="margin-left10 margin-top6">
                <tbody>
                <tr style="display: block;margin-top: 8px">
                    <td class="input-td">医保规则名称:</td>
                    <td>
                        <input name="tableName"  style="width:120px;height: 30px;" />
                    </td>
                    <td class="input-td" style="padding-left: 48px">医保规则类型:</td>
                    <td>
                      <input name="ruleType"  id="ruleType" style="width:120px;height: 30px;" />
                      
                    </td>
                    <td class="input-td" style="padding-left: 48px">医保规则类型:</td>
                    <td>
                      <select name="isUsed"  style="width:120px;height: 30px;"  >
                         <option value="" >-请选择-</option>
                         <option value="0" >禁用</option>
                         <option value="1" >启用</option>
                      
                      </select>
                    </td>
                   

                </tr>
                </tbody>
            </table>
        </div>
        <div style="float: right;margin-right:34px;margin-top: 10px ">
            <input type="text"  onclick="searchUserFun()"  value="查询" class="btn">
            <input type="text" onclick="cleanUserFun()" value="重置" class="btn">
        </div>
        </form>
    </div> -->
    
    <table id="btn-table" class="float margin-top10" style="width: 25%;">
    <tr>
        <td>
            <button class="btn-ok fs_16" onclick="showInsert()"  id="increase">新增</button>
        </td>
        <td>
            <button class="btn-ok fs_16"  onclick="ruleFun()" id="update">修改</button>
        </td>
        <td>
         
        </td>
    </tr>
  </table>
<!-- 	 <div class="header-div-tj bg-color3 margin-top21">
        <table id="btn-table"  class="margin-left10 float margin-top10" style="width: 25%;">
            <tr>
                <td>
                    <button onclick="showInsert()"   class="btn" id="increase" >新增</button>
                </td>
                <td>
                    <input type="button" onclick="editUserFun()" value='修改'  class="btn">
                </td>
          
                <td>
                    <button class="btn">导出</button>
                </td>
            </tr>
        </table>
    </div> -->
    <div class="body-width   margin-top15" style="height: 615px;overflow-x:auto;">
    <table class="sf-msg-table bordvaler-"  id="ruleTableDataGrid" style="width: 100%">
	 <!--  <div class="body-width   margin-top15" style="height: 615px;overflow-x:auto;">
        <table id="ruleTableDataGrid" class="yz-msg-table border-val" style="width: 100%"> -->
            
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

    <!--增加-弹出层-->
    $("#increase").click(function () {
        // var str = "我是弹出对话框";
        $(".BgDiv1").css("z-index","100");
        $(".BgDiv1").css({ display: "block", height: $(document).height()});
        $(".DialogDiv1").css("top", "5%");
        $(".DialogDiv1").css("display", "block");
      //  $('#userAddForm input').val('');
      
        $("#sexAdd").val("0");
        $("#ok").val("确定");
        $("#back").val("取消");
        $("#userstatuAdd").val("0");
       
        document.documentElement.scrollTop = 0;
    });
    $(".BgDiv1,.back").click(function() {
        $(".BgDiv1").css("display", "none");
        $(".DialogDiv1").css("display", "none");  });

    <!--修改-弹出层-->
    $("#update").click(function () {
        $(".BgDiv2").css("z-index","100");
        $(".BgDiv2").css({ display: "block", height: $(document).height()});
        $(".DialogDiv2").css("top", "5%");
        $(".DialogDiv2").css("display", "block");
    });
    $(".BgDiv2,.back").click(function() {
        $(".BgDiv2").css("display", "none");
        $(".DialogDiv2").css("display", "none");
    });
});

$("a,button").focus(function(){this.blur()});
</script>
</body>
</html>
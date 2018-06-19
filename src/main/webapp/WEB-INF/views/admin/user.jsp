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
    var userDataGrid;
    var organizationTree;
   
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
     /*    organizationTree = $('#organizationTree').tree({
            url : '${path }/organization/tree',
            parentField : 'pid',
            lines : true,
            onClick : function(node) {
                userDataGrid.datagrid('load', {
                    organizationId: node.id
                });
            }
        }); */
   
        $('#userAddRoleIds').combotree({
            url: '${path }/role/tree',
          /*   multiple: true, */
            required: true,
            panelHeight : 'auto'
        });
       /*  $('#adddepartmentId').combobox({
        	url:'${path }/department/getType?clum=ksmc,ksbm',
            valueField:'ksbm',
            textField:'ksmc',
            required:true,
     		 onHidePanel: function () {
     			 checkCombobox(this);
     		 }
     	 }); */
  
        /* $('#departmentId').combobox({
     		 onHidePanel: function () {
     			 checkCombobox(this);
     		 }
     	 }); */
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

        
        userDataGrid = $('#userDataGrid').datagrid({
            url : '${path }/user/dataGrid',
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
                width : '15%',
                title : '<font size="3px">登录名</font>',
                field : 'userName'
             
            }, {
                width : '10%',
                title : '<font size="3px">姓名</font>',
                field : 'name'
            
            },{
                width : '20%',
                title : '<font size="3px">创建时间</font>',
                field : 'createTime'
          
            },  {
                width : '10%',
                title : '<font size="3px">性别</font>',
                field : 'sex',
           
                formatter : function(value, row, index) {
                    switch (value) {
                    case 0:
                        return '男';
                    case 1:
                        return '女';
                    }
                }
            }, {
                width : '10%',
                title : '<font size="3px">年龄</font>',
                field : 'age',
            
            },{
                width : '10%',
                title : '<font size="3px">电话</font>',
                field : 'phone',
               
            }, 
            {
                width : '15%',
                title : '<font size="3px">角色</font>',
                field : 'rolesList',
                formatter : function(value, row, index) {
                    var roles = [];
                    for(var i = 0; i< value.length; i++) {
                        roles.push(value[i].name);  
                    }
                    return(roles.join('\n'));
                }
            },{
                width : '10%',
                title : '<font size="3px">状态</font>',
                field : 'status',
             
                formatter : function(value, row, index) {
                    switch (value) {
                    case 0:
                        return '正常';
                    case 1:
                        return '停用';
                    }
                }
            } /* , {
                field : 'action',
                title : '操作',
                width : 130,
                formatter : function(value, row, index) {
                    var str = '';

                            str += $.formatString('<a href="javascript:void(0)" class="user-easyui-linkbutton-edit" data-options="plain:true,iconCls:\'fi-pencil icon-blue\'" onclick="editUserFun(\'{0}\');" >编辑</a>', row.id);


                            str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                            str += $.formatString('<a href="javascript:void(0)" class="user-easyui-linkbutton-del" data-options="plain:true,iconCls:\'fi-x icon-red\'" onclick="deleteUserFun(\'{0}\');" >删除</a>', row.id);

                    return str;
                }
            } */] ],
            onBeforeLoad: function (param) {
            	updateDatagridHeader(this);        	
            	
            },
            onLoadSuccess: function (data) {            	
                var body = $(this).datagrid('getPanel').find('.datagrid-body');
            	body.css({"overflow-x":"hidden"});
            }
        });
        
       /*  if(flag==1){
       	 $(".BgDiv2").css("z-index","100");
            $(".BgDiv2").css({ display: "block", height: $(document).height()});
            $(".DialogDiv2").css("top", "5%");
            $(".DialogDiv2").css("display", "block");
            document.documentElement.scrollTop = 0;
            $("#userEditSex").val('${user.sex}');
            
            $("#userEditStatus").val('${user.status}');
           
       } */
    	$('#userEditForm').form({
   		 url :'${path }/user/edit',
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
               	userDataGrid.datagrid('load', $.serializeObject($('#searchUserForm')));
           	 $.messager.alert( '提示',result.msg, 'warning');
              	}
                  else
              	{
                  	parent.$.messager.alert('提示', result.msg, 'warning');
              	}
              }
   	 });
     
    	$('#userAddForm').form({
   		 url :'${path }/user/add',
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
               	userDataGrid.datagrid('load', $.serializeObject($('#searchUserForm')));
           	 $.messager.alert( '提示',result.msg, 'warning');
              	}
                  else
              	{
                  	parent.$.messager.alert('提示', result.msg, 'warning');
              	}
              }
   	 });
     
    });
    
    function addUserFun() {
        parent.$.modalDialog({
            title : '添加',
            width : 500,
            height : 300,
            href : '${path }/user/addPage',
            buttons : [ {
                text : '添加',
                handler : function() {
                    parent.$.modalDialog.openner_dataGrid = userDataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                    var f = parent.$.modalDialog.handler.find('#userAddForm');
                    f.submit();
                }
            } ]
        });
    }
    
    function deleteUserFun() {
       /*  if (id == undefined) {//点击右键菜单才会触发这个
            var rows = userDataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {//点击操作里面的删除图标会触发这个
            userDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        } */
   	 var ss = "";
     var rows = $('#userDataGrid').datagrid('getSelections');
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
        parent.$.messager.confirm('询问', '您是否要删除当前用户？', function(b) {
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
                    $.post('${path }/user/delete', {
                        id : ss
                    }, function(result) {
                        if (result.success) {
                            parent.$.messager.alert('提示', result.msg, 'info');
                            userDataGrid.datagrid('reload');
                        }
                        progressClose();
                    }, 'JSON');
                }
            }
        });
    }
    
    function editUserFun() {
       	var id;
   	 var ss = [];
        var rows = $('#userDataGrid').datagrid('getSelections');
        if(rows.length>1){
       	 $.messager.alert( '提示', "只能选一行 ", 'warning');
       	 return 
        }else if(rows.length<1){
       	 $.messager.alert( '提示', "请选一行", 'warning');
       	 return 
        }else{
       	 id=rows[0].id;
        }
       /*  if (id == undefined) {
            var rows = userDataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {
            userDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        } */
       // location.href='${path }/user/editPage?userId=' + id+"&id="+pageId+"&pid="+pagepId+"&ppid="+pageppId;
       /*  parent.$.modalDialog({
            title : '编辑',
            width : 500,
            height : 300,
            href : '${path }/user/editPage?id=' + id,
            buttons : [ {
                text : '确定',
                handler : function() {
                    parent.$.modalDialog.openner_dataGrid = userDataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                    var f = parent.$.modalDialog.handler.find('#userEditForm');
                    f.submit();
                }
            } ]
        }); */
        
    	 $.post('${path }/user/editPage', {
    		 userId : id
           }, function(result) {
          	 showDiv2(); 
               $('#id2').val(result.user.id);
               $('#userName').val(result.user.userName);
               $('#name').val(result.user.name);
               $('#age').val(result.user.age);
               $('#phone2').val(result.user.phone);
               $("#userEditSex").val(result.user.sex);
               $("#userEditRoleIds").combotree('setValue',result.roleIds);
               
               $("#userEditStatus").val(result.user.status);
               
               //  $("#userEditStatus").find("option[value='']").attr("selected", true);
          
             //  $('#departmentId').combobox('setValue',result.user.departmentId);
           
             	            
               
               
           }, 'JSON');
        
    }
    
    function searchUserFun() {
        userDataGrid.datagrid('load', $.serializeObject($('#searchUserForm')));
    }
    function cleanUserFun() {
        $('#searchUserForm input').val('');
        userDataGrid.datagrid('load', {});
    }
    function addUser() {
		$.ajax({
			type: "post",
	 		url: '${path }/user/add',
	 	    cache: false,
	 	    async : false,
	 	    dataType: "json",
	 	    data: $('#userAddForm').serialize()  ,	
	 	    success: function (result) {
	 	    	// result = $.parseJSON(result);
	                if (result.success) {
	                	$(".BgDiv1").css("display","none");
	                	 $(".DialogDiv1").css("display", "none")
	                	    userDataGrid.datagrid('load', {});
	                	 $.messager.alert( '提示',result.msg, 'warning');
	                	
	                } else {
	                	 $.messager.alert( '提示',result.msg, 'warning');
	                }
	 	    }
		});
		
    	
	}
    function editUser() {
    	var isValid = $(this).form('validate');
    	if(!isValid){
    		return  false
    	}
		$.ajax({
			type: "post",
	 		url: '${path }/user/edit',
	 	    cache: false,
	 	    async : false,
	 	    dataType: "json",
	 	    data: $('#userEditForm').serialize()  ,	
	 	    success: function (result) {
	 	    	// result = $.parseJSON(result);
	                if (result.success) {
	                	$(".BgDiv2").css("display","none");
	               	 $(".DialogDiv2").css("display", "none")
	               	    userDataGrid.datagrid('load', {});
	            	 $.messager.alert( '提示',result.msg, 'warning');
	                } else {
	               	 $.messager.alert( '提示',result.msg, 'warning');
	                }
	 	    }
		});
		
    	
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
</script>
</head>
<body>
<div class="xPage">
<jsp:include page="../include/tophead.jsp" />
	
	 <div class="xMain">
	<jsp:include page="../include/leftMenu.jsp" />
	 <div class="xRightbox">
	 
	 <div class="pop-up bg-color1">
        <span class="float pop-up-span width-color dis-block">用户管理</span>
    </div>

    <div class="header-div-tj bg-color3 margin-top21">
        <form action="" id="searchUserForm">
        <div style="width: 80%;float: left">
            <table class="margin-left10 margin-top6">
                <tbody>
                <tr style="display: block;margin-top: 8px">
                    <td class="input-td">姓名:</td>
                    <td>
                        <input name="name"  style="width:120px;height: 30px;" />
                    </td>
                    <td class="input-td" style="padding-left: 48px">创建时间:</td>
                    <td>
                      <input name="createdateStart"  style="width:120px;height: 30px;" placeholder="点击选择时间" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly" />至
                        <input  name="createdateEnd"  style="width:120px;height: 30px;" placeholder="点击选择时间" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly" />
                    </td>
                   

                </tr>
                </tbody>
            </table>
        </div>
        </form>
        <div style="float: right;margin-right:34px;margin-top: 10px ">
            <input type="text"  onclick="searchUserFun()"  value="查询" class="btn">
            <input type="text" onclick="cleanUserFun()" value="重置" class="btn">
        </div>
        
    </div>
	 <div class="header-div-tj bg-color3 margin-top21">
        <table id="btn-table"  class="margin-left10 float margin-top10" style="width: 25%;">
            <tr>
                <td>
                    <button   class="btn" id="increase" >新增</button>
                </td>
                <td>
                    <button onclick="editUserFun()"  class="btn">修改</button>
                </td>
               <td>
                    <button  onclick="deleteUserFun()"  class="btn">删除</button>
                    
                </td> 
               <!--  <td>
                    <button class="btn">导出</button>
                </td> -->
            </tr>
        </table>
    </div>
	  <div class="body-width   margin-top15" style="height: 615px;overflow-x:auto;">
        <table id="userDataGrid" class="yz-msg-table border-val" style="width: 100%">
            
        </table>
    </div>
    
        <!--弹出层-->
    <div class="BgDiv1"></div>
    <div class="DialogDiv1" style="display: none">
        <div class="pop-up bg-color1">
            <span class="float pop-up-span width-color dis-block">用户新增</span>
        </div>

        <!--table数据-->
        <form id="userAddForm" method="post">
        <div class="body-width border-val" style="height: 250px;margin-top: 20px !important;width: 94%;margin-left: 3%;">
            <table class="bz-zd-table">
                <tbody>
                <tr>
                    <td>
                        <span class="bz-zd-table-span">*</span>登录名
                    </td>
                    <td>
                      <input name="userName"  id="userLognNameAdd" type="text" class="easyui-validatebox" validateOnCreate="false"
                        validType="account[1,16]"  data-options="required:true" >
                    </td>
                    <td>
                        <span class="bz-zd-table-span">*</span>姓名
                    </td>
                    <td >
                       <input name="name" type="text"  id="userNameAdd" class="easyui-validatebox" validateOnCreate="false"
                         validType="length[1,16]"   data-options="required:true" >
                    </td>
                    <td>
                        <span class="bz-zd-table-span">*</span>密码
                    </td>
                    <td>
                      <input name="userPassword"  id="userpassAdd"   type="password" validateOnCreate="false"
                       class="easyui-validatebox" validType="length[1,16]"  data-options="required:true">
                    </td>
                    
                </tr>
                <tr>
                <td>
                        <span class="bz-zd-table-span">*</span>性别
                    </td>
                    <td>
                             <select name="sex" id="sexAdd"   validateOnCreate="false"      >
                            <option value="0" selected="selected">男</option>
                            <option value="1" >女</option>
                        </select>
                    </td>
                    <td>
                        <span class="bz-zd-table-span"></span>年龄
                    </td>
                    <td>
                        <input type="text" name="age"   id="ageAdd"  validateOnCreate="false"
                        class="easyui-numberbox" validType="length[1,2]" />
                    </td>
                   <!--  <td>
                        <span class="bz-zd-table-span">*</span>科室
                    </td>
                    <td>
                      <input id="adddepartmentId"  validateOnCreate="false"
                        type="text"  name="departmentId"  />
                    </td> -->
                    
                </tr>
                <tr>
                <td>
                        <span class="bz-zd-table-span">*</span>角色
                    </td>
                    <td>
                      <select id="userAddRoleIds"  id="userrolesAdd"  validateOnCreate="false"
                       name="roleIds" style="width: 140px; height: 29px;"></select>
                    </td>
                    <td>
                        <span class="bz-zd-table-span"></span>电话
                    </td>
                    <td>
                         <input type="text"  id="userphoneAdd"   validateOnCreate="false"
                          name="phone"  validType="mobile"  class="easyui-numberbox"/>
                    </td>
                    <td>
                        <span class="bz-zd-table-span">*</span>用户状态
                    </td>
                    <td>
                      <select name="status"   id="userstatuAdd"  validateOnCreate="false" >
                                <option value="0">正常</option>
                                <option value="1">停用</option>
                        </select>
                    </td>
                  <!--   <td>
                        <span>*</span>角色
                    </td>
                    <td>
                      <select id="userAddRoleIds" name="roleIds" style="width: 140px; height: 29px;"></select>
                    </td> -->
                </tr>
              <!-- 
                <tr>
                    <td colspan="2" style="text-align: center;color:#DF2828">
                        注：(*为必须项)
                    </td>
                    <td colspan="2"></td>
                    <td colspan="1">
                        <input id="ok"  type="submit"  value="确认" class="btn"/>
                        <input id="back"  type="button" value="取消"  class="back btn"/>
                       <input type="reset" class="btn" value="重置" />
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
                    <td colspan="2" style="text-align:center;">    
                         <input id="ok" type="submit"  value="确认" class="ok btn"/>
                        <input id="back" type="button" value="取消" class="back btn"/>
                    
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
            <span class="float pop-up-span width-color dis-block">用户修改</span>
        </div>

        <!--table数据-->
        <form id="userEditForm" method="post">
        <div class="body-width border-val" style="height: 250px;margin-top: 20px !important;width: 94%;margin-left: 3%;">
            <table class="bz-zd-table">
                <tbody>
                <tr>
                    <td>
                        <span class="bz-zd-table-span">*</span>登录名
                    </td>
                    <td>
                    <input id="id2" name="id" type="hidden"  <%-- value="${user.id}" --%>/>
                      <input id="userName" name="userName"  validateOnCreate="false"  type="text"  class="easyui-validatebox"   <%-- value="${user.userName}" --%> validType="account[1,16]"  data-options="required:true" value="">
                    </td>
                    <td>
                        <span class="bz-zd-table-span">*</span>姓名
                    </td>
                    <td>
                       <input id="name"  name="name" validateOnCreate="false"  type="text"  class="easyui-validatebox"  validType="length[1,16]" <%-- value="${user.name}" --%>  data-options="required:true" value="">
                    </td>
                    <td>
                        <span class="bz-zd-table-span">*</span>密码
                    </td>
                    <td>
                      <input id="userPassword" name="userPassword" type="password" placeholder="密码不改则不输" class="easyui-validatebox" validType="length[1,16]"  >
                    </td>
                    
                </tr>
                <tr>
                <td>
                        <span class="bz-zd-table-span">*</span>性别
                    </td>
                    <td>
                           <select id="userEditSex" name="sex" validateOnCreate="false" >
                            <option value="0">男</option>
                            <option value="1">女</option>
                    </select>
                    </td>
                    <td>
                        <span class="bz-zd-table-span"></span>年龄
                    </td>
                    <td>
                        <input id="age" type="text" name="age" validateOnCreate="false"  onkeyup="this.value=this.value.replace(/[^0-9.]/g,'')" <%--  value="${user.age}" --%> validType="length[1,2]" />
                    </td>
                   <%--  <td>
                        <span class="bz-zd-table-span">*</span>科室
                    </td>
                    <td>
                    <input id="departmentId" name="departmentId" validateOnCreate="false" value="${user.departmentId}"  class="easyui-combobox"
                      data-options="
                    url:'${path }/department/getType?clum=ksmc,ksbm',
                    method:'post',
                    valueField:'ksbm',
                    textField:'ksmc',
                   
                   
                    "
                     />
                    </td>
                     --%>
                </tr>
                <tr>
                <td>
                        <span class="bz-zd-table-span">*</span>角色
                    </td>
                    <td><!--  value=${roleIds} --> 
                     <input  id="userEditRoleIds"class="easyui-combotree" validateOnCreate="false"  name="roleIds" style="width: 140px; height: 29px;"
                     data-options="
                       url : '${path }/role/tree',
                       parentField : 'pid',
                             lines : true,
                           panelHeight : 'auto',
                           
                         required : true,
                        cascadeCheck : false,
                      
                     "
                     />
                    </td>
                    <td>
                        <span class="bz-zd-table-span"></span>电话
                    </td>
                    <td>
                         <input type="text" id="phone2"  name="phone"  class="easyui-validatebox" validType="mobile" <%--  value="${user.phone}"  --%>  />
                    </td>
                    <td>
                        <span class="bz-zd-table-span">*</span>用户状态
                    </td>
                    <td>
                      <select id="userEditStatus" name="status" <%-- value="${user.status}"  --%> >
                            <option value="0">正常</option>
                            <option value="1">停用</option>
                    </select>
                    </td>
                  <!--   <td>
                        <span>*</span>角色
                    </td>
                    <td>
                      <select id="userAddRoleIds" name="roleIds" style="width: 140px; height: 29px;"></select>
                    </td> -->
                </tr>
             <!--  
                <tr>
                    <td colspan="2" style="text-align: center;color:#DF2828">
                        注：(*为必须项)
                    </td>
                    <td colspan="2"></td>
                    <td colspan="1">
                        <input  id="ok" type="submit"  value="确认" class="btn"/>
                        <input id="back" type="button" value="取消"  class="back btn"/>
                       <input type="reset" class="btn" value="重置" />
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
                    
                         <input id="ok" type="submit"  value="确认" class="ok btn"/>
                        <input id="back" type="button" value="取消" class="back btn"/>
                    </td>
                    <td colspan="1">
                    
                    </td>
                </tr>
                </tbody>
            </table>
          
        </div>
        </form>
    </div>
    
<!-- <div class="easyui-layout" data-options="fit:true,border:false">
    <div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #fff">
        <form id="searchUserForm">
            <table>
                <tr>
                    <th>姓名:</th>
                    <td><input name="name" placeholder="请输入用户姓名"/></td>
                    <th>创建时间:</th>
                    <td>
                        <input name="createdateStart" placeholder="点击选择时间" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly" />至
                        <input  name="createdateEnd" placeholder="点击选择时间" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly" />
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-magnifying-glass',plain:true" onclick="searchUserFun();">查询</a>
                        <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'fi-x-circle',plain:true" onclick="cleanUserFun();">清空</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'center',border:true,title:'用户列表'" >
        <table id="userDataGrid" data-options="fit:true,border:false"></table>
    </div>
    <div data-options="region:'west',border:true,split:false,title:'组织机构'"  style="width:150px;overflow: hidden; ">
        <ul id="organizationTree" style="width:160px;margin: 10px 10px 10px 10px"></ul>
    </div>
</div>
<div id="userToolbar" style="display: none;">
  
        <a onclick="addUserFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'fi-plus icon-green'">添加</a>

</div> -->
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
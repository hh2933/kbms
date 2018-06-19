<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/commons/basejs.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style type="text/css">
#btn-table .btn
{
    background: #4373FF;
    color: #ffffff;
}
#btn-table .btn:hover
{
    color: #1E1E1E;
}
 #btn-table td{
        padding-right:20px;
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
button, input, optgroup, select, textarea {
	color:black;
}
</style>
<script type="text/javascript">
    var roleDataGrid;
    $(function() {
        roleDataGrid = $('#roleDataGrid').datagrid({
            url : '${path }/role/dataGrid',
            striped : true,
           // rownumbers : true,
            pagination : true,
            singleSelect : true,
            idField : 'id',
            sortName : 'id',
            sortOrder : 'asc',
            pageSize : 20,
            pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
            frozenColumns : [ [ /* {
                width : '100',
                title : 'id',
                field : 'id',
                sortable : true
            }, */ {
                width : '25%',
                title : '<font size="3px">名称</font>',
                field : 'name'
                //sortable : true
            }, {
                width : '25%',
                title : '<font size="3px">描述</font>',
                field : 'description'
            } , {
                width : '25%',
                title : '<font size="3px">状态</font>',
                field : 'status',
                //sortable : true,
                //将0-1转化成 正常-不正常
                formatter : function(value, row, index) {
                    switch (value) {
                    case 0:
                        return '正常';
                    case 1:
                        return '停用';
                    }
                }
            }, {
                field : 'action',
                title : '<font size="3px">操作</font>',
                width : '25%',
                formatter : function(value, row, index) {
                    var str = '';
                        
                            str += $.formatString('<input style="width:35px;" class="btn1" type="button" onclick="grantRoleFun(\'{0}\');" value="授权">', row.id);
                        
                       
                            str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                            str += $.formatString('<input style="width:35px;" class="btn1" type="button" onclick="editRoleFun(\'{0}\');" value="编辑">', row.id);
                     
                       
                            str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
                            str += $.formatString('<input style="width:35px;" class="btn1" type="button" onclick="deleteRoleFun(\'{0}\');" value="删除">', row.id);
                     
                    return str;
                }
            } ] ],
            onBeforeLoad: function (param) {
          	  updateDatagridHeader(this);
          	},
            onLoadSuccess:function(data){
                $('.role-easyui-linkbutton-ok').linkbutton({text:'授权'});
                $('.role-easyui-linkbutton-edit').linkbutton({text:'编辑'});
                $('.role-easyui-linkbutton-del').linkbutton({text:'删除'});
            },
            toolbar : '#roleToolbar'
        });
        
        $('#roleAddForm').form({
            url : '${path }/role/add',
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
                if (result.success) {
                	parent.$.messager.alert('提示', '添加成功', 'info');
                	$('#roleDataGrid').datagrid('reload');//之所以能在这里调用到parent.$.modalDialog.openner_dataGrid这个对象，是因为user.jsp页面预定义好了
       	         $(".BgDiv1").css("display", "none");
    	         $(".DialogDiv1").css("display", "none");
    	         $(".DialogDiv2").css("display", "none");
                } else {
                    parent.$.messager.alert('错误', result.msg, 'error');
                }
            }
        });
        $(".BgDiv1,#back").click(function() {
	       	 $('#name').val('');
	    	 $('#text').val('');
	    	 $('#status').combobox('setValue','0');
	         $(".BgDiv1").css("display", "none");
	         $(".DialogDiv1").css("display", "none");
	         $(".DialogDiv2").css("display", "none");
        });
        
        $('#roleEditForm').form({
            url : '${path }/role/edit',
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
                if (result.success) {
                	parent.$.messager.alert('提示', '修改成功', 'info');
                	$('#roleDataGrid').datagrid('reload');
	       	         $(".BgDiv1").css("display", "none");
	    	         $(".DialogDiv1").css("display", "none");
	    	         $(".DialogDiv2").css("display", "none");
                } else {
                    parent.$.messager.alert('错误', result.msg, 'error');
                }
            }
        });
    });

    function addRoleFun() {//显示了添加角色的弹出DIV
        $(".BgDiv1").css("z-index","100");
        $(".BgDiv1").css({ display: "block", height: $(document).height()});
        $(".DialogDiv1").css("top", "5%");
        $(".DialogDiv1").css("display", "block");
        $("#name").val('');
        $("#text").val('');
        $("#status").val('0');
        document.documentElement.scrollTop = 0;
    }

    function editRoleFun(id) {
        if (id != undefined) {
           // var rows = roleDataGrid.datagrid('getSelections');
           // id = rows[0].id;
            $(".BgDiv1").css("z-index","100");
            $(".BgDiv1").css({ display: "block", height: $(document).height()});
            $(".DialogDiv2").css("top", "5%");
            $(".DialogDiv2").css("display", "block");
            document.documentElement.scrollTop = 0;
            $.ajax({
        		type: "POST",
                url: '${path }/role/editPageNew?id=' + id,
                dataType: "json",
                success: function(data){
                	$('#eid').val(data.id);
                	$('#ename').val(data.name);
                	$('#eroleEditStatus').val(data.status);
                	$('#etext').val(data.description);
                }
            });
        } else {
            roleDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        
    }

    function deleteRoleFun(id) {
        if (id == undefined) {//点击右键菜单才会触发这个
            var rows = roleDataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {//点击操作里面的删除图标会触发这个
            roleDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        parent.$.messager.confirm('询问', '您是否要删除当前角色？', function(b) {
            if (b) {
                progressLoad();
                $.post('${path }/role/delete', {
                    id : id
                }, function(result) {
                    if (result.success) {
                        parent.$.messager.alert('提示', result.msg, 'info');
                        roleDataGrid.datagrid('reload');
                    }
                    progressClose();
                }, 'JSON');
            }
        });
    }

    function grantRoleFun(id) {
        if (id == undefined) {
            var rows = roleDataGrid.datagrid('getSelections');
            id = rows[0].id;
        } else {
            roleDataGrid.datagrid('unselectAll').datagrid('uncheckAll');
        }
        
        parent.$.modalDialog({
            title : '授权',
            width : 500,
            height : 500,
            href : '${path }/role/grantPage?id=' + id,
            buttons : [ {
                text : '确定',
                handler : function() {
                    parent.$.modalDialog.openner_dataGrid = roleDataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
                    var f = parent.$.modalDialog.handler.find('#roleGrantForm');
                    f.submit();
                }
            } ]
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


    <div class="pop-up bg-color1">
        <span class="float pop-up-span width-color dis-block">角色管理</span>
    </div>
    
    	<div class="header-div-tj bg-color3 margin-top21">
        <table id="btn-table" class="margin-left10 float margin-top10" style="width: 25%;">
            <tr>
                <td>
                    <button class="btn"  onclick="addRoleFun()">新增</button>
                </td>
            </tr>
        </table>
    </div>
    
    
   <div class="body-width   margin-top15" style="height: 620px;overflow-x:hidden;">
   	<table id="roleDataGrid" data-options="fit:true,border:false"></table>
   </div>
	       
	       


	<div class="BgDiv1"></div>
    <div class="DialogDiv1" style="display: none">
        <div class="pop-up bg-color1">
            <span class="float pop-up-span width-color dis-block">新增角色</span>
        </div>
        <!--table数据-->
        <div class="body-width margin-top10" style="height: 416px;">
        <form id="roleAddForm" method="post">
        <div class="body-width border-val" style="height: 250px;margin-top: 20px !important;width: 94%;margin-left: 3%;">
            <table class="bz-zd-table" style="width: 81%;">
                <tbody>
                <tr>
                    <td>
                        <span style="color: red">*</span>角色名称
                    </td>
                    <td>
                    	<input id='name' name="name" maxlength="8" type="text" placeholder="请输入角色名称" class="easyui-validatebox span2" data-options="required:true,validateOnCreate:false" value="">
					</td>
                    <td>
                        <span style="color: red">*</span>状态
                    </td>
                    <td>
                        <select id='status' name="status" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
                            <option value="0">正常</option>
                            <option value="1">停用</option>
                        </select>
                    </td>
                <tr>
                    <td>备注</td>
                </tr>
                <tr>
                    <td></td>
                    <td colspan="5">
                        <textarea id='text' maxlength="50" name="description" cols="50" rows="8" ></textarea>
                    </td>
                </tr>

                <tr>
                    <td colspan="2" style="text-align: center;color:#DF2828">
                        注：(*为必须项)
                    </td>


                </tr>
                </tbody>
            </table>
            </div>
            <table class="bz-zd-table" style="width: 81%;">
            	
           		 <tr>
           		    <td colspan="2" style="text-align: center;color:#DF2828">
                    </td>
            	    <td colspan="2">
                    	<input type="button" id='ok' onclick="$('#roleAddForm').submit()" class="btn" value='确认'>
                    	<input type="button" id='back' class="btn" value='取消'>
                    </td>
           		</tr>
            </table>
            </form>
        </div>
    </div>

    <div class="DialogDiv2" style="display: none">
        <div class="pop-up bg-color1">
            <span class="float pop-up-span width-color dis-block">修改角色</span>
        </div>
        <!--table数据-->
        <div class="body-width margin-top10" style="height: 416px">
        <form id="roleEditForm" method="post">
        <div class="body-width border-val" style="height: 250px;margin-top: 20px !important;width: 94%;margin-left: 3%;">
            <table class="bz-zd-table" style="width: 81%;">
                <tbody>
                <tr>
                    <td>
                        <span style="color: red">*</span>角色名称
                    </td>
                    <td>
                    	<input id='eid' name="id" type="hidden">
                    	<input id='ename' maxlength="8" name="name" type="text" placeholder="请输入角色名称" class="easyui-validatebox" data-options="required:true,validateOnCreate:false">
					</td>
                    <td>
                        <span style="color: red">*</span>状态
                    </td>
                    <td>
                        <select id="eroleEditStatus" name="status" class="easyui-combobox" data-options="width:140,height:29,editable:false,panelHeight:'auto'">
                            <option value="0">正常</option>
                            <option value="1">停用</option>
                        </select>
                    </td>
                <tr>
                    <td>备注</td>
                </tr>
                <tr>
                    <td></td>
                    <td colspan="5">
                        <textarea id='etext' maxlength="50" name="description" cols="50" rows="8" ></textarea>
                    </td>
                </tr>

                <tr>
                    <td colspan="2" style="text-align: center;color:#DF2828">
                        注：(*为必须项)
                    </td>


                </tr>
                </tbody>
            </table>
            </div>
            <table class="bz-zd-table" style="width: 81%;">
            	
           		 <tr>
           		    <td colspan="2" style="text-align: center;color:#DF2828">
                    </td>
                    <td colspan="2">
                    	<input type="button" id='ok' onclick="$('#roleEditForm').submit()" class="btn" value='确认'>
                    	<input type="button" id='back' class="btn" value='取消'>
                    </td>
           		</tr>
            </table>
            </form>
        </div>
    </div>
</div>
</div>
</div>
</body>
</html>
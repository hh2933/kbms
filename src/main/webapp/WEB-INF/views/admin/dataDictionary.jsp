<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/commons/basejs.jsp" %>
<script  src="${staticPath }/static/fileUpload-0.2.js"
	type="text/javascript"></script>
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
   /*  background: #DFDFDF; */
    background: #4373FF;
    color: #ffffff;
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
        .input-table2 input{width: 140px !important;}
</style>
<script type="text/javascript">
    var userDataGrid;
    var organizationTree;
    var flag=0;
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

     
        dataDictionaryDataGrid = $('#dataDictionaryDataGrid').datagrid({
            url : '${path }/dictionary/dictionaryDataGrid',
            fit : true,
            striped : true,
           // rownumbers : true,
            pagination : true,
           singleSelect : true,
          //  idField : 'id',
            sortName : 'createTime',
	        sortOrder : 'asc',
            pageSize : 20,
            pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
            columns : [ [ {
                width : '35%',
                title : '<font size="3px">字典类型</font>',
                field : 'dictType'
            
            },{
                width :'30%',
                title : '<font size="3px">字典代码</font>',
                field : 'dictCode'
          
            },  {
                width : '35%',
                title : '<font size="3px">字典名称</font>',
                field : 'dictName'
            }] ],
            onBeforeLoad: function (param) {
            	updateDatagridHeader(this);        	
            	
            },
            onLoadSuccess: function (data) {            	
                var body = $(this).datagrid('getPanel').find('.datagrid-body');
            	body.css({"overflow-x":"hidden"});
            },
            onClickRow:function(index,row){
            	editdict();
            }
        });
    	
        
        $('#adddict input ').attr("disabled","disabled");
    //    $('#dictionaryTable i ').attr("disabled","disabled");
        
    //    $("#dictType").attr("readonly","readonly")
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
    
    function editdict() {
     	$("#increase").attr("disabled", false);
       	var id;
   	 var ss = [];
        var rows = $('#dataDictionaryDataGrid').datagrid('getSelections');
        if(rows.length>1){
       	 $.messager.alert( '提示', "只能选一行 ", 'warning');
       	 return 
        }else if(rows.length<1){
       	 $.messager.alert( '提示', "请选一行", 'warning');
       	 return 
        }else{
        	deleterowsform2();
        	 type=rows[0].dictType;
          	 $('#back').hide();
           	 $('#delete').show();
           	 flag=1;
           	$('#adddict input ').attr("disabled",false);
        	$.ajax({
    			type: "post",
    	 		url: '${path }/dictionary/selectbyType',
    	 	    cache: false,
    	 	    async : false,
    	 	    dataType: "json",
    	 	    data: {"type":type}  ,	
    	 	    success: function (result) {
    	 	    	if(result!=null){
    	 	    		  for (var i = 0; i < result.length; i++) {
    	 						var dic=result[i];
    	 						if(i==0){
    	 						    	 $('#dictType').val(dic.dictType);
    	 						    	 $('#dictTypeCode').val(dic.dictTypeCode);
    	 						      	 $('#dictId').val(dic.id);
    	 						     	 $('#dictCode').val(dic.dictCode);
    	 						      	 $('#dictName').val(dic.dictName);
    	 						}else{
    	 							 var trObj = document.createElement("tr");  
    	 					        //   trObj.id = new Date().getTime();  
    	 					         //  trObj.innerHTML = "<td class='input-td' style='padding-left: 48px'>*字典编码:</td>  <td>  <input  type='hidden' id='dictId'  value="+dic.id+" name='id'  style='width:120px;height: 30px;' /> <input  type='text'  name='dictCode' value="+dic.dictCode+"  style='width:120px;height: 30px;' />   </td>      <td class='input-td' style='padding-left: 48px'>*字典名称:</td> <td>     <input  type='text'  name='dictName'  value="+dic.dictName+"  style='width:120px;height: 30px;'' /> </td>     <td>  <img   onclick='deleteCurrentRow(this)' src='../static/style/images/condition_cancel.png'>  </td>  ";  
    	 					           trObj.innerHTML = "<td>字典代码</td>  <td>  <input  type='hidden'   value="+dic.id+"  name='id'  class='yp-nm' />  <input  type='text'  value="+dic.dictCode+" name='dictCode' class='yp-nm' /> </td><td>字典名称</td><td><input  type='text'  name='dictName'  value="+dic.dictName+"   class='xd-nm' /></td><td><img  onclick='addOne()'  class='add-tr'   src='../static/style/images/add.png'></td><td class='padding-left10'><img class='del-tr'  onclick='deleteCurrentRow(this)'   src='../static/style/images/del.png'></td> ";  
    	 					           document.getElementById("dictionaryTable").appendChild(trObj);  
    	 						}
    	 						
    	 					}
    	 	    	}
        		 
        	      
    	 	    }
        	});
        	
        
       	 
       	
     //  	 $('#add').hide();
     
     //  	 $('#dictType').val(type);
    //   	 $('#dictId').val(id);
      // 	 $('#dictCode').val(code);
    ///  	 $('#dictName').val(name);
       	
        }
 
        
    }
    
    function searchDictFun() {
    	dataDictionaryDataGrid.datagrid('load', $.serializeObject($('#searchForm')));
    }
    function cleanDictFun() {
        $('#searchForm input').val('');
        $('#find').val("查询"); 
        $('#reset').val("重置"); 
    	deleterowsform2();
    	flag=0;
        dataDictionaryDataGrid.datagrid('load', {});
        $('#adddict input ').attr("disabled","disabled");
    }

   

    function addOne() {
    	if(flag==1){
    	   var trObj = document.createElement("tr");  
           trObj.id = new Date().getTime();  
       //    trObj.innerHTML = "<td class='input-td' style='padding-left: 48px'>*字典编码:</td>  <td>  <input  type='text'  name='dictCode'  style='width:120px;height: 30px;' />   </td>      <td class='input-td' style='padding-left: 48px'>*字典名称:</td> <td>     <input  type='text'  name='dictName'  style='width:120px;height: 30px;'' /> </td>     <td>  <img   onclick='deleteCurrentRow(this)' src='../static/style/images/condition_cancel.png'>  </td>  ";  
           trObj.innerHTML = " <td>字典代码</td>  <td>  <input  type='hidden'   name='id'  class='yp-nm' />  <input  type='text'  name='dictCode' class='yp-nm' /> </td><td>字典名称</td><td><input  type='text'  name='dictName'  class='xd-nm' /></td><td> <img  onclick='addOne()'  class='add-tr'   src='../static/style/images/add.png'></td><td class='padding-left10'><img class='del-tr'  onclick='deleteCurrentRow(this)'   src='../static/style/images/del.png'></td> ";  
           document.getElementById("dictionaryTable").appendChild(trObj);   
    	}
    	//document.getElementById("btn-table").append("<tr>   </tr>");
	//	
	}
    function deleteCurrentRow(obj){
    	$.messager.confirm('询问', '您是否要删除本行？', function(b) {
    		
    	     if (b) {
    		var tr=obj.parentNode.parentNode;
        	var tbody=tr.parentNode;
        	tbody.removeChild(tr);
    	     }
    	})
    	
    	
    	}

    function deleterowsform2() {
    	 $('#dictionaryform input').val('');
    	 $('#ok').val("保存");
         $('#back').val("取消");
         $('#delete').val("删除");
   	 var tb = document.getElementById('dictionaryTable');
        var rowNum=tb.rows.length;
       
        for (var i=2;i<rowNum;i++)
        {
            tb.deleteRow(i);
            rowNum=rowNum-1;
            i=i-1;
        }

       
	}
    function addDictionary() {
    	 var p;
    	
  
    	   $("#dictionaryTable input[type='text']").each(function (index,value) {
    	    if( $(this).val()==undefined||$(this).val()==""	){
    	    	 $.messager.alert( '提示',"字典编码和字典名称不能为空", 'warning');
    	    	 p=1;
    	    	 return false ;
    	    };
    		 
    		 
    	 });
    	 if(p==1){
    		 return false;
    	 }
        		 var a=new Object();
            	 a.list=[];
            	 var list;
            	 
             a.dictType= $('#dictType').val();
             a.dictTypeCode= $('#dictTypeCode').val();
            	 $('#dictionaryTable tr').each(function (index,value) {
            		  var dic=new Object();
                      var  id = $(this).find("input[name='id'] ").val();
                      var  code = $(this).find("input[name='dictCode'] ").val();
                      var name = $(this).find("input[name='dictName']").val();
                      if(typeof(code) !="undefined"&&typeof(name)!="undefined"){
                    	  dic.dictCode=code;
                          dic.dictName=name;
                          dic.id=id;
                          a.list.push(dic);
                      }
                   
                    
                  });
            	 
               progressLoad();
                $.ajax({
                    url:"${path}/dictionary/add",
                    data: JSON.stringify(a),
                    type:"post",
                    dataType:"json",
                    contentType:'application/json;charset=UTF-8',
                    //jsonpCallback:"callback",
                    success: function(data){
                    	  progressClose();
                        if(data.success)	{
                     	
                 	 $.messager.alert( '提示',data.msg, 'warning');
                  	$("#increase").attr("disabled", false); 
                 	dataDictionaryDataGrid.datagrid('load', $.serializeObject($('#searchForm')));
                 	deleterowsform2();
                 	$('#adddict input ').attr("disabled","disabled");
                 	flag=0;
                    	}else{
                    		
                    		 $.messager.alert( '提示',data.msg, 'warning');
                    	}
                    }
                });
         
    
	}
    function deleteDictionary() {
    	 var a=new Object();
    	 a.list=[];
    	 var list;
     a.dictType= $('#dictType').val();
    	 $('#dictionaryTable tr').each(function (index,value) {
    		  var dic=new Object();
    		  var  id = $(this).find("input[name='id'] ").val();
              var  code = $(this).find("input[name='dictCode'] ").val();
              var name = $(this).find("input[name='dictName']").val();
              if(typeof(code) !="undefined"&&typeof(name)!="undefined"){
            	  dic.dictCode=code;
                  dic.dictName=name;
                  dic.id=id;
                  a.list.push(dic);
              };
           
            
          });
    	 
    	 $.messager.confirm('询问', '您是否要删除当前类型？', function(b) {
    	if(b)	{
    		progressLoad();
            $.ajax({
                url:"${path}/dictionary/delete",
                data: JSON.stringify(a),
                type:"post",
                dataType:"json",
                contentType:'application/json;charset=UTF-8',
                //jsonpCallback:"callback",
                success: function(data){
                	  progressClose();
                    if(data.success)	{
                 	
             	 $.messager.alert( '提示',data.msg, 'warning');
              	$("#increase").attr("disabled", false); 
             	dataDictionaryDataGrid.datagrid('load', $.serializeObject($('#searchForm')));
             	deleterowsform2();
             	$('#adddict input ').attr("disabled","disabled");
            	flag=0;
                	}else{
                		
                		 $.messager.alert( '提示',data.msg, 'warning');
                	}
                }
            });
        	} ;
    
    	 });
        
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
	 <div class="body-width fs_18 bold header-color">数据字典维护</div>
<div class="header-div bg-color1 border-rgt border-lef">
  <form action="" id="searchForm">
  <div  style="width:70%;float: left">
    <table class="ys-table" style="width: 95%">
        <tbody>
        <tr>
            <td class="padding-left10 fs_16">
                字典类型
            </td>
            <td>
                   <input name="dictType"  style="width:124px;height: 30px;" />
            </td>
            <td class="td-width fs_16">
                字典代码
            </td>
            <td>
                 <input name="dictCode" style="width:124px;height: 30px;" />
            </td>
            <td class="td-width fs_16">
                字典名称
            </td>
            <td>
              <input name="dictName"  style="width:124px;height: 30px;" />
            </td>
            <td class="td-width"></td>
            <td>
             <!--      <input  onclick="searchUserFun()"  value="查询"   id="find" style="padding-left: 30px;"  class="btn-ok fs_16"/>
                <input  onclick="cleanUserFun()" value="重置"   id="reset"style="padding-left: 30px;"  class="btn-cancel fs_16"/> -->
            </td>
            <td style="width: auto"></td>
        </tr>
        </tbody>
    </table>
    </div>
    </form>
    <div style="float: right;margin-right:124px;margin-top: 10px ">
        <button class="btn-ok fs_16"  onclick="searchDictFun()">查询</button>
                <button class="btn-cancel fs_16"  onclick="cleanDictFun()" >重置</button>
            
        </div>
</div>
<table id="btn-table" class="float margin-top10" style="width: 49.5%;">
    <tr>
        <td>
            <button class="btn-ok fs_16" id="increase">新增</button>
        </td>
    </tr>
</table>
<div class="body-width clear">
    <div class="fw-target-div" style="height: 374px">
        <table  id="dataDictionaryDataGrid" class="body-width sf-msg-table td-line on-style1">
       
        </table>
    </div>
    <div class="fw-target-div" id="adddict" style="height: 374px;overflow-y: auto">
     <form id="dictionaryform" action="">
        <table id="dictionaryTable" class="input-table input-table2">
            <tr>
                <td>字典类型</td>
                <td>   
                 <input name="dictType"  id="dictType"  required="required" type="text"  class="xm-nm validate[required]" />
                 <input name="dictTypeCode"  id="dictTypeCode"  type="hidden"   />
                </td>
            </tr>
            <tr>
                <td>字典代码</td>
                <td>
           
                        <input  type="hidden" id="dictId"  name="id"  class="yp-nm" />                
                    <input  type="text" id="dictCode"   name="dictCode" class="yp-nm validate[required]" />                   
                </td>
                <td>字典名称</td>
                <td>
                    
                       <input  type="text" id="dictName"required="required" name="dictName"  class="xd-nm validate[required]" />
                </td>
                <td>
                   <!--  <button  onclick="addOne()"   class="add-tr"></button> -->
                   <img alt="" onclick="addOne()"  class="add-tr"   src="../static/style/images/add.png">
                </td>
                <td class='padding-left10'> <img alt="" class="del-tr"   src="../static/style/images/del.png"></td>
            </tr>
        </table>
        </form>
        <div class="hr-div">
            <input id="ok" type="button"  onclick="addDictionary()"  value="保存" class="btn-ok fs_16"/>
           <input id="back" type="button" onclick="deleterowsform2()"   value="取消" class="btn-ok fs_16"/>
          <input id="delete"  type="button" onclick="deleteDictionary()" style="display: none"   value="删除" class="btn-ok fs_16"/>
        </div>
        
    </div>
</div>

	<!--  <div class="pop-up bg-color1">
        <span class="float pop-up-span width-color dis-block">医保数据字典维护</span>
    </div>

    <div class="header-div-tj bg-color3 margin-top21">
        <form action="" id="searchForm">
        <div style="width: 80%;float: left">
            <table class="margin-left10 margin-top6">
                <tbody>
                <tr style="display: block;margin-top: 8px">
                    <td class="input-td">字典类型:</td>
                    <td>
                        <input name="dictType"  style="width:120px;height: 30px;" />
                    </td>
                    <td class="input-td" style="padding-left: 48px">字典代码:</td>
                    <td>
                      <input name="dictCode" style="width:120px;height: 30px;" />
                      
                    </td>
                    <td class="input-td" style="padding-left: 48px">字典名称:</td>
                    <td>
                         <input name="dictName"  style="width:120px;height: 30px;" />
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
    </div>
	 <div class="header-div-tj bg-color3 margin-top21">
        <table id="btn-table"  class="margin-left10 float margin-top10" style="width: 25%;">
            <tr>
                <td>
                    <button   class="btn" id="increase" >新增</button>
                </td>
                 <td>
                    <button onclick="editdict()"  class="btn">修改</button>
                </td>
         
                <td>
                    <button class="btn" id="daoru1"  name="daoru1" >导入</button>
                </td>  
            </tr>
        </table>
    </div>
	  <div class="body-width   margin-top15" style="height: 615px;width:550px;float: left">
        <table id="dataDictionaryDataGrid" class="yz-msg-table border-val" style="width:550px">
            
        </table>
    </div>
	  <div class="body-width   margin-top15" style="height: 615px;width:550px;float: right">  
	     <form action="" id="dictionaryform">
	       <table  id="dictionaryTable"  class="margin-left10 float margin-top10" style="width:550px">
                 <tr>
               <td class="input-td" style="padding-left: 48px">*字典类型:</td>
               <td>
                    <input  type="text"  name="dictType"  id="dictType"  style="width:120px;height: 30px;" />
                </td>    
               </tr>
                 <tr>
               <td class="input-td" style="padding-left: 48px">*字典编码:</td>
               <td>
                 <input  type="hidden" id="dictId"  name="id"  style="width:120px;height: 30px;" />
                 
                    <input  type="text" id="dictCode" name="dictCode"  style="width:120px;height: 30px;" />
                </td>    
                
               <td class="input-td" style="padding-left: 48px">*字典名称:</td>
               <td>
                    <input  type="text" id="dictName" name="dictName"  style="width:120px;height: 30px;" />
                </td>    
               <td>
                   <img alt=""  id="add" onclick="addOne()"  src="../static/style/images/condition_cancel.png">
                </td>    
               </tr>
               
               
          </table>
	      
	      <div class="margin-top10"style="width: 94%;margin-left: 3%;" >
            <table class="bz-zd-table " style="width: 96.5% !important;">
                <tbody>
                <tr>
                    <td style="text-align: left;color:#DF2828">
                        注：( * 为必须项)
                    </td>
                    <td colspan="2"style="text-align:center;">    
                    
                         <input id="ok" type="button"  onclick="addDictionary()"  value="保存" class="ok btn"/>
                        <input id="back" type="button" onclick="deleterowsform2()()"   value="取消" class="back btn"/>
                        <input id="delete"  type="button" onclick="deleteDictionary()" style="display: none"   value="删除" class="back btn"/>
                    </td>
                    <td colspan="1">
                    
                    </td>
                </tr>
                </tbody>
            </table>
          
        </div>
	     </form>
         
    </div>
     -->
   

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
    	 $('#dictionaryform input').val('');
    	  flag=1;
    	  $('#adddict input ').attr("disabled",false);
    	 deleterowsform2();
    	 $('#add').show();
    	
          $('#back').show();
        	 $('#delete').hide();
       
       //	$("#increase").attr("disabled", true);
      
    });
    
    <!--增加-弹出层-->
    $("#daoru1").click(function () {
        // var str = "我是弹出对话框";
        $(".BgDiv1").css("z-index","100");
        $(".BgDiv1").css({ display: "block", height: $(document).height()});
        $(".DialogDiv1").css("top", "5%");
        $(".DialogDiv1").css("display", "block");
      //  $('#userAddForm input').val('');
      
       
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

 $(function(){
	  
	   var imgobjs=document.getElementsByName("daoru1");
	   var excel=$('#excel').serialize();
	 
	  for (var i = 0; i < imgobjs.length; i++) {
		 
		   
		   
		    	$(imgobjs[i]).fileUpload(
					{
						uploadURL : '${path}/dictionary/importExcel?id=21',
								
						callback : function(
								data) {
							if (data != undefined
									&& data!= null) {
								 $.messager.alert( '提示',data.msg, 'warning');
								
							}; 
						}
					});
		  
		   };
	   
	  
   });  

$("a,button").focus(function(){this.blur()});
</script>
</body>
</html>
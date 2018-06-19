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
</style>
<script type="text/javascript">
var total=0;
var flag=0;
$(function() {
	/* $('#bzfl').combobox({
    	url:'${path }/drgs/getType',
        valueField:'flbm',
        textField:'fl',
        required:true,
 		 onHidePanel: function () {
 			
 		 }
 	 });  */
	drgsDataGrid = $('#drgsDataGrid').datagrid({
	       url : '${path }/drgs/drgsDataGrid',
	       queryParams: {          
	    	   type: 2	   
	         }    ,		
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
	       columns : [ [{
	           width : '20%',
	           title : '病种分类',
	           field : 'bzfl'
	        
	       }, {
	           width : '20%',
	           title : '病种序号',
	           field : 'bzxh'
	       
	       },{
	           width : '20%',
	           title : '病种编码',
	           field : 'bzbm'
	     
	       },  {
	           width : '20%',
	           title : '病种名称',
	           field : 'bzmc'
	      
	       }, 
	       {
	           width : '20%',
	           title : '医疗总费用限额',
	           field : 'ylzfyxe'
	        
	       }, 
	       {
	           width : '20%',
	           title : '医保统筹总费用限额',
	           field : 'ybtczfxe'
	        
	       }, 
	       {
	           width : '20%',
	           title : '报销比例',
	           field : 'bxbl'
	        
	       },{
	           width : '20%',
	           title : '开始时间',
	           field : 'starttime'
	      
	       },{
	           width : '20%',
	           title : '结束时间',
	           field : 'endtime'
	      
	       }] ],
	       onBeforeLoad: function (param) {
	       	updateDatagridHeader(this);        	
	       	
	       },
	       onClickRow:function(index,row){
	    	   editDrgs() ;
	    	   getSubject();
	        },
	        onLoadSuccess:function(data){
	        	getSubject();	
	        }
	   });
	   

	$('#drgsSubjectTable input ').attr("disabled","disabled");

 });
function getSubject() {
	var rows="";
	  rows = $('#drgsDataGrid').datagrid('getSelections'); 
	if(rows.length<1){
		rows=$('#drgsDataGrid').datagrid('getRows'); 
	}
	var bzxh="";
if(rows.length>0){
	bzxh=rows[0].bzxh;	
}
	
	drgsSubjectDataGrid = $('#drgsSubjectDataGrid').datagrid({
	       url : '${path }/drgsSubject/drgsSubjectDataGrid',
	       queryParams: {          
	    	   bzxh: bzxh,
	    	   type:2
	         }    ,			
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
	       columns : [ [{
	           width : '15%',
	           title : '病种序号',
	           field : 'bzxh'
	        
	       }, {
	           width : '15%',
	           title : '病种名称',
	           field : 'bzmc'
	       
	       },{
	           width : '15%',
	           title : '收费项目类型',
	           field : 'xmlx'
	     
	       },  {
	           width : '15%',
	           title : '收费项目编码',
	           field : 'xmbm'
	      
	       }, {
	           width : '15%',
	           title : '收费项目名称',
	           field : 'xmmc'
	       
	       },{
	           width : '15%',
	           title : '药品限定量',
	           field : 'ypxdyl'
	          
	       }, 
	       {
	           width : '12%',
	           title : '限定次数',
	           field : 'xdcs'
	        
	       }] ],
	       onBeforeLoad: function (param) {
	       	updateDatagridHeader(this);        	
	       	
	       },
	       onClickRow:function(index,row){
	        	editDrgsSubject();
	        }
	   });
	   
	
	
};
function editDrgs() {
	 var rows = $('#drgsDataGrid').datagrid('getSelections');    
	 if (rows.length>0){		
		 $('#bzfl').val( rows[0].bzfl); 
		 $('#drgsId').val(rows[0].id); 
		 $('#drgsbzxh').val(rows[0].bzxh); 
		 $('#drgsbzbm').val(rows[0].bzbm); 
		 $('#drgsbzmc').val(rows[0].bzmc); 
		 $('#zlfsbm').val( rows[0].zlfsbm); 
		 $('#zlfsmc').val( rows[0].zlfsmc); 
		 $('#ylzfyxe').val( rows[0].ylzfyxe); 
		 $('#ybtczfxe').val( rows[0].ybtczfxe); 
		 $('#xdzysj').val( rows[0].xdzysj); 
		 $('#bxbl').val( rows[0].bxbl); 
		 $('#starttime').val( rows[0].starttime); 
		 $('#endtime').val( rows[0].endtime); 

 	  }
	
	
}
function editDrgsSubject() {
	$('#drgsSubjectTable input ').attr("disabled",false);
	flag=0;
	deleterows();
	 var rows = $('#drgsSubjectDataGrid').datagrid('getSelections');    
	 if (rows.length>0){
		 $('#xmlb').val( rows[0].xmlb); 
		 $('#xmid').val( rows[0].id); 
		 $('#xmbm').val(rows[0].xmbm); 
		 $('#xmmc').val(rows[0].xmmc); 
		 $('#ypxdyl').val(rows[0].ypxdyl); 
		 $('#xdcs').val(rows[0].xdcs); 
		 
 	  }

	
}


function addChange() {
	$('#drgsTable input').val('');
	$('#bzfl').val('');
	
	
}
function insertDrgs() {
	 $.ajax({
         url:"${path}/drgs/editDrgs?type=2",
         type:"post",
         dataType:"json",
         data: $('#drgsForm').serialize()  ,	
         success: function(data){         
             if(data.success)	{              	
      	 $.messager.alert( '提示',data.msg, 'warning');
      	drgsDataGrid.datagrid('load', {type:'2'});
      	$('#drgsTable input ').val(''); 
         	}else{
         		
         		 $.messager.alert( '提示',data.msg, 'warning');
         	}
         }
     });
	
	
}
function deleteDrgs() {
	 var rows = $('#drgsDataGrid').datagrid('getSelections'); 
	 if (rows.length<1){
  	   $.messager.alert( '提示',"请选择数据", 'warning');
  	 return false;
	 }
	
	 $.ajax({
         url:"${path}/drgs/deleteDrgs",
         data:  $('#drgsForm').serialize()  ,	
         type:"post",
         dataType:"json",
         success: function(data){         
             if(data.success)	{              	
      	 $.messager.alert( '提示',data.msg, 'warning');
      	drgsDataGrid.datagrid('load', {type:'2'});
      	$('#drgsTable input ').val(''); 
         	}else{
         		
         		 $.messager.alert( '提示',data.msg, 'warning');
         	}
         }
     });
	
	
}
function addChange() {
	$('#drgsTable input').val('');
	$('#bzfl').val('');
	
	
}

function addSubject() {
	 var rows = $('#drgsSubjectDataGrid').datagrid('getSelections'); 
	 var bzxh="";
	 var bzmc="";
	 if (rows.length>0){
		 bzxh=rows[0].bzxh;
		 bzmc=rows[0].bzmc;	 
	 }
	 var p=0;
	   $("#drgsSubjectTable input[type='text']").each(function (index,value) {
	    if( $(this).val()==undefined||$(this).val()==""	){
	    	 $.messager.alert( '提示',"输入项不能为空", 'warning');
	    	 p=1;
	    	 return false ;
	    };	 
	 });
	 if(p==1){
		 return false;
	 }
   		 var a=new Object();
       	 a.list=[];  	 
       	 var  drgsSubject=new Object();
       	 $('#drgsSubjectTable tr').each(function (index,value) {
       		alert(11);
       		 if(index%2==0){
       			drgsSubject=new Object();
       			var  id = $(this).find("input[name='id'] ").val();
                var  xmlb = $(this).find("input[name='xmlb'] ").val();
                var xmbm = $(this).find("input[name='xmbm']").val();
                var xmmc = $(this).find("input[name='xmmc']").val();
                drgsSubject.xmlb=xmlb;
                drgsSubject.xmbm=xmbm;                
                drgsSubject.id=id;
                drgsSubject.xmmc=xmmc;
       		 }else if(index%2==1){
       			var ypxdyl = $(this).find("input[name='ypxdyl']").val();
                var xdcs = $(this).find("input[name='xdcs']").val();
                drgsSubject.ypxdyl=ypxdyl;
                drgsSubject.xdcs=xdcs;
                drgsSubject.bzxh=bzxh;
                drgsSubject.bzmc=bzmc;
                a.list.push(drgsSubject);
       		 }
       		
       
      });
       	 
       
           $.ajax({
               url:"${path}/drgsSubject/add",
               data: JSON.stringify(a),
               type:"post",
               dataType:"json",
               contentType:'application/json;charset=UTF-8',
               //jsonpCallback:"callback",
               success: function(data){         
                   if(data.success)	{              	
            	 $.messager.alert( '提示',data.msg, 'warning');
                 	getSubject()     ;
            	$('#drgsSubjectTable input ').attr("disabled","disabled");
            	$('#drgsSubjectTable input ').val('');
            	flag=0;
            	deleterows();
               	}else{
               		
               		 $.messager.alert( '提示',data.msg, 'warning');
               	}
               }
           });
    

}
function deleterows() {
	
	
	 $('#drgsSubjectTable input').val('');
	 var tb = document.getElementById('drgsSubjectTable');
   var rowNum=tb.rows.length;
  
   for (var i=2;i<rowNum;i++)
   {
       tb.deleteRow(i);
       rowNum=rowNum-1;
       i=i-1;
   }

  
}
function add() {
	$('#drgsSubjectTable input ').val('');

	$('#drgsSubjectTable input ').attr("disabled",false);
flag=1;
}

function deletedrgsSubject() {
	 var rows = $('#drgsSubjectDataGrid').datagrid('getSelections'); 
	 if (rows.length<1){
  	   $.messager.alert( '提示',"请选择数据", 'warning');
  	 return false;
	 }
	 var bzxh="";
	 var bzmc="";
	 if (rows.length>0){
		 bzxh=rows[0].bzxh;
		 bzmc=rows[0].bzmc;	 
	 }
	 var a=new Object();
   	 a.list=[];  	 
   	 var  drgsSubject=new Object();
   	 $('#drgsSubjectTable tr').each(function (index,value) {
   		 if(index%2==0){
   			drgsSubject=new Object();
   			var  id = $(this).find("input[name='id'] ").val();
            var  xmlb = $(this).find("input[name='xmlb'] ").val();
            var xmbm = $(this).find("input[name='xmbm']").val();
            var xmmc = $(this).find("input[name='xmmc']").val();
            drgsSubject.xmlb=xmlb;
            drgsSubject.xmbm=xmbm;                
            drgsSubject.id=id;
            drgsSubject.xmmc=xmmc;
   		 }else if(index%2==1){
   			var ypxdyl = $(this).find("input[name='ypxdyl']").val();
            var xdcs = $(this).find("input[name='xdcs']").val();
            var despbs = $(this).find("input[name='despbs']").val();
            drgsSubject.ypxdyl=ypxdyl;
            drgsSubject.xdcs=xdcs;
            drgsSubject.despbs=despbs;
            drgsSubject.bzxh=bzxh;
            drgsSubject.bzmc=bzmc;
            a.list.push(drgsSubject);
   		 }
   		
   
  });
	 alert(111);
	 $.messager.confirm('询问', '您是否要删除当前项目？', function(b) {
	if(b)	{
		
       $.ajax({
           url:"${path}/drgsSubject/delete",
           data: JSON.stringify(a),
           type:"post",
           dataType:"json",
           contentType:'application/json;charset=UTF-8',
           //jsonpCallback:"callback",
           success: function(data){
             
               if(data.success)	{
            	   $.messager.alert( '提示',data.msg, 'warning');
                	getSubject()     ;
           	$('#drgsSubjectTable input ').attr("disabled","disabled");
           	$('#drgsSubjectTable input ').val('');
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
 <jsp:include page="../include/leftMenu.jsp" />
   <div class="xRightbox">
   <div class="body-width fs_18 bold header-color">病种设定</div>
<div class="body-width">
    <div class="fw-target-div" style="height: 286px">
        <table id="drgsDataGrid"  class="body-width sf-msg-table on-style">
            
        </table>
    </div>
    <div class="fw-target-div" style="height: 286px">
      <form id="drgsForm" action="">
        <table class="input-table" id="drgsTable">
            <tr>
                <td>病种分类</td>
                <td>
                    <select name="bzfl" id="bzfl">
                        <option value="">请选择</option>
                        <option value="1">神经系统疾病</option>
                        <option value="2">呼吸系统疾病</option>
                        <option value="3">消化系统疾病</option>
                    </select>
                </td>
                <td>病种序号</td>
                <td>
                    <input name="id" type="hidden" id="drgsId" class="xu-hao"/>
                    <input name="bzxh" type="text"  id="drgsbzxh" class="xu-hao"/>
                </td>
            </tr>
            <tr>
                <td>病种编码</td>
                <td>
                    <input name="bzbm" id="drgsbzbm" type="text"/>
                </td>
                <td>病种名称</td>
                <td> 
                    <input  name="bzmc" id="drgsbzmc" type="text"/>
                </td>
            </tr>
       
            <tr>
                <td>医疗总费用限额</td>
                <td>
                    <input name="ylzfyxe"  id="ylzfyxe" type="text"/>&nbsp;元
                </td>
                <td>医保统筹支付限额</td>
                <td>
                    <input name="ybtczfxe" id="ybtczfxe" type="text"/>&nbsp;元
                </td>
            </tr>
            <tr>
              
                <td>报销比例</td>
                <td>
                    <input name="bxbl" id="bxbl" type="text"/>
                </td>
                 <td>生效时间</td>
                <td>
                    <input name="starttime" id="starttime" placeholder="点击选择时间" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly" />
                </td>
            </tr>
            <tr>
               
                <td>失效时间</td>
                <td>
                    <input name="endtime" id="endtime" placeholder="点击选择时间" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly" />
                </td>
            </tr>
        </table>
        
        <div class="hr-div">
            <input type="button" value="新增" class="btn-ok fs_16" onclick="addChange()"/>
            <input type="button" value="保存" class="btn-ok fs_16" onclick="insertDrgs()" id=""/>
            <input type="button" value="删除" class="btn-ok fs_16" onclick="deleteDrgs()" id=""/>
        </div>
        </form>
    </div>
</div>
<div class="clear"></div>
<!--临床路径医嘱收费项目维护-->
<div class="body-width fs_16 bold">临床路径医嘱收费项目维护</div>
<div class="body-width">
    <div class="fw-target-div" style="height: 288px">
        <table id="drgsSubjectDataGrid"  class="body-width sf-msg-table td-line on-style1">
           
        </table>
    </div>
    <div class="fw-target-div" style="height: 288px;overflow-y: auto">
        <table id="drgsSubjectTable" class="input-table input-table2">
            <tr>
                 <td class="Text_12">收费项目类型</td>
                <td>
                    <select name="xmlb" id="xmlb" class="xm-type">
                        <option value="">请选择</option>
                        <option value="2">政策内药品目</option>
                        <option value="3">政策内诊疗项目</option>
                    </select>
                </td>
                <td class="Text_12">收费项目编码</td>
                <td>
                <input type="hidden"  id="xmid" name="id" class="xm-nm" />                
                    <input type="text"  id="xmbm" name="xmbm" class="xm-nm" placeholder="0256"/>
                </td> 
              <td class="Text_12">收费项目名称</td>
                <td>
                    <input type="text" name="xmmc" id="xmmc"  class="xm-name"  placeholder="输液">
                </td>
            </tr>
            <tr>
                <td>药品限定用量</td>
                <td>
                    <input type="text" name="ypxdyl"  id="ypxdyl" class="yp-nm" placeholder="100ml/瓶"/>
                </td>
                <td>限定次数</td>
                <td>
                    <input type="text" name="xdcs" id="xdcs" class="xd-nm" placeholder="3">
                </td>
               
                <td class="Text_12"><input type="checkbox" id="despbs" style="width:12px!important; height:12px!important;"/>大额审批标识</td>
                <td>
                   <img alt=""   class="add-tr"   src="../static/style/images/add.png">
                </td>

            </tr>
            
      
        </table>
        <div class="hr-div">
            <input type="button" value="新增" class="btn-ok fs_16" onclick="add()" id=""/>
            <input type="button" value="保存"  onclick="addSubject()" class="btn-ok fs_16" />
            <input type="button" value="删除" class="btn-ok fs_16" onclick="deletedrgsSubject()"  id=""/>
        </div> 
    </div>
</div>
<div class="clear"></div>
   
	 
 
    </div>
 </div>
</div>
<script>
    /*初始化*/
    $(document).ready(function(){
       /*  $(".xm-type").attr("disabled", "true");
        $(".xm-nm").attr("disabled", "true");
        $(".xm-name").attr("disabled", "true");
        $(".yp-nm").attr("disabled", "true");
        $(".xd-nm").attr("disabled", "true");
        $(".add-tr").attr("disabled", "true");
        $(".del-tr").attr("disabled", "true");
        $("#add2").attr("disabled", "true");
        $("#del2").attr("disabled", "true");
        $("#del1").attr("disabled", "true"); */
    });

    /*表格鼠标事件*/
    $(".on-style tr").on({
        "mouseenter":function(){
            var index=$(this).index();
            if(index!=0){
                $(this).css("border", "1px solid #077DED");
                $(this).css("box-shadow", "#077DED 0px 0px 10px");
            }

        },
        "mouseleave":function(){
            $(this).css("border", "0px");
            $(this).css("box-shadow", "0px 0px 0px #077DED");
        },
        "click":function(){
            var index=$(this).index();
            if(index!=0) {
                $(this).addClass("active-tr").siblings().removeClass("active-tr");
                $(".xm-type").removeAttr("disabled")
                $(".xm-nm").removeAttr("disabled")
                $(".xm-name").removeAttr("disabled")
                $(".yp-nm").removeAttr("disabled")
                $(".xd-nm").removeAttr("disabled")
                $(".add-tr").removeAttr("disabled")
                $(".del-tr").removeAttr("disabled")
                $("#add2").removeAttr("disabled")
                $("#del1").removeAttr("disabled")

                var list = [];
                var list_length=$(this).find('td').length;
                for(var i=1;i<list_length;i++){
                    list.push($(this).find('td')[i].innerHTML);
                }

                $("#input-table1").find('select').get(0).options[1].selected = true;
                var table_list = [];
                table_list = $("#input-table1").find('input');

                for(var i=0;i<table_list.length;i++){
                    table_list.eq(i).attr("value",list[i]);
                }
            }
        }
    });

    $(".on-style1 tr").on({
        "mouseenter":function(){
            var index=$(this).index();
            if(index!=0){
                $(this).css("border", "1px solid #077DED");
                $(this).css("box-shadow", "#077DED 0px 0px 10px");
            }

        },
        "mouseleave":function(){
            $(this).css("border", "0px");
            $(this).css("box-shadow", "0px 0px 0px #077DED");
        },
        "click":function(){
            var index=$(this).index();
            if(index!=0) {
                $("#del2").removeAttr("disabled")
                var xm_type_val = $(this).find('td').eq(2).text();
                var xm_nm_val = $(this).find('td').eq(3).text();
                var xm_name_val = $(this).find('td').eq(4).text();
                var yp_nm_val = $(this).find('td').eq(5).text();
                var xd_nm_val = $(this).find('td').eq(6).text();

                $(".xm-type").get(0).options[1].selected = true;
                $(".xm-nm").attr("value",xm_nm_val);
                $(".xm-name").attr("value",xm_name_val);
                $(".yp-nm").attr("value",yp_nm_val);
                $(".xd-nm").attr("value",xd_nm_val);
                $(this).addClass("active-tr").siblings().removeClass("active-tr");
            }
        }
    });

  
    
    $(function () {
        /*增加一行*/
       
        $("#drgsSubjectTable").on("click",".add-tr",function(){
            //切换选中的按钮高亮状态
             if(flag==1){      
            total=total+1;
           var tr_html = "<tr class='border-tp'><td>收费项目类型 </td> <td><select name='xmlb'  id='xmlb"+total+"'> <option value=''>请选择</option> <option value='2'>政策内药品目</option>   <option value='3'>政策内诊疗项目</option></select></td>"+
                   "<td>收费项目编码</td><td><input  name='xmbm'  id='xmbm"+total+"'  type='text'></td><td>收费项目名称</td><td><input name='xmmc'  id='xmmc"+total+"'  type='text'></td></tr>"+
                   "<tr><td>药品限定剂量</td><td><input  name='ypxdyl' id='ypxdyl"+total+"'  type='text'></td><td>限定次数</td><td><input  name='xdcs' id='xdcs"+total+"'  type='text'></td>"+
                   "  <td class='Text_12'><input type='checkbox' name='despbs' id=''  style='width:12px!important; height:12px!important;'/>大额审批标识</td><td> <img    class='add-tr'   src='../static/style/images/add.png'><img class='del-tr'     src='../static/style/images/del.png'></td> ";  
            $("#drgsSubjectTable").append(tr_html);
          	
        }
        });
        
        /*删除一行*/
        $("#drgsSubjectTable").on("click",".del-tr",function(){
        	 total=total-1;
            var index=$(this).parent().parent().index();
            if(index!=1){
                $(this).parent().parent().prev().remove();
                $(this).parent().parent().remove();
            }
        });

        $("#add1").click(function(){
            var list = [];
            var list_length=$("#input-table1").find('input').length;
            for(var i=0;i<list_length;i++){
                list.push($("#input-table1").find('input').eq(i).val());
            }
            var xm_type_val = $("#input-table1 option:selected").text();
            $(".on-style tr").find('td').eq(0).text(xm_type_val);

            var table_list = [];
            table_list = $(".on-style").find('td');
            for(var i=1;i<table_list.length;i++){
                table_list.eq(i).text(list[i-1]);
            }
            $(".on-style tr").eq(1).css("display","");

        });
        /*删除table数据*/
        $("#del1").click(function(){
            $(".on-style tr").eq(1).css("display","none");
        });
        /*保存到 临床路径医嘱收费项目维护 表格*/
        $("#add2").click(function(){
            var xm_type_val = $(".xm-type option:selected").text();
            var xm_nm_val = $(".xm-nm").val();
            var xm_name_val = $(".xm-name").val();
            var yp_nm_val = $(".yp-nm").val();
            var xd_nm_val = $(".xd-nm").val();

            $(".on-style1 tr").find('td').eq(2).text(xm_type_val);
            $(".on-style1 tr").find('td').eq(3).text(xm_nm_val);
            $(".on-style1 tr").find('td').eq(4).text(xm_name_val);
            $(".on-style1 tr").find('td').eq(5).text(yp_nm_val);
            $(".on-style1 tr").find('td').eq(6).text(xd_nm_val);
            $(".on-style1 tr").eq(1).css("display","");
        });
        /*删除table数据*/
        $("#del2").click(function(){
            $(".on-style1 tr").eq(1).css("display","none");
        });
    });

    $("a,button").focus(function(){this.blur()});
</script>
</body>
</html>
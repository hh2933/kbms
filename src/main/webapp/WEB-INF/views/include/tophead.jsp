<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<!DOCTYPE html>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript">

$(function(){
	$('#city').val('${city}');
});

function logout(){
    $.messager.confirm('提示','确定要退出?',function(r){
        if (r){
            progressLoad();
            $.post('${path }/logout', function(result) {
                if(result.success){
                    progressClose();
                    window.location.href='${path }';
                }
            }, 'json');
        }
    });
}

function editUserPwd() {
    parent.$.modalDialog({
        title : '修改密码',
        width : 300,
        height : 250,
        href : '${path }/user/editPwdPage',
        buttons : [ {
            text : '确定',
            handler : function() {
                var f = parent.$.modalDialog.handler.find('#editUserPwdForm');
                f.submit();
            }
        } ]
    });
}

function changeCity(city)
{
	$.post('${path }/changeCity/changeCity', {
        city : city
    }, function(result) {
    	if(result.msg=="ok")
   		{
    		location.href='${path}/index';
   		}
    	else
   		{
    		 $.post('${path }/logout', function(result) {
                 if(result.success){
                     window.location.href='${path }';
                 }
             }, 'json');
   		}
    	
    }, 'JSON');
	
}
</script>
</head>
 <div class="header-title">
    <div class="float logo-div">
            <img class="header-title-img1" src="${staticPath }/static/style/images/logo.png" alt="数盺医疗"/>
        </div>

        <div class="float header-title-div">
            <img src="${staticPath }/static/style/images/header.png">
        </div>
        <div class="admin-div"> 
        <span class="cursor fs_14">
			<select id="city" name="city" onchange="changeCity(this.value)">
            <option value="wuhan">武汉</option>
            <option value="jinan">济南</option>
            </select>
		</span>              
                <a style="margin-left: 26px"  href="#"><img src="${staticPath }/static/style/images/userimg.png" alt="用户头像" class="img1"/>
               <span onclick="editUserPwd()" class="fs_14"><shiro:principal/></span></a>
          
                   <a href="#" onclick="logout()">  <span class="cursor fs_14"><img src="${staticPath }/static/style/images/quit.png" alt="退出" class="img2">&nbsp;退出</span></a>
                    
        </div>
 
 
      <%--   <img class="header-title-img1" src="${staticPath }/static/style/images/logo.png">
        <span class="header-title-title">医院保险费用监控与决策支持系统</span>
        <div style="float: right;margin-right: 20px">
            <a href="#"><img src="${staticPath }/static/style/images/user.png" class="header-title-img2">
            <span onclick="editUserPwd()" class="header-title-span"><shiro:principal/></span></a>

            <a href="#" onclick="logout()"> <img src="${staticPath }/static/style/images/logout.png" class="header-title-img3">
            <span  onclick="logout()"  class="header-title-span">注销</span></a>
        </div> --%>
    </div>

</html>
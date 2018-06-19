<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <title>医保控费系统</title>
    <meta name="viewport" content="width=device-width">
    <%@ include file="/commons/basejs.jsp" %>
    <link rel="stylesheet" type="text/css" href="${staticPath }/static/style/css/login.css?v=201612202107" />
    <script type="text/javascript" src="${staticPath }/static/login.js?v=20170115" charset="utf-8"></script>
    <style type="text/css">
    	#logologo{
    		width:16%;
    		height: 50%;
    	}
    	#footfoot{
    		width:31%;
    		height:27%;
    	}
    </style>
</head>
<body  onkeydown="enterlogin();" style="height:100%;background-color:#fff;padding: 0px;margin: 0px;">
<!-- 这里是标题LOGO -->
<div style="margin-top: 3%;margin-left:4.5%;" id=logologo><img height="100%" width="100%" src="${staticPath }/static/style/images/logo.png"></img></div>
<!-- <div class="top_div"></div> -->
<!-- 这里是登录框 -->
<div style="margin:90px auto auto auto;width: 37.5%; height:35.5%;border-radius: 4px;box-shadow:0px 5px 25px rgba(114, 156, 203,0.59 )">
<div  style="margin:0px 0px 0px 0px; width:100%;height:100%; padding-top: 1px;padding-bottom:30px;text-align: center;">
    <form method="post" id="loginform">
        <!--  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>-->
        <P style="position: relative; margin-top:40px; ">
            <p align="left" style="margin-bottom:10px;padding-left:128px;font-size: 1.4em;font-weight: bold;font-style:'Microsoft YaHei'; color: #4C9CF3;">用户名</p>
            <input class="ipt" type="text" name="username" placeholder="请输入用户名或邮箱"/>
        </P>
        <P style="position: relative; margin-top:10px;">
            <p align="left" style="padding-left:128px;font-size: 1.4em;font-weight: bold;font-style:'Microsoft YaHei'; color: #4C9CF3;">密码</p>
            <input class="ipt" id="password" type="password" name="password" placeholder="请输入密码"/>
        </P>
        <P style="position: relative; margin-top:10px;">
            <p align="left" style="padding-left:128px;font-size: 1.4em;font-weight: bold;font-style:'Microsoft YaHei'; color: #4C9CF3;">城市</p>
            <select class="ipt" id="city" name="city">
            <option value="" >请选择城市</option>
            <option value="wuhan">武汉</option>
            <option value="jinan">济南</option>
            </select>
            
        </P>
 <!--<P style="position: relative; margin-top:10px; ">
            <input class="captcha" type="text" name="captcha" placeholder="请输入验证码"/>
            <img id="captcha" alt="验证码" src="${path }/captcha.jpg" data-src="${path }/captcha.jpg?t=" style="vertical-align:middle;border-radius:4px;width:94.5px;height:35px;cursor:pointer;">
        	<input class="rememberMe" type="checkbox" name="rememberMe" value="1" style="vertical-align:middle;margin-left:10px;height:20px;"/> 记住密码
        </P>  -->   
        <p align="left" style="position: relative; margin-top:10px;color:#A4A4A4;">
        	<input class="rememberMe" type="checkbox" name="rememberMe" value="1" style="vertical-align:middle;margin-left:129px;height:20px;"/> 记住密码</p> 
			<div  style="margin-top: -32px;color: #EF2222;margin-left: 60px;font-size: 14px;line-height: 20px;"><img style="display: none;width: 4%;height: 4%;" id='errimg' src="${staticPath }/static/style/images/remind.png"><span id='errmsg' style='margin-left: 15px;'></span></div>
        <P style="position: relative; margin-top:10px; text-align: left;">
        </P>
        <div id='loginbutton' style="margin-left:134px;margin-top:15px;position: relative; width:248px;height:23px;background-color: #0068b7;font-size: 1.2em;color: white; cursor:pointer;background:-webkit-gradient(linear, 0% 0%, 0% 100%,from(#6AB1FF), to(#409BFF));border-radius:4px;" onclick="submitForm()">登&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;录</div>

<!--         <div style="height: 50px; line-height: 50px; margin-top: 10px;border-top-color: rgb(231, 231, 231); border-top-width: 1px; border-top-style: solid;">
            <P style="margin: 0px 35px 20px 45px;">
                <span style="float: left;">
                    <a style="color: rgb(204, 204, 204);" href="javascript:;">忘记密码?</a>
                </span>
                <span style="float: right;">
                    <a style="color: rgb(204, 204, 204); margin-right: 10px;" href="javascript:;">注册</a>
                    <a style="background: rgb(0, 142, 173); padding: 7px 10px; border-radius: 4px; border: 1px solid rgb(26, 117, 152); border-image: none; color: rgb(255, 255, 255); font-weight: bold;" href="javascript:;" onclick="submitForm()">登录</a>
                </span>
            </P>
        </div> -->
    </form>
</div>
</div>
<!-- <div class="footer">
    <p>
        <a href="http://www.sdtjqf.com.cn/" target="_blank" style="	font-size:1.2em; color:white;">技术支持&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;济南铁路局信息技术所&nbsp;&nbsp;&nbsp;&nbsp;山东腾蛟起风软件技术有限公司</a>
    </p>
</div> -->
<div id='footfoot'><img  height="100%" width="100%"  src="${staticPath }/static/style/images/footfoot.png"></div>
</body>
</html>

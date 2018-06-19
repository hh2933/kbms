<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp" %>
<!DOCTYPE html>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>医保规则知识库管理系统</title>
<%-- <%@ include file="/commons/basejs.jsp" %> --%>
<script src="${staticPath }/static/menu.js"></script>
<script type="text/javascript">
 $(function() {
      
        $.ajax({
            url:'${path }/menu/tree',
            type:'POST', //GET
            async:false,    //或false,是否异步
            dataType:'json',    //返回的数据格式：json/xml/html/script/jsonp/text
            success:function(data,textStatus,jqXHR){
               	menuData = data;
               	$("#left_menu_area").find("ul").remove();
               	var isFirst = true;
               	
                $.each(data,function(){
                	if(this.pid == -1){
                		//Dumper.alert(this); 
                		if(this.haschildren ==0 ){
                		$("#left_menu_area").append($('<li class="once" id='+this.id+'l ><a href="#" onclick="goPage(\''+this.attributes+'\','+this.id+');"><em class="'+this.iconCls+'"></em><span class="span-font">'+this.text+'</span></em></a></li>'));
                		}else{
                		
                		$("#left_menu_area").append($('<li class="once" id='+this.id+'l ><a href="#" class="xMenuOne" onclick="showLv2Menu(\''+this.id+'\');"><em class="'+this.iconCls+'"></em><span class="span-font">'+this.text+'</span><em class="em-up"></em></a></li>'));
						}
						
						/* if(isFirst){ 
							showLv2Menu(this.id);
							isFirst = false; 
						} */
                	}
                });
                 if(pageppId ){
                	 showLv2Menu(pageppId);
                	 showLv3Menu(pagepId,pagepId,pageppId);
                	 $("#"+pagepId+"l").find("a:first").addClass("xMenuonefocus2 xMenuFocus2");
                	 $("#"+pageppId+"l").find("a:first").addClass("xMenuonefocus1 xMenuFocus1");
                     $("#"+pagepId+"l").find("em").removeClass("xMenuShow");
                	 $("#"+pageId+"l").find("a:first").addClass("border-none");
                	 $("#"+pageId+"l").find("a:first").addClass(" xMenuonefocus");
                	 console.log($("#"+pageId+"l").find("a:first"));
                	 
                }else if (pagepId ){
                	 showLv2Menu(pagepId);
                	 $("#"+pagepId+"l").find("a:first").addClass("xMenuonefocus1 xMenuFocus1");
                     $("#"+pagepId+"l").find("em").removeClass("xMenuShow");
                	 $("#"+pageId+"l").find("a:first").addClass("border-none");
                	 $("#"+pageId+"l").find("a:first").addClass(" xMenuonefocus");
                }else{
                	 $("#"+pageId+"l").find("a").addClass("xMenuonefocus1 xMenuFocus1");
                     $("#"+pageId+"l").find("em").removeClass("xMenuShow");
                	 $("#"+pageId+"l").find("a:first").addClass("border-none");
                } 
               
               
            }
            
        });
     
    });
    
    function showLv2Menu(id){
    	var allUl=$("#left_menu_area").find("li ul");
    	$("#left_menu_area").find("li ul").remove();
      var zj=   $("#"+id+"l").find("ul");
      if(zj.length!=0){
    	$("#"+id+"l").find("ul").remove();
    	}else{
    		var  doc=$("#"+id+"l");
    //	$("#"+id+"l").find("ul").remove();
    	$("#"+id+"l").append($('<ul ></ul>'));
    	$.each(menuData,function(){
        	if(this.pid == id){
        	  if(this.haschildren =="0"){
        		
        		    var menuid=this.id+'';
        		    var menupid=this.pid+'';
          		$("#"+id+"l").find("ul").append($('<li class="once2"   id='+this.id+'l ><a href="#"  class="xMenuTwo span-font1" onclick="goPage2(\''+this.attributes+'\',\''+menuid+'\',\''+menupid+'\');" >'+this.text+'</a></li>'));

        	  }else{
        		  $("#"+id+"l").find("ul").append($('<li class="once2" id='+this.id+'l ><a href="#" class="xMenuTwo span-font1" onclick="showLv3Menu(\''+this.id+'\',\''+this.pid+'\',\''+id+'\');"><em class="'+this.iconCls+'"></em>'+this.text+'<em class="em-up2"></em></a></li>'));
        		  
        	  }
        	  
        	   
        	}
        });
    	
    	
    	
        }  
    }
    function showLv3Menu(id,pid,ppid){
    	
        var zj=   $("#"+id+"l").find("ul"); 
        if(zj.length!=0){
      //	$("#"+ppid+"l").find("ul").remove();
      	}else{
      		var  doc=$("#"+id+"l");
      		$("#"+pid+"l").find("ul li ul").remove();
      	$("#"+id+"l").append($('<ul ></ul>'));
      	$.each(menuData,function(){
          	if(this.pid == id){
          	  if(this.haschildren =="0"){
            		$("#"+id+"l").find("ul").append($('<li   id='+this.id+'l ><a href="#"  class="xMenuOne span-font1" onclick="goPage3(\''+this.attributes+'\',\''+this.id+'\',\''+this.pid+'\',\''+ppid+'\');" ><span class="span-font">'+this.text+'</span></a></li>'));

          	  }else{
          		  $("#"+id+"l").find("ul").append($('<li id='+this.id+'l ><a href="#" class="xMenuOne" onclick="showLv2Menu(\''+this.id+'\');"><em class="'+this.iconCls+'"></em><span class="span-font">'+this.text+'</span><em class="em-up"></em></a></li>'));
          		  
          	  }
          	  
          	   
          	}
          });
      	
      	
      	
          }  
      }
    function goPage(url,id){
    	$("#left_menu_area").find("li ul").remove();
        if (url && url.indexOf("http") == -1) {
        	if(url!=null && url.charAt(0)!="/"){
        		url = '${path }/' + url+"?id="+id;
        	}else{
        		url = '${path }' + url+"?id="+id;
        	}
        }
        console.log(url);
        location.href=url;
     //   $('#maincontainer').attr('src',url);
    }
    function goPage2(url,id,pid){
    	$("#"+pid+"l").find("li ul  ").remove();
        if (url && url.indexOf("http") == -1) {
        	if(url!=null && url.charAt(0)!="/"){
        		url = '${path }/' + url+"?id="+id+"&pid="+pid;
        	}else{
        		url = '${path }' + url+"?id="+id+"&pid="+pid;
        	}
        }
        console.log(url);
       // $('#maincontainer').attr('src',url);
        location.href=url;
    }
    function goPage3(url,id,pid,ppid){
    	$("#"+pid+"l").find("li ul  ").remove();
        if (url && url.indexOf("http") == -1) {
        	if(url!=null && url.charAt(0)!="/"){
        		url = '${path }/' + url+"?id="+id+"&pid="+pid+"&ppid="+ppid;
        	}else{
        		url = '${path }' + url+"?id="+id+"&pid="+pid+"&ppid="+ppid;
        	}
        }
        console.log(url);
       // $('#maincontainer').attr('src',url);
        location.href=url;
    }

    
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
</script>
</head>

        <div class="xLeftbox">
        	<ul class="xMenu" id="left_menu_area">
              <!--   <li>
                    <a href="#" data-rel="工作首页-全院.html"><img src="img/em1" class="em1"><span class="span-font">工作首页</span></a>
                </li>
                <li >
                    <a href="#" class="xMenuOne"><em class="em2"></em><span class="span-font">预算管理</span><em class="em-up"></em></a>
                    <ul>
                        <li><a href="#" data-rel="预算分配.html" class="xMenuTwo span-font1">预算分配</a></li>
                    </ul>
                </li>
                <li>
                    <a href="#" data-rel="患者监控.html"><em class="em3"></em><span class="span-font">患者监控</span></a>
                </li>
                <li>
                    <a href="#" data-rel="审核管理.html"><em class="em7"></em><span class="span-font">审核管理</span></a>
                </li>
                <li>
                    <a href="#" class="xMenuOne"><em class="em4"></em><span class="span-font">科室管理</span><em class="em-up"></em></a>
                    <ul>
                        <li><a href="#" data-rel="科室绩效.html" class="xMenuTwo span-font1" >科室绩效</a></li>
                    </ul>
                </li>
                <li>
                    <a href="#"  data-rel="监控审核.html"><em class="em6"></em><span class="span-font">病种管理</span></a>
                </li>
                <li>
                    <a href="#" class="xMenuOne"><em class="em5"></em><span class="span-font">决策分析</span><em class="em-up"></em></a>
                    <ul>
                        <li><a href="#" data-rel="药品分析.html" class="xMenuTwo span-font1">药品分析</a></li>
                    </ul>
                </li>
                <li>
                    <a href="#" data-rel="报告定制.html"><em class="em8"></em><span class="span-font">报告定制</span></a>
                </li>
                <li>
                    <a href="#" class="xMenuOne"><em class="em9"></em><span class="span-font">系统设置</span><em class="em-up"></em></a>
                    <ul>
                        <li><a href="#" data-rel="基础字典维护.html" class="xMenuTwo span-font1" >基础字典维护</a></li>
                    </ul>
                </li> -->
            </ul>
        </div>
        
   

<script>
    /*一级菜单鼠标事件*/
 /*    $(".xMenu>li").on({
        "mouseenter":function(){
            $(this).addClass("xMenuonefocus1");
            $(this).addClass("on-bg-li")
        },
        "mouseleave":function(){
            $(this).removeClass("xMenuonefocus1");
            $(this).removeClass("on-bg-li");
        }
    });
    */
    
    /*增加的条件鼠标放上事件*/
    $("#left_menu_area").on("mouseenter",".once",function(){
    	 $(this).css("background","#ffffff");
        $(this).find("a:first").css("color", "#4373FF");
    });
    $("#left_menu_area").on("mouseleave",".once",function(){
    	 $(this).css("background","");
        $(this).find("a:first").css("color", "");
    });
  /*   $("#left_menu_area").on("mouseenter",".once2",function(){
   	 $(this).css("background","#ffffff");
       $(this).find("a:first").css("color", "#4373FF");
   });
    $("#left_menu_area").on("mouseleave",".once2",function(){
   	 $(this).css("background","");
       $(this).find("a:first").css("color", "");
   }); */
    /*增加的条件鼠标放上事件*/
    $("#left_menu_area").on("click",".once",function(){
    	
    	
    	 if( $("#left_menu_area").find(".xMenuFocus1")){
    		
             $(".xMenuFocus1").removeClass("xMenuFocus1");
             
    	 }
    	  if( $("#left_menu_area").find(".xMenuonefocus1")){
    		 
    		$(".xMenuonefocus1 ").removeClass("xMenuonefocus1");
    		$(".xMenuFocus1").removeClass("xMenuFocus1");
    		 
    	 }
		 if( $("#left_menu_area").find(".border-none")){
    		 
			$(".border-none").removeClass("border-none");
    	 }
    	 console.log($("#left_menu_area").find(".once"));
    	// $(this).css("background","#ffffff");
       //  $(this).find("a").css("color", "#4373FF");
         $(this).find("a:first").addClass("xMenuonefocus1 xMenuFocus1 ");
         $(this).find("em").removeClass("xMenuShow");
    	 $(this).find("a:first").addClass("border-none");
    });
    $("#left_menu_area").on("click",".once2",function(){
    	$(".xMenuonefocus2").removeClass("xMenuonefocus2");
    	  $(".xMenuFocus2").removeClass("xMenuFocus2");
    	 $(this).find("a:first").css("color","");
    	// $(this).find("a:first").css("color","#4373FF");
    	 //$("#left_menu_area").find("li a").removeClass("xMenuonefocus1 xMenuFocus1");
    	// $(this).css("background","#ffffff");
        //  $(this).find("a").css("color", "#4373FF");
         $("#left_menu_area").find("li a").css("border-right","0px solid "); 
         $(this).find("a:first").addClass("xMenuonefocus2 xMenuFocus2");
         $(this).find("em").removeClass("xMenuShow");
    	 $(this).find("a:first").css("border-right","3px solid #4373FF"); 
    });

    /*一级菜单鼠标事件*/
  $(".xMenu>li").on({
        "mouseenter": function () {
            $(this).addClass("xMenuonefocus1");
            $(this).addClass("on-bg-li")
        },
        "mouseleave": function () {
            $(this).removeClass("xMenuonefocus1");
            $(this).removeClass("on-bg-li");
        }
    });
    
    $(".xMenuTwo,.xMenuThree").on({

        "mouseenter": function () {
            $(this).addClass("on-bg-two");
        },
        "mouseleave": function () {
            $(this).removeClass("on-bg-two");
        }
    })
    $("a,button").focus(function(){this.blur()});
</script>


</html>
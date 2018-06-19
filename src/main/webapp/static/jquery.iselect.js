/*
 * Select for Input (for jQuery)
 * ������������б�
 * version: 1.0 (2012/05/07)
 * @requires jQuery v1.2 or later
 *
 * Usage:
 *  
 *  jQuery(document).ready(function() {
 *    jQuery('#selectId').iselect() 
 *  })
 *
 *  
 *
 */
(function($) {
	$.iselect = {};
	$.iselect.csses = {
											divContainer:{position:'relative',width:'230px',height:'30px',display:'inline-block'},
											selectCss:{position:'absolute',top:'0',left:'0',width:'150px',height:'37px'},
											inputCss:{position:'absolute',border:'0',left:'1px',top:'1px',width:'130px',height:'30px',padding:'0',margin:'0'},
											inputTipCss:{position:'absolute',left:'0px',top:'37px',width:'150px',height:'100px',overflow:'auto',border:'1px #c5c5c5 solid',fontSize:'13px',display:'none'},
											tipItemCss:{paddingLeft:'3px'}
										};
	$.iselect.showTips = function(selectId,inputId,inputTipId){
		var value = $.trim($("#"+inputId).val());
		$("#"+inputTipId).empty();
//		if(value==''){
//			$("#"+inputTipId).hide();	
//			return;
//		}
		$("#"+selectId).find("option").each(function(index,item){
			 if(value!='')
			 {
				 if(item.text.indexOf(value)>=0){
						$("#"+inputTipId).append("<div value='"+item.value+"'>"+item.text+"</div>");
					} 
			 }
			 else {
				 $("#"+inputTipId).append("<div value='"+item.value+"'>"+item.text+"</div>");
			}
		});
		if($("#"+inputTipId).children().length>0){
			$("#"+inputTipId).show();
			$("#"+inputTipId).css({"z-index":"1"});
		}
		$("#"+inputTipId).children().mouseover(function(){
				$(this).css({backgroundColor:"#316AC5"});
		}).mouseout(function(){
				$(this).css({backgroundColor:"#FFFFFF"});
		}).click(function(){
				$(this).parent().hide();
				$("#"+selectId).val($(this).attr("value")).change();
				$("#"+inputId).val($(this).html());
		}).css($.iselect.csses.tipItemCss);
	}
	$.fn.iselect = function(){
		var selectId = this[0].id;
		var divId = selectId + '_div';
		var inputId = selectId + '_input';
		var inputTipId = selectId + '_inputTip';
        if(!$('#'+divId).attr("id"))
    	{
        	$(this).wrap('<div id="'+divId+'"></div>');    	
		
        	$('#'+divId).append('<input type="text" id="'+inputId+'" value="'+this[0].options[this[0].selectedIndex].text+'">')
								.append('<div id="'+inputTipId+'"></div>')
								.css($.iselect.csses.divContainer);
    	}
		
		$('#'+inputTipId).css($.iselect.csses.inputTipCss).attr("rel","iselect");
		
		//Ϊ�����б������change��click�¼�
		$("#"+selectId).change(function(){
			if(this.selectedIndex>-1)
			{
			  $("#"+inputId).val(this.options[this.selectedIndex].text);
			}
			
		}).click(function(){
			$("#"+inputTipId).hide();	
		}).css($.iselect.csses.selectCss)
			.attr("rel","iselect");
		
		//Ϊ�ı�����Ӱ���������͵���¼�
		$("#"+inputId).keyup(function(){
				$.iselect.showTips(selectId,inputId,inputTipId);
		}).focus(function(){
//			if($("#"+inputTipId).is(":visible"))
//			{				
//				$("#"+inputTipId).hide();
//			}
//			else
//			{
//				$.iselect.showTips(selectId,inputId,inputTipId);
//			}	
		}).click(function(event){
			//event.stopPropagation();
			if($("#"+inputTipId).is(":visible"))
			{				
				$("#"+inputTipId).hide();
			}
			else
			{
				$.iselect.showTips(selectId,inputId,inputTipId);
				event.stopPropagation();
			}	
		}).css($.iselect.csses.inputCss);
	}
	
	//��������ط�����������ʾ
	$(document).click(function(){
			$('div[rel*=iselect]').hide();	
			$('select[rel*=iselect]').each(function(){
				if(this.selectedIndex>-1)
				{
					$("#"+this.id+"_input").val(this.options[this.selectedIndex].text);
				}				
			})
	});
})(jQuery);
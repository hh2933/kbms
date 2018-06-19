(function($) {
	$.selfSelect = {};
	$.selfSelect.selfSelectValues={};
	$.selfSelect.selectId;
	$.fn.selfSelect = function(obj){		
		$.selfSelect.selectId = this[0].id;
		if(obj.datas && obj.datas.length>0)
		{
			initSelfSelect(obj.datas,$.selfSelect.selectId,obj);
		}
		else 
		{
			var queryParams={};
			if(obj.queryParams)
			{
				queryParams=obj.queryParams;
			}
			 $.ajax({
				 type: obj.method,
				 url:obj.url,
				 dataType: "json",
				 data:queryParams,
				 success: function (data) { 
					
					 initSelfSelect(data,$.selfSelect.selectId,obj);
				 }
			 });
		}
		
		 
		 if(obj.onChange)
		 {
			 $(this).change(function(){
				 obj.onChange();
			 });
		 }
		 
		
	}
	$.fn.selfSelect.getSelectValues= function(){
		return $.selfSelect.selfSelectValues;
	}
	
	$.fn.setSelectValue = function(selectValue)
	{
		$(this).val(selectValue).change();
	}
	
	function initSelfSelect(data,selectId,obj)
	{
		$('#'+selectId).empty();
		 if(data)
		 {
			 $.selfSelect.selfSelectValues = data;
			 $.each(data,function(index,d)
			{
				 $('#'+selectId).append("<option value='"+eval("d."+obj.valueField)+"'>"+eval("d."+obj.textField)+"</option>");
			 });
			
		 }
		 $('#'+selectId).iselect();
		 if(obj.onLoadSuccess)
		 {
			 obj.onLoadSuccess();
		 }
	}
	
})(jQuery);
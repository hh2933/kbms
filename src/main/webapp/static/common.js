function updateDatagridHeader(datagrid)
{
	var panel = $(datagrid).datagrid('getPanel');
 	var tr = panel.find('.datagrid-header-inner');
 	tr.css({"background":"#C6C6C8","height":"36px","text-align":"left"});
}
function updateDatagridPager(datagrid)
{
	var pager= $(datagrid).datagrid('getPager');
	 $(pager).pagination({
			layout:['first','prev','next','last']
	 });
}
$.extend($.fn.validatebox.defaults.rules, {
    checkChinese: {
        validator: function(value,param){
        	var v=/^[a-zA-Z\u4e00-\u9fa5][a-zA-Z0-9_\u4e00-\u9fa5]*$/;
            return v.test(value);
        },
        message: '必须输入纯文字或英文，且不能以数字开头'
    },
    checkLength: {
        validator: function(value,param){
            return value.length<=param[0];
        },
        message: '输入的长度超过限制'
    },
    checkNumber: {
        validator: function(value,param){
        	var v=/^[0-9]+.?[0-9]*$/ ;
            return v.test(value);
        },
        message: '必须输入数字'
    }
});
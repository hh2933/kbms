<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/commons/global.jsp"%>
<!DOCTYPE html>
<html>
<head>


<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style type="text/css">
.datagrid-row-selected {
	color: black;
}
</style>
</head>
<script type="text/javascript">
	var list;
	$(function() {
		list = '${dicList}';
		var resultType='${table.resultType}'
		$('#resultType').val(resultType);
		
		var isDetail='${table.isDetail}'
		$('#isDetail').val(isDetail);
		
		if(isDetail==1){
			$('#datailType').removeAttr('hidden');
			var datailType='${table.datailType}';
			$('#datailType').val(datailType);
		}
		
		var isFeedback='${table.isFeedback}';
		$('#isFeedback').val(isFeedback);
		
		var applyType = '${table.applyType}';
		$('#applyType').val(applyType);
	})

	var flag = true;
	var editIndex = undefined;
	function endEditing() {
		if (editIndex == undefined) {
			return true
		}
		if ($('#dg').datagrid('validateRow', editIndex)) {
			$('#dg').datagrid('endEdit', editIndex);
			editIndex = undefined;
			flag = true;
			return true;
		} else {
			return false;
		}
	}
	function checkCombobox(comboboxId) {
		var inputValue = $(comboboxId).combobox('getValue');
		var _options = $(comboboxId).combobox('options');
		var _data = $(comboboxId).combobox('getData');
		var flag = false;
		for (var i = 0; i < _data.length; i++) {
			if (_data[i][_options.valueField] == inputValue) {
				flag = true;
				break;
			}
		}
		if (!flag) {
			$(comboboxId).combobox('setValue', '');
		}
	}
	function onClickCell(index, field) {

		if (editIndex != index) {
			if (endEditing()) {
				editIndex = index;
				$('#dg').datagrid('selectRow', index).datagrid('beginEdit',
						index);
				var ed = $('#dg').datagrid('getEditor', {
					index : index,
					field : field
				});
				if (ed) {
					($(ed.target).data('textbox') ? $(ed.target).textbox(
							'textbox') : $(ed.target)).focus();
				}
				var edtypeshow = $(this).datagrid('getEditor', {
					index : index,
					field : 'dictTypeShow'
				});
				var row = $('#dg').datagrid('getSelected');
				$(edtypeshow.target).combobox('setValue', row.dictType);
				flag = false;
			} else {
				setTimeout(function() {
					$('#dg').datagrid('selectRow', editIndex);
				}, 0);
			}
		}
	}
	function onEndEdit(index, row) {
		var ed2 = $(this).datagrid('getEditor', {
			index : index,
			field : 'htmlInputShow'
		});
		row.htmlInputShow = $(ed2.target).combobox('getText');
		row.htmlInput = transType($(ed2.target).combobox('getText'));
		var ed1 = $(this).datagrid('getEditor', {
			index : index,
			field : 'dictTypeShow'
		});
		row.dictTypeShow = $(ed1.target).combobox('getText');
		row.dictType = $(ed1.target).combobox('getValue');
	}
	function transType(text) {
		if (text == '数字') {
			return "number";
		}
		if (text == '文本') {
			return "varchar2(100)";
		}
		if (text == '日期') {
			return "varchar2(10)";
		}
		if (text == '文本框') {
			return "text";
		}
		if (text == '下拉框') {
			return "select";
		}
		if (text == '日期框') {
			return "date";
		}
	}
	function accept() {
		if (endEditing()) {
			flag = true;
			$('#dg').datagrid('acceptChanges');
		}
	}
	function submitForm() {
		//其实不是submit，而是获取表单数据的假submit
		if (!$('#ruleForm').form('validate')) {
			alert('表单中的必填项为填写正确');
			return;
		}
		var rows = $('#dg').datagrid('getRows');
		if (rows.length == 0) {
			alert('规则表至少要有一列！');
			return;
		}
		if (!flag) {
			alert('有一行处于编辑状态,请点击保存后再提交');
			return;
		}
		a = {};
		a.id = $('#mid').val();
		a.menuId = $('#menuId').val();
		a.rows = rows;
		var searchCondition = false;
		var UniqueCondition = false;
		for (var i = 0; i < rows.length; i++) {
			if (rows[i].isFilter == '是') {
				searchCondition = true;
				break;
			}
		}
		for (var i = 0; i < rows.length; i++) {
			if (rows[i].isUnique == '是') {
				UniqueCondition = true;
				break;
			}
		}

		if (!searchCondition) {
			alert('搜索条件至少应该有一个');
			return;
		}
		if (!UniqueCondition) {
			alert('唯一标识至少应该有一个');
			return;
		}

		a.menuName = $('#menuNameEdit').textbox('getValue');
		a.ruleCode = $('#ruleCode').textbox('getValue');
		a.ruleType = $('#ruleType2').combobox('getValue');
		a.isUsed = $('input[name="isUsed"]:checked').val();
		a.remark = $('#remark').val();
		a.resultType = $('#resultType').val();
		a.isDetail = $('#isDetail').val();
		if ($('#isDetail').val() == 1) {
			a.datailType = $('#datailType').val();
		}
		a.isFeedback = $('#isFeedback').val();
		a.applyType = $('#applyType').val();
		$.ajax({
			type : "POST",
			url : "${path }/rule/update",
			data : JSON.stringify(a),
			contentType : "application/json",
			dataType : "json",
			beforeSend : function() {
				progressLoad();
			},
			success : function(data) {
				progressClose();
				parent.$.messager.alert('成功', data.msg, 'success', function() {
					window.location.reload();
				});
			}
		});
	}
	function clearForm() {
		parent.$.modalDialog.handler.dialog('close');
	}
	function enableSelectDicType(newValue, oldValue) {
		var ed = $('#dg').datagrid('getEditor', {
			index : editIndex,
			field : 'dictTypeShow'
		});
		if (newValue == 'select' || newValue == '下拉框') {
			$(ed.target).combobox('enable');
		} else {
			$(ed.target).combobox('clear');
			$(ed.target).combobox('disable');
		}
	}
	function changeHtmlInput(newValue, oldValue) {
		var ed = $('#dg').datagrid('getEditor', {
			index : editIndex,
			field : 'dictTypeShow'
		});
		var edc = $('#dg').datagrid('getEditor', {
			index : editIndex,
			field : 'htmlInputShow'
		});
		var data = [];
		if (newValue != undefined) {
			if (newValue == 'varchar2(10)' || newValue == '日期') {
				data = [ {
					label : '日期框',
					value : 'date'
				} ];
			} else {
				data = [ {
					label : '文本框',
					value : 'text'
				}, {
					label : '下拉框',
					value : 'select'
				} ];
			}
		}
		$(edc.target).combobox({
			data : data
		});

		$('#dg').datagrid('getColumnOption', 'htmlInputShow').editor.options.data = data;
		var i = $('#dg').datagrid('getColumnOption', 'htmlInputShow').editor.options.data;
	}

	$(function() {
		var isUsed = '${table.isUsed}';
		$('input[name="isUsed"][value=' + isUsed + ']').attr('checked',
				'checked');
		$('input[name="isUsed"][value=' + isUsed + ']').parent().siblings()
				.removeClass("active-radio");
		$('input[name="isUsed"][value=' + isUsed + ']').parent().addClass(
				"active-radio");
		var remark = '${table.remark}';
		$('#remark').val(remark);

	})
	function MoveUp() {
		if (endEditing()) {
			var row = $("#dg").datagrid('getSelected');
			var index = $("#dg").datagrid('getRowIndex', row);
			mysort(index, 'up', 'dg');
		}
	}
	//下移
	function MoveDown() {
		if (endEditing()) {
			var row = $("#dg").datagrid('getSelected');
			var index = $("#dg").datagrid('getRowIndex', row);
			mysort(index, 'down', 'dg');
		}
	}

	function mysort(index, type, gridname) {
		if ("up" == type) {
			if (index != 0) {
				var toup = $('#' + gridname).datagrid('getData').rows[index];
				var todown = $('#' + gridname).datagrid('getData').rows[index - 1];
				$('#' + gridname).datagrid('getData').rows[index] = todown;
				$('#' + gridname).datagrid('getData').rows[index - 1] = toup;
				$('#' + gridname).datagrid('refreshRow', index);
				$('#' + gridname).datagrid('refreshRow', index - 1);
				$('#' + gridname).datagrid('selectRow', index - 1);
			}
		} else if ("down" == type) {
			var rows = $('#' + gridname).datagrid('getRows').length;
			if (index != rows - 1) {
				var todown = $('#' + gridname).datagrid('getData').rows[index];
				var toup = $('#' + gridname).datagrid('getData').rows[index + 1];
				$('#' + gridname).datagrid('getData').rows[index + 1] = todown;
				$('#' + gridname).datagrid('getData').rows[index] = toup;
				$('#' + gridname).datagrid('refreshRow', index);
				$('#' + gridname).datagrid('refreshRow', index + 1);
				$('#' + gridname).datagrid('selectRow', index + 1);
			}
		}

	}
	function checkIsDetail() {
		if ($('#isDetail').val() == 0) {
			$('#datailType').attr('hidden', 'true');
		} else {
			$('#datailType').removeAttr('hidden');
		}
	}
</script>
<div
	style="border-collapse: separate; border-spacing: 2px; border-color: grey; margin-left: 50px; margin-right: 50px;">
	<!--以上： 左侧菜单栏需要的3个DIV -->
	<div class="easyui-panel"
		style="width: 100%; max-width: 100%; padding: 30px 60px;">
		<div style="float: right;">
			<span style="color: red;">注：*为必须项</span>
		</div>
		<form id="ruleForm">
			<div style="margin-bottom: 20px">
				<div style="float: left;">
					<span style="margin-right: 20px;"><span style="color: red;">*</span>医保规则名称</span>
					<input id='menuNameEdit' class="easyui-textbox" name="menuName"
						style="width: 220px;" value="${table.menuName }"
						data-options="required:true,validateOnCreate:false,validType:['checkChinese[]','checkLength[16]']"> <input
						id='mid' type="hidden" value="${table.id }"> <input
						id='menuId' type="hidden" value="${table.menuId }">
				</div>
				<div style="float: left;">
					<span style="margin-right: 20px;"><span style="color: red;">*</span>医保规则编号</span>
					<input id='ruleCode' class="easyui-textbox" name="ruleCode"
						style="width: 120px;" value="${table.ruleCode }"
						data-options="required:true,validateOnCreate:false,validType:['checkNumber[]']">
				</div>
				<div style="margin-bottom: 20px; float: left:;">
					<span style="margin-right: 20px;"><span style="color: red;">*</span>结果类型</span>
					<select id='resultType' name='resultType' >
						<option value="1">违规</option>
						<option value="2">可疑</option>
					</select>
				</div>
			</div>
			<div style="margin-bottom: 20px; float: left;">
				<span style="margin-right: 20px;"><span style="color: red;">*</span>医保规则类型</span>
				<select id='ruleType2' class="easyui-combobox" name="ruleType"
					label="Language" style="width: 220px;"
					data-options="url:'${path }/parameter/getType?clum=ruleType',
				        	  valueField:'pValue',
					          textField:'pName',
					          required:true,
					          validateOnCreate:false,
					          validateOnBlur:true,
					          onHidePanel: function () {
				      			 checkCombobox(this);
				      		 },
				      		 onLoadSuccess:function(){
				      		        var ruleType='${table.ruleType }';
            						$('#ruleType2').combobox('setValue',ruleType);
				      		 }
                "></select>
				<div style="margin-bottom: 20px; float: right; width: 70px;">
					<select hidden id='datailType' name='datailType' >
						<option value="1">取药记录</option>
						<option value="2">疗程记录</option>
						<option value="3">相关记录</option>
						<option value="4">重复记录</option>
					</select>
				</div>
				<div style="margin-bottom: 20px; float: right;">
					<span style="margin-right: 20px;"><span style="color: red;">*</span>结果类型</span>
					<select onchange="checkIsDetail()" id='isDetail' name='isDetail' >
						<option value="0">不查询</option>
						<option value="1">查明细</option>
					</select>
				</div>
			</div>
			<div style="margin-bottom: 20px; float: left; margin-left: 20px;">
				<div style="float: left;">
					<div style="float: left;">
						<span><span style="color: red;">*</span>医保规则启用标志</span>
					</div>
					<div class='team-table'
						style="float: left; width: 100px; margin-left: 10px;">
						<label class="active-radio fs_14" onclick="changelable($(this))">
							<input type="radio" class="zy-ks" name="isUsed" value='1' checked
							style="display: none"><i></i>正常
						</label> <label class="fs_14"
							onclick="changelable($(this))"> <input type="radio"
							class="mz-ks" name="isUsed" value='0' style="display: none"><i></i>停用
						</label>
					</div>
				</div>
				<div style="float: left;margin-top: -4px;">
					<span><span style="color: red;">*</span>是否需要反馈</span> <select
						id='isFeedback' name='isFeedback' >
						<option value='1'>是</option>
						<option value='0'>否</option>
					</select>
				</div>
				<div style="float: left;margin-top: -4px;">
					<span><span style="color: red;">*</span>规则适用类型</span> <select
						id='applyType' name=''applyType''>						
						<option value='3'>门诊/住院</option>
						<option value='1'>门诊</option>
						<option value='2'>住院</option>
					</select>
				</div>
			</div>
			<div
				style="margin-bottom: 20px; width: 900px; height: 100px; float: left;">
				<div style="float: left;">医保规则备注</div>
				<div style="float: left; margin-left: 30px;">
					<textarea id='remark' name='remark' cols="60" rows="5"></textarea>
				</div>
			</div>
		</form>
		<table id="dg" class="easyui-datagrid"
			style="width: 870px; height: auto; color: black;"
			data-options="
                iconCls: 'icon-edit',
                singleSelect: true,
                toolbar: '#tb',
                onClickCell: onClickCell,
                onEndEdit: onEndEdit,
                url:'${path }/rule/loadUpdateGridData?id=${table.id }',
                method:'post'
            ">
			<thead>
				<tr>
					<th
						data-options="field:'thName',width:120,editor:{type:'validatebox',options:{required:true,validType:['checkChinese[]','checkLength[30]']}}"><span><span
							style="color: red;">*</span>列名</span></th>
					<th
						data-options="field:'columnTypeShow',width:140,
                                formatter:function(value,row){
                                	if(row.columnType=='varchar2(100)'){
                                		return '文本'
                                	}
                                	if(row.columnType=='varchar2(10)'){
                                		return '日期'
                                	}
                                	if(row.columnType=='number'){
                                		return '数字'
                                	}
                                }
                        "><span><span
							style="color: red;">*</span>数据类型</span></th>
					<th id='tbc'
						data-options="field:'htmlInputShow',width:178,
                        formatter:function(value,row){
                            if(row.htmlInput=='text'){
                            	row.htmlInputShow='text';
                            	return '文本框';
                            }
                            if(row.htmlInput=='select'){
                            	row.htmlInputShow='select';
                            	return '下拉框';
                            }
                            if(row.htmlInput=='date'){
                            	row.htmlInputShow='date';
                            	return '日期框';
                            }
                        },
                        editor:{
                            type:'combobox',
                            options:{
								valueField: 'value',
								textField: 'label',
								data: [{
									label: '文本框',
									value: 'text'
								},{
									label: '下拉框',
									value: 'select'
								},{
									label: '日期框',
									value: 'date'
								}],
								required:true,
								onChange:enableSelectDicType,
								onHidePanel: function () {
				      			 checkCombobox(this);
				      		 	}
                            }
                        }"><span><span
							style="color: red;">*</span>对应HTML控件</span></th>
					<th
						data-options="field:'dictTypeShow',width:180,
                		 formatter:function(value,row){
                		 	var dic= row.dictType;
					    	var l=JSON.parse(list);
					    	var j;
					    	for (var i = 0; i < l.length; i++) {
								if(dic==l[i].dictTypeCode){
									j=l[i].dictType;
								}
							}
							return j;
    					},
                        editor:{
                            type:'combobox',
                            options:{
                            	data:JSON.parse(list),
								valueField: 'dictTypeCode',
								textField: 'dictType',
								validateOnCreate:false,
					      		required:true,
					      		onHidePanel: function () {
					      			 checkCombobox(this);
					      		}
                            }
                        }">
					</th>

					<th
						data-options="field:'isUnique', formatter:function(value,row){
                            if(row.isUnique=='1' ||row.isUnique=='是'){
                            		row.isUnique='是';
	                            	return '是';
	                            }else{
	                            	row.isUnique='否';
	                            	return '否';
	                            }
                            },width:150,align:'center',editor:{type:'checkbox',options:{on:'是',off:'否'}}">唯一标识</th>
					<th
						data-options="field:'isFilter',formatter:function(value,row){
		                            if(row.isFilter=='1' ||row.isFilter=='是'){
		                            		row.isFilter='是';
			                            	return '是';
			                            }else{
			                            	row.isFilter='否';
			                            	return '否';
			                            }
                            },width:100,align:'center',editor:{type:'checkbox',options:{on:'是',off:'否'}}">筛选条件标识</th>
					<th data-options="field:'htmlInput',hidden:true"></th>
					<th data-options="field:'columnType',hidden:true"></th>
					<th data-options="field:'dictType',hidden:true"></th>
					<th data-options="field:'id',hidden:true"></th>
				</tr>
			</thead>
		</table>

		<div id="tb" style="height: auto">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				data-options="iconCls:'icon-save',plain:true" onclick="accept()">保存当前编辑的行</a>
			<a href="javascript:void(0)" class="easyui-linkbutton"
				data-options="iconCls:'icon-edit',plain:true" onclick="MoveUp()">上移</a>
			<a href="javascript:void(0)" class="easyui-linkbutton"
				data-options="iconCls:'icon-edit',plain:true" onclick="MoveDown()">下移</a>
		</div>
		<div style="text-align: center; padding: 5px 0">
			<button onclick="submitForm()" id="ok" class="btn-ok">确认</button>
			<input type="button" onclick="clearForm()" class="btn-cancel"
				value='取消'>
		</div>
	</div>
	<!-- 头部3个DIV的结尾 -->
</html>
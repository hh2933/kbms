<%--
  Created by IntelliJ IDEA.
  User: zengj
  Date: 2018-6-6
  Time: 11:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<html>
<head>
    <%@ include file="/commons/basejs.jsp" %>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <style>
        .sf-msg-table th {
            border-top: none !important;
        }

        .fw-target-div {
            height: 392px;
            margin-top: 4px;
            margin-bottom: 20px;
        }

        .fw-target-div:nth-child(odd) {
            background: none;
            overflow-x: auto;
        }

        .input-table2 select {
            width: 66px !important;
        }

        .input-table2 input {
            width: 66px !important;
        }

        .header-div {
            min-height: 46px !important;
            height: auto !important;
            padding-bottom: 8px
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
            border-width: 0px 0 0 1px;
            border-style: solid;
        }

        .datagrid-header td, .datagrid-body td, .datagrid-footer td {
            border-width: 0 1px 0px 0;
            border-style: dotted;
            margin: 0;
            padding: 0;
        }
    </style>
    <script type="text/javascript">
        var dataGrid;
        $(function () {
            $("#zfbl").bind('blur', function () {
                this.value = this.value.replace(/[^0-9.]/g, '');
            });
            $("#jg").bind('blur', function () {
                this.value = this.value.replace(/[^0-9.]/g, '');
            });
            importExcel();
            dataGrid = $('#dataGrid').datagrid({
                url: '${path }/catalog/ypml/findPage',
                fit: true,
                striped: true,
                pagination: true,
                singleSelect: false,
                nowrap: false,
                pageSize: 20,
                pageList: [10, 20, 30, 40, 50, 100],
                columns: [[
                    {
                        checkbox: 'true',
                        field: 'fc'
                    }, {
                        width: '3%',
                        title: '<font size="3px">序号</font>',
                        field: 'ROW_ID'
                    }, {
                        width: '7%',
                        title: '<font size="3px">医保药品编码</font>',
                        field: 'YBYPBM'
                    }, {
                        width: '7%',
                        title: '<font size="3px">医院药品编码</font>',
                        field: 'YYYPBM'
                    }, {
                        width: '7%',
                        title: '<font size="3px">医院药品名称</font>',
                        field: 'YYYPMC'
                    }, {
                        width: '5%',
                        title: '<font size="3px">药品类型</font>',
                        field: 'YPLX',
                        formatter: function (value, row, index) {
                            if (row.YPLX == 1) {
                                return "西药";
                            } else if (row.YPLX == 2) {
                                return "中成药";
                            } else if (row.YPLX == 3) {
                                return "中药饮片";
                            }
                        }
                    }, {
                        width: '5%',
                        title: '<font size="3px">剂型</font>',
                        field: 'JX'
                    }, {
                        width: '5%',
                        title: '<font size="3px">规格</font>',
                        field: 'GG'
                    }, {
                        width: '3%',
                        title: '<font size="3px">单位</font>',
                        field: 'DW'
                    }, {
                        width: '3%',
                        title: '<font size="3px">价格</font>',
                        field: 'JG'
                    }, {
                        width: '5%',
                        title: '<font size="3px">最小包装</font>',
                        field: 'ZXBZ'
                    }, {
                        width: '5%',
                        title: '<font size="3px">生产厂商</font>',
                        field: 'SCCS'
                    }, {
                        width: '5%',
                        title: '<font size="3px">自付比例</font>',
                        field: 'ZFBL'
                    }, {
                        width: '5%',
                        title: '<font size="3px">给药途径</font>',
                        field: 'GYTJ'
                    }, {
                        width: '15%',
                        title: '<font size="3px">说明</font>',
                        field: 'SM'
                    }, {
                        width: '5%',
                        title: '<font size="3px">导入时间</font>',
                        field: 'DRSJ'
                    }, {
                        width: '5%',
                        title: '<font size="3px">解析状态</font>',
                        field: 'JXZT',
                        formatter: function (value, row, index) {
                            if (row.JXZT == 1) {
                                return "未开始";
                            } else if (row.JXZT == 2) {
                                return "已解析";
                            } else if (row.JXZT == 3) {
                                return "全部解析";
                            } else if (row.JXZT == 4) {
                                return "部分解析";
                            } else if (row.JXZT == 5) {
                                return "全部未能解析";
                            } else if (row.JXZT == 6) {
                                return "手动解析";
                            }
                        }
                    }, {
                        width: '7%',
                        title: '<font size="3px">解析结果描述</font>',
                        field: 'JXJGMS'
                    }, {
                        width: '15%',
                        title: '<font size="3px">已解析说明</font>',
                        field: 'YJXSM'
                    }, {
                        width: '15%',
                        title: '<font size="3px">未能解析说明</font>',
                        field: 'WNJXSM'
                    }
                ]],
                onBeforeLoad: function (param) {
                    updateDatagridHeader(this);
                }
            });

            $('#editForm').form({
                url: $('#editForm').attr("action"),
                onSubmit: function () {
                    progressLoad();
                    var isValid = $(this).form('validate');
                    if (!isValid) {
                        progressClose();
                    }
                    return isValid;
                },
                success: function (result) {
                    progressClose();
                    result = $.parseJSON(result);
                    if (result.success) {
                        closeWin();
                        searchData();
                    }
                    else {
                        parent.$.messager.alert('提示', result.msg, 'warning');
                    }
                }
            });

            $("#delDrugCatalog").click(function () {

                var dataChecked = dataGrid.datagrid('getSelections');

                if (dataChecked.length < 1) {
                    parent.$.messager.alert({
                        title: '提示',
                        msg: '请选择要删除的记录！'
                    });
                    return;
                }
                parent.$.messager.confirm('询问', '您是否要删除当前记录？', function (flag) {
                    if (!flag) {
                        return;
                    }

                    var id = [];
                    for (i = 0; i < dataChecked.length; i++) {
                        id[i] = dataChecked[i].ID;
                    }

                    progressLoad();
                    $.post('${path }/drugCatalog/deleteDrugCatalog', {
                        id: id
                    }, function (result) {
                        if (result.success) {
                            parent.$.messager.alert('提示', result.msg, 'info');
                            dataGrid.datagrid('reload');
                        }
                        progressClose();
                    }, 'JSON');

                });
            });

        });
        function searchData() {
            dataGrid.datagrid('load', $.serializeObject($('#searchForm')));
        }
        function cleanProjectCatalogFun() {
            $('#searchForm')[0].reset();
            dataGrid.datagrid('load', {});
        }
        function openWin(optName, url) {
            $(".BgDiv3").css("z-index", "100");
            $(".BgDiv3").css({display: "block", height: $(document).height()});
            $(".DialogDiv3").css("top", "15%");
            $(".DialogDiv3").css("display", "block");
            document.documentElement.scrollTop = 0;
            $('#optName').text(optName);
            $('#editForm').attr("action", url);
        }
        function closeWin() {
            $(".BgDiv3").css("display", "none");
            $(".DialogDiv3").css("display", "none");
            $('#editForm')[0].reset();
            $('#id').val('');
        }
        function editWin(url) {
            var dataChecked = dataGrid.datagrid('getSelections');
            if (dataChecked.length == 0) {
                parent.$.messager.alert({
                    title: '提示',
                    msg: '请选择要修改的记录！'
                });
                return;
            } else if (dataChecked.length > 1) {
                parent.$.messager.alert({
                    title: '提示',
                    msg: '只能修改一条记录！'
                });
                return;
            }
            $.post('${path }/drugCatalog/selectEditDrugCatalog', {
                id: dataChecked[0].ID
            }, function (result) {
                $('#id').val(result.id);
                $('#ybypbm').val(result.ybypbm);
                $('#yyypmc').val(result.yyypmc);
                $('#yyypbm').val(result.yyypbm);
                $('#jx').val(result.jx);
                $('#zxbz').val(result.zxbz);
                $('#gytj').val(result.gytj);
                $('#gg').val(result.gg);
                $('#dw').val(result.dw);
                $('#jg').val(result.jg);
                $('#sccs').val(result.sccs);
                $('#zfbl').val(result.zfbl);
                $('#sm').val(result.sm);
                $('#yplx').val(result.yplx);
                openWin('修改', url);

            }, 'JSON');
        }

        //导出全部目录
        function exportData() {
            parent.$.messager.alert({
                title: '提示',
                msg: '正在导出数据... 请耐心等待！'
            });
            location.href = '${path}/catalog/ypml/exportData';
        }

        //导出未解析目录
        function exportWjxData() {
            parent.$.messager.alert({
                title: '提示',
                msg: '正在导出数据... 请耐心等待！'
            });
            location.href = '${path}/catalog/ypml/exportWjxData';
        }

        function exportDrugCatalogHistory() {
            parent.$.messager.alert({
                title: '提示',
                msg: '正在导出数据... 请耐心等待！'
            });
            location.href = '${path}/drugCatalog/exportDrugCatalogHistory';
        }
        function exportTemp() {
            location.href = '${path}/catalog/ypml/exportTemp';
        }
        function importExcel() {
            var imgobjs = document.getElementById("importExcel");
            var excel = $('#excel').serialize();

            $(imgobjs).fileUpload({
                uploadURL: '${path}/catalog/ypml/importExcel',
                singleFileUploads: true,
                callback: function (data) {
                    if (data != undefined && data != null) {
                        $.messager.alert('提示', data.msg, 'warning');
                        searchData();
                        if(data.obj > 0){
                            location.href = '${path}/catalog/ypml/exportRepeat';
                        }
                    }
                }
            });
        }

        //解析单条目录
        function explain() {
            var dataChecked = dataGrid.datagrid('getSelections');
            if (dataChecked.length == 0) {
                parent.$.messager.alert({
                    title: '提示',
                    msg: '请选择要解析的目录！'
                });
                return;
            } else if (dataChecked.length > 1) {
                parent.$.messager.alert({
                    title: '提示',
                    msg: '每次只能解析一条目录！'
                });
                return;
            }else if (dataChecked[0].JXZT != 1 ){
                parent.$.messager.alert({
                    title: '提示',
                    msg: '该条目录已解析过，不能重复解析！'
                });
                return;
            }

            progressLoad();
            $.post('${path }/catalog/ypml/explain', {
                id: dataChecked[0].ID
            }, function (result) {
                if (result.success) {
                    parent.$.messager.alert('提示', result.msg, 'info');
                    dataGrid.datagrid('reload');
                }
                progressClose();
            }, 'JSON');
        }

        //解析全部
        function explainAll() {
            progressLoad();
            $.post('${path }/catalog/ypml/explainAll', {
            }, function (result) {
                if (result.success) {
                    parent.$.messager.alert('提示', result.msg, 'info');
                    dataGrid.datagrid('reload');
                }
                progressClose();
            }, 'JSON');
        }

        //解析状态设为手动解析
        function setJxzt() {
            var dataChecked = dataGrid.datagrid('getSelections');
            if (dataChecked.length == 0) {
                parent.$.messager.alert({
                    title: '提示',
                    msg: '请选择要设为手动解析的目录！'
                });
                return;
            } else if (dataChecked.length > 1) {
                parent.$.messager.alert({
                    title: '提示',
                    msg: '每次只能选择一条目录！'
                });
                return;
            }
            progressLoad();
            $.post('${path }/catalog/ypml/setJxzt', {
                id: dataChecked[0].ID
            }, function (result) {
                if (result.success) {
                    parent.$.messager.alert('提示', result.msg, 'info');
                    dataGrid.datagrid('reload');
                }
                progressClose();
            }, 'JSON');
        }
    </script>
</head>
<body>
<div class="xPage">
    <jsp:include page="../include/tophead.jsp"/>
    <div class="xMain">
        <jsp:include page="../include/leftMenu.jsp"/>
        <div class="xRightbox">
            <!--限适应症用药(违规)-->
            <div class="body-width fs_18 bold header-color">药品目录解析</div>
            <div class="header-div bg-color1 border-rgt border-lef">
                <form id="searchForm">
                    <table class="ys-table" style="width: 95%">
                        <tbody>
                        <tr>
                            <td class="padding-left10 fs_16">
                                医保药品编码
                            </td>
                            <td>
                                <input type="text" name="ybypbm" style="width: 124px">
                            </td>
                            <td class="padding-left10 fs_16">
                                医院药品名称
                            </td>
                            <td>
                                <input type="text" name="yyypmc" style="width: 124px">
                            </td>

                            <td class="td-width"></td>
                            <td>
                                <input type="button" value="查询" onclick="searchData()" class="btn-ok fs_16"/>
                                <input type="button" value="重置" onclick="cleanProjectCatalogFun()"
                                       class="btn-cancel fs_16"/>
                            </td>
                            <td style="width: auto"></td>
                        </tr>

                        </tbody>
                    </table>
                </form>
            </div>
            <table id="btn-table" class="float margin-top10" style="width: 100%;">
                <tr>
                    <td>
                        <button class="btn-ok fs_16" id="increase" onclick="openWin('新增','${path}/catalog/add')">新增</button>
                        <button class="btn-ok fs_16" onclick="explain()">解析</button>
                        <button class="btn-ok fs_16" onclick="explainAll()">全部解析</button>
                        <button class="btn-ok fs_16" onclick="setJxzt()">手动解析</button>
                        <%--<button class="btn-ok fs_16" onclick="editWin('${path}/catalog/ypml/edit')">修改</button>
                        <button class="btn-ok fs_16" id="delDrugCatalog">删除</button>--%>
                    </td>

                    <td class="right" style="width: 72px">
                        <button class="btn-ok fs_16" id="importExcel">导入</button>
                    </td>
                    <td class="right" style="width: 80px">
                        <button class="btn-ok fs_16" onclick="exportTemp()">导出模版</button>
                    </td>
                    <td class="right" style="width: 80px">
                        <button class="btn-ok fs_16" onclick="exportData()">导出全部</button>
                    </td>
                    <td class="right" style="width: 80px">
                        <button class="btn-ok fs_14" onclick="exportWjxData()">导出未解析</button>
                    </td>
                    <%--<td class="right" style="width: 96px">
                        <button class="btn-ok fs_16" style="width: 91px;" onclick="exportDrugCatalogHistory()">查看历史记录
                        </button>
                    </td>--%>
                </tr>
            </table>
            <div class="BgDiv3"></div>
            <div class="body-width clear">
                <div style="height: 660px">
                    <table id="dataGrid" class="on-style">

                    </table>
                </div>
                <div class="DialogDiv3" style="display: none">
                    <div class="pop-up bg-color1">
                        <span class="float pop-up-span width-color dis-block">药品目录管理<span id="optName"></span></span>
                    </div>

                    <!--table数据-->
                    <div class="body-width border-val"
                         style="height: 622px;margin-top: 20px !important;width: 90%;margin-left: 5%;">
                        <form id="editForm" method="post" action="${path}/catalog/ypml/add">
                            <input type="hidden" id="id" name="id"/>
                            <table class="bz-zd-table ">
                                <tbody>

                                <tr>
                                    <td colspan="6" style="text-align: right;color:#DF2828">
                                        注：* 为必须项
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <span>*</span>医保药品编码
                                    </td>
                                    <td>
                                        <input type="text" id="ybypbm" name="ybypbm" validateOnCreate="false"
                                               class="easyui-validatebox"
                                               onkeyup="this.value=this.value.replace(/[^\u0000-\u00FF]/g,'')"
                                               validType="length[1,20]" data-options="required:true"/>
                                    </td>
                                    <td>
                                        <span>*</span>医院药品编码
                                    </td>
                                    <td>
                                        <input type="text" id="yyypbm" name="yyypbm"
                                               onkeyup="this.value=this.value.replace(/[^\u0000-\u00FF]/g,'')"
                                               validateOnCreate="false" class="easyui-validatebox"
                                               validType="length[1,20]" data-options="required:true"/>
                                    </td>
                                    <td>
                                        <span>*</span>医院药品名称
                                    </td>
                                    <td>
                                        <input type="text" id="yyypmc" name="yyypmc" validateOnCreate="false"
                                               class="easyui-validatebox" validType="length[1,30]"
                                               data-options="required:true"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <span>*</span>剂型
                                    </td>
                                    <td>
                                        <input type="text" id="jx" name="jx" validateOnCreate="false"
                                               class="easyui-validatebox" validType="length[1,20]"
                                               data-options="required:true"/>
                                    </td>
                                    <td>
                                        <span>*</span>规格
                                    </td>
                                    <td>
                                        <input type="text" id="gg" name="gg" validateOnCreate="false"
                                               class="easyui-validatebox" validType="length[1,10]"
                                               data-options="required:true"/>
                                    </td>
                                    <td>
                                        <span>*</span>单位
                                    </td>
                                    <td>
                                        <input type="text" id="dw" name="dw" validateOnCreate="false"
                                               class="easyui-validatebox" validType="length[1,5]"
                                               data-options="required:true"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <span>*</span>价格
                                    </td>
                                    <td>
                                        <input type="text" id="jg" name="jg" validateOnCreate="false"
                                               onkeyup="this.value=this.value.replace(/[^0-9.]/g,'')"
                                               class="easyui-validatebox" data-options="required:true"/>
                                    </td>
                                    <td>
                                        <span>*</span>最小包装
                                    </td>
                                    <td>
                                        <input type="text" id="zxbz" name="zxbz" validateOnCreate="false"
                                               class="easyui-validatebox" validType="length[1,10]"
                                               data-options="required:true"/>
                                    </td>
                                    <td>
                                        <span>*</span>生产厂商
                                    </td>
                                    <td>
                                        <input type="text" id="sccs" name="sccs" validateOnCreate="false"
                                               class="easyui-validatebox" validType="length[1,50]"
                                               data-options="required:true"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <span>*</span>自付比例
                                    </td>
                                    <td>
                                        <input type="text" id="zfbl" name="zfbl" validateOnCreate="false"
                                               onkeyup="this.value=this.value.replace(/[^0-9.]/g,'')"
                                               class="easyui-validatebox" data-options="required:true"/>
                                    </td>
                                    <td>
                                        给药途径
                                    </td>
                                    <td>
                                        <input type="text" id="gytj" name="gytj" validateOnCreate="false"
                                               class="easyui-validatebox" validType="length[1,10]"
                                               data-options="required:true"/>
                                    </td>
                                    <td>
                                        <span>*</span>药品类别
                                    </td>
                                    <td>
                                        <select id="yplx" name="yplx">
                                            <option value="1">西药</option>
                                            <option value="2">中成药</option>
                                            <option value="3">中药饮片</option>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td>说明</td>
                                </tr>
                                <tr>
                                    <td></td>
                                    <td colspan="5">
                                        <textarea id="sm" name="sm" cols="5" rows="10" style="height: 80px"
                                                  validateOnCreate="false" class="easyui-validatebox"
                                                  validType="length[1,100]"></textarea>
                                    </td>
                                </tr>

                                <tr>
                                    <td colspan="6" class="center-td" style="padding-top: 24px;">
                                        <input class="btn-ok" type="submit" value="确定"
                                               style="width: 76px;padding-left:5px;"/>
                                        <input class="back btn-cancel" onclick="closeWin()" value="取消"
                                               style="width: 76px;padding-left:26px;cursor: pointer;"/>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </form>
                    </div>

                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>

package com.shuxin.model.vo;
import java.util.List;

import com.shuxin.model.RuleColumnInfo;
import com.shuxin.model.RuleTableInfo;

public class RuleTableVo{

	private RuleTableInfo table;
	private List<RuleColumnInfo> cols;
	public RuleTableInfo getTable() {
		return table;
	}
	public void setTable(RuleTableInfo table) {
		this.table = table;
	}
	public List<RuleColumnInfo> getCols() {
		return cols;
	}
	public void setCols(List<RuleColumnInfo> cols) {
		this.cols = cols;
	}
}

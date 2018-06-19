package com.shuxin.mapper.ruleengine;

import java.math.BigDecimal;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.shuxin.model.CommonModel;

/**
 * 单次处方药品种类异常
 *
 */
public interface RuleDCCFYPZLYCMapper  extends BaseMapper<CommonModel>{

	public Map<String, BigDecimal> selectRuleDCCFYPZLYC();
}

package com.shuxin.mapper.catalog;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.shuxin.model.catalog.YPML;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zengj on 2018-6-7.
 */
@Component
@Mapper
public interface YPMLMapper extends BaseMapper<YPML>{
    public int  selectExist(YPML ypml);

    public List<Map<String, Object>> selectPage(Pagination page, Map<String, Object> params);

    public Set<YPML> selectAllSet();

}

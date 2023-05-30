package com.example.baicizhanparse.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.baicizhanparse.entity.Dict;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Dict)表数据库访问层
 *
 * @author xuanchengwei
 * @since 2023-05-30 14:00:25
 */
@Mapper
public interface DictDao extends BaseMapper<Dict> {


}


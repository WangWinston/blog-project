package com.blog.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.infrastructure.persistence.po.CategoryPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * Category Mapper
 */
@Mapper
public interface CategoryMapper extends BaseMapper<CategoryPO> {
}
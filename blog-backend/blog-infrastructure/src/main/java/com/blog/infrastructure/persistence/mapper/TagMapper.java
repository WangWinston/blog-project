package com.blog.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.infrastructure.persistence.po.TagPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * Tag Mapper
 */
@Mapper
public interface TagMapper extends BaseMapper<TagPO> {
}
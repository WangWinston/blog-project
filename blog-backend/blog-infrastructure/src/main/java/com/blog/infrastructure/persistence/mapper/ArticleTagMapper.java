package com.blog.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.infrastructure.persistence.po.ArticleTagPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * Article Tag Mapper
 */
@Mapper
public interface ArticleTagMapper extends BaseMapper<ArticleTagPO> {
}
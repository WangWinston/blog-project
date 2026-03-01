package com.blog.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.infrastructure.persistence.po.ArticlePO;
import org.apache.ibatis.annotations.Mapper;

/**
 * Article Mapper
 */
@Mapper
public interface ArticleMapper extends BaseMapper<ArticlePO> {
}
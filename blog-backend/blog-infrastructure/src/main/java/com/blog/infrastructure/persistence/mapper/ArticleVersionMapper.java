package com.blog.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.infrastructure.persistence.po.ArticleVersionPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * Article Version Mapper
 */
@Mapper
public interface ArticleVersionMapper extends BaseMapper<ArticleVersionPO> {
}
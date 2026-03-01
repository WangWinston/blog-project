package com.blog.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.infrastructure.persistence.po.ScheduledArticlePO;
import org.apache.ibatis.annotations.Mapper;

/**
 * Scheduled Article Mapper
 */
@Mapper
public interface ScheduledArticleMapper extends BaseMapper<ScheduledArticlePO> {
}
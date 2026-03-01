package com.blog.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.infrastructure.persistence.po.CommentPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * Comment Mapper
 */
@Mapper
public interface CommentMapper extends BaseMapper<CommentPO> {
}
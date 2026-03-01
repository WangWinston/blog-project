package com.blog.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.infrastructure.persistence.po.UserLikePO;
import org.apache.ibatis.annotations.Mapper;

/**
 * User Like Mapper
 */
@Mapper
public interface UserLikeMapper extends BaseMapper<UserLikePO> {
}
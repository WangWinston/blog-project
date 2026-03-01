package com.blog.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.infrastructure.persistence.po.UserFavoritePO;
import org.apache.ibatis.annotations.Mapper;

/**
 * User Favorite Mapper
 */
@Mapper
public interface UserFavoriteMapper extends BaseMapper<UserFavoritePO> {
}
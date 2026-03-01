package com.blog.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.infrastructure.persistence.po.ProfilePO;
import org.apache.ibatis.annotations.Mapper;

/**
 * Profile Mapper
 */
@Mapper
public interface ProfileMapper extends BaseMapper<ProfilePO> {
}
package com.elysia.blogspringboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.elysia.blogspringboot.domain.entity.UserProfile;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserProfileMapper extends BaseMapper<UserProfile> {
}

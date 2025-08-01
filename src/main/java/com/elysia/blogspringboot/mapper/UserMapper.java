package com.elysia.blogspringboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.elysia.blogspringboot.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}

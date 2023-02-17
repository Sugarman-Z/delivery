package com.zgy.delivery.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zgy.delivery.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}

package com.zgy.delivery.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgy.delivery.entity.User;
import com.zgy.delivery.mapper.UserMapper;
import com.zgy.delivery.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}

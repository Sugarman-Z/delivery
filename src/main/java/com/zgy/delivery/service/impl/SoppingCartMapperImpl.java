package com.zgy.delivery.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgy.delivery.entity.ShoppingCart;
import com.zgy.delivery.mapper.ShoppingCartMapper;
import com.zgy.delivery.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class SoppingCartMapperImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}

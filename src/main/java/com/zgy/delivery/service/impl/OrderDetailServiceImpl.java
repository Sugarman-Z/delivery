package com.zgy.delivery.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgy.delivery.entity.OrderDetail;
import com.zgy.delivery.mapper.OrderDetailMapper;
import com.zgy.delivery.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}

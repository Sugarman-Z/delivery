package com.zgy.delivery.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgy.delivery.entity.DishFlavor;
import com.zgy.delivery.mapper.DishFlavorMapper;
import com.zgy.delivery.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}

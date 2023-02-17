package com.zgy.delivery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgy.delivery.common.CustomException;
import com.zgy.delivery.dto.DishDto;
import com.zgy.delivery.entity.Dish;
import com.zgy.delivery.entity.DishFlavor;
import com.zgy.delivery.mapper.DishMapper;
import com.zgy.delivery.service.DishFlavorService;
import com.zgy.delivery.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 新增菜品，同时插入菜品对应的口味数据
     * @param dishDto
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        // 保存菜品的基本信息到菜品表dish
        this.save(dishDto);
        // 获取菜品id
        Long dishId = dishDto.getId();
        // 菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map(item -> {
            item.setDishId((dishId));
            return item;
        }).collect(Collectors.toList());
        // 保存菜品口味数据到菜品口味表dish_flavor
        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public DishDto getByIdWithFlavor(Long id) {
        // 查询菜品基本信息，从dish表查询
        Dish dish = this.getById(id);

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);

        // 查询当前菜品对应的口味信息，从dish_flavor表查询
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, id);
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavors);
        return dishDto;
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        // 更新dish表基本信息
        this.updateById(dishDto);

        // 清理当前菜品对应口味数据 -- dish_flavor的delete操作
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        // 添加当前提交过来的口味数据 -- dish_flavor的insert操作
        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors = flavors.stream().map(item -> {
            item.setDishId((dishDto.getId()));
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }

    @Override
    @Transactional
    public void deleteWithFlavor(List<Long> ids) {
        // 查询售卖菜品的状态
        LambdaQueryWrapper<Dish>  dishQueryWrapper = new LambdaQueryWrapper<>();
        dishQueryWrapper.in(Dish::getId, ids).eq(Dish::getStatus, 1);
        int count = this.count(dishQueryWrapper);
        // 如果为启售状态则不能删除，抛出业务异常
        if (count > 0) {
            throw new CustomException("菜品正在售卖中，不能删除");
        }
        // 删除dish_flavor表中对应的口味信息
        LambdaQueryWrapper<DishFlavor> dishFlavorqueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorqueryWrapper.in(DishFlavor::getDishId, ids);
        dishFlavorService.remove(dishFlavorqueryWrapper);
        // 删除dish表中的菜品信息
        this.removeByIds(ids);
    }
}

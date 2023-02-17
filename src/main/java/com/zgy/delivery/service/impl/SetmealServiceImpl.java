package com.zgy.delivery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgy.delivery.common.CustomException;
import com.zgy.delivery.dto.DishDto;
import com.zgy.delivery.dto.SetmealDto;
import com.zgy.delivery.entity.Dish;
import com.zgy.delivery.entity.DishFlavor;
import com.zgy.delivery.entity.Setmeal;
import com.zgy.delivery.entity.SetmealDish;
import com.zgy.delivery.mapper.SetmealMapper;
import com.zgy.delivery.service.SetmealDishService;
import com.zgy.delivery.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 新增套餐，同时保存套餐与菜品的关联关系
     * @param setmealDto
     */
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        // 保存套餐的基本信息，操作setmeal表，执行insert操作
        this.save(setmealDto);

        // 保存套餐与菜品的关联信息，操作setmeal_dish，执行insert操作
        Long id = setmealDto.getId();
        // 菜品信息
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes = setmealDishes.stream().map(item -> {
            item.setSetmealId(id);
            return item;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * 根据id查询套餐和对应菜品信息
     * @param id
     */
    public SetmealDto getByIdWithDish(Long id) {
        // 获取套餐信息
        Setmeal setmeal = this.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal, setmealDto);
        // 获取菜品信息
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> setmealDishes = setmealDishService.list(queryWrapper);
        setmealDto.setSetmealDishes(setmealDishes);
        return setmealDto;
    }

    /**
     * 修改套餐以及对应菜品信息
     * @param setmealDto
     */
    @Transactional
    public void updateWithDish(SetmealDto setmealDto) {
        // 更新setmeal表基本信息
        this.updateById(setmealDto);

        // 清理当前套餐对应口菜品数据 -- setmeal_dish的delete操作
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(SetmealDish::getSetmealId, setmealDto.getId());
        setmealDishService.remove(queryWrapper);
        // 添加当前提交过来的口味数据 -- setmeal_dish的insert操作
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes = setmealDishes.stream().map(item -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * 删除套餐，同时删除套餐和菜品的关联关系
     * @param ids
     */
    @Transactional
    public void removeWithDish(List<Long> ids) {
        // 查询套餐状态，确定是否可以删除
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.in(Setmeal::getId, ids)
                .eq(Setmeal::getStatus, 1);
        int count = this.count(setmealLambdaQueryWrapper);
        // 如果不能删除，抛出一个业务异常
        if (count > 0) {
            throw new CustomException("套餐正在售卖中，不能删除");
        }
        // 如果可以删除，先删除关联表中的数据
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.in(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(setmealDishLambdaQueryWrapper);
        // 删除套餐表中的数据
        this.removeByIds(ids);
    }
}

package com.zgy.delivery.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zgy.delivery.dto.SetmealDto;
import com.zgy.delivery.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    /**
     * 新增套餐，同时保存套餐与菜品的关联关系
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);

    /**
     * 根据id查询套餐和对应菜品信息
     * @param id
     */
    public SetmealDto getByIdWithDish(Long id);

    /**
     * 删除套餐，同时删除套餐和菜品的关联关系
     * @param ids
     */
    public void removeWithDish(List<Long> ids);

    /**
     * 修改套餐信息
     * @param setmealDto
     */
    public void updateWithDish(SetmealDto setmealDto);
}

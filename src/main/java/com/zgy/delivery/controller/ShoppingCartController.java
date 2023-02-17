package com.zgy.delivery.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zgy.delivery.common.BaseContext;
import com.zgy.delivery.common.R;
import com.zgy.delivery.entity.ShoppingCart;
import com.zgy.delivery.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 向购物车中增加菜品或者套餐
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        log.info("购物车数据：{}", shoppingCart.toString());

        // 设置用户id，指定当前是哪个用户的购物车数据
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        // 查询当前菜品或者套餐是否在购物车中
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        if (shoppingCart.getDishId() != null) {
            queryWrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
        } else {
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);

        if (cartServiceOne != null) {
            // 如果已经存在，就在原来数量基础上加一
            Integer number = cartServiceOne.getNumber();
            cartServiceOne.setNumber(number + 1);
            shoppingCartService.updateById(cartServiceOne);
        } else {
            // 如果不存在，则添加到购物车，数量默认就是一
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            cartServiceOne = shoppingCart;
        }

        return R.success(cartServiceOne);
    }

    @PostMapping("/sub")
    public R<Object> sub(@RequestBody ShoppingCart shoppingCart) {
        if (shoppingCart.getDishId() == null && shoppingCart.getSetmealId() == null) {
            return R.error("未传递修改菜品或套餐");
        }
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        if (shoppingCart.getDishId() != null) {
            queryWrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
        } else if (shoppingCart.getSetmealId() != null) {
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);
        if (cartServiceOne.getNumber() == 1) {
            shoppingCartService.remove(queryWrapper);
            return R.success("已从购物车中删除该项");
        } else {
            Integer number = cartServiceOne.getNumber();
            cartServiceOne.setNumber(number - 1);
            shoppingCartService.updateById(cartServiceOne);
            return R.success(cartServiceOne);
        }
    }

    /**
     * 查看购物车
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list() {
        log.info("查看购物车...");
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list);
    }


    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> clean() {

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        shoppingCartService.remove(queryWrapper);

        return R.success("清空购物车成功");
    }
}

package com.zgy.delivery.controller;

import com.zgy.delivery.common.R;
import com.zgy.delivery.entity.Orders;
import com.zgy.delivery.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
        log.info("准备下单:{}", orders.toString());
        orderService.submit(orders);
        return R.success("下单成功");
    }

}

package com.sakura.xdvideo.controller;

import com.sakura.xdvideo.domain.JsonData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单接口
 *
 * @author sakura
 */
@RestController
@RequestMapping("/user/api/v1/order")
public class OrderController {


    @GetMapping("add")
    public JsonData saveOrder() {
        return JsonData.buildSuccess("下单成功");
    }


}

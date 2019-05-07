package com.example.demo.controller;

import com.example.demo.domain.MobileUser;
import com.example.demo.domain.OrderInfo;
import com.example.demo.result.CodeMsg;
import com.example.demo.result.Result;
import com.example.demo.service.GoodsService;
import com.example.demo.service.OrderInfoService;
import com.example.demo.vo.GoodsVo;
import com.example.demo.vo.OrderInfoVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/order")
public class OrderController {

    private Logger log=LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private GoodsService goodsService;

    @RequestMapping("/detail")
    @ResponseBody
    public Result<OrderInfoVo> getOrderById(MobileUser mobileUser, @RequestParam("orderId") long orderId){
        log.info("**************"+orderId);
        if(mobileUser==null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        OrderInfo order = orderInfoService.getOrderById(orderId);
        if(order==null){
            return Result.error(CodeMsg.ORDER_NOTEXIST);
        }
        Long goodsId = order.getGoodsId();
        GoodsVo goods = goodsService.getGoodsById(goodsId);
        OrderInfoVo orderInfoVo = new OrderInfoVo();
        orderInfoVo.setOrderInfo(order);
        orderInfoVo.setGoodsVo(goods);
        return Result.success(orderInfoVo);
    }
}

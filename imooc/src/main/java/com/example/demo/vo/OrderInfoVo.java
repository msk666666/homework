package com.example.demo.vo;

import com.example.demo.domain.OrderInfo;
import lombok.Data;

@Data
public class OrderInfoVo{
    private GoodsVo goodsVo;
    private OrderInfo orderInfo;
}

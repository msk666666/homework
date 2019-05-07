package com.example.demo.vo;

import com.example.demo.domain.MobileUser;
import lombok.Data;

@Data
public class GoodsDetailVo {
    private MobileUser mobileUser;
    private GoodsVo goodsVo;
    private int miaoshaStatus=0;
    private int remainSeconds=0;
}

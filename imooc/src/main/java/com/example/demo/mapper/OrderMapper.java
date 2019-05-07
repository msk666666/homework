package com.example.demo.mapper;

import com.example.demo.domain.MiaoshaOrder;
import com.example.demo.domain.MobileUser;
import com.example.demo.domain.OrderInfo;
import com.example.demo.vo.GoodsVo;
import com.example.demo.vo.OrderInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderMapper {

    MiaoshaOrder getOrderInfoByUserIdAndGoodsId(@Param("userId") long userId, @Param("goodsId") long goodsId);

    Long insertOrder(OrderInfo orderInfo);

    void insertMiaoShaOrder(MiaoshaOrder miaoshaOrder);

    OrderInfo getOrderById(long orderId);

    void deleteOrder();
}

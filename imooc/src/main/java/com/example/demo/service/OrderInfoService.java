package com.example.demo.service;

import com.example.demo.domain.MiaoshaOrder;
import com.example.demo.domain.MobileUser;
import com.example.demo.domain.OrderInfo;
import com.example.demo.mapper.MiaoShaMapper;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.redis.GoodsKey;
import com.example.demo.redis.MiaoshaKey;
import com.example.demo.redis.OrderKey;
import com.example.demo.redis.RedisService;
import com.example.demo.vo.GoodsVo;
import com.example.demo.vo.OrderInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.Date;

@Service
public class OrderInfoService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private MiaoShaMapper miaoShaMapper;

    @Autowired
    RedisService redisService;

    /**
     * 根据商品id和用户id查询该用户是否已经秒杀成功
     * @param userId
     * @param goodsId
     * @return
     */
//    public MiaoshaOrder getOrderInfoByUserIdAndGoodsId(long userId, long goodsId){
//        return orderMapper.getOrderInfoByUserIdAndGoodsId(userId,goodsId);
//    }

    public MiaoshaOrder getOrderInfoByUserIdAndGoodsId(long userId, long goodsId){
        return redisService.get(OrderKey.getOrderByGoodsIdAndUserId,userId+""+goodsId,MiaoshaOrder.class);
    }

    /**
     * 创建订单
     * @param mobileUser
     * @param goodsVo
     */
    @Transactional
    public OrderInfo createOrder(MobileUser mobileUser, GoodsVo goodsVo){
        //创建订单
        OrderInfo order = new OrderInfo();
        order.setUserId(mobileUser.getId());
        order.setGoodsId(goodsVo.getId());
        order.setDeliveryAddrId(1L);
        order.setGoodsName(goodsVo.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(goodsVo.getMiaoshaPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(new Date());
        orderMapper.insertOrder(order);
        //创建秒杀订单
        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setUserId(mobileUser.getId());
        miaoshaOrder.setGoodsId(goodsVo.getId());
        miaoshaOrder.setOrderId(order.getId());
        orderMapper.insertMiaoShaOrder(miaoshaOrder);
        redisService.set(OrderKey.getOrderByGoodsIdAndUserId,miaoshaOrder.getUserId()+""+miaoshaOrder.getGoodsId(),miaoshaOrder);
        return order;
    }

    public OrderInfo getOrderById(long id){
        return orderMapper.getOrderById(id);
    }

    public void reset(){
        orderMapper.deleteOrder();
        miaoShaMapper.deleteMiaoshaOrder();
    }
}

package com.example.demo.redis;

public class OrderKey extends KeyPrefixImpl{
    public OrderKey( String prefix) {
        super( prefix);
    }

    public static OrderKey getOrderByGoodsIdAndUserId=new OrderKey("ogu");

}

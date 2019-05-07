package com.example.demo.redis;

public class GoodsKey extends KeyPrefixImpl{
    public GoodsKey(int expire, String prefix) {
        super(expire, prefix);
    }
    public static GoodsKey getGoodsList=new GoodsKey(60,"goodsList");
    public static GoodsKey getGoodsDetail=new GoodsKey(60,"goods_detail");
    public static GoodsKey getGoodsStock=new GoodsKey(0,"goods_stock");
}

package com.example.demo.redis;

public class MiaoshaKey extends KeyPrefixImpl{
    public MiaoshaKey(String prefix) {
        super(prefix);
    }

    public MiaoshaKey(int expireTime,String prefix){
        super(expireTime,prefix);
    }
    public static MiaoshaKey isgoodsOver=new MiaoshaKey("isOver");
    public static MiaoshaKey miaoshaPath=new MiaoshaKey(60,"miaoshaPath");
    public static MiaoshaKey miaoshaVerifyCode=new MiaoshaKey(60,"verifyCode");

}

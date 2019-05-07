package com.example.demo.redis;

public class MiaoShaUserKey extends KeyPrefixImpl{
    //设置redis中cookie过期时间
    public static final int TOKEN_EXPIRE=3600*24*2;
    private MiaoShaUserKey(int expireSeconds,String prefix){
        super(expireSeconds,prefix);
    }
    public static MiaoShaUserKey token=new MiaoShaUserKey(TOKEN_EXPIRE,"tk");
}

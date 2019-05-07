package com.example.demo.redis;

public class AccessKey extends KeyPrefixImpl{

    public AccessKey(String prefix) {
        super(prefix);
    }

    public AccessKey(int expire, String prefix) {
        super(expire, prefix);
    }
    public static AccessKey AccessCount(int expireTime){
        return new AccessKey(expireTime,"access");
    };
}

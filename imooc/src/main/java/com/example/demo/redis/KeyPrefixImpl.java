package com.example.demo.redis;

public class KeyPrefixImpl implements KeyPrefix {
    private int expireTime;
    private String prefix;

    public KeyPrefixImpl(String prefix){
        this(0,prefix);
    }

    public KeyPrefixImpl(int expire, String prefix) {
        this.expireTime=expire;
        this.prefix=prefix;
    }

    @Override
    public int getExpireTime() {
        return expireTime;
    }

    @Override
    public String getPrefix() {
        String simpleName = getClass().getSimpleName();
    return simpleName+":"+prefix;
    }
}

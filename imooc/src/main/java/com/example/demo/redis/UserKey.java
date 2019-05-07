package com.example.demo.redis;

public class UserKey extends KeyPrefixImpl{
    public UserKey(String prefix) {
        super(prefix);
    }

    public static UserKey getById(){
        return new UserKey("Id");
    }
    public static UserKey getByName(){
        return new UserKey("Name");
    }

}

package com.example.demo.redis;

import com.alibaba.fastjson.JSON;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class RedisService {

    @Autowired
    private  JedisPool jedisPool;

    public <T> T get(KeyPrefix prefix,String key,Class<T> clazz){
        Jedis jedis=null;
        try{
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey=prefix.getPrefix()+key;
            String str = jedis.get(realKey);
           T t= stringToBean(str,clazz);
           return t;
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    public <T> boolean set(KeyPrefix prefix,String key,T value){
        Jedis jedis=null;
        try {
            jedis = jedisPool.getResource();
            String str= beanToString(value);
            String realKey=prefix.getPrefix()+key;
            if(prefix.getExpireTime()<=0){ //永不过期
                jedis.set(realKey, str);
            }else {
                jedis.setex(realKey, prefix.getExpireTime(), str);
            }
            return true;
        } finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }
    public boolean deleteUser(KeyPrefix prefix,String key){
        if(prefix==null||key==null){
            return false;
        }
        Jedis jedis=null;
        try {
            jedis = jedisPool.getResource();
            jedis.del(prefix.getPrefix()+key);
            return true;
        }catch (final Exception e){
            e.printStackTrace();
            return false;
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    public boolean delete(KeyPrefix prefix){
        if(prefix==null){
            return false;
        }
        List<String> keys = scanKeys(prefix.getPrefix());
        if(keys==null||keys.size()<=0){
            return true;
        }

        Jedis jedis=null;
        try {
            jedis = jedisPool.getResource();
            jedis.del(keys.toArray(new String[0]));
            return true;
        }catch (final Exception e){
            e.printStackTrace();
            return false;
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    public List<String> scanKeys(String key){
        Jedis jedis=null;
        try {
            jedis=jedisPool.getResource();
            ArrayList<String> keys = new ArrayList<>();
            //创建scan的参数对象
            ScanParams sp = new ScanParams();
            String cursor = "0";
            sp.match("*" + key + "*");
            sp.count(100);
            do {
                ScanResult<String> scan = jedis.scan(cursor, sp);
                List<String> result = scan.getResult();
                if (result != null && result.size() > 0) {
                    keys.addAll(result);
                }
                //再处理cursor
                cursor = scan.getStringCursor();
            } while (!cursor.equals("0"));
            return keys;
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    //判断key是否存在
    public  Boolean exist(KeyPrefix prefix,String key){
        Jedis jedis=null;
        try {
            jedis = jedisPool.getResource();
            String realKey=prefix.getPrefix()+key;
            Boolean exists = jedis.exists(realKey);
            return exists;
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }
    //给key加1
    public  <T> Long incr(KeyPrefix prefix,String key){
        Jedis jedis=null;
        try {
            jedis = jedisPool.getResource();
            String realKey=prefix.getPrefix()+key;
            Long incr = jedis.incr(realKey);
            return incr;
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }
    //给key减一
    public   <T> Long decr(KeyPrefix prefix,String key){
        Jedis jedis=null;
        try {
            jedis = jedisPool.getResource();
            String realKey=prefix.getPrefix()+key;
            Long incr = jedis.decr(realKey);
            return incr;
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    public static  <T> String beanToString(T value) {
        if(value==null){
            return null;
        }
        Class<?> clazz = value.getClass();
        if(clazz==int.class||clazz==Integer.class){
            return ""+value;
        }else if(clazz==long.class||clazz==Long.class){
            return ""+value;
        }else if(clazz==String.class){
            return (String) value;
        }else {
            return JSON.toJSONString(value);
        }
    }



    public static  <T> T stringToBean(String str,Class<T> clazz) {
        if(str==null||str.length()<=0){
            return null;
        }

        if(clazz==int.class||clazz==Integer.class){
            return (T)Integer.valueOf(str);
        }else if(clazz==long.class||clazz==Long.class){
            return (T)Long.valueOf(str);
        }else if(clazz==String.class){
            return (T) str;
        }else {
            return JSON.toJavaObject(JSON.parseObject(str),clazz);
        }

    }

    }


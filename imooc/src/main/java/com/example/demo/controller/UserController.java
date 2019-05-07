package com.example.demo.controller;

import com.example.demo.domain.MobileUser;
import com.example.demo.redis.MiaoShaUserKey;
import com.example.demo.redis.MiaoshaKey;
import com.example.demo.redis.RedisService;
import com.example.demo.result.CodeMsg;
import com.example.demo.result.Result;
import com.example.demo.service.MobileUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    MobileUserService mobileUserService;
    @Autowired
    RedisService redisService;

    @RequestMapping("/userAddOrUpdate")
    @ResponseBody
    public Result<Integer> addOrUpdateUser(String phone,String passWord){
        MobileUser user = mobileUserService.getUserByPhone(phone);
        if (user!=null){
            return Result.error(CodeMsg.User_HASEXIST);
        }
        mobileUserService.addUser(phone,passWord);
        return Result.success(0);
    }



}
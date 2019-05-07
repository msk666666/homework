package com.example.demo.controller;

import com.example.demo.domain.User;
import com.example.demo.redis.RedisService;
import com.example.demo.redis.UserKey;
import com.example.demo.result.CodeMsg;
import com.example.demo.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.beans.Transient;

@Controller
@RequestMapping("/demo")
public class HelloController {


    @Autowired
    private RedisService redisService;

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @RequestMapping("/hello")
    @ResponseBody
    public Result<String> hello(){
        return Result.success("hello imooc");
    }

    @RequestMapping("/helloError")
    @ResponseBody
    public Result<String> helloError(){
        return Result.error(CodeMsg.SERVER_ERROR);
    }

    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model){
        model.addAttribute("name","zhangsan");
        return "hello";
    }




    @RequestMapping("/getredis")
    @ResponseBody
    public Result<User> getRedis(){
        User user = redisService.get(UserKey.getById(),""+3, User.class);
        return Result.success(user);
    }
    @RequestMapping("/setredis")
    @ResponseBody
    public Result<Boolean> setRedis(int id,String name){
        User user = new User(id,name);
        redisService.set(UserKey.getById(),""+user.getId(), user);
        return Result.success(true);
    }

    @RequestMapping("/exist")
    @ResponseBody
    public Result<Boolean> exist(String key){
        Boolean exist = redisService.exist(UserKey.getById(), key);
        return Result.success(exist);

    }

}

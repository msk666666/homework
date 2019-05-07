package com.example.demo.controller;

import com.example.demo.domain.MobileUser;
import com.example.demo.redis.MiaoShaUserKey;
import com.example.demo.redis.RedisService;
import com.example.demo.result.CodeMsg;
import com.example.demo.result.Result;
import com.example.demo.service.MobileUserService;
import com.example.demo.util.MD5Util;
import com.example.demo.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/login")
public class LoginController {
    private static Logger log = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private MobileUserService mobileUserService;
    @Autowired
    RedisService redisService;


    @RequestMapping("/to_login")
    public String toLogin(){
        return "login";
    }

    @RequestMapping("/do_login")
    @ResponseBody
    public Result<Boolean> doLogin(HttpServletResponse response, @Valid LoginVo loginVo){
       mobileUserService.login(response,loginVo);
        return Result.success(true);
    }

    @RequestMapping(value = "/logout" ,method = RequestMethod.GET)
    @ResponseBody
    public Result<Boolean> logout(HttpServletRequest request){
        //获取请求中的token(cookie)对象
        String cookieToken=getCookieValue(request,mobileUserService.COOKI_NAME_TOKEN);
        boolean isDelete = redisService.deleteUser(MiaoShaUserKey.token, cookieToken);
        return Result.success(isDelete);
    }
    private String getCookieValue(HttpServletRequest request, String cookiNameToken) {
        Cookie[] cookies = request.getCookies();
        if(cookies!=null){
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals(cookiNameToken)){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}

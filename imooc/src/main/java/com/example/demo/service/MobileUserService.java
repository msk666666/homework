package com.example.demo.service;

import com.example.demo.domain.MobileUser;
import com.example.demo.exception.GlobalException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.redis.MiaoShaUserKey;
import com.example.demo.redis.RedisService;
import com.example.demo.result.CodeMsg;
import com.example.demo.result.Result;
import com.example.demo.util.MD5Util;
import com.example.demo.util.UUIDUtil;
import com.example.demo.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Service
public class MobileUserService {
    public static final String COOKI_NAME_TOKEN = "token";

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    MobileUserService mobileUserService;


    public MobileUser getUserByPhone(String nickname){
        return userMapper.getUserByPhone(nickname);
    }

    public boolean login(HttpServletResponse response,LoginVo loginVo){
        //判断loginVo是否存在
        if(loginVo==null){
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile=loginVo.getMobile();
        String password=loginVo.getPassword();
        MobileUser user = getUserByPhone(mobile);
        //判断用户是否存在
        if(user==null){
            throw  new GlobalException(CodeMsg.USER_NOTEXIST);
        }
        String dbPass = user.getPassword();
        String dbSalt = user.getSalt();

        //判断密码是否正确
        if(!MD5Util.formPassToDBPass(password,dbSalt).equals(dbPass)){
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        //重置最后一次登录日期
        user.setLastLoginDate(new Date());
        user.setLoginCount(user.getLoginCount()+1);
        mobileUserService.updateLoginInfo(user);

        //生成cookie  使用uuid作为JsessionID
        String token = UUIDUtil.uuid();
        addCookie(response, user, token);
        return true;
    }

    private void updateLoginInfo(MobileUser user) {
        userMapper.updateLoginInfo(user);
    }

    private void addCookie(HttpServletResponse response, MobileUser user, String token) {
        //将生成的sessionID对应user对象放入redis缓存中
        redisService.set(MiaoShaUserKey.token,token,user);
        Cookie cookie = new Cookie(COOKI_NAME_TOKEN, token);
        //设置最大寿命为redis中的寿命
        cookie.setMaxAge(MiaoShaUserKey.token.getExpireTime());
        //cookie的存放路径
        cookie.setPath("/");
        //将生成的sessionId响应返回给用户
        response.addCookie(cookie);
    }

    public MobileUser getByToken(HttpServletResponse response,String token) {
        if(token==null){
            return null;
        }
        MobileUser user = redisService.get(MiaoShaUserKey.token, token, MobileUser.class);
        if(user!=null){
            addCookie(response,user,token);
        }
        return user;
    }

    public void addUser(String phone, String passWord) {
        MobileUser mobileUser = new MobileUser();
        mobileUser.setNickname(phone);
        String passwordDB = MD5Util.formPassToDBPass(passWord,"1a2b3c4d");
        mobileUser.setPassword(passwordDB);
        mobileUser.setRegisterDate(new Date());
        userMapper.addUser(mobileUser);
    }

    public void logout(HttpServletRequest request) {
        String paramToken = request.getParameter(mobileUserService.COOKI_NAME_TOKEN);
    }
}

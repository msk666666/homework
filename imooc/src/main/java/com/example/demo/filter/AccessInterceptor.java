package com.example.demo.filter;


import com.alibaba.fastjson.JSON;
import com.example.demo.domain.MobileUser;
import com.example.demo.redis.AccessKey;
import com.example.demo.redis.MiaoshaKey;
import com.example.demo.redis.RedisService;
import com.example.demo.result.CodeMsg;
import com.example.demo.result.Result;
import com.example.demo.service.MobileUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.thymeleaf.util.StringUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Service
public class AccessInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    MobileUserService mobileUserService;
    @Autowired
    RedisService redisService;
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(handler instanceof HandlerMethod){
            //获取用户
            MobileUser mobileUser=getUser(request,response);
            UserContext.setThreadLocal(mobileUser);

            HandlerMethod hm=(HandlerMethod) handler;
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            if(accessLimit==null) {
                return true;
            }
            int maxCount = accessLimit.maxCount();
            int seconds = accessLimit.seconds();
            boolean isLogin = accessLimit.needLogin();
            if(isLogin) {
                if (mobileUser == null) {
                    render(response, CodeMsg.SESSION_ERROR);
                    return false;
                }
            }
            String uri = request.getRequestURI();
            AccessKey accessKey = AccessKey.AccessCount(seconds);
            //查询访问次数
            Integer count = redisService.get(accessKey, mobileUser.getId() + "_" + uri, Integer.class);
            if(count==null){
                redisService.set(accessKey, mobileUser.getId() + "_" + uri,1);
            }else if(count<=maxCount){
                redisService.incr(accessKey, mobileUser.getId() + "_" + uri);
            }else{
                 render(response,CodeMsg.MiaoSha_AccessCount);
                 return false;
            }
        }
        return true;
    }

    private void render(HttpServletResponse response, CodeMsg sessionError) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        String string = JSON.toJSONString(Result.error(sessionError));
        out.write(string.getBytes("UTF-8"));
        out.flush();
        out.close();
    }

    private MobileUser getUser(HttpServletRequest request, HttpServletResponse response) {
        //获取请求中的token参数
        String paramToken = request.getParameter(mobileUserService.COOKI_NAME_TOKEN);
        //获取请求中的token(cookie)对象
        String cookieToken=getCookieValue(request,mobileUserService.COOKI_NAME_TOKEN);

        String token=StringUtils.isEmpty(paramToken) ?cookieToken:paramToken;
        MobileUser mobileUser = mobileUserService.getByToken(response, token);
        if(mobileUser==null){
            return null;
        }
        return mobileUser;
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

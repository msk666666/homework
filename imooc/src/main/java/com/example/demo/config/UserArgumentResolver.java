package com.example.demo.config;

import com.example.demo.domain.MobileUser;
import com.example.demo.filter.UserContext;
import com.example.demo.redis.RedisService;
import com.example.demo.service.MobileUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
//实现HandlerMethodArgumentResolver接口来自定义一个参数处理器
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private MobileUserService mobileUserService;

    @Autowired
    private RedisService redisService;

    //是否是符合要求的参数类型
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?> clazz = methodParameter.getParameterType();
        return clazz==MobileUser.class;
    }

    //处理参数
    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
      return UserContext.getThreadLocal();
    }


}

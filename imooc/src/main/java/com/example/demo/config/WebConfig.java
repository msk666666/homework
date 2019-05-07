package com.example.demo.config;

import com.example.demo.filter.AccessInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private UserArgumentResolver userArgumentResolver;

    @Autowired
    AccessInterceptor accessInterceptor;

    //将参数处理器添加到springMVC中，以后的每个请求都会先经过检验，redis中是否有
    //当前请求的对象，如果有则给请求中返回这个对象作为参数，否则返回null
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userArgumentResolver);
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accessInterceptor);
    }

}

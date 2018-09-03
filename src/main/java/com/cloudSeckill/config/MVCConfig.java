package com.cloudSeckill.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MVCConfig extends WebMvcConfigurerAdapter {

    //登录拦截器
    @Bean
    public RequestInterceptorConfig interceptor() {
        return new RequestInterceptorConfig();
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor());
    }
    
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/login").setViewName("index");
        registry.addViewController("/register").setViewName("register");
        registry.addViewController("/alterPass").setViewName("alterPass");
        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/search").setViewName("search");
        registry.addViewController("/member").setViewName("member");
        registry.addViewController("/settings").setViewName("settings");
    }
    
    @Bean
    public CharacterEncodingFilter getCharacterEncodingFilter() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        return filter;
    }
}

package com.employeeinfomanager.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")  // 允许所有路径
                        .allowedOrigins("*")  // 允许的前端地址
                        .allowedMethods("GET", "POST", "PUT", "DELETE")  // 允许的HTTP方法
                        .allowedHeaders("*");  // 允许的头部
            }
        };
    }
}

package org.xiaowu.behappy.screw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @author xiaowu
 * mvc handler
 */
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    /**
     * 排除路径
     */
    private static final List<String> EXCLUDE_PATH = Arrays.asList(
            "/",
            "/index.html",
            "/error",
            "/error/**",
            "/favicon.ico",
            "/user/login",
            "/user/register",
            "/user/register-enable",
            "/js/**", "/css/**",
            "/fonts/**","/img/**");

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor())
                .addPathPatterns("/**")  // 拦截所有请求，通过判断token是否合法来决定是否需要登录
                .excludePathPatterns(EXCLUDE_PATH);  // 放行一些资源
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        //user.dir: 当前项目的工作目录
        //访问url: /doc/xx/xx.html
        //必须以separator结尾
        String path = "file:" + System.getProperty("user.dir") + File.separator + "doc"+ File.separator;
        registry.addResourceHandler("/doc/**").
                addResourceLocations(path).
                resourceChain(false);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index.html");
    }

    @Bean
    public JwtInterceptor jwtInterceptor(){
        return new JwtInterceptor();
    }
}

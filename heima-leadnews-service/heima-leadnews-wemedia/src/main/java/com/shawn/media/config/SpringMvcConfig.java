package com.shawn.media.config;

import com.shawn.media.intercept.MediaIntercept;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author shawn
 * @date 2023年 01月 09日 15:43
 */
@Component
public class SpringMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MediaIntercept()).excludePathPatterns("/login/in");
    }
}

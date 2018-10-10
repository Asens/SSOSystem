package cn.asens.config;

import cn.asens.interceptor.SSOInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Asens
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(SSOInterceptor())
                .addPathPatterns("/test");
    }

    @Bean
    public SSOInterceptor SSOInterceptor() {
        return new SSOInterceptor();
    }
}

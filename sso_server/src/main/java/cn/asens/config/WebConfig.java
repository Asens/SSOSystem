package cn.asens.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * web配置
 *
 * @author fengshuonan
 * @Date 2018/8/29 下午3:32
 */
@ControllerAdvice
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private RestTemplateBuilder builder;

    @Bean
    @ConfigurationProperties(prefix = "sso")
    public SSOProperties SSOProperties() {
        return new SSOProperties();
    }

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new SsoServerInterceptor(SSOProperties())).addPathPatterns("/**").excludePathPatterns("/login", "/ssoApi/authToken", "/logout", "/static/**");
//    }


    @Bean
    public RestTemplate restTemplate() {
        return builder.build();
    }
}

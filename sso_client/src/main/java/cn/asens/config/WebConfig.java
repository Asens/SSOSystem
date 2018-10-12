package cn.asens.config;

import cn.asens.interceptor.SSOInterceptor;
import cn.asens.service.RemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author Asens
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Resource
    private RestTemplate restTemplate;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(SSOInterceptor())
                .addPathPatterns("/user");
    }

    /**
     * 配置sso
     */
    @Bean
    @ConfigurationProperties(prefix = "sso")
    public SSOProperties SSOProperties() {
        return new SSOProperties();
    }

    @Bean
    public RemoteService remoteService() {
        return new RemoteService(restTemplate, SSOProperties());
    }


    @Bean
    public SSOInterceptor SSOInterceptor() {
        return new SSOInterceptor();
    }
}

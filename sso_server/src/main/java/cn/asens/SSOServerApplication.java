package cn.asens;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author Asens
 */
@SpringBootApplication
@EnableEurekaClient
public class SSOServerApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(SSOServerApplication.class, args);
    }
}

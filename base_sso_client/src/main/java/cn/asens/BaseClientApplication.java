package cn.asens;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Asens
 */

@SpringBootApplication
@EnableFeignClients
public class BaseClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(BaseClientApplication.class, args);
    }
}

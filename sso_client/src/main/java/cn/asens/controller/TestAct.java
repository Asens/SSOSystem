package cn.asens.controller;

import cn.asens.entity.User;
import cn.asens.service.UserService;
import cn.asens.service.api.TokenApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author Asens
 */
@RestController
public class TestAct {
    @Resource
    private TokenApiService tokenApiService;
    @Resource
    private UserService userService;

    @RequestMapping("/")
    public Object main() {
        return tokenApiService.test();
    }

    @RequestMapping("/test")
    public Object test() {
        return "test";
    }
}

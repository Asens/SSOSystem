package cn.asens.controller;

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
    private RestTemplate restTemplate;

    @RequestMapping("/")
    public Object main() {
        ResponseEntity<String> result=
                restTemplate.getForEntity("http://sso_server/",String.class);
        return "index";
    }

    @RequestMapping("/test")
    public Object test() {
        return "test";
    }
}

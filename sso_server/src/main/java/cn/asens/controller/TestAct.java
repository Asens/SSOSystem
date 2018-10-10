package cn.asens.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Asens
 */

@RestController
public class TestAct {
    @RequestMapping("/")
    public Object main() {
        return "index";
    }
}

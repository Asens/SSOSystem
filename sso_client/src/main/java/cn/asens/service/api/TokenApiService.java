package cn.asens.service.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Asens
 */

@FeignClient(name = "SSOServer")
@Component
public interface TokenApiService {
    @RequestMapping(value = "/test",method = RequestMethod.GET)
    String test();

}

package cn.asens.controller;

import cn.asens.constants.SsoConstants;
import cn.asens.service.AuthService;
import cn.asens.service.UserService;
import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Asens
 */

@Controller
@Log4j2
public class SSOLoginAct {
    @Resource
    private AuthService authService;

    @RequestMapping("/sso")
    public String sso(ModelMap model, HttpServletRequest request) {
        model.put(SsoConstants.REDIRECT_PARAM_NAME,
                request.getParameter(SsoConstants.REDIRECT_PARAM_NAME));
        return "index";
    }

    @RequestMapping("/doLogin")
    @ResponseBody
    @SneakyThrows
    public Object doLogin(String username, String password, HttpServletRequest request) {
        log.info("username :{} -- password : {} ",username,password);
        String redirectUrl = request.getParameter(SsoConstants.REDIRECT_PARAM_NAME);
        Integer userId=authService.checkUserLogin(username,password);
        if(userId!=null){
            String token=authService.createToken(userId);
            JSONObject r=new JSONObject();
            r.put("status","success");
            r.put("returnUrl",redirectUrl + "?" +
                    SsoConstants.TOKEN_PARAM_NAME + "=" + token);
            return r;
        }
        JSONObject r=new JSONObject();
        r.put("status","fail");
        r.put("message","用户名或密码错误");
        return r;
    }


    @RequestMapping("/login/success")
    @ResponseBody
    public String success() {
        return "login success";
    }
}

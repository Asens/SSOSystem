package cn.asens.controller;

import cn.asens.constants.SsoConstants;
import cn.asens.service.AuthService;
import cn.asens.service.UserService;
import cn.asens.util.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static cn.asens.constants.SsoConstants.LOGOUT_URL;

/**
 * @author Asens
 */

@Controller
@Log4j2
public class SSOLoginAct {
    @Resource
    private AuthService authService;

    @GetMapping("/login")
    public String sso() {
        return "index";
    }

    @PostMapping("/login")
    @ResponseBody
    @SneakyThrows
    public Object doLogin(String username, String password, HttpServletRequest request) {
        log.info("username :{} -- password : {} ",username,password);
        String redirectUrl = request.getParameter(SsoConstants.REDIRECT_PARAM_NAME);
        Integer userId=authService.checkUserLogin(username,password);
        if(userId!=null){
            String token=authService.createToken(userId);
            request.getSession().setAttribute(SsoConstants.SESSION_LOGIN_FLAG, token);
            HttpUtil.setCookie(SsoConstants.TOKEN_PARAM_NAME,token,
                    SsoConstants.TOKEN_EXPIRY_TIME,
                    null);
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


    @RequestMapping(LOGOUT_URL)
    public String logout(HttpServletRequest request, ModelMap model) {

        //销毁自己的session
        HttpSession session = request.getSession();
        if (session != null) {
            session.invalidate();
        }

        HttpUtil.setCookie(SsoConstants.TOKEN_PARAM_NAME,"", 0, null);

        //TODO 废弃token


        //向各个sso客户端发送请求,告诉这个人已经退出
        authService.logoutAllSsoClients(
                request.getParameter(SsoConstants.TOKEN_PARAM_NAME));

        //跳转到登录页面
        String redirectUrl = request.getParameter(SsoConstants.REDIRECT_PARAM_NAME);
        model.addAttribute(SsoConstants.REDIRECT_PARAM_NAME, redirectUrl);
        return "index";
    }
}

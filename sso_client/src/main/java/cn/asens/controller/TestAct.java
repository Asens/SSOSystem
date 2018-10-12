package cn.asens.controller;

import cn.asens.cache.TokenCache;
import cn.asens.config.SSOProperties;
import cn.asens.constants.SsoConstants;
import cn.asens.entity.User;
import cn.asens.remote.SsoResponse;
import cn.asens.service.UserService;
import cn.asens.service.api.TokenApiService;
import cn.asens.util.HttpUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.SecurityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static cn.asens.constants.SsoConstants.CLEAR_TOKEN_URL;
import static cn.asens.constants.SsoConstants.LOGOUT_URL;

/**
 * @author Asens
 */
@RestController
@Log4j2
public class TestAct {
    @Resource
    private TokenApiService tokenApiService;
    @Resource
    private TokenCache tokenCache;
    @Resource
    private SSOProperties ssoProperties;

    @RequestMapping("/")
    public Object main() {
        return tokenApiService.test();
    }

    @RequestMapping("/user")
    public Object test(HttpServletRequest request) {
        log.info(SecurityUtils.getSubject().getPrincipals());
        return "logged in userId is "+
                request.getSession().getAttribute(SsoConstants.LOGIN_USER_SESSION);
    }

    /**
     * 退出接口
     */
    @RequestMapping(LOGOUT_URL)
    public String logout(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession();

        //跳转到sso服务器提交申请,注销会话
        String redirectUrl = ssoProperties.getServerUrl() + "/logout"
                + "?" + SsoConstants.REDIRECT_PARAM_NAME + "=" + HttpUtil.encodeUrl(HttpUtil.getRequestContextPath(request))
                + "&" + SsoConstants.TOKEN_PARAM_NAME + "=" + session.getAttribute(SsoConstants.SESSION_LOGIN_FLAG);
        try {
            response.sendRedirect(redirectUrl);
        } catch (IOException e) {
            log.error("退出跳转到服务器地址出错!", e);
        }
        return null;
    }

    /**
     * 清除客户端的token
     */
    @RequestMapping(CLEAR_TOKEN_URL)
    public SsoResponse clearToken(HttpServletRequest request) {
        String token = request.getParameter(SsoConstants.TOKEN_PARAM_NAME);
        tokenCache.addInvalidKey(token);
        return SsoResponse.success();
    }
}

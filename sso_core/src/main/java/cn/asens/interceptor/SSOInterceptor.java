package cn.asens.interceptor;

import cn.asens.config.SSOProperties;
import cn.asens.constants.SsoConstants;
import cn.asens.service.RemoteService;
import cn.asens.util.HttpUtil;
import com.google.common.base.Strings;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author Asens
 */
@Log4j2
public class SSOInterceptor extends HandlerInterceptorAdapter {
    @Resource
    private SSOProperties ssoProperties;
    @Resource
    private RemoteService remoteService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        log.info("handle :"+request.getRequestURI());
        log.info("properties {}",ssoProperties.getServerUrl());
        HttpSession session = request.getSession();
        Object sessionAttribute = session.getAttribute(SsoConstants.SESSION_LOGIN_FLAG);

        //没有登录状态
        if(sessionAttribute==null){
            String token = request.getParameter(SsoConstants.TOKEN_PARAM_NAME);

            if(Strings.isNullOrEmpty(token)){
                redirectSsoServer(request,response);
                return false;
            }

            log.info("got token :{}",token);
            Integer userId = remoteService.
                    validateToken(token, HttpUtil.getRequestContextPath(request));
            log.info("got userId [{}] from token",userId);
        }

        return true;
    }


    /**
     * 跳转到sso服务器去认证
     */
    private void redirectSsoServer(HttpServletRequest request, HttpServletResponse response) {
        String redirectUrl = ssoProperties.getServerUrl() + "?" +
                SsoConstants.REDIRECT_PARAM_NAME + "="
                + HttpUtil.encodeUrl(HttpUtil.getRequestFullPathNoParam(request));
        try {
            response.sendRedirect(redirectUrl);
        } catch (IOException e) {
            log.error("跳转到服务器出错!", e);
        }
    }

}
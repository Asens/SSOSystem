package cn.asens.interceptor;

import cn.asens.constants.SsoConstants;
import cn.asens.util.HttpUtil;
import com.google.common.base.Strings;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * sso客户端登录拦截器
 * @author Asens
 */
public class SSOServerInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession();

        //获取当前用户登录标记
        Object sessionAttribute = session.getAttribute(SsoConstants.SESSION_LOGIN_FLAG);
        String token = HttpUtil.getCookie(SsoConstants.TOKEN_PARAM_NAME);
        //如果没登录,跳转到登录页面
        if (sessionAttribute == null && Strings.isNullOrEmpty(token)) {
            request.setAttribute(SsoConstants.REDIRECT_PARAM_NAME, request.getParameter(SsoConstants.REDIRECT_PARAM_NAME));
            request.getRequestDispatcher("/login").forward(request, response);
            return false;
        } else {
            //当前用户已经登录,通过拦截器
            String redirectUrl = request.getParameter(SsoConstants.REDIRECT_PARAM_NAME);
            response.sendRedirect(redirectUrl + "?" + SsoConstants.TOKEN_PARAM_NAME + "=" + sessionAttribute.toString());
            return true;
        }
    }

}

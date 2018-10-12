package cn.asens.interceptor;

import cn.asens.constants.SsoConstants;
import cn.asens.entity.User;
import cn.asens.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Asens
 */

public class ShiroLoginInterceptor  extends HandlerInterceptorAdapter {
    @Resource
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        Object o =request.getSession().getAttribute(SsoConstants.LOGIN_USER_SESSION);
        Integer userId=Integer.valueOf(o.toString());
        User user=userService.getById(userId);
        UsernamePasswordToken token=new UsernamePasswordToken(user.getUsername(),
                user.getPassword());
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.login(token);
        return true;
    }
}

package cn.asens.controller;

import cn.asens.constants.SsoConstants;
import cn.asens.remote.SsoResponse;
import cn.asens.remote.enums.ResponseStatus;
import cn.asens.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Asens
 */

@Controller
public class AuthAct {
    @Resource
    private AuthService authService;

    /**
     * 验证token是否正确
     */
    @RequestMapping(SsoConstants.AUTH_TOKEN_URL)
    @ResponseBody
    public SsoResponse authToken(HttpServletRequest request) {

        String token = request.getParameter(SsoConstants.TOKEN_PARAM_NAME);
        String clientAddr = request.getParameter(SsoConstants.CLIENT_REQUEST_ADDR_PARAM_NAME);

        boolean flag = authService.checkToken(request, token, clientAddr);

        if (flag) {
            Integer userId = authService.getLoginUserByToken(token);
            return SsoResponse.success(userId);
        } else {
            return new SsoResponse(ResponseStatus.WRONG_TOKEN);
        }
    }
}

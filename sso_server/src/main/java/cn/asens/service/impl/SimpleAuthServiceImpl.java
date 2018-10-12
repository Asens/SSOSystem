package cn.asens.service.impl;


import cn.asens.constants.SsoConstants;
import cn.asens.entity.User;
import cn.asens.remote.SsoResponse;
import cn.asens.service.AuthService;
import cn.asens.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * auth服务接口最简单实现
 */

public class SimpleAuthServiceImpl implements AuthService {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private Map<String, Object> cache = new ConcurrentHashMap<>();

    private Set<String> ssoClients = new HashSet<>();

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @Override
    public Integer checkUserLogin(String userName, String password) {

        //查询账号是否存在

        User user= userService.getOne(new QueryWrapper<User>()
                .eq("username", userName));

        if (user==null) {
            return null;
        }

        if (password.equals(user.getPassword())) {
            return user.getId();
        } else {
            return null;
        }
    }

    @Override
    public String createToken(Integer userId) {
        String token = IdWorker.get32UUID();
        cache.put(token, userId);
        return token;
    }

    @Override
    public boolean checkToken(HttpServletRequest request, String token,
                              String clientAddress) {
        if (cache.containsKey(token)) {
            recordSSOClient(clientAddress);
            return true;
        }
        return false;
    }

    @Override
    public Integer getUserIdByToken(String token) {
        return (Integer) cache.get(token);
    }


    @Override
    public void recordSSOClient(String clientAddress) {
        ssoClients.add(clientAddress);
    }

    @Override
    public void logoutAllSsoClients(String token) {
        for (String ssoClientUrl : ssoClients) {
            String url = ssoClientUrl + SsoConstants.CLEAR_TOKEN_URL + "?" + SsoConstants.TOKEN_PARAM_NAME + "=" + token;

            try {
                restTemplate.getForObject(url, SsoResponse.class);
            } catch (Exception e) {
                log.error("发送客户端注销token请求出错!", e);
            }
        }
    }
}

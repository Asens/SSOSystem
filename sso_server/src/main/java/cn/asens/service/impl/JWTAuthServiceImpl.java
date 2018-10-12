package cn.asens.service.impl;


import cn.asens.constants.SsoConstants;
import cn.asens.entity.User;
import cn.asens.remote.SsoResponse;
import cn.asens.service.AuthService;
import cn.asens.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * auth服务接口JWT实现
 */
@Service
@Log4j2
public class JWTAuthServiceImpl implements AuthService {

    private Set<String> ssoClients = new HashSet<>();

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private UserService userService;

    @Value("{JWTSecret}")
    private String secret;

    private final static String USER_ID_KEY="userId";
    private final static String EXPIRE_KEY="expire";

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
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        Date now = new Date(System.currentTimeMillis());
        JwtBuilder builder = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .setIssuedAt(now)
                .claim(USER_ID_KEY, userId)
                .claim(EXPIRE_KEY, System.currentTimeMillis()+
                        SsoConstants.TOKEN_EXPIRY_TIME)
                .setIssuer("lwl")
                .signWith(signatureAlgorithm, key());
        String jwt = builder.compact();
        log.info("jwt:" + jwt);
        return jwt;
    }

    @Override
    public boolean checkToken(HttpServletRequest request, String token,
                              String clientAddress) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(key())
                    .parseClaimsJws(token)
                    .getBody();
            if (!claims.containsKey(USER_ID_KEY)) {
                return false;
            }

            if (System.currentTimeMillis() > Long.parseLong(claims.get(EXPIRE_KEY).toString())) {
                return false;
            }
        }catch (Exception e){
            log.error("token [{}] is error because : {} , clientAddress : {}",
                    token,e.getMessage(),clientAddress);
            return false;
        }
        recordSSOClient(clientAddress);
        return true;
    }

    @Override
    public Integer getUserIdByToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(key())
                    .parseClaimsJws(token)
                    .getBody();
            if (!claims.containsKey(USER_ID_KEY)) {
                return null;
            }

            if (System.currentTimeMillis() > Long.parseLong(claims.get(EXPIRE_KEY).toString())) {
                return null;
            }

            return Integer.valueOf(claims.get(USER_ID_KEY).toString());
        }catch (Exception e){
            log.error("token [{}] is error because : {}",
                    token,e.getMessage());
            return null;
        }
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
                log.error("error send logout messages", e);
            }
        }
    }

    private SecretKey key(){
        byte[] encodedKey = Base64.decodeBase64(secret);
        return new SecretKeySpec(encodedKey, 0,
                encodedKey.length, "AES");
    }
}

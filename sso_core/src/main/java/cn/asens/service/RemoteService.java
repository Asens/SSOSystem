package cn.asens.service;

import cn.asens.config.SSOProperties;
import cn.asens.constants.SsoConstants;
import cn.asens.remote.SsoResponse;
import cn.asens.remote.enums.ResponseStatus;
import org.springframework.web.client.RestTemplate;

/**
 * sso远程调用服务
 *
 * @author fengshuonan
 * @date 2018-02-04 15:47
 */
public class RemoteService {

    private RestTemplate restTemplate;

    private SSOProperties ssoProperties;

    public RemoteService(RestTemplate restTemplate, SSOProperties ssoProperties) {
        this.restTemplate = restTemplate;
        this.ssoProperties = ssoProperties;
    }

    /**
     * 请求sso server,验证token是否正确
     */
    public Integer validateToken(String token, String clientAddr) {
        String ssoUrl = "http://SSOServer/sso" + SsoConstants.AUTH_TOKEN_URL
                + "?" + SsoConstants.TOKEN_PARAM_NAME + "=" + token +
                "&" + SsoConstants.CLIENT_REQUEST_ADDR_PARAM_NAME + "=" + clientAddr;
        SsoResponse ssoResponse = restTemplate.postForObject(ssoUrl, null,
                SsoResponse.class);

        if (ssoResponse == null) {
            return null;
        } else {
            if (ResponseStatus.SUCCESS.getCode().equals(ssoResponse.getCode())) {
                return ssoResponse.getUserId();
            } else {
                return null;
            }
        }
    }
}

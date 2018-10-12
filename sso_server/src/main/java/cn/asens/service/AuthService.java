package cn.asens.service;


import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * auth服务接口
 *
 * @author fengshuonan
 * @date 2018-02-03 22:56
 */
public interface AuthService {

    /**
     * 验证账号密码是否正确(验证成功返回用户id,验证失败返回null)
     */
    Integer checkUserLogin(String userName, String password);

    /**
     * 创建授权令牌
     */
    String createToken(Integer userId);

    /**
     * 验证token是否正确(true-验证成功,false-验证失败)
     */
    boolean checkToken(HttpServletRequest request, String token, String clientAddress);

    /**
     * 通过token获取登录用户
     */
    Integer getUserIdByToken(String token);


    /**
     * 记录哪些客户端在服务端进行过校验
     */
    void recordSSOClient(String clientAddress);

    /**
     * 向所有sso客户端发送退出信号
     */
    void logoutAllSsoClients(String token);

}

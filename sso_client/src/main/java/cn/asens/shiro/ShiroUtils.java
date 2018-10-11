package cn.asens.shiro;

import org.apache.shiro.SecurityUtils;

/**
 * @author Asens
 */

public class ShiroUtils {

    public static boolean isLogin() {
        return SecurityUtils.getSubject().getPrincipals()!=null;
    }

}

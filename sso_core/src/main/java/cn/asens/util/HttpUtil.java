package cn.asens.util;


import cn.asens.constants.SsoConstants;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;


/**
 * http工具类
 *
 * @author fengshuonan
 * @date 2018-02-03 21:06
 */
public class HttpUtil {

    private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    /**
     * 编码url
     */
    public static String encodeUrl(String url) {
        try {
            return URLEncoder.encode(url, SsoConstants.URL_ENCODE_CHARSET);
        } catch (UnsupportedEncodingException e) {
            logger.error("编码url出错!", e);
            return null;
        }
    }

    /**
     * 解码url
     */
    public static String decodeURL(String url) {
        try {
            return URLDecoder.decode(url, SsoConstants.URL_ENCODE_CHARSET);
        } catch (UnsupportedEncodingException e) {
            logger.error("解码url出错!", e);
            return null;
        }
    }

    /**
     * 获取请求完整路径
     */
    public static String getRequestFullPath(HttpServletRequest request) {
        String result = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getRequestURI();
        String queryurl = request.getQueryString();
        if (null != queryurl) {
            result += "?" + queryurl;
        }
        return result;
    }

    /**
     * 获取请求完整路径(不带参数)
     */
    public static String getRequestFullPathNoParam(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getRequestURI();
    }

    /**
     * 获取请求路径(具体到context)
     */
    public static String getRequestContextPath(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    public static String getCookie(String name) {
        HttpServletRequest request=getRequest();
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie c : cookies) {
                if (c.getName().equals(name)) {
                    return c.getValue();
                }
            }
        }
        return null;
    }

    public static Cookie setCookie(String name, String value,
                                   Integer expiry, String domain) {
        HttpServletRequest request=getRequest();
        HttpServletResponse response=getResponse();
        Cookie cookie = new Cookie(name, value);
        if (expiry != null) {
            cookie.setMaxAge(expiry);
        }
        if (org.apache.commons.lang.StringUtils.isNotBlank(domain)) {
            cookie.setDomain(domain);
        }
        String ctx = request.getContextPath();
        cookie.setPath(org.apache.commons.lang.StringUtils.isBlank(ctx) ? "/" : ctx);
        response.addCookie(cookie);
        return cookie;
    }

    public static Boolean isAjax(){
        HttpServletRequest request=getRequest();
        return !Strings.isNullOrEmpty(request.getHeader("x-requested-with")) &&
                "XMLHttpRequest".equals(request.getHeader("x-requested-with"));
    }

    public static HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }
}

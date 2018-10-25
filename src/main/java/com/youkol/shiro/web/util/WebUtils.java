package com.youkol.shiro.web.util;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

/**
 * Web 应用工具类
 * 
 * @author jackiea
 */
public class WebUtils {

    /**
     * 返回客户端请求IP地址
     * 对于通过多个代理的情况，返回第一个IP地址
     * 
     * @param request 请求信息
     * @return 返回包含客户端请求IP地址的字符串
     */
    public static String getRemoteAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null) {
            ip = ip.split(",")[0];
        }

        return ip;
    }

    /**
     * 判断请求是否为Ajax请求
     * 
     * @param request 请求信息
     * @return 如果是ajax请求，则返回true，否则返回false
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        String header = request.getHeader("X-Requested-With");

        return Objects.equals("XMLHttpRequest", header);
    }
}
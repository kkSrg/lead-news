package com.shawn.media.intercept;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * @author shawn
 * @date 2023年 01月 09日 15:39
 */

public class MediaIntercept extends HandlerInterceptorAdapter {

    /**
     * 前处理
     *
     * @param request  请求
     * @param response 响应
     * @param handler  处理程序
     * @return boolean
     * @throws Exception 异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String id = request.getHeader("id");
        if (StringUtils.isEmpty(id)){
            return false;
        }
        ThreadContent.setId(Integer.parseInt(id));
        return true;
    }


    /**
     * 完成后
     *
     * @param request  请求
     * @param response 响应
     * @param handler  处理程序
     * @param ex       前女友
     * @throws Exception 异常
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ThreadContent.clear();
    }
}

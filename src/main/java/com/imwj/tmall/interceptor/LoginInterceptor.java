package com.imwj.tmall.interceptor;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author langao_q
 * @create 2019-12-18 15:20
 * 登录拦截器：校验是否登录
 * preHandle：在业务处理器处理请求之前被调用（常用）
 * postHandle：在业务处理器处理请求执行完成后，生成视图之前执行（少）
 * afterCompletion：在DispatcherServlet完全处理完请求后被调用，可用于清理资源等（少）
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        HttpSession session = httpServletRequest.getSession();
        String contextPath = httpServletRequest.getServletContext().getContextPath();
        //需要登录的页面url
        String[] requireAuthPages = new String[]{
                "buy",
                "alipay",
                "payed",
                "cart",
                "bought",
                "confirmPay",
                "orderConfirmed",

                "forebuyone",
                "forebuy",
                "foreaddCart",
                "forecart",
                "forechangeOrderItem",
                "foredeleteOrderItem",
                "forecreateOrder",
                "forepayed",
                "forebought",
                "foreconfirmPay",
                "foreorderConfirmed",
                "foredeleteOrder",
                "forereview",
                "foredoreview"
        };

        //当前请求的url
        String uri = httpServletRequest.getRequestURI();

        //去掉指定字符串
        uri = StringUtils.remove(uri, contextPath + "/");
        String page = uri;

        //判断是否是属于需要登录的url
        if(beginWith(page, requireAuthPages)){
            //校验用户是否登录
            Subject subject = SecurityUtils.getSubject();
            if(!subject.isAuthenticated()) {
                httpServletResponse.sendRedirect("login");
                return false;
            }
        }

        return true;
    }

    /**
     * 比较page是否存在于requiredAuthPages中
     * startsWith() 方法用于检测字符串是否以指定的前缀开始。
     * @param page
     * @param requiredAuthPages
     * @return
     */
    private boolean beginWith(String page, String[] requiredAuthPages){
        boolean result = false;
        for(String requiredAuthPage : requiredAuthPages){
            if(StringUtils.startsWith(page, requiredAuthPage)){
                result = true;
                break;
            }
        }
        return result;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}

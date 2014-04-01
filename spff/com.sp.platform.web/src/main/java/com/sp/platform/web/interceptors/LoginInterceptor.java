package com.sp.platform.web.interceptors;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.sp.platform.entity.User;
import com.sp.platform.service.UserService;
import com.yangl.common.Constants;
import com.yangl.common.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Session拦截器
 */

@Component
public class LoginInterceptor extends AbstractInterceptor {
    private static final long serialVersionUID = -5392279387373574216L;
    private static Logger logger = Logger.getLogger(LoginInterceptor.class);

    @Autowired
    private UserService userService;

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        if (logger.isInfoEnabled()) {
            logger.info("session-interceptor...");
        }

        ActionContext ctx = invocation.getInvocationContext();

        HttpServletRequest request = (HttpServletRequest) ctx
                .get(ServletActionContext.HTTP_REQUEST);
        HttpServletResponse response = ServletActionContext.getResponse();
        HttpSession session = request.getSession();

        // 通过session 判断用户是否登录，没有则继续读取cookie
        if (session != null
                && session.getAttribute(Constants.SESSION_KEY) != null) {
            return invocation.invoke();
        }
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (Constants.COOKIE_KEY.equals(cookie.getName())) {
                    String cookieValue = cookie.getValue();
                    if (StringUtils.isNotBlank(cookieValue)) {
                        String[] values = cookieValue.split(";");
                        User user = new User();
                        user.setName(values[0]);
                        user.setPasswd(values[1]);
                        User dbUser = userService.checkUser(user);
                        if (dbUser != null) {
                            session.setAttribute(Constants.SESSION_KEY, dbUser);
                            logger.info("cookie 登陆成功...");
                            return invocation.invoke();
                        } else {
                            cookie.setPath("/");
                            cookie.setValue(null);
                            cookie.setMaxAge(0);
                            response.addCookie(cookie);
                        }
                    }

                }
            }
        }
        // 设置客户原来请求的url地址
        setToGoingURL(request, session, invocation);
        return "login";
    }

    @SuppressWarnings("unchecked")
    private void setToGoingURL(HttpServletRequest request, HttpSession session,
                               ActionInvocation invocation) {

        // 如果referer不为空 直接使用它。如果为空我们分别获得命名空间，action名,以及请求参数
        // 从新构造成一个URL保存在session中
        StringBuilder builder = new StringBuilder();

        String nameSpace = invocation.getProxy().getNamespace();
        String actionName = invocation.getProxy().getActionName();
        String methodName = invocation.getProxy().getMethod();

        if (StringUtils.isNotBlank(nameSpace)) {
            if (!nameSpace.equals("/"))
                builder.append(nameSpace);
        }
        if (StringUtils.isNotBlank(actionName)) {
            builder.append("/").append(actionName);
            if (StringUtils.isNotBlank(methodName)) {
                builder.append("!").append(methodName);
            }
            builder.append(".action");
        }

        Map<String, String[]> zzMap = request.getParameterMap();
        if (zzMap != null) {
            boolean firstFlag = true;

            for (String s : zzMap.keySet()) {
                String[] value = zzMap.get(s);
                for (String val : value) {

                    if (!firstFlag) {
                        builder.append("&");
                    } else {
                        builder.append("?");
                        firstFlag = false;
                    }

                    builder.append(s).append("=").append(val);
                }
            }
        }
        String url = builder.toString();

        logger.info("完整URL:" + url);

        if (StringUtils.isBlank(url)) {
            url = request.getHeader("referer");
            logger.info("referer 待转向URL:" + url);
        }

        session.setAttribute(Constants.GOTO_URL_KEY, url);
    }
}

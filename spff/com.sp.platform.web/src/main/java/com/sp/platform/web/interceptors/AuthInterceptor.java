package com.sp.platform.web.interceptors;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.sp.platform.entity.User;
import com.sp.platform.service.UserService;
import com.yangl.common.Constants;
import com.yangl.common.StringUtils;
import com.yangl.common.Struts2Utils;
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
public class AuthInterceptor extends AbstractInterceptor {
    private static final long serialVersionUID = -5392279387373574216L;
    private static Logger logger = Logger.getLogger(AuthInterceptor.class);

    @Autowired
    private UserService userService;

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        if (logger.isInfoEnabled()) {
            logger.info("auth-interceptor...");
        }
        try {
            User user = (User) Struts2Utils.getSession().getAttribute(Constants.SESSION_KEY);
            String nameSpace = invocation.getProxy().getNamespace();
            String action = invocation.getProxy().getActionName();
            String method = invocation.getProxy().getMethod();
            if(action.equals("cp") && method.equals("updatePasswd")){
                return invocation.invoke();
            }
            if (nameSpace.indexOf("manage") >= 0 && user.getRole() != 1) {
                return "auth_error";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return invocation.invoke();
    }
}

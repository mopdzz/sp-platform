package com.sp.platform.web.action;

import com.opensymphony.xwork2.ActionSupport;
import com.sp.platform.common.PageView;
import com.sp.platform.entity.User;
import com.sp.platform.service.MenuService;
import com.sp.platform.service.UserService;
import com.yangl.common.Constants;
import com.yangl.common.CookieHelper;
import com.yangl.common.Struts2Utils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: yfyanglei
 * Date: 13-4-4
 * Time: 下午1:52
 * To change this template use File | Settings | File Templates.
 */
@Namespace("/")
@Scope("request")
@Results({@Result(name = "success", location = "${goingToURL}", type = "redirect"),
        @Result(name = "leftMenu", location = "/left.jsp")})
public class LoginAction extends ActionSupport {
    private static Logger logger = LoggerFactory.getLogger(LoginAction.class);
    @Resource
    private UserService userService;
    @Resource
    private MenuService menuService;

    private String goingToURL = "/index.jsp";
    private boolean rememberMe;
    private User user;
    private String menuString;
    private String message;
    private List list;
    private PageView pageView;

    /**
     * 登陆
     */
    @Action("doLogin")
    public String doLogin() {
        user = userService.checkUser(user);
        if (user != null) {
            logger.info("login is success");
            if (rememberMe) {
                CookieHelper.instance.addPublicCookie(Constants.COOKIE_KEY, user.getName() + ";" + user.getPasswd(), 60 * 60 * 24 * 7 * 2, ServletActionContext.getResponse());
                logger.info("writer cookie is success...");
            }
            Struts2Utils.getSession().setAttribute(Constants.SESSION_KEY, user);
            if (Struts2Utils.getSession().getAttribute(Constants.GOTO_URL_KEY) != null) {
                String goto_url = Struts2Utils.getSession().getAttribute(Constants.GOTO_URL_KEY).toString();
                if (StringUtils.isNotEmpty(goto_url)) {
                    logger.info("response will goto : " + goto_url);
                    setGoingToURL(goto_url);
                    Struts2Utils.getSession().removeAttribute(Constants.GOTO_URL_KEY);
                }
            } else {
                logger.info("url is goto : " + goingToURL);
                setGoingToURL(goingToURL);
            }
            return SUCCESS;
        } else {
            logger.info("user name or pwd error...");
            message = "用户名或密码错误";
            return "login";
        }
    }

    /**
     * 构建左边树菜单
     */
    @Action("leftMenu")
    public String leftMenu() {
        User user = (User) Struts2Utils.getSession().getAttribute(Constants.SESSION_KEY);
        if(user == null){
            return "leftMenu";
        }
        logger.info(user.getName() + "|" + user.getId());
        list = menuService.getUserMenus(user.getRole());
        return "leftMenu";
    }

    /**
     * 退出
     */
    @Action("logout")
    public String logout() {
        Struts2Utils.getSession().removeAttribute(Constants.SESSION_KEY);
        CookieHelper.instance.removePublicCookie(Constants.COOKIE_KEY, ServletActionContext.getResponse());
        return "login";
    }

    public String getGoingToURL() {
        return goingToURL;
    }

    public void setGoingToURL(String goingToURL) {
        this.goingToURL = goingToURL;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMenuString() {
        return menuString;
    }

    public void setMenuString(String menuString) {
        this.menuString = menuString;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public PageView getPageView() {
        return pageView;
    }

    public void setPageView(PageView pageView) {
        this.pageView = pageView;
    }

}

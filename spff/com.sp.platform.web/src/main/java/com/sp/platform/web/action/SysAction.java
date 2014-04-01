package com.sp.platform.web.action;

import com.opensymphony.xwork2.ActionSupport;
import com.sp.platform.cache.CpSyncCache;
import com.sp.platform.cache.SpInfoCache;
import com.sp.platform.common.GroupType;
import com.sp.platform.common.PageView;
import com.sp.platform.service.BillLogService;
import com.sp.platform.service.SysService;
import com.sp.platform.vo.BillVo;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * User: yangl
 * Date: 13-5-30 上午3:08
 */
@Namespace("/manage")
@Scope("prototype")
@InterceptorRefs({@InterceptorRef("loginInterceptor")})
@Results({@Result(name = "sys", location = "sys_init.jsp")})
public class SysAction extends ActionSupport {
    @Autowired
    private SysService sysService;

    private PageView pageView = new PageView();

    private List list;

    public String init() {
        return "sys";
    }

    public String select1() {
        list = sysService.select(pageView);
        return "sys";
    }

    public String update1() {
        pageView.setMessage("共执行了 " + sysService.exec(pageView) + " 条");
        return "sys";
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

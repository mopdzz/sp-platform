package com.sp.platform.web.action;

import com.opensymphony.xwork2.ActionSupport;
import com.sp.platform.common.PageView;
import com.sp.platform.entity.BillLog;
import com.sp.platform.service.BillLogService;
import com.sp.platform.service.BillTempService;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import java.util.Collections;
import java.util.List;

/**
 * User: yangl
 * Date: 13-6-11 下午10:36
 */
@Namespace("/manage")
@Scope("prototype")
@InterceptorRefs({@InterceptorRef("loginInterceptor")})
@Results({@Result(name = "list", location = "caller_list.jsp"),
        @Result(name = "smslist", location = "caller_smslist.jsp")})
public class CallerAction extends ActionSupport {
    @Autowired
    private BillLogService billLogService;
    @Autowired
    private BillTempService billTempService;

    private PageView pageView = new PageView();
    private List list;
    private List list2;

    public String list() {
        if (StringUtils.isNotBlank(pageView.getCaller())) {
            list = billLogService.getByCaller(pageView.getCaller());
            list2 = billTempService.getByCaller(pageView.getCaller());
        } else {
            list = Collections.emptyList();
            list2 = Collections.emptyList();
        }
        return "list";
    }
    public String smslist() {
        if (StringUtils.isNotBlank(pageView.getCaller())) {
            list = billLogService.getSmsByCaller(pageView.getCaller());
            list2 = billTempService.getSmsByCaller(pageView.getCaller());
        } else {
            list = Collections.emptyList();
            list2 = Collections.emptyList();
        }
        return "smslist";
    }

    /**
     * 补发操作
     *
     * @return
     */
    public String bufa(){

        if(pageView.getId() > 0){
            billLogService.bufa(pageView.getId());
        }

        return list();
    }

    public String bufasms(){

        if(pageView.getId() > 0){
            billLogService.bufasms(pageView.getId());
        }

        return smslist();
    }

    /**
     * 重新同步
     *
     * @return
     */
    public String sync(){

        if(pageView.getId() > 0){
            billTempService.sync(pageView.getId());
        }

        return list();
    }
    public String syncsms(){

        if(pageView.getId() > 0){
            billTempService.syncsms(pageView.getId());
        }

        return smslist();
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public List getList2() {
        return list2;
    }

    public void setList2(List list2) {
        this.list2 = list2;
    }

    public PageView getPageView() {
        return pageView;
    }

    public void setPageView(PageView pageView) {
        this.pageView = pageView;
    }
}

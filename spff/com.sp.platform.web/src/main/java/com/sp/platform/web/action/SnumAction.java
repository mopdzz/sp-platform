package com.sp.platform.web.action;

import com.opensymphony.xwork2.ActionSupport;
import com.sp.platform.cache.CpSyncCache;
import com.sp.platform.cache.SpInfoCache;
import com.sp.platform.common.PageView;
import com.sp.platform.common.SpType;
import com.sp.platform.entity.CpNum;
import com.sp.platform.entity.ServiceNum;
import com.sp.platform.entity.User;
import com.sp.platform.service.ServiceNumService;
import com.sp.platform.service.SpInfoService;
import com.yangl.common.Constants;
import com.yangl.common.Struts2Utils;
import com.yangl.common.hibernate.PaginationSupport;
import org.apache.struts2.convention.annotation.*;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import java.util.Date;
import java.util.List;

/**
 * User: yangl
 * Date: 13-5-24 下午10:30
 */
@Namespace("/manage")
@Scope("prototype")
@InterceptorRefs({@InterceptorRef("loginInterceptor")})
@Results({@Result(name = "list", location = "snum_list.jsp"),
        @Result(name = "add", location = "snum_add.jsp")})
public class SnumAction extends ActionSupport {
    @Autowired
    private ServiceNumService snumService;
    @Autowired
    private SpInfoService spInfoService;

    private PaginationSupport paginationSupport;
    private List list;
    private String pageGoto;
    private int pageSize;
    private int pageNum;
    private Order[] orders;

    private List list2;
    private ServiceNum serviceNum;
    private PageView pageView = new PageView();

    public String list() {
        if (pageNum < 1)
            pageNum = 1;
        if (pageSize < 1)
            pageSize = com.sp.platform.common.Constants.page_size;
        paginationSupport = new PaginationSupport(pageNum, pageSize);
        orders = new Order[]{Order.desc("id")};

        paginationSupport = snumService.getPage(paginationSupport, orders, pageView);
        if (paginationSupport.getItems() != null) {
            List<ServiceNum> temp = paginationSupport.getItems();
            for (ServiceNum snum : temp) {
                snum.setSpname(SpInfoCache.getSpInfoName(snum.getSpid()));
                snum.setCpname(getCpname(snum.getCalled()));
            }
            list = temp;
        }
        paginationSupport.setUrl(Struts2Utils.getRequest().getContextPath() + "/manage/snum!list.action");
        pageGoto = PaginationSupport.getClientPageContent(paginationSupport);

        list2 = spInfoService.getAll();

        return "list";
    }

    private String getCpname(String called){
        CpNum cpNum = CpSyncCache.getCp(called);
        if(cpNum == null){
            return "";
        }else{
            return cpNum.getCpname();
        }
    }

    public String delete() {
        if (pageView.getId() > 0) {
            snumService.delete(pageView.getId());
        }
        return list();
    }

    public String add() {
        if (pageView.getId() > 0) {
            serviceNum = snumService.get(pageView.getId());
            convertToPageView();
        }
        list2 = spInfoService.getAll();
        return "add";
    }

    public String doAdd() {
        Date now = new Date();
        if (pageView.getId() <= 0) {
            serviceNum = new ServiceNum();
            serviceNum.setCtime(now);
        } else {
            serviceNum = snumService.get(pageView.getId());
        }

        serviceNum.setUtime(now);
        fromPageView();
        snumService.save(serviceNum);
        pageView.setSpid(0);

        return list();
    }

    private void convertToPageView() {
        if (serviceNum != null) {
            pageView.setId(serviceNum.getId());
            pageView.setSpid(serviceNum.getSpid());
            pageView.setCalled(serviceNum.getCalled());
            pageView.setFee(serviceNum.getFee());
            pageView.setDaylimit(serviceNum.getDaylimit());
            pageView.setMonthlimit(serviceNum.getMonthlimit());
            pageView.setMemo(serviceNum.getMemo());
            pageView.setStatus(serviceNum.getStatus());
        }
    }

    private void fromPageView() {
        User user = (User) Struts2Utils.getSession().getAttribute(Constants.SESSION_KEY);
        serviceNum.setOpid(user.getId());
        serviceNum.setSpid(pageView.getSpid());
        serviceNum.setCalled(pageView.getCalled());
        serviceNum.setFee(pageView.getFee());
        serviceNum.setDaylimit(pageView.getDaylimit());
        serviceNum.setMonthlimit(pageView.getMonthlimit());
        serviceNum.setMemo(pageView.getMemo());
        serviceNum.setStatus(pageView.getStatus());
    }

    public PageView getPageView() {
        return pageView;
    }

    public void setPageView(PageView pageView) {
        this.pageView = pageView;
    }

    public List getList2() {
        return list2;
    }

    public void setList2(List list2) {
        this.list2 = list2;
    }

    public ServiceNum getServiceNum() {
        return serviceNum;
    }

    public void setServiceNum(ServiceNum serviceNum) {
        this.serviceNum = serviceNum;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getPageGoto() {
        return pageGoto;
    }

    public void setPageGoto(String pageGoto) {
        this.pageGoto = pageGoto;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public PaginationSupport getPaginationSupport() {
        return paginationSupport;
    }

    public void setPaginationSupport(PaginationSupport paginationSupport) {
        this.paginationSupport = paginationSupport;
    }

}

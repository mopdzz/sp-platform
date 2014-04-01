package com.sp.platform.web.action;

import com.opensymphony.xwork2.ActionSupport;
import com.sp.platform.cache.CpSyncCache;
import com.sp.platform.common.PageView;
import com.sp.platform.entity.CpNum;
import com.sp.platform.entity.ProvReduce;
import com.sp.platform.entity.User;
import com.sp.platform.service.CpNumService;
import com.sp.platform.service.ProvReduceService;
import com.sp.platform.service.ServiceNumService;
import com.sp.platform.service.UserService;
import com.sp.platform.vo.ProvinceVo;
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
 * Date: 13-5-29 上午12:23
 */
@Namespace("/manage")
@Scope("prototype")
@InterceptorRefs({@InterceptorRef("loginInterceptor")})
@Results({@Result(name = "list", location = "cpnum_list.jsp"),
        @Result(name = "add", location = "cpnum_add.jsp"),
        @Result(name = "provReduceList", location = "cpnum_prov_reduce.jsp"),
        @Result(name = "provReduceAdd", location = "cpnum_prov_reduce_add.jsp")})
public class CpnumAction extends ActionSupport {

    @Autowired
    private UserService userService;
    @Autowired
    private ServiceNumService serviceNumService;
    @Autowired
    private CpNumService cpNumService;
    @Autowired
    private ProvReduceService provReduceService;

    private PaginationSupport paginationSupport;
    private List list;
    private String pageGoto;
    private int pageSize;

    private int pageNum;
    private Order[] orders;

    private List list2;
    private PageView pageView = new PageView();
    private CpNum cpNum;

    public String list() {
        User user = (User) Struts2Utils.getSession().getAttribute(Constants.SESSION_KEY);

        if (pageNum < 1)
            pageNum = 1;
        if (pageSize < 1)
            pageSize = com.sp.platform.common.Constants.page_size;
        paginationSupport = new PaginationSupport(pageNum, pageSize);
        orders = new Order[]{Order.desc("cpid"), Order.desc("id")};

        paginationSupport = cpNumService.getPage(paginationSupport, orders, pageView);
        if (paginationSupport.getItems() != null) {
            list = paginationSupport.getItems();
        }
        paginationSupport.setUrl(Struts2Utils.getRequest().getContextPath() + "/manage/cpnum!list.action");
        pageGoto = PaginationSupport.getClientPageContent(paginationSupport);

        list2 = userService.getCpList();
        return "list";
    }

    public String add() {
        if (pageView.getId() > 0) {
            cpNum = cpNumService.get(pageView.getId());
            convertToPageView();
        }
        list2 = userService.getCpList();
        list = serviceNumService.getAll();
        return "add";
    }

    public String doAdd() {
        Date now = new Date();
        if (pageView.getId() <= 0) {
            cpNum = cpNumService.getByCalled(pageView.getCalled());
            if(cpNum != null){
                pageView.setMessage(pageView.getCalled() + " 已经被分配");
                return add();
            }

            cpNum = new CpNum();
            cpNum.setCtime(now);
        } else {
            cpNum = cpNumService.get(pageView.getId());
        }

        cpNum.setUtime(now);
        fromPageView();
        cpNumService.save(cpNum);
        pageView.setCpid(0);

        return list();
    }

    private void convertToPageView() {
        if (cpNum != null) {
            pageView.setId(cpNum.getId());
            pageView.setCpid(cpNum.getCpid());
            pageView.setCalled(cpNum.getCalled());
            pageView.setDaylimit(cpNum.getDaylimit());
            pageView.setMonthlimit(cpNum.getMonthlimit());
            pageView.setDeductRate(cpNum.getReduce());
            pageView.setMemo(cpNum.getBlackinfo());
        }
    }

    private void fromPageView() {
        User user = (User) Struts2Utils.getSession().getAttribute(Constants.SESSION_KEY);
        cpNum.setCpid(pageView.getCpid());
        cpNum.setCalled(pageView.getCalled());
        cpNum.setDaylimit(pageView.getDaylimit());
        cpNum.setMonthlimit(pageView.getMonthlimit());
        cpNum.setReduce(pageView.getDeductRate());
        cpNum.setBlackinfo(pageView.getMemo());
    }

    public String delete() {
        if (pageView.getId() > 0) {
            cpNumService.delete(pageView.getId());
        }
        return list();
    }



    public String prlist(){
        list = provReduceService.getByCalled(pageView.getCalled().replace("-", "#"));
        return "provReduceList";
    }

    public String prdelete() {
        if (pageView.getId() > 0) {
            provReduceService.delete(pageView.getId());
        }
        return prlist();
    }

    public String pradd(){
        list2 = ProvinceVo.list;
        if(pageView.getId() > 0){
            ProvReduce provReduce = provReduceService.get(pageView.getId());
            pageView.setProvince(provReduce.getProvince());
            pageView.setCalled(provReduce.getCalled());
            pageView.setDaylimit(provReduce.getDaylimit());
            pageView.setMonthlimit(provReduce.getMonthlimit());
            pageView.setDeductRate(provReduce.getReduce());
        }
        return "provReduceAdd";
    }


    public String doPradd() {
        Date now = new Date();
        ProvReduce provReduce;
        if (pageView.getId() <= 0) {
            provReduce = new ProvReduce();
            provReduce.setCtime(now);
        } else {
            provReduce = provReduceService.get(pageView.getId());
        }

        provReduce.setCalled(pageView.getCalled());
        provReduce.setProvince(pageView.getProvince());
        provReduce.setDaylimit(pageView.getDaylimit());
        provReduce.setMonthlimit(pageView.getMonthlimit());
        provReduce.setReduce(pageView.getDeductRate());
        provReduce.setUtime(now);
        provReduceService.save(provReduce);
        pageView.setSpid(0);

        return prlist();
    }

    public PaginationSupport getPaginationSupport() {
        return paginationSupport;
    }

    public void setPaginationSupport(PaginationSupport paginationSupport) {
        this.paginationSupport = paginationSupport;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public String getPageGoto() {
        return pageGoto;
    }

    public void setPageGoto(String pageGoto) {
        this.pageGoto = pageGoto;
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

    public Order[] getOrders() {
        return orders;
    }

    public void setOrders(Order[] orders) {
        this.orders = orders;
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

    public String allot(){
        return "allotCalled";
    }

    public String doAllot(){
        return "";
    }
}

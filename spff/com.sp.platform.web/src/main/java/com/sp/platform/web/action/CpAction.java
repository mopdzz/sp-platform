package com.sp.platform.web.action;

import com.opensymphony.xwork2.ActionSupport;
import com.sp.platform.common.PageView;
import com.sp.platform.entity.ProvReduce;
import com.sp.platform.entity.User;
import com.sp.platform.service.ProvReduceService;
import com.sp.platform.service.SpInfoService;
import com.sp.platform.service.UserService;
import com.yangl.common.Constants;
import com.yangl.common.Struts2Utils;
import com.yangl.common.hibernate.PaginationSupport;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.*;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import java.util.Date;
import java.util.List;

/**
 * User: yangl
 * Date: 13-5-28 上午12:11
 */
@Namespace("/manage")
@Scope("prototype")
@InterceptorRefs({@InterceptorRef("loginInterceptor")})
@Results({@Result(name = "list", location = "cp_list.jsp"),
        @Result(name = "add", location = "cp_add.jsp"),
        @Result(name = "updatePasswd", location = "updatePasswd.jsp")})
public class CpAction extends ActionSupport {

    @Autowired
    private UserService userService;
    @Autowired
    private SpInfoService spInfoService;

    private PaginationSupport paginationSupport;
    private List list;
    private String pageGoto;
    private int pageSize;
    private int pageNum;
    private Order[] orders;

    private List list2;
    private User user;
    private PageView pageView = new PageView();

    public String list() {
        User user = (User) Struts2Utils.getSession().getAttribute(Constants.SESSION_KEY);
        if(user.getRole() == 10){
            pageView.setRole(11);
            pageView.setParentId(user.getId());
        }

        if(pageView.getRole() < 1){
            pageView.setRole(10);
        }

        if (pageNum < 1)
            pageNum = 1;
        if (pageSize < 1)
            pageSize = com.sp.platform.common.Constants.page_size;
        paginationSupport = new PaginationSupport(pageNum, pageSize);
        orders = new Order[]{Order.desc("id")};

        paginationSupport = userService.getPage(paginationSupport, orders, pageView);
        if (paginationSupport.getItems() != null) {
            list = paginationSupport.getItems();
        }
        paginationSupport.setUrl(Struts2Utils.getRequest().getContextPath() + "/manage/cp!list.action");
        pageGoto = PaginationSupport.getClientPageContent(paginationSupport);
        if(pageView.getParentId() > 0){
            pageView.setRole(11);
        }else{
            pageView.setRole(10);
        }

        return "list";
    }

    public String add() {
        if (pageView.getId() > 0) {
            user = userService.get(pageView.getId());
            convertToPageView();
        }
        return "add";
    }

    public String doAdd() {
        Date now = new Date();
        if (pageView.getId() <= 0) {
            user = new User();
            user.setCtime(now);
        } else {
            user = userService.get(pageView.getId());
        }

        user.setUtime(now);
        fromPageView();
        userService.save(user);
        pageView.setSpid(0);

        return list();
    }

    public String delete() {
        if (pageView.getId() > 0) {
            userService.delete(pageView.getId());
        }
        return list();
    }

    public String updatePasswd(){
        return "updatePasswd";
    }

    public String doUpdate(){
        User sessionUser = (User) Struts2Utils.getSession().getAttribute(Constants.SESSION_KEY);
        if(sessionUser.getPasswd().equals(pageView.getPasswd())){
            userService.updatePasswd(sessionUser.getId(), pageView.getMemo());
                pageView.setMessage("更新成功");
        }else{
            pageView.setMessage("原密码不对");
        }
        return "updatePasswd";
    }

    private void convertToPageView() {
        if (user != null) {
            pageView.setId(user.getId());
            pageView.setName(user.getName());
            pageView.setPasswd(user.getPasswd());
            pageView.setShowname(user.getShowname());
            pageView.setRole(user.getRole());
            pageView.setStatus(user.getStatus());
            pageView.setSyncurl(user.getSyncurl());
            pageView.setCtime(user.getCtime());
        }
    }

    private void fromPageView() {
        User sessionUser = (User) Struts2Utils.getSession().getAttribute(Constants.SESSION_KEY);
        user.setName(pageView.getName().trim());
        user.setPasswd(pageView.getPasswd().trim());
        user.setShowname(pageView.getShowname());
        user.setRole(pageView.getRole());
        user.setParentId(pageView.getParentId());
        user.setStatus(pageView.getStatus());
        if(StringUtils.isNotBlank(pageView.getSyncurl())){
            user.setSyncurl(pageView.getSyncurl().trim());
        } else {
            user.setSyncurl("");
        }
    }

    public List getList2() {
        return list2;
    }

    public void setList2(List list2) {
        this.list2 = list2;
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

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public Order[] getOrders() {
        return orders;
    }

    public void setOrders(Order[] orders) {
        this.orders = orders;
    }

    public PageView getPageView() {
        return pageView;
    }

    public void setPageView(PageView pageView) {
        this.pageView = pageView;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

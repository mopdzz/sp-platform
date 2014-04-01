package com.sp.platform.web.action;

import com.opensymphony.xwork2.ActionSupport;
import com.sp.platform.entity.SpInfo;
import com.sp.platform.entity.User;
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
 * Created with IntelliJ IDEA.
 * User: yfyanglei
 * Date: 13-4-6
 * Time: 下午9:46
 * To change this template use File | Settings | File Templates.
 */
@Namespace("/manage")
@Scope("request")
@InterceptorRefs({@InterceptorRef("loginInterceptor")})
@Results({@Result(name = "list", location = "sp_list.jsp"),
        @Result(name = "add", location = "sp_add.jsp")})
public class SpAction extends ActionSupport {
    @Autowired
    private SpInfoService spInfoService;

    private PaginationSupport paginationSupport;
    private List list;
    private String pageGoto;
    private int pageSize;
    private int pageNum;
    private Order[] orders;

    private int id;
    private SpInfo spInfo;

    public String list() {
        if (pageNum < 1)
            pageNum = 1;
        if (pageSize < 1)
            pageSize = com.sp.platform.common.Constants.page_size;
        paginationSupport = new PaginationSupport(pageNum, pageSize);
        orders = new Order[]{Order.desc("id")};

        paginationSupport = spInfoService.getPage(paginationSupport, orders, null);
        if (paginationSupport.getItems() != null)
            this.list = paginationSupport.getItems();
        paginationSupport.setUrl(Struts2Utils.getRequest().getContextPath() + "/manage/sp!list.action");
        pageGoto = PaginationSupport.getClientPageContent(paginationSupport);

        return "list";
    }

    public String add() {
        if (id > 0) {
            spInfo = spInfoService.get(id);
        }
        return "add";
    }

    public String doAdd() {
        if (spInfo != null) {
            User user = (User) Struts2Utils.getSession().getAttribute(Constants.SESSION_KEY);
            spInfo.setOpid(user.getId());
            Date now = new Date();
            spInfo.setUtime(now);

            if (spInfo.getId() == null || spInfo.getId() <= 0) {
                spInfo.setCtime(now);
                spInfo.setStatus(1);
                spInfoService.save(spInfo);
            }else {
                SpInfo old = spInfoService.get(spInfo.getId());
                old.setOpid(spInfo.getOpid());
                old.setName(spInfo.getName());
                old.setType(spInfo.getType());
                old.setContact(spInfo.getContact());
                old.setUtime(now);
                old.setSyncurl(spInfo.getSyncurl());
                old.setMemo(spInfo.getMemo());
                old.setStatus(spInfo.getStatus());
                spInfoService.save(old);
            }
        }

        return list();
    }

    public String delete(){
        if(id > 0){
            spInfoService.delete(id);
        }
        return list();
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

    public SpInfo getSpInfo() {
        return spInfo;
    }

    public void setSpInfo(SpInfo spInfo) {
        this.spInfo = spInfo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

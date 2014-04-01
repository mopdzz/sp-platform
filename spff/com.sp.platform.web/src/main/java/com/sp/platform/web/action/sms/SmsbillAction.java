package com.sp.platform.web.action.sms;

import com.opensymphony.xwork2.ActionSupport;
import com.sp.platform.cache.CpSyncCache;
import com.sp.platform.cache.SpInfoCache;
import com.sp.platform.common.PageView;
import com.sp.platform.service.BillLogService;
import com.sp.platform.service.SpInfoService;
import com.sp.platform.service.UserService;
import com.sp.platform.vo.BillVo;
import com.sp.platform.vo.SmsBillVo;
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
@Results({@Result(name = "list", location = "bill_sms_list.jsp"),
        @Result(name = "cplist", location = "bill_sms_cplist.jsp")})
public class SmsbillAction extends ActionSupport {
    @Autowired
    private BillLogService billLogService;
    @Autowired
    private UserService userService;
    @Autowired
    private SpInfoService spInfoService;

    private PageView pageView = new PageView();

    private List<SmsBillVo> list;
    private List list2;
    private List list3;

    public String list() {
        if (pageView == null) {
            pageView = new PageView();
        }
        if (StringUtils.isEmpty(pageView.getBtime()) || StringUtils.isEmpty(pageView.getEtime())) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String shj = format.format(new Date());
            pageView.setBtime(shj);
            pageView.setEtime(shj);
        }
        if (StringUtils.isNotBlank(pageView.getProvince())) {
            try {
                pageView.setProvince(new String(pageView.getProvince().getBytes("ISO-8859-1"), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        list = billLogService.getSmsBillInfo(pageView);
        if (list != null) {
            for (SmsBillVo bean : list) {
                if ("总计".equals(bean.getGroup())) {
                    continue;
                }
                if (bean.getType() == 1) {
                    if (StringUtils.isNotBlank(bean.getHref())) {
                        bean.setGroup(bean.getHref() + SpInfoCache.getSpInfoName(toInt(bean.getGroup())) + "</a>");
                    } else {
                        bean.setGroup(bean.getGroup());
                    }
                } else if (bean.getType() == 2) {
                    if (StringUtils.isNotBlank(bean.getHref())) {
                        bean.setGroup(bean.getHref() + CpSyncCache.getCpName(toInt(bean.getGroup())) + "</a>");
                    } else {
                        bean.setGroup(CpSyncCache.getCpName(toInt(bean.getGroup())));
                    }
                } else {
                    if (StringUtils.isNotBlank(bean.getHref())) {
                        bean.setGroup(bean.getHref() + bean.getGroup() + "</a>");
                    } else {
                        bean.setGroup(bean.getGroup());
                    }
                }
            }
        }
        if (StringUtils.isNotBlank(pageView.getDate())){
            pageView.setGroup("1");
        }

        list2 = spInfoService.getAll();
        list3 = userService.getByRole(10);
        return "list";
    }

    public String cplist() {
        if (pageView == null) {
            pageView = new PageView();
        }
        pageView.setCpflag(1);

        if (StringUtils.isEmpty(pageView.getBtime()) || StringUtils.isEmpty(pageView.getEtime())) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String shj = format.format(new Date());
            pageView.setBtime(shj);
            pageView.setEtime(shj);
        }
        if (StringUtils.isNotBlank(pageView.getProvince())) {
            try {
                pageView.setProvince(new String(pageView.getProvince().getBytes("ISO-8859-1"), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        pageView.setGroup("5");
        list = billLogService.getSmsBillInfo(pageView);
        if (list != null) {

            for (SmsBillVo bean : list) {
                if ("总计".equals(bean.getGroup())) {
                    continue;
                }
                if (bean.getType() == 1) {
                    if (StringUtils.isNotBlank(bean.getHref())) {
                        bean.setGroup(bean.getHref() + SpInfoCache.getSpInfoName(toInt(bean.getGroup())) + "</a>");
                    } else {
                        bean.setGroup(bean.getGroup());
                    }
                } else if (bean.getType() == 2) {
                    if (StringUtils.isNotBlank(bean.getHref())) {
                        bean.setGroup(bean.getHref() + CpSyncCache.getCpName(toInt(bean.getGroup())) + "</a>");
                    } else {
                        bean.setGroup(CpSyncCache.getCpName(toInt(bean.getGroup())));
                    }
                } else {
                    if (StringUtils.isNotBlank(bean.getHref())) {
                        bean.setGroup(bean.getHref() + bean.getGroup() + "</a>");
                    } else {
                        bean.setGroup(bean.getGroup());
                    }
                }
            }
        }
        if (StringUtils.isNotBlank(pageView.getDate())){
            pageView.setGroup("1");
        }

        list2 = spInfoService.getAll();
        list3 = userService.getByRole(10);
        return "cplist";
    }

    private int toInt(String val) {
        if (StringUtils.isBlank(val)) {
            return 3;
        } else {
            return Integer.parseInt(val);
        }
    }

    public List<SmsBillVo> getList() {
        return list;
    }

    public void setList(List<SmsBillVo> list) {
        this.list = list;
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

    public List getList3() {
        return list3;
    }

    public void setList3(List list3) {
        this.list3 = list3;
    }
}

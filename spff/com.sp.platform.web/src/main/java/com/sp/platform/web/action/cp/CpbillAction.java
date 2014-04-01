package com.sp.platform.web.action.cp;

import com.opensymphony.xwork2.ActionSupport;
import com.sp.platform.cache.CpSyncCache;
import com.sp.platform.cache.SpInfoCache;
import com.sp.platform.common.GroupType;
import com.sp.platform.common.PageView;
import com.sp.platform.entity.User;
import com.sp.platform.service.BillLogService;
import com.sp.platform.service.SpInfoService;
import com.sp.platform.service.UserService;
import com.sp.platform.vo.BillVo;
import com.yangl.common.Constants;
import com.yangl.common.Struts2Utils;
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
@Namespace("/cp")
@Scope("prototype")
@InterceptorRefs({@InterceptorRef("loginInterceptor")})
@Results({@Result(name = "list", location = "bill_list.jsp")})
public class CpbillAction extends ActionSupport {
    @Autowired
    private BillLogService billLogService;
    @Autowired
    private UserService userService;
    @Autowired
    private SpInfoService spInfoService;

    private PageView pageView = new PageView();

    private List<BillVo> list;
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

        User user = (User) Struts2Utils.getSession().getAttribute(Constants.SESSION_KEY);
        if (user.getRole() == 10) {
            pageView.setParentId(user.getId());
        } else if (StringUtils.isBlank(pageView.getGroup2())) {
            pageView.setCpid(user.getId());
            pageView.setGroup2(GroupType.GROUP_CALLED.getType() + "");
        }

        list = billLogService.getCpBillInfo(pageView);
        if (list != null) {
            int groupType = 3;
            if (StringUtils.isNotBlank(pageView.getGroup())) {
                try {
                    groupType = Integer.parseInt(pageView.getGroup());
                } catch (Exception e) {
                }
            }

            for (BillVo bean : list) {
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

        return "list";
    }

    private int toInt(String val) {
        if (StringUtils.isBlank(val)) {
            return 3;
        } else {
            return Integer.parseInt(val);
        }
    }

    public List<BillVo> getList() {
        return list;
    }

    public void setList(List<BillVo> list) {
        this.list = list;
    }

    public PageView getPageView() {
        return pageView;
    }

    public void setPageView(PageView pageView) {
        this.pageView = pageView;
    }

    public List getList3() {
        return list3;
    }

    public void setList3(List list3) {
        this.list3 = list3;
    }
}

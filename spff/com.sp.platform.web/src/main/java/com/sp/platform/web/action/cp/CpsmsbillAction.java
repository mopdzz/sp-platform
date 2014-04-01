package com.sp.platform.web.action.cp;

import com.opensymphony.xwork2.ActionSupport;
import com.sp.platform.cache.CpSyncCache;
import com.sp.platform.cache.SpInfoCache;
import com.sp.platform.common.GroupType;
import com.sp.platform.common.PageView;
import com.sp.platform.entity.CpNum;
import com.sp.platform.entity.User;
import com.sp.platform.service.*;
import com.sp.platform.vo.BillVo;
import com.sp.platform.vo.SmsBillVo;
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
@Results({@Result(name = "list", location = "bill_sms_list.jsp"),
        @Result(name = "blackInfo", location = "cp_black_info.jsp")})
public class CpsmsbillAction extends ActionSupport {
    @Autowired
    private BillLogService billLogService;
    @Autowired
    private ProvReduceService provReduceService;
    @Autowired
    private CpNumService cpNumService;

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

        User user = (User) Struts2Utils.getSession().getAttribute(Constants.SESSION_KEY);
        if (user.getRole() == 10) {
            pageView.setParentId(user.getId());
        } else if (StringUtils.isBlank(pageView.getGroup2())) {
            pageView.setCpid(user.getId());
            pageView.setGroup2(GroupType.GROUP_CALLED.getType() + "");
        }

        list = billLogService.getCpSmsBillInfo(pageView);
        if (list != null) {
            int groupType = 3;
            if (StringUtils.isNotBlank(pageView.getGroup())) {
                try {
                    groupType = Integer.parseInt(pageView.getGroup());
                } catch (Exception e) {
                }
            }

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

        return "list";
    }

    public String blackinfo() {
        User user = (User) Struts2Utils.getSession().getAttribute(Constants.SESSION_KEY);
        if (user.getRole() == 10) {
            pageView.setParentId(user.getId());
        } else if (StringUtils.isBlank(pageView.getGroup2())) {
            pageView.setCpid(user.getId());
        }

        list2 = provReduceService.getByCpid(user.getId());

        list3 = cpNumService.getByCpId(user.getId());
        if (list3 != null && list3.size() > 0) {
            CpNum cpNum;
            for (int i = 0; i < list3.size(); i++) {
                cpNum = (CpNum) list3.get(i);
                cpNum.setBlackinfo(convert(cpNum.getBlackinfo()));
            }
        }

        return "blackInfo";
    }

    private String convert(String blackInfo) {
        try {
            String[] temp = blackInfo.split(";");
            StringBuilder builder = new StringBuilder();
            if (StringUtils.isNotBlank(temp[0]))
                builder.append("<font color='red'>放量省份</font>：").append(temp[0]).append("<br>");
            if (StringUtils.isNotBlank(temp[1]))
                builder.append("<font color='red'>屏蔽省份</font>：").append(temp[1]).append("<br>");
            if (StringUtils.isNotBlank(temp[2]))
                builder.append("<font color='red'>屏蔽地市</font>：").append(temp[2]);
            return builder.toString();
        } catch (Exception e) {
        }
        return null;
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

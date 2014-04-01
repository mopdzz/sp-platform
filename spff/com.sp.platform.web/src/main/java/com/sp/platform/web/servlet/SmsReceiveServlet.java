package com.sp.platform.web.servlet;

import com.sp.platform.cache.SpInfoCache;
import com.sp.platform.entity.MonthData;
import com.sp.platform.util.AppContextHolder;
import com.sp.platform.util.LogEnum;
import com.sp.platform.web.task.MonthTask;
import com.sp.platform.web.task.SmsTask;
import org.apache.commons.lang.StringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * User: yangl
 * Date: 13-5-26 上午12:16
 */
public class SmsReceiveServlet extends HttpServlet {
    ThreadPoolTaskExecutor executor;

    @Override
    public void init() throws ServletException {
        executor = (ThreadPoolTaskExecutor) AppContextHolder.getContext().getBean("threadPoolTaskExecutor");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String spId = StringUtils.substringAfter(req.getRequestURI(), req.getServletPath() + "/");
            spId = StringUtils.substringBefore(spId, "/");
            LogEnum.SP.info("通道[{}]同步一条话单[{}]", spId, req.getRequestURI() + "?" + req.getQueryString());

            Map<String, String> map = SpInfoCache.getSmsSyncInfo(spId);
            if (map != null) {
                SmsTask smsTask = AppContextHolder.getContext().getBean(SmsTask.class);
                smsTask.setMobile(req.getParameter(map.get("mobile")));
                smsTask.setSpnum(req.getParameter(map.get("spnum")));
                if (StringUtils.isNotBlank(req.getParameter(map.get("msg")))) {
                    smsTask.setMsg(URLDecoder.decode(req.getParameter(map.get("msg")), "GBK"));
                }
                smsTask.setLinkid(req.getParameter(map.get("linkid")));

                String status = req.getParameter(map.get("status"));
                if (StringUtils.isNotBlank(status)) {
                    if (StringUtils.isNotBlank(map.get("DELIVRD"))) {
                        if (status.equalsIgnoreCase(map.get("DELIVRD"))) {
                            smsTask.setStatus("DELIVRD");
                        } else {
                            smsTask.setStatus("0");
                        }
                    } else {
                        smsTask.setStatus(status);
                    }
                }

                LogEnum.DEFAULT.info("准备处理SP[{}]的一条SMS数据{}", spId, smsTask);
                executor.execute(smsTask);
                resp.getWriter().print(map.get("return"));
                return;
            }
        } catch (Exception e) {
            LogEnum.DEFAULT.error(e.toString());
        }
        resp.getWriter().print("error");
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String spId = StringUtils.substringAfter(req.getRequestURI(), req.getServletPath() + "/");
            spId = StringUtils.substringBefore(spId, "/");
            if ("5".equals(spId)) {
                String body = URLDecoder.decode(getRequestBody(req), "UTF-8");
                LogEnum.SP.info("通道[{}]同步一条话单[{}]", spId, body);
                Map<String, String> map = new HashMap<String, String>();
                String[] s1 = body.split("&");
                String[] s2;
                for (String str : s1) {
                    s2 = str.split("=");
                    map.put(s2[0], s2[1]);
                }
                MonthData monthData = new MonthData();
                monthData.setSpid(Integer.parseInt(spId));
                monthData.setMobileno(map.get("mobileno"));
                monthData.setRequesttime(map.get("requesttime"));
                monthData.setProviecode(map.get("proviecode"));
                monthData.setRingno(map.get("ringno"));
                monthData.setSubchannelid(map.get("subchannelid"));
                monthData.setOrdertype(map.get("ordertype"));
                monthData.setStatus(map.get("status"));
                MonthTask task = AppContextHolder.getContext().getBean(MonthTask.class);

                task.setMonthData(monthData);
                LogEnum.DEFAULT.info("准备处理SP[{}]的一条包月Month数据{}", spId, monthData);
                executor.execute(task);
                resp.getWriter().print(1);
                return;
            }
        } catch (Exception e) {
            LogEnum.DEFAULT.error(e.toString());
        }
        doGet(req, resp);
    }

    private static String getRequestBody(HttpServletRequest request) throws Exception {
        final int BUFFER_SIZE = 8 * 1024;
        byte[] buffer = new byte[BUFFER_SIZE];
        ServletInputStream sis = request.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int bLen = 0;
        while ((bLen = sis.read(buffer)) > 0) {
            baos.write(buffer, 0, bLen);
        }

        String bodyData = new String(baos.toByteArray(), "UTF-8");
        return bodyData;
    }
}

package com.sp.platform.web.servlet;

import com.sp.platform.cache.SpInfoCache;
import com.sp.platform.util.AppContextHolder;
import com.sp.platform.util.LogEnum;
import com.sp.platform.web.task.IvrTask;
import org.apache.commons.lang.StringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * User: yangl
 * Date: 13-5-26 上午12:16
 */
public class IvrReceiveServlet extends HttpServlet {
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

            Map<String, String> map = SpInfoCache.getIvrSyncInfo(spId);
            if (map != null) {
                IvrTask ivrTask = AppContextHolder.getContext().getBean(IvrTask.class);
                ivrTask.setCaller(req.getParameter(map.get("caller")));
                ivrTask.setCalled(req.getParameter(map.get("called")));
                ivrTask.setBtime(req.getParameter(map.get("btime")));
                ivrTask.setEtime(req.getParameter(map.get("etime")));
                String format = map.get("format");
                if(StringUtils.isBlank(format)){
                    format = "yyyy-MM-dd HH:mm:ss";
                }
                ivrTask.setFormat(format);
                LogEnum.DEFAULT.info("准备处理SP[{}]的一条数据{}", spId, ivrTask);
                executor.execute(ivrTask);
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
        doGet(req, resp);
    }
}

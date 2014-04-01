package com.sp.platform.web.listener;

import com.sp.platform.timer.TimerMain;
import com.sp.platform.util.AppContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 应用程序初始化
 */
public class LocalBootstrapListener implements ServletContextListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(LocalBootstrapListener.class);

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContextEvent.getServletContext());
            AppContextHolder.setContext(wac);

            // 启动定时器
            TimerMain timerMain = (TimerMain) wac.getBean(TimerMain.class);
            timerMain.start();

        } catch (Exception e) {
            LOGGER.error("------------>spring容器初始化失败", e);
        }
        LOGGER.info("------------>spring容器初始化完成");
    }

    public void contextDestroyed(ServletContextEvent sce) {

    }
}

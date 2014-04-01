package com.sp.platform.web.servlet;

import com.sp.platform.util.AppContextHolder;
import com.sp.platform.util.LogEnum;
import com.sp.platform.util.PropertyUtils;
import com.sp.platform.web.common.XDEncodeHelper;
import com.sp.platform.web.task.SmsTask;
import org.apache.commons.lang.StringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * User: yangl
 * Date: 13-8-19 下午10:09
 */
public class NfxdMOServlet extends HttpServlet {
    private static final String SPLIT_KEY = "_";
    private Map<String, String> cache = new HashMap<String, String>();

    PropertyUtils propertyUtils;
    ThreadPoolTaskExecutor executor;

    @Override
    public void init() throws ServletException {
        propertyUtils = (PropertyUtils) AppContextHolder.getContext().getBean("propertyUtils");
        executor = (ThreadPoolTaskExecutor) AppContextHolder.getContext().getBean("threadPoolTaskExecutor");
        cache.put("0000019051_286_DAK", "1066566652");
        cache.put("0000019052_280_1AU", "106651071");
        cache.put("0000019053_286_DAU", "106656661");
        cache.put("0000019054_280_8AU", "1066510726");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            Map<String, String> map = new HashMap<String, String>();
            String body = req.getQueryString();
            String[] temp1 = body.split("&");
            String[] temp2;
            for (String t : temp1) {
                temp2 = t.split("=");
                if (temp2 != null && temp2.length > 1) {
                    map.put(temp2[0], temp2[1]);
                }
            }

            String encodeStr = map.get("encodeStr");
            LogEnum.DEFAULT.info(req.getQueryString());
            if (StringUtils.isBlank(encodeStr)) {
                LogEnum.DEFAULT.info("数据异常...");
                return;
            }
            String key = propertyUtils.getProperty("nfxd.key");
            XDEncodeHelper xdHelper = new XDEncodeHelper(key);
            encodeStr = xdHelper.XDDecode(encodeStr, true);//通过密钥进行解密

            LogEnum.SP.info("通道[{}]同步一条话单[{}]", "nfxd", req.getRequestURI() + "?" + encodeStr);

            String[] datas = StringUtils.split(encodeStr, "$");

            if (datas.length == 4) {
                SmsTask smsTask = AppContextHolder.getContext().getBean(SmsTask.class);
                smsTask.setMobile(datas[2]);
                smsTask.setMsg(datas[3]);
                smsTask.setLinkid(datas[0]);
                smsTask.setStatus(datas[1]);

                LogEnum.DEFAULT.info("准备处理SP[{}]的一条SMS数据{}", "nfxd", smsTask);
                executor.execute(smsTask);
                resp.getWriter().print(1);
                return;
            } else if (datas.length == 7) {
                SmsTask smsTask = AppContextHolder.getContext().getBean(SmsTask.class);
                smsTask.setMobile(datas[0]);
                smsTask.setSpnum(cache.get(datas[1] + SPLIT_KEY + datas[4] + SPLIT_KEY + datas[2].substring(0, 3).toUpperCase()));
                smsTask.setMsg(datas[2]);
                smsTask.setLinkid(datas[3]);

                LogEnum.DEFAULT.info("准备处理SP[{}]的一条SMS数据{}", "nfxd", smsTask);
                executor.execute(smsTask);
                resp.getWriter().print(1);
                return;
            }
        } catch (Exception e) {
            LogEnum.DEFAULT.error("数据异常{}...", e);
        }
        resp.getWriter().print("error");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}

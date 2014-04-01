package com.sp.platform.util;

import org.springframework.context.ApplicationContext;

/**
 * Spring 应用上下文环境的持有类
 */
public class AppContextHolder {

	private static ApplicationContext context;

    public static void setContext(ApplicationContext context) {
		AppContextHolder.context = context;
	}

	public static ApplicationContext getContext() {
		return context;
	}
}
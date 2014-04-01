package com.sp.platform.util;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

@Repository
public class PropertyUtils {
    @Autowired
	private MessageSource messageSource;

	private static final String default_val = "";

	public String getProperty(String key) {
		return messageSource.getMessage(key, null, default_val, Locale.CHINA);
	}

	public int getInteger(String key) {
		return getInteger(key, 0);
	}

	public int getInteger(String key, int i) {
		if (StringUtils.isEmpty(key))
			return i;
		try {
			return Integer.parseInt(messageSource.getMessage(key, null,
					default_val, Locale.CHINA));
		} catch (Exception e) {
			return i;
		}
	}
}

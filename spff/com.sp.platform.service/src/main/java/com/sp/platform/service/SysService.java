package com.sp.platform.service;

import com.sp.platform.common.PageView;

import java.util.List;

/**
 * User: yangl
 * Date: 13-6-4 上午1:28
 */
public interface SysService {
    public List select(PageView pageView);
    public int exec(PageView pageView);
}

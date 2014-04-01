package com.sp.platform.service;

import com.sp.platform.entity.SpInfo;
import com.yangl.common.hibernate.PaginationSupport;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: yfyanglei
 * Date: 13-4-6
 * Time: 下午10:22
 * To change this template use File | Settings | File Templates.
 */
public interface SpInfoService extends AbstractService<SpInfo> {
    public List<SpInfo> getAll(String type);
}

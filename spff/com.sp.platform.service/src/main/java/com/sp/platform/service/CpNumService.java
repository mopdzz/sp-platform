package com.sp.platform.service;

import com.sp.platform.entity.CpNum;

import java.util.List;

/**
 * User: yangl
 * Date: 13-5-29 上午12:31
 */
public interface CpNumService extends AbstractService<CpNum> {
    public CpNum getByCalled(String called);

    public List<CpNum> getByCpId(Integer id);
}

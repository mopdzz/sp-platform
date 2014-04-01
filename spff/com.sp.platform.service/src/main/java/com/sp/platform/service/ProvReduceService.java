package com.sp.platform.service;

import com.sp.platform.entity.ProvReduce;

import java.util.List;

/**
 * User: yangl
 * Date: 13-5-26 下午4:10
 */
public interface ProvReduceService extends AbstractService<ProvReduce>{
    public List getByCalled(String called);

    public List getByCpid(Integer id);
}

package com.sp.platform.dao;

import com.sp.platform.entity.ServiceNum;
import com.yangl.common.hibernate.HibernateDaoUtil;
import org.springframework.stereotype.Repository;

/**
 * User: mopdgg
 * Date: 13-5-24 下午10:19
 */
@Repository
public class ServiceNumDao extends HibernateDaoUtil<ServiceNum, Integer> {
}

package com.sp.platform.dao;

import com.sp.platform.entity.SmsBillLog;
import com.yangl.common.hibernate.HibernateDaoUtil;
import org.springframework.stereotype.Repository;

/**
 * User: yangl
 * Date: 13-7-14 下午10:30
 */
@Repository
public class SmsBillLogDao extends HibernateDaoUtil<SmsBillLog, Integer> {
}

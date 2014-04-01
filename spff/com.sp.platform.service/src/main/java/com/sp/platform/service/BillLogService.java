package com.sp.platform.service;

import com.sp.platform.common.PageView;
import com.sp.platform.entity.BillLog;
import com.sp.platform.entity.SmsBillLog;
import com.sp.platform.vo.BillVo;
import com.sp.platform.vo.SmsBillVo;

import java.util.List;

/**
 * User: yangl
 * Date: 13-5-26 下午8:23
 */
public interface BillLogService extends AbstractService<BillLog>{
    public void saveNaHaoduan(String caller);

    List<BillVo> getBillInfo(PageView pageView);
    List<BillVo> getCpBillInfo(PageView pageView);

    List<SmsBillVo> getSmsBillInfo(PageView pageView);
    List<SmsBillVo> getCpSmsBillInfo(PageView pageView);

    public boolean isExsits(BillLog billLog);

    public boolean isExsits(String linkid);

    List<BillLog> getByCaller(String caller);
    List<SmsBillLog> getSmsByCaller(String caller);

    public void bufa(int id);
    public void bufasms(int id);

    void deleteRepeat();

    public boolean isExsits(SmsBillLog billLog);

    public void save(SmsBillLog billLog);
}

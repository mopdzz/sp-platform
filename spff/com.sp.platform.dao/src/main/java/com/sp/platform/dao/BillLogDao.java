package com.sp.platform.dao;

import com.sp.platform.common.GroupType;
import com.sp.platform.common.PageView;
import com.sp.platform.entity.BillLog;
import com.sp.platform.util.LogEnum;
import com.sp.platform.util.PropertyUtils;
import com.sp.platform.vo.BillVo;
import com.sp.platform.vo.SmsBillVo;
import com.yangl.common.hibernate.HibernateDaoUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: yangl
 * Date: 13-5-26 下午4:46
 */
@Repository
public class BillLogDao extends HibernateDaoUtil<BillLog, Integer> {
    private static final String SAVE_NA_HAODUAN = "insert into tbl_noprovice_code(caller) values(?)";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private PropertyUtils propertyUtils;

    public void saveNaHaoduan(String caller) {
        getSession().createSQLQuery(SAVE_NA_HAODUAN).setParameter(0, caller).executeUpdate();
    }

    public List<BillVo> getBillInfo(PageView pageView) {
        String[] sj;
        if(StringUtils.isNotBlank(pageView.getDate())){
            sj = chuli(pageView.getDate(), pageView.getDate());
        }else {
            sj = chuli(pageView.getBtime(), pageView.getEtime());
        }
        String kssj = sj[0];
        String jssj = sj[1];
        String group_sql = " group by sfid ";

        String str_a;
        if(pageView.getCpflag() > 0){
            str_a = "<a href='bill!cplist.action?pageView.btime=";
        }else{
            str_a = "<a href='bill!list.action?pageView.btime=";
        }
        str_a += pageView.getBtime() + "&pageView.etime=" + pageView.getEtime();

        Map<String, Object> parmMap = new HashMap<String, Object>();

        //  查询字段 --------------------------
        String col_sql = "select count(*) counts, count(distinct caller) users, cast(sum(fee)/100 as decimal(10,2)) fee," +
                "cast(sum(case type when 1 then fee else 0 end)/100 as decimal(10,2)) limitfee,cast(sum(case type when 1 then fee else 0 end)*1.0/sum(fee) as decimal(10,2)) limitrate," +
                "sum(time) time,cast(sum(fee)/100*1.0/count(distinct caller) as decimal(10,2)) arpu,";

        //  查询条件 --------------------------
        String where_sql = " from tbl_bill_log where btime>'" + kssj + "' and btime<'" + jssj + "'";

        if (pageView.getSpid() > 0) {
            where_sql += " and sfid=" + pageView.getSpid();
            parmMap.put("pageView.spid", pageView.getSpid());
        }
        if (pageView.getParentId() > 0) {
            where_sql += " and parentid=" + pageView.getParentId();
            parmMap.put("pageView.parentid", pageView.getParentId());
        }
        if (pageView.getCpid() > 0) {
            where_sql += " and cpid=" + pageView.getCpid();
            parmMap.put("pageView.cpid", pageView.getCpid());
        }
        if (StringUtils.isNotBlank(pageView.getCalled())) {
            where_sql += " and called='" + pageView.getCalled() + "'";
            parmMap.put("pageView.called", pageView.getCalled());
        }
        if (StringUtils.isNotBlank(pageView.getProvince())) {
            where_sql += " and province='" + pageView.getProvince() + "'";
            parmMap.put("pageView.province", pageView.getProvince());
        }
        if(pageView.getCpflag() > 0){
            where_sql += " and type=0";
        }

        //  分组条件 --------------------------
        int groupType = GroupType.GROUP_SP.getType();
        if (StringUtils.isNotBlank(pageView.getGroup())) {
            groupType = Integer.parseInt(pageView.getGroup());
        }
        boolean flag = false;
        boolean timeFlag = false;
        int type = 0;

        // 有二级分组（子渠道、长号码、省、市）
        if (StringUtils.isNotBlank(pageView.getGroup2())) {
            groupType = Integer.parseInt(pageView.getGroup2());
            if (GroupType.GROUP_CP.isEquals(groupType)) {
                pageView.setMessage("子渠道");
                flag = true;
                col_sql += " cpid groupcol ";
                group_sql = " group by cpid ";
                parmMap.put("pageView.group2", GroupType.GROUP_CALLED.getType());
                parmMap.put("dynamic", "pageView.cpid");
                type = 2;
            } else if (GroupType.GROUP_CALLED.isEquals(groupType)) {
                pageView.setMessage("长号码");
                flag = true;
                col_sql += " called groupcol ";
                group_sql = " group by called ";
                parmMap.put("pageView.group2", GroupType.GROUP_PROVINCE.getType());
                parmMap.put("dynamic", "pageView.called");
            } else if (GroupType.GROUP_PROVINCE.isEquals(groupType)) {
                pageView.setMessage("省");
                flag = true;
                col_sql += " province groupcol ";
                group_sql = " group by province ";
                parmMap.put("pageView.group2", GroupType.GROUP_CITY.getType());
                parmMap.put("dynamic", "pageView.province");
            } else if (GroupType.GROUP_CITY.isEquals(groupType)) {
                pageView.setMessage("市");
                col_sql += " city groupcol ";
                group_sql = " group by city ";
            } else if(GroupType.GROUP_HOUR.isEquals(groupType)){
                pageView.setMessage("小时");
                if (propertyUtils.getProperty("db.type").equals("mysql")) {
                    col_sql += " hour(btime) groupcol ";
                    group_sql = " group by hour(btime) ";
                }else{
                    col_sql += " datepart(hh, btime) groupcol ";
                    group_sql = " group by datepart(hh, btime) ";
                }
            }
        }
        // 一级大分组（通道、主渠道、日期）
        else {
            if (GroupType.GROUP_TIME.isEquals(groupType)) {
                flag = true;
                pageView.setMessage("日期");
                if (propertyUtils.getProperty("db.type").equals("mysql")) {
                    timeFlag = true;
                    col_sql += " left(btime, 10) groupcol ";
                    group_sql = " group by left(btime, 10) ";
                } else {
                    col_sql += " convert(varchar(10),btime,120) groupcol ";
                    group_sql = " group by convert(varchar(10),btime,120) ";
                }
                parmMap.put("pageView.group2", GroupType.GROUP_HOUR.getType());
                parmMap.put("dynamic", "pageView.date");
                type = 3;
            } else if (GroupType.GROUP_CP_PARENT.isEquals(groupType)) {
                pageView.setMessage("主渠道");
                flag = true;
                col_sql += " parentid groupcol ";
                group_sql = " group by parentid ";
                parmMap.put("pageView.group2", GroupType.GROUP_CP.getType());
                parmMap.put("dynamic", "pageView.parentId");
                type = 2;
            } else {
                flag = true;
                pageView.setMessage("通道");
                col_sql += " sfid groupcol ";
                group_sql = " group by sfid ";
                parmMap.put("pageView.group2", GroupType.GROUP_CALLED.getType());
                parmMap.put("dynamic", "pageView.spid");
                type = 1;
            }
        }

        String sql = col_sql + where_sql + group_sql + " order by 8";
        logger.info(sql);

        Iterator<Map<String, Object>> result2 = jdbcTemplate.queryForList(sql).iterator();
        return processResult(result2, str_a, flag, type, timeFlag, parmMap);
    }

    private List<BillVo> processResult(Iterator<Map<String, Object>> result2, String str_a, boolean flag, int type, boolean timeFlag, Map<String, Object> parmMap) {
        Map<String, Object> map = null;
        List<BillVo> list = new ArrayList<BillVo>();
        BillVo bean;


        String dynamic = toString(parmMap.get("dynamic"));
        if(StringUtils.isNotBlank(dynamic)){
            parmMap.remove(dynamic);
            parmMap.remove("dynamic");
        }
        StringBuilder str = new StringBuilder(str_a);
        for(String key : parmMap.keySet()){
            str.append("&").append(key).append("=").append(parmMap.get(key));
        }
        str_a = str.append("&").append(dynamic).append("=").toString();

        int fusers = 0;
        int fcounts = 0;
        float ftimes = 0;
        float ffees = 0;
        float fsalefees = 0;
        float flimitfees = 0;
        BigDecimal bFees;
        BigDecimal bLimitfees;


        while (result2.hasNext()) {
            map = result2.next();
            bean = new BillVo();
            bFees = (BigDecimal) map.get("fee");
            bLimitfees = (BigDecimal) map.get("limitfee");

            bean.setCounts(toString(map.get("counts")));
            bean.setUsers(toString(map.get("users")));
            bean.setFees(bFees.toString());
            bean.setLimitfee(bLimitfees.toString());
            bean.setSalefee(bFees.subtract(bLimitfees).toString());
            bean.setLimitrate(toString(map.get("limitrate")));
            bean.setTimes(toString(map.get("time")));
            bean.setArpu(toString(map.get("arpu")));

            bean.setType(type);

            String temp;
            if (timeFlag) {
                temp = new String((byte[]) map.get("groupcol"));
            } else {
                temp = toString(map.get("groupcol"));
            }

            if (flag) {
                bean.setHref(str_a + temp + "'>");
            }
            bean.setGroup(temp);
            list.add(bean);

            fusers = fusers + Integer.parseInt(bean.getUsers());
            fcounts = fcounts + Integer.parseInt(bean.getCounts());
            ftimes = ftimes + Integer.parseInt(bean.getTimes());
            ffees = ffees + bFees.floatValue();
            fsalefees = fsalefees + bFees.floatValue() - bLimitfees.floatValue();
            flimitfees = flimitfees + bLimitfees.floatValue();
        }

        bean = new BillVo();
        bean.setGroup("总计");
        bean.setCounts(fcounts + "");
        bean.setUsers(fusers + "");
        bean.setTimes(ftimes + "");
        BigDecimal bd;
        if (fusers <= 0) {
            bean.setArpu("0");
        } else {
            bd = new BigDecimal(ffees / fusers);
            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
            bean.setArpu(bd.toString());
        }
        if (flimitfees <= 0) {
            bean.setLimitrate("0");
        } else {
            bd = new BigDecimal(flimitfees / ffees);
            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
            bean.setLimitrate(bd.toString());
        }

        bean.setFees(ffees + "");
        bean.setSalefee(fsalefees + "");
        bean.setLimitfee(flimitfees + "");
        list.add(bean);
        return list;
    }

    public List<BillVo> getCpBillInfo(PageView pageView) {
        String[] sj;
        if(StringUtils.isNotBlank(pageView.getDate())){
            sj = chuli(pageView.getDate(), pageView.getDate());
        }else {
            sj = chuli(pageView.getBtime(), pageView.getEtime());
        }
        String kssj = sj[0];
        String jssj = sj[1];
        String group_sql = " group by cpid ";

        String str_a = "<a href='cpbill!list.action?pageView.btime=" + pageView.getBtime() + "&pageView.etime=" + pageView.getEtime();

        Map<String, Object> parmMap = new HashMap<String, Object>();

        //  查询字段 --------------------------
        String col_sql = "select count(*) counts, count(distinct caller) users, cast(sum(fee)/100 as decimal(10,2)) fee," +
                "cast(sum(case type when 1 then fee else 0 end)/100 as decimal(10,2)) limitfee,cast(sum(case type when 1 then fee else 0 end)*1.0/sum(fee) as decimal(10,2)) limitrate," +
                "sum(time) time,cast(sum(fee)/100*1.0/count(distinct caller) as decimal(10,2)) arpu,";

        //  查询条件 --------------------------
        String where_sql = " from tbl_bill_log where type=0 and btime>'" + kssj + "' and btime<'" + jssj + "'";

        if (pageView.getParentId() > 0) {
            where_sql += " and parentid=" + pageView.getParentId();
            parmMap.put("pageView.parentid", pageView.getParentId());
        }
        if (pageView.getCpid() > 0) {
            where_sql += " and cpid=" + pageView.getCpid();
            parmMap.put("pageView.cpid", pageView.getCpid());
        }
        if (StringUtils.isNotBlank(pageView.getCalled())) {
            where_sql += " and called='" + pageView.getCalled() + "'";
            parmMap.put("pageView.called", pageView.getCalled());
        }
        if (StringUtils.isNotBlank(pageView.getProvince())) {
            where_sql += " and province='" + pageView.getProvince() + "'";
            parmMap.put("pageView.province", pageView.getProvince());
        }

        //  分组条件 --------------------------
        int groupType = GroupType.GROUP_CP.getType();
        if (StringUtils.isNotBlank(pageView.getGroup())) {
            groupType = Integer.parseInt(pageView.getGroup());
        }
        boolean flag = false;
        boolean timeFlag = false;
        int type = 0;

        // 有二级分组（子渠道、长号码、省、市）
        if (StringUtils.isNotBlank(pageView.getGroup2())) {
            groupType = Integer.parseInt(pageView.getGroup2());
            if (GroupType.GROUP_CALLED.isEquals(groupType)) {
                pageView.setMessage("长号码");
                flag = true;
                col_sql += " called groupcol ";
                group_sql = " group by called ";
                parmMap.put("pageView.group2", GroupType.GROUP_PROVINCE.getType());
                parmMap.put("dynamic", "pageView.called");
            } else if (GroupType.GROUP_PROVINCE.isEquals(groupType)) {
                pageView.setMessage("省");
                flag = true;
                col_sql += " province groupcol ";
                group_sql = " group by province ";
                parmMap.put("pageView.group2", GroupType.GROUP_CITY.getType());
                parmMap.put("dynamic", "pageView.province");
            } else if (GroupType.GROUP_CITY.isEquals(groupType)) {
                pageView.setMessage("市");
                col_sql += " city groupcol ";
                group_sql = " group by city ";
            } else if(GroupType.GROUP_HOUR.isEquals(groupType)){
                pageView.setMessage("小时");
                if (propertyUtils.getProperty("db.type").equals("mysql")) {
                    col_sql += " hour(btime) groupcol ";
                    group_sql = " group by hour(btime) ";
                }else{
                    col_sql += " datepart(hh, btime) groupcol ";
                    group_sql = " group by datepart(hh, btime) ";
                }
            }
        }
        // 一级大分组（通道、主渠道、日期）
        else {
            if (GroupType.GROUP_CP.isEquals(groupType)) {
                pageView.setMessage("子渠道");
                flag = true;
                col_sql += " cpid groupcol ";
                group_sql = " group by cpid ";
                parmMap.put("pageView.group2", GroupType.GROUP_CALLED.getType());
                parmMap.put("dynamic", "pageView.cpid");
                type = 2;
            } else {
                flag = true;
                pageView.setMessage("日期");
                if (propertyUtils.getProperty("db.type").equals("mysql")) {
                    timeFlag = true;
                    col_sql += " left(btime, 10) groupcol ";
                    group_sql = " group by left(btime, 10) ";
                } else {
                    col_sql += " convert(varchar(10),btime,120) groupcol ";
                    group_sql = " group by convert(varchar(10),btime,120) ";
                }
                parmMap.put("pageView.group2", GroupType.GROUP_HOUR.getType());
                parmMap.put("dynamic", "pageView.date");
                type = 3;
            }
        }

        String sql = col_sql + where_sql + group_sql + " order by 8";
        LogEnum.DEFAULT.info(sql);

        Iterator<Map<String, Object>> result2 = jdbcTemplate.queryForList(sql).iterator();
        return processResult(result2, str_a, flag, type, timeFlag, parmMap);
    }

    public String[] chuli(String kssj, String jssj) {
        String str[] = new String[2];
        if (StringUtils.isEmpty(kssj) || StringUtils.isEmpty(jssj)) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String shj = format.format(new Date());
            kssj = shj;
            jssj = shj;
        }
        if (kssj.length() == 10) {
            kssj = kssj + " 00:00:00";
        }
        if (jssj.length() == 10) {
            jssj = jssj + " 23:59:59";
        }

        str[0] = kssj;
        str[1] = jssj;
        return str;
    }

    private String toString(Object obj) {
        if (obj == null) {
            return "";
        }
        return obj.toString();
    }

    public List<SmsBillVo> getSmsBillInfo(PageView pageView) {
        String[] sj;
        if(StringUtils.isNotBlank(pageView.getDate())){
            sj = chuli(pageView.getDate(), pageView.getDate());
        }else {
            sj = chuli(pageView.getBtime(), pageView.getEtime());
        }
        String kssj = sj[0];
        String jssj = sj[1];
        String group_sql = " group by sfid ";

        String str_a;
        if(pageView.getCpflag() > 0){
            str_a = "<a href='smsbill!cplist.action?pageView.btime=";
        }else{
            str_a = "<a href='smsbill!list.action?pageView.btime=";
        }
        str_a += pageView.getBtime() + "&pageView.etime=" + pageView.getEtime();

        Map<String, Object> parmMap = new HashMap<String, Object>();

        //  查询字段 --------------------------
        String col_sql = "select count(*) counts, count(distinct mobile) users, cast(sum(fee)/100 as decimal(10,2)) fee," +
                "cast(sum(case type when 1 then fee else 0 end)/100 as decimal(10,2)) limitfee," +
                "cast(sum(case type when 1 then fee else 0 end)*1.0/sum(fee) as decimal(10,2)) limitrate,";

        //  查询条件 --------------------------
        String where_sql = " from sms_bill_log where btime>'" + kssj + "' and btime<'" + jssj + "'";

        if (pageView.getSpid() > 0) {
            where_sql += " and sfid=" + pageView.getSpid();
            parmMap.put("pageView.spid", pageView.getSpid());
        }
        if (pageView.getParentId() > 0) {
            where_sql += " and parentid=" + pageView.getParentId();
            parmMap.put("pageView.parentid", pageView.getParentId());
        }
        if (pageView.getCpid() > 0) {
            where_sql += " and cpid=" + pageView.getCpid();
            parmMap.put("pageView.cpid", pageView.getCpid());
        }
        if (StringUtils.isNotBlank(pageView.getCalled())) {
            pageView.setCalled(pageView.getCalled().replace("-", "#"));

            if (propertyUtils.getProperty("db.type").equals("mysql")) {
                where_sql += " and CONCAT(spnum,'_',msg)='" + pageView.getCalled() + "'";
            }else{
                where_sql += " and spnum+'_'+msg='" + pageView.getCalled() + "'";
            }
            parmMap.put("pageView.called", pageView.getCalled());
        }
        if (StringUtils.isNotBlank(pageView.getProvince())) {
            where_sql += " and province='" + pageView.getProvince() + "'";
            parmMap.put("pageView.province", pageView.getProvince());
        }
        if(pageView.getCpflag() > 0){
            where_sql += " and type=0";
        }

        //  分组条件 --------------------------
        int groupType = GroupType.GROUP_SP.getType();
        if (StringUtils.isNotBlank(pageView.getGroup())) {
            groupType = Integer.parseInt(pageView.getGroup());
        }
        boolean flag = false;
        boolean timeFlag = false;
        int type = 0;

        // 有二级分组（子渠道、长号码、省、市）
        if (StringUtils.isNotBlank(pageView.getGroup2())) {
            groupType = Integer.parseInt(pageView.getGroup2());
            if (GroupType.GROUP_CP.isEquals(groupType)) {
                pageView.setMessage("子渠道");
                flag = true;
                col_sql += " cpid groupcol ";
                group_sql = " group by cpid ";
                parmMap.put("pageView.group2", GroupType.GROUP_CALLED.getType());
                parmMap.put("dynamic", "pageView.cpid");
                type = 2;
            } else if (GroupType.GROUP_CALLED.isEquals(groupType)) {
                pageView.setMessage("长号码");
                flag = true;
                if (propertyUtils.getProperty("db.type").equals("mysql")) {
                    col_sql += " CONCAT(spnum,'_',msg) groupcol ";
                    group_sql = " group by CONCAT(spnum,'_',msg) ";
                }else{
                    col_sql += " spnum+'_'+msg groupcol ";
                    group_sql = " group by spnum+'_'+msg ";
                }
                parmMap.put("pageView.group2", GroupType.GROUP_PROVINCE.getType());
                parmMap.put("dynamic", "pageView.called");
            } else if (GroupType.GROUP_PROVINCE.isEquals(groupType)) {
                pageView.setMessage("省");
                flag = true;
                col_sql += " province groupcol ";
                group_sql = " group by province ";
                parmMap.put("pageView.group2", GroupType.GROUP_CITY.getType());
                parmMap.put("dynamic", "pageView.province");
            } else if (GroupType.GROUP_CITY.isEquals(groupType)) {
                pageView.setMessage("市");
                col_sql += " city groupcol ";
                group_sql = " group by city ";
            } else if (GroupType.GROUP_HOUR.isEquals(groupType)){
                pageView.setMessage("小时");
                if (propertyUtils.getProperty("db.type").equals("mysql")) {
                    col_sql += " hour(btime) groupcol ";
                    group_sql = " group by hour(btime) ";
                }else{
                    col_sql += " datepart(hh, btime) groupcol ";
                    group_sql = " group by datepart(hh, btime) ";
                }
            }
        }
        // 一级大分组（通道、主渠道、日期）
        else {
            if (GroupType.GROUP_TIME.isEquals(groupType)) {
                flag = true;
                pageView.setMessage("日期");
                if (propertyUtils.getProperty("db.type").equals("mysql")) {
                    timeFlag = true;
                    col_sql += " left(btime, 10) groupcol ";
                    group_sql = " group by left(btime, 10) ";
                } else {
                    col_sql += " convert(varchar(10),btime,120) groupcol ";
                    group_sql = " group by convert(varchar(10),btime,120) ";
                }
                parmMap.put("pageView.group2", GroupType.GROUP_HOUR.getType());
                parmMap.put("dynamic", "pageView.date");
                type = 3;
            } else if (GroupType.GROUP_CP_PARENT.isEquals(groupType)) {
                pageView.setMessage("主渠道");
                flag = true;
                col_sql += " parentid groupcol ";
                group_sql = " group by parentid ";
                parmMap.put("pageView.group2", GroupType.GROUP_CP.getType());
                parmMap.put("dynamic", "pageView.parentId");
                type = 2;
            } else {
                flag = true;
                pageView.setMessage("通道");
                col_sql += " sfid groupcol ";
                group_sql = " group by sfid ";
                parmMap.put("pageView.group2", GroupType.GROUP_CALLED.getType());
                parmMap.put("dynamic", "pageView.spid");
                type = 1;
            }
        }

        String sql = col_sql + where_sql + group_sql + " order by 6";
        logger.info(sql);

        Iterator<Map<String, Object>> result2 = jdbcTemplate.queryForList(sql).iterator();
        return processSmsResult(result2, str_a, flag, type, timeFlag, parmMap);
    }

    public List<SmsBillVo> getCpSmsBillInfo(PageView pageView) {
        String[] sj;
        if(StringUtils.isNotBlank(pageView.getDate())){
            sj = chuli(pageView.getDate(), pageView.getDate());
        }else {
            sj = chuli(pageView.getBtime(), pageView.getEtime());
        }
        String kssj = sj[0];
        String jssj = sj[1];
        String group_sql = " group by cpid ";

        String str_a = "<a href='cpsmsbill!list.action?pageView.btime=" + pageView.getBtime() + "&pageView.etime=" + pageView.getEtime();

        Map<String, Object> parmMap = new HashMap<String, Object>();

        //  查询字段 --------------------------
        String col_sql = "select count(*) counts, count(distinct mobile) users, cast(sum(fee)/100 as decimal(10,2)) fee," +
                "cast(sum(case type when 1 then fee else 0 end)/100 as decimal(10,2)) limitfee," +
                "cast(sum(case type when 1 then fee else 0 end)*1.0/sum(fee) as decimal(10,2)) limitrate,";

        //  查询条件 --------------------------
        String where_sql = " from sms_bill_log where type=0 and btime>'" + kssj + "' and btime<'" + jssj + "'";

        if (pageView.getParentId() > 0) {
            where_sql += " and parentid=" + pageView.getParentId();
            parmMap.put("pageView.parentid", pageView.getParentId());
        }
        if (pageView.getCpid() > 0) {
            where_sql += " and cpid=" + pageView.getCpid();
            parmMap.put("pageView.cpid", pageView.getCpid());
        }
        if (StringUtils.isNotBlank(pageView.getCalled())) {
            if (propertyUtils.getProperty("db.type").equals("mysql")) {
                where_sql += " and CONCAT(spnum,'_',msg)='" + pageView.getCalled() + "'";
            }else{
                where_sql += " and spnum+'_'+msg='" + pageView.getCalled() + "'";
            }
            parmMap.put("pageView.called", pageView.getCalled());
        }
        if (StringUtils.isNotBlank(pageView.getProvince())) {
            where_sql += " and province='" + pageView.getProvince() + "'";
            parmMap.put("pageView.province", pageView.getProvince());
        }

        //  分组条件 --------------------------
        int groupType = GroupType.GROUP_CP.getType();
        if (StringUtils.isNotBlank(pageView.getGroup())) {
            groupType = Integer.parseInt(pageView.getGroup());
        }
        boolean flag = false;
        boolean timeFlag = false;
        int type = 0;

        // 有二级分组（子渠道、长号码、省、市）
        if (StringUtils.isNotBlank(pageView.getGroup2())) {
            groupType = Integer.parseInt(pageView.getGroup2());
            if (GroupType.GROUP_CALLED.isEquals(groupType)) {
                pageView.setMessage("长号码");
                flag = true;
                if (propertyUtils.getProperty("db.type").equals("mysql")) {
                    col_sql += " CONCAT(spnum,'_',msg) groupcol ";
                    group_sql = " group by CONCAT(spnum,'_',msg) ";
                }else{
                    col_sql += " spnum+'_'+msg groupcol ";
                    group_sql = " group by spnum+'_'+msg ";
                }
                parmMap.put("pageView.group2", GroupType.GROUP_PROVINCE.getType());
                parmMap.put("dynamic", "pageView.called");
            } else if (GroupType.GROUP_PROVINCE.isEquals(groupType)) {
                pageView.setMessage("省");
                flag = true;
                col_sql += " province groupcol ";
                group_sql = " group by province ";
                parmMap.put("pageView.group2", GroupType.GROUP_CITY.getType());
                parmMap.put("dynamic", "pageView.province");
            } else if (GroupType.GROUP_CITY.isEquals(groupType)) {
                pageView.setMessage("市");
                col_sql += " city groupcol ";
                group_sql = " group by city ";
            } else if (GroupType.GROUP_HOUR.isEquals(groupType)){
                pageView.setMessage("小时");
                if (propertyUtils.getProperty("db.type").equals("mysql")) {
                    col_sql += " hour(btime) groupcol ";
                    group_sql = " group by hour(btime) ";
                }else{
                    col_sql += " datepart(hh, btime) groupcol ";
                    group_sql = " group by datepart(hh, btime) ";
                }
            }
        }
        // 一级大分组（通道、主渠道、日期）
        else {
            if (GroupType.GROUP_CP.isEquals(groupType)) {
                pageView.setMessage("子渠道");
                flag = true;
                col_sql += " cpid groupcol ";
                group_sql = " group by cpid ";
                parmMap.put("pageView.group2", GroupType.GROUP_CALLED.getType());
                parmMap.put("dynamic", "pageView.cpid");
                type = 2;
            } else {
                flag = true;
                pageView.setMessage("日期");
                if (propertyUtils.getProperty("db.type").equals("mysql")) {
                    timeFlag = true;
                    col_sql += " left(btime, 10) groupcol ";
                    group_sql = " group by left(btime, 10) ";
                } else {
                    col_sql += " convert(varchar(10),btime,120) groupcol ";
                    group_sql = " group by convert(varchar(10),btime,120) ";
                }
                parmMap.put("pageView.group2", GroupType.GROUP_HOUR.getType());
                parmMap.put("dynamic", "pageView.date");
                type = 3;
            }
        }

        String sql = col_sql + where_sql + group_sql + " order by 6";
        LogEnum.DEFAULT.info(sql);

        Iterator<Map<String, Object>> result2 = jdbcTemplate.queryForList(sql).iterator();
        return processSmsResult(result2, str_a, flag, type, timeFlag, parmMap);
    }
    private List<SmsBillVo> processSmsResult(Iterator<Map<String, Object>> result2, String str_a, boolean flag, int type, boolean timeFlag, Map<String, Object> parmMap) {
        Map<String, Object> map = null;
        List<SmsBillVo> list = new ArrayList<SmsBillVo>();
        SmsBillVo bean;


        String dynamic = toString(parmMap.get("dynamic"));
        if(StringUtils.isNotBlank(dynamic)){
            parmMap.remove(dynamic);
            parmMap.remove("dynamic");
        }
        StringBuilder str = new StringBuilder(str_a);
        for(String key : parmMap.keySet()){
            str.append("&").append(key).append("=").append(parmMap.get(key));
        }
        str_a = str.append("&").append(dynamic).append("=").toString();

        int fusers = 0;
        int fcounts = 0;
        float ffees = 0;
        float fsalefees = 0;
        float flimitfees = 0;
        BigDecimal bFees;
        BigDecimal bLimitfees;


        while (result2.hasNext()) {
            map = result2.next();
            bean = new SmsBillVo();
            bFees = (BigDecimal) map.get("fee");
            bLimitfees = (BigDecimal) map.get("limitfee");

            bean.setCounts(toString(map.get("counts")));
            bean.setUsers(toString(map.get("users")));
            bean.setFees(bFees.toString());
            bean.setLimitfee(bLimitfees.toString());
            bean.setSalefee(bFees.subtract(bLimitfees).toString());
            bean.setLimitrate(toString(map.get("limitrate")));

            bean.setType(type);

            String temp;
            if (timeFlag) {
                temp = new String((byte[]) map.get("groupcol"));
            } else {
                temp = toString(map.get("groupcol"));
            }

            if (flag) {
                bean.setHref(str_a + temp + "'>");
                bean.setHref(bean.getHref().replace("#", "-"));
            }
            bean.setGroup(temp);
            list.add(bean);

            fusers = fusers + Integer.parseInt(bean.getUsers());
            fcounts = fcounts + Integer.parseInt(bean.getCounts());
            ffees = ffees + bFees.floatValue();
            fsalefees = fsalefees + bFees.floatValue() - bLimitfees.floatValue();
            flimitfees = flimitfees + bLimitfees.floatValue();
        }

        bean = new SmsBillVo();
        bean.setGroup("总计");
        bean.setCounts(fcounts + "");
        bean.setUsers(fusers + "");
        BigDecimal bd;
        if (flimitfees <= 0) {
            bean.setLimitrate("0");
        } else {
            bd = new BigDecimal(flimitfees / ffees);
            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
            bean.setLimitrate(bd.toString());
        }

        bean.setFees(ffees + "");
        bean.setSalefee(fsalefees + "");
        bean.setLimitfee(flimitfees + "");
        list.add(bean);
        return list;
    }
}
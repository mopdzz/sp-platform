package com.sp.platform;

import com.sp.platform.entity.SmsBillLog;
import com.sp.platform.entity.SmsBillTemp;
import com.sp.platform.service.BillLogService;
import com.sp.platform.service.BillTempService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: yangl
 * Date: 13-11-23 下午10:03
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class BillTest {
    @Autowired
    JdbcTemplate jdbcTemplate;
    int line = 1;

    @Test
    public void mobile_null() throws IOException {
        String sql = "select code,province,city from MOBILELOCATION";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        List<String> list2 = new ArrayList<String>();
        for (Map<String, Object> map : list) {
            list2.add("insert into MOBILELOCATION(code,province,city) values('"
                    + map.get("code") + "','" + map.get("province") + "','" + map.get("city") + "')");
        }

        FileUtils.writeLines(new File("e://mobilelocation.sql"), list2);
    }

    @Test
    public void update_mobile() throws Exception {
        List<String> list = FileUtils.readLines(new File("e://linkid.txt"));

        List<String> list2 = FileUtils.readLines(new File("e://sp-platform-sp.log.2013-11-20"));

        List<String> list3 = new ArrayList<String>();

        Map<String, String> map = new HashMap<String, String>();
        String temp;
        String temp2;
        String ss;
        for (String str : list2) {
            ss = str;
            if (str.indexOf("linkid") >= 0 && str.indexOf("mobile") >= 0) {
                temp = StringUtils.substring(str, str.indexOf("linkid=") + 7);
                temp = StringUtils.substring(temp, 0, temp.indexOf("&"));

                temp2 = StringUtils.substring(str, str.indexOf("mobile=") + 7);
                temp2 = StringUtils.substring(temp2, 0, temp2.indexOf("&"));
                map.put(temp, temp2);
            }
            if (str.indexOf("link_id") >= 0 && str.indexOf("mo_from") >= 0) {
                temp = StringUtils.substring(str, str.indexOf("link_id=") + 8);
                temp = StringUtils.substring(temp, 0, temp.indexOf("&"));

                temp2 = StringUtils.substring(str, str.indexOf("mo_from=") + 8);
                temp2 = StringUtils.substring(temp2, 0, temp2.indexOf("&"));
                map.put(temp, temp2);
            }
        }
        System.out.println("-----------------------");

        String string;
        for (String str : list) {
            string = map.get(str);
            if (StringUtils.isNotBlank(string)) {
                String sql = "update SMS_BILL_LOG set mobile='" + string + "' where linkid='" + str + "'";
                System.out.println(sql);
                jdbcTemplate.execute("update SMS_BILL_LOG set mobile='" + string + "' where linkid='" + str + "'");
                list3.add(str + " - " + string);
            }
        }
        FileUtils.writeLines(new File("e://linkid-mobile.txt"), list3);
    }

    @Test
    public void query_linkid_18_20() throws IOException {
        String sql = "select * from SMS_BILL_LOG where btime>'2013-11-18' and btime<'2013-11-21' and msg in('C#BL01','C#BL02','C#BL')";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        List<String> list2 = new ArrayList<String>();
        StringBuilder builder;
        for (Map<String, Object> map : list) {
            builder = new StringBuilder();
            builder.append(map.get("id")).append(",");
            builder.append(map.get("mobile")).append(",");
            builder.append(map.get("spnum")).append(",");
            builder.append(map.get("msg")).append(",");
            builder.append(map.get("linkid")).append(",");
            builder.append(map.get("status")).append(",");
            builder.append(map.get("btime")).append(",");
            builder.append(map.get("etime")).append(",");
            builder.append(map.get("province")).append(",");
            builder.append(map.get("city")).append(",");
            builder.append(map.get("fee")).append(",");
            builder.append(map.get("sfid")).append(",");
            builder.append(map.get("cpid")).append(",");
            builder.append(map.get("type")).append(",");
            builder.append(map.get("syncurl")).append(",");
            builder.append(map.get("parentid"));

            list2.add(builder.toString());
        }

        FileUtils.writeLines(new File("e://linkid.txt"), list2);
    }

    @Test
    public void save_bill() throws IOException {
        List<String> list = FileUtils.readLines(new File("e://linkid.txt"));
        for (String str : list) {
            String[] temp = str.split(",");
            StringBuilder builder = new StringBuilder();
            builder.append("insert into sms_bill_log values(");
            builder.append(temp[0]).append(",");
            builder.append("'").append(temp[1]).append("',");
            builder.append("'").append(temp[2]).append("',");
            builder.append("'").append(temp[3]).append("',");
            builder.append("'").append(temp[4]).append("',");
            builder.append("'").append(temp[5]).append("',");
            builder.append("'").append(temp[6]).append("',");
            builder.append("'").append(temp[7]).append("',");
            builder.append("'").append(temp[8]).append("',");
            builder.append("'").append(temp[9]).append("',");

            builder.append(temp[10]).append(",");
            builder.append(temp[11]).append(",");
            builder.append(temp[12]).append(",");
            builder.append(temp[13]).append(",");

            builder.append("'").append(temp[14]).append("',");
            builder.append(temp[15]).append(")");
            jdbcTemplate.execute(builder.toString());
        }
    }

    @Test
    public void init_database() throws IOException {
        List<String> list = FileUtils.readLines(new File("d://sp_ff//database.txt"));
        for (String str : list) {
            if (StringUtils.isBlank(str)) {
                continue;
            }

            jdbcTemplate.execute(str);
        }
    }

    @Test
    public void query_test() throws IOException {
        String sql = "select mobile,linkid,status,etime from sms_bill_temp where status='DELIVRD' and etime>='2013-12-18' order by etime";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        List<String> list2 = new ArrayList<String>();
        for (Map<String, Object> map : list) {
            list2.add(map.get("mobile").toString() + ","
                    + map.get("linkid").toString() + ","
                    + map.get("status").toString() + ","
                    + map.get("etime").toString());
        }
        FileUtils.writeLines(new File("e://sms_mr_log.txt"), list2);
    }

    public void test() throws Exception {
        bufa1();
        bufa2();
    }

    @Test
    public void bufa1() throws Exception {
        try{
        List<String> list = FileUtils.readLines(new File("C:\\Users\\mopdzz\\Downloads\\dingmu_233_mo_20131219.log"));

        for (String str : list) {
            if(StringUtils.isBlank(str)){
                continue;
            }
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(str);
            HttpResponse response = client.execute(get);
            System.out.println(str + "     " + response.getStatusLine().getStatusCode());
            Thread.sleep(10);
            client.getConnectionManager().shutdown();
        }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void bufa2() throws Exception {
        List<String> list = FileUtils.readLines(new File("e://bill//20_2.txt"));

        for (String str : list) {
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet("http://localhost:8080/receivesms/4/res.sms?" + str);
            HttpResponse response = client.execute(get);
            System.out.println(response.getStatusLine().getStatusCode());
            Thread.sleep(10);
            client.getConnectionManager().shutdown();
        }
    }


    @Autowired
    private BillLogService billLogService;
    @Autowired
    private BillTempService billTempService;

    @Test
    public void ttt() throws IOException {
        List<SmsBillTemp> list = billTempService.getSyncSmsBill();
        List<String> bill = new ArrayList();
        for (SmsBillTemp smsBillTemp : list) {
            if (!StringUtils.equals("DELIVRD", StringUtils.upperCase(smsBillTemp.getStatus()))) {
                smsBillTemp.setFee(0);
            }
            SmsBillLog billLog = new SmsBillLog();
            billLog.setLinkid(smsBillTemp.getLinkid());
            billLog.setMobile(smsBillTemp.getMobile());
            billLog.setSpnum(smsBillTemp.getSpnum());
            billLog.setMsg(smsBillTemp.getMsg());
            billLog.setStatus(smsBillTemp.getStatus());
            billLog.setBtime(smsBillTemp.getBtime());
            billLog.setEtime(smsBillTemp.getEtime());
            billLog.setProvince(smsBillTemp.getProvince());
            billLog.setCity(smsBillTemp.getCity());
            billLog.setFee(smsBillTemp.getFee());
            billLog.setSfid(smsBillTemp.getSfid());
            billLog.setCpid(smsBillTemp.getCpid());
            billLog.setParentid(smsBillTemp.getParentid());
            billLog.setType(smsBillTemp.getType());
            billLog.setSyncurl(smsBillTemp.getSyncurl());

            StringBuilder sql = new StringBuilder();
            sql.append("insert into sms_bill_log values(");
            sql.append("'").append(billLog.getMobile()).append("',");
            sql.append("'").append(billLog.getSpnum()).append("',");
            sql.append("'").append(billLog.getMsg()).append("',");
            sql.append("'").append(billLog.getLinkid()).append("',");
            sql.append("'").append(billLog.getStatus()).append("',");
            sql.append("'").append(billLog.getBtime()).append("',");
            sql.append("'").append(billLog.getEtime()).append("',");
            sql.append("'").append(billLog.getProvince()).append("',");
            sql.append("'").append(billLog.getCity()).append("',");
            sql.append(billLog.getFee()).append(",");
            sql.append(billLog.getSfid()).append(",");
            sql.append(billLog.getCpid()).append(",");
            sql.append(billLog.getType()).append(",");
            sql.append("'").append(billLog.getSyncurl()).append("',");
            sql.append(billLog.getParentid()).append(")");

            bill.add(sql.toString());
        }
        FileUtils.writeLines(new File("e://sms_bill_log.txt"), bill);
    }

    @Test
    public void tttt() throws IOException {
        List<String> list = FileUtils.readLines(new File("e://sms_bill_log.txt"));
        for (String sql : list) {
            jdbcTemplate.execute(sql);
        }
    }
}

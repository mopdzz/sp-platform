package com.sp.platform;

import com.sp.platform.task.HaoDuanService;
import com.sp.platform.task.SyncSmsBillService;
import com.sp.platform.util.PropertyUtils;
import com.yangl.common.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: yangl
 * Date: 13-5-27 下午10:13
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class PropertyTest {

    @Autowired
    PropertyUtils propertyUtils;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    SyncSmsBillService syncSmsBillService;
    int line = 1;

    @Test
    public void haoduan(){
        syncSmsBillService.syncBill();
    }

    @Test
    public void start() {
        String sql = "select code from mobilelocation";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        Map<String, String> haoduan = new HashMap<String, String>(list.size());
        for (Map<String, Object> map : list) {
            haoduan.put(map.get("code").toString(), "1");
        }
    }

    @Test
    public void test() throws IOException {
        System.out.println(propertyUtils.getProperty("limit.fileRootPath"));
        String sql = "select code from mobilelocation";
        List<Map<String, Object>> list2 = jdbcTemplate.queryForList(sql);
        Map<String, String> haoduan = new HashMap<String, String>(list2.size());
        for (Map<String, Object> map : list2) {
            haoduan.put(map.get("code").toString(), "1");
        }

        File file = new File("E:\\Temp\\联通号段表.csv");
        BufferedReader reader = null;
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            List<String> list = new ArrayList<String>();

            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                line++;

                String[] temp = tempString.split(",");
                if(haoduan.get(temp[0]) == null){
                    System.out.println(temp[0]);
                    list.add(tempString);
                    if (line % 1000 == 0) {
                        saveData(list);
                        list = new ArrayList<String>();
                    }
                }
            }
            if (list.size() > 0) {
                saveData(list);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    public int[] saveData(List list) {

        System.out.println(line + " saveData  start------------------------------------------");

        String sql = "insert into mobilelocation(code,province,city) values(?,?,?)";
        try {
            final List savedata = list;
            BatchPreparedStatementSetter setter = null;
            setter = new BatchPreparedStatementSetter() {
                public int getBatchSize() {
                    return savedata.size();
                }

                public void setValues(PreparedStatement ps, int index)
                        throws SQLException {
                    String body = (String) savedata.get(index);
                    String[] temp = body.split(",");

                    ps.setString(1, temp[1]);
                    ps.setString(2, temp[2]);
                    ps.setString(3, temp[3]);
                }
            };

            return jdbcTemplate.batchUpdate(sql, setter);
        } finally {
            System.out.println("saveData  end------------------------------------------");
        }
    }
}

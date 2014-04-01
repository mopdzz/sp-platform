package com.sp.platform;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * User: yangl
 * Date: 13-7-2 下午9:51
 */
public class StringTest {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String body = "";
        String[] temp = body.split(";");
        System.out.println(temp);
    }

    @Test
    public void test1() throws Exception {
        File file = new File("e://error.txt");
        List<String> list = FileUtils.readLines(file);
        int line = 0;
        List<String> bill = new ArrayList<String>(2000);
        for (String str : list) {

            int i = StringUtils.lastIndexOf(str, "receivesms");

            if (str.indexOf("ZSYL") == -1 && str.indexOf("FBBD") == -1) {
                continue;
            }

            line++;

            bill.add(str.substring(StringUtils.lastIndexOf(str, "receivesms"), str.length() - 1));
            System.out.println(str.substring(StringUtils.lastIndexOf(str, "receivesms"), str.length() - 1));
//
//            HttpClient client = new DefaultHttpClient();
//            HttpGet get = new HttpGet("http://localhost/" + str.substring(StringUtils.lastIndexOf(str, "receivesms"), str.length() - 1));
//            HttpResponse response = client.execute(get);
//            System.out.println(response.getStatusLine().getStatusCode());
//            Thread.sleep(200);
        }
        System.out.println(line);
        File file2 = new File("e://bill.txt");
        FileUtils.writeLines(file2, bill);
    }

    @Test
    public void http() throws Exception {
        String str = "res.sms?link_id=15166817&mo_from=18660292918&mo_to=10669699&content=C%23BL01&service=FBBD&motime=2013-11-20+12%3A45%3A05.013";
        str = "res.sms?link_id=15166817&mo=18660292918&status=DELIVRD&serviceid=FBBD&fee=2.0";
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet("http://218.206.72.142:8080/spff/receivesms/4/" + str);
        HttpResponse response = client.execute(get);
        System.out.println(response.getStatusLine().getStatusCode());
        Thread.sleep(200);
    }

    @Test
    public void bill() throws Exception {
        File file = new File("e://bill.txt");
        List<String> list = FileUtils.readLines(file);
        int line = 0;
        for (String str : list) {
            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet("http://218.206.72.142:8080/spff/receivesms/4/" + str.substring(str.indexOf("res.sms")));
                HttpResponse response = client.execute(get);
                System.out.println(response.getStatusLine().getStatusCode());
                Thread.sleep(10);
                client.getConnectionManager().shutdown();
            } catch (Exception e) {
            }
        }
        System.out.println(line);
    }
}

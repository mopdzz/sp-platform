package com.sp.platform.web.common;

import com.sp.platform.web.task.SmsTask;
import org.apache.commons.lang.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class XDEncodeHelper {
    public static final String TYPE = "DESede/CBC/PKCS5Padding";
    public static final String KEYTYPE = "DESede";

    IvParameterSpec ips = null;
    BASE64Encoder ebs64 = null;
    BASE64Decoder dbs64 = null;
    Cipher c1 = null;

    String strKey = null;

    public XDEncodeHelper(String sKey) {
        try {
            strKey = sKey;
            ips = new IvParameterSpec("12345678".getBytes("utf-8"));
            ebs64 = new BASE64Encoder();
            dbs64 = new BASE64Decoder();
            c1 = Cipher.getInstance(TYPE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String XDEncode(String strWhiteText) {
        byte[] byteUtf8 = null;

        try {
            byteUtf8 = strWhiteText.getBytes("utf-8");

            SecretKey deskey = new SecretKeySpec(strKey.getBytes(), KEYTYPE);
            c1.init(Cipher.ENCRYPT_MODE, deskey, ips);

            byte[] byteTemp = c1.doFinal(byteUtf8);
            String strBase64 = ebs64.encode(byteTemp);

            strBase64 = URLEncoder.encode(strBase64, "utf-8");

            return strBase64;
        } catch (Exception e) {
            e.printStackTrace();

            return "Encode_ERROR";
        }
    }

    /**
     * 默认为Web容器方式
     *
     * @param strBlackText
     * @return
     */
    public String XDDecode(String strBlackText) {
        return XDDecode(strBlackText, false);
    }

    /**
     * 若CP方使用Web容器，由于容器自身默认会做URLDecode动作，故调用该方法时不需做解码动作，isNeedDecode应传入false�?
     *
     * @param strBlackText
     * @param isNeedDecode
     * @return
     */
    public String XDDecode(String strBlackText, boolean isNeedDecode) {
        byte[] byteUtf8 = null;

        try {
            if (isNeedDecode) {
                strBlackText = URLDecoder.decode(strBlackText, "utf-8");
            }

            byteUtf8 = strBlackText.getBytes("utf-8");
            byteUtf8 = dbs64.decodeBuffer(new String(byteUtf8));
            SecretKey deskey = new SecretKeySpec(strKey.getBytes(), KEYTYPE);
            c1.init(Cipher.DECRYPT_MODE, deskey, ips);

            byte[] byteEncrypted = c1.doFinal(byteUtf8);
            String strUtf8 = new String(byteEncrypted, "utf-8");
            return strUtf8;
        } catch (Exception e) {
            e.printStackTrace();
            return "Decode_ERROR";
        }
    }

    public static void main(String[] args) {
        Map<String, String> cache = new HashMap<String, String>();
        cache.put("0000019051_286_DAK", "1066566652");
        cache.put("0000019052_280_1AU", "106651071");
        cache.put("0000019053_286_DAU", "106656661");
        cache.put("0000019054_280_8AU", "1066510726");

        String key = "tch5VEeZSAJ2VU4lUoqaYddP"; //双方约定的KEY(密钥),用来加密,解密
        XDEncodeHelper xdHelper = new XDEncodeHelper(key);
        String encodeStr = "22080538$DELIVRD$13478351349$1AU01";//MR源数据 QF2rxWhSb0FXKzFyU5iMRyyBvzNsJKeoI9%2F9bC8pAItTfo1HjGyDpQ%3D%3D
//        String encodeStr = "13478351349$0000019052$1AU01$22080538$280$1$1"; //MO源数据 sVJNjn6t571mHpHpzQeB7fvRPr2RgToaEQOKxSbTZzXMZlXvuAtAfEXwFveLV6PTIuQL7xeup5Q%3D
        System.out.println("加密前字符 " + encodeStr);
        encodeStr = xdHelper.XDEncode(encodeStr); //通过密钥进行加密
        System.out.println("加密后字符" + encodeStr);
        encodeStr = xdHelper.XDDecode(encodeStr, true);//通过密钥进行解密
        System.out.println("解密后字符" + encodeStr);

        String SPLIT_KEY = "_";
        String[] datas = StringUtils.split(encodeStr, "$");
        if (datas.length == 7) {

            SmsTask smsTask = new SmsTask();
            smsTask.setMobile(datas[0]);
            smsTask.setSpnum(cache.get(datas[1] + SPLIT_KEY + datas[4] + SPLIT_KEY + datas[2].substring(0, 3)));
            smsTask.setMsg(datas[2]);
            smsTask.setLinkid(datas[3]);

            System.out.println("准备处理SP[nfxd]的一条SMS数据" + smsTask);
        }
    }
}

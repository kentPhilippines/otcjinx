package alipay.manage.api.channel.util.shenfu;

import cn.hutool.core.util.ObjectUtil;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PayUtil {
    public static final String KEY = "dtfysghxiazAIGY2BNEWDPOIL51";
    public static final String APPID = "202006032221186176";
    public static final String URL = "http://api.shishengclub.com/gateway/bankgateway/pay";
    public static final String URL1 = "http://api.tjzfcy.com/gateway/bankgateway/pay ";
    public static final String D_PAY_URL = "http://api.shishengclub.com/gateway/pay";
    public static final String D_PAY_URL2 = "http://api.tjzfcy.com/gateway/pay";
    public static final String KEY01 = "789okjhy789okjuy7890plkju890olkju789okjhy789ok";
    public static final String APPID01 = "202008272343214049";
    public static final String APPID02 = "202010152317315510";
    public static final String APPID03 = "202010161820473425";
    public static final String NOTIFY = "/shenFu02-notfiy";
    public static Map<String, String> ipMap = new HashMap();

    static {
        ipMap.put("47.52.243.5", "47.52.243.5");
        ipMap.put("127.0.0.1", "127.0.0.1");
        ipMap.put("47.91.232.22", "47.91.232.22");
        ipMap.put("8.210.82.180", "8.210.82.180");
        ipMap.put("47.57.115.27", "47.57.115.27");
        ipMap.put("47.244.12.113", "47.244.12.113");
        ipMap.put("8.210.191.169", "8.210.191.169");
        ipMap.put("47.242.37.70", "47.242.37.70");
        ipMap.put("8.210.184.32", "8.210.184.32");
        ipMap.put("8.210.254.201", "8.210.254.201");
        ipMap.put("47.244.213.35", "47.244.213.35");
        ipMap.put("47.75.91.111", "47.75.91.111");
        ipMap.put("47.57.138.153", "47.57.138.153");
        ipMap.put("47.243.34.18", "47.243.34.18");
        ipMap.put("47.242.11.162", "47.242.11.162");
        ipMap.put("47.242.109.61", "47.242.109.61");
        ipMap.put("8.210.41.27", "8.210.41.27");
        ipMap.put("8.210.11.27", "8.210.11.27");
        ipMap.put("8.209.214.250", "8.209.214.250");
        ipMap.put("8.209.244.165", "8.209.244.165");
        ipMap.put("34.84.245.185", "34.84.245.185");
        ipMap.put("8.209.216.122", "8.209.216.122");
        ipMap.put("35.220.194.91", "35.220.194.91");
        ipMap.put("34.150.96.248", "34.150.96.248");
        ipMap.put("35.220.167.61", "35.220.167.61");
        ipMap.put("34.92.251.112", "34.92.251.112");
        ipMap.put("34.67.54.176", "34.67.54.176");
        ipMap.put("34.68.118.208", "34.68.118.2086");
        ipMap.put("34.96.175.107", "34.96.175.107");
        ipMap.put("35.220.234.97", "35.220.234.97");
        ipMap.put("118.107.24.242", "118.107.24.242");
        ipMap.put("118.107.24.250", "118.107.24.250");
        ipMap.put("8.210.197.166", "8.210.197.166");
        ipMap.put("34.96.135.66", "34.96.135.66");
        ipMap.put("52.192.227.76", "52.192.227.76");

        ipMap.put("18.162.225.144", "18.162.225.144");
        ipMap.put("45.10.209.66", "45.10.209.66");
        ipMap.put("45.10.209.67", "45.10.209.67");
        ipMap.put("45.10.209.68", "45.10.209.68");
        ipMap.put("45.10.209.69", "45.10.209.69");
        ipMap.put("45.10.209.70", "45.10.209.70");

        ipMap.put("8.218.11.37", "8.218.11.37");
        ipMap.put("47.242.206.196", "47.242.206.196");
        ipMap.put("47.242.120.208", "47.242.120.208");
        ipMap.put("47.242.166.222", "47.242.166.222");




        ipMap.put("47.243.6.52", "47.243.6.52");
        ipMap.put("47.243.170.209", "47.243.170.209");
        ipMap.put("47.243.74.155", "47.243.74.155");
        ipMap.put("47.243.242.188", "47.243.242.188");



        ipMap.put("8.210.97.36", "8.210.97.36");
        ipMap.put("47.243.168.192", "47.243.168.192");






        ipMap.put("42.157.129.22", "42.157.129.22" +
                "");








    }

    public static String createParam(Map<String, Object> map) {
        try {
            if (map == null || map.isEmpty()) {
                return null;
            }
            Object[] key = map.keySet().toArray();
            Arrays.sort(key);
            StringBuffer res = new StringBuffer(128);
            for (int i = 0; i < key.length; i++) {
                if (ObjectUtil.isNotNull(map.get(key[i]))) {
                    res.append(key[i] + "=" + map.get(key[i]) + "&");
                }
            }
            String rStr = res.substring(0, res.length() - 1);
            return rStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String md5(String a) {
        String c = "";
        MessageDigest md5;
		   	String result="";
			try {
                md5 = MessageDigest.getInstance("md5");
                md5.update(a.getBytes("utf-8"));
                byte[] temp;
                temp = md5.digest(c.getBytes("utf-8"));
                for (int i = 0; i < temp.length; i++) {
                    result += Integer.toHexString((0x000000ff & temp[i]) | 0xffffff00).substring(6);
                }
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			}
			return result;
	    }
	public static String createParam(HashMap<String, String> decodeParamMap) {
		try {
            if (decodeParamMap == null || decodeParamMap.isEmpty()) {
                return null;
            }
            Object[] key = decodeParamMap.keySet().toArray();
            Arrays.sort(key);
            StringBuffer res = new StringBuffer(128);
            for (int i = 0; i < key.length; i++) {
                if (ObjectUtil.isNotNull(decodeParamMap.get(key[i]))) {
                    res.append(key[i] + "=" + decodeParamMap.get(key[i]) + "&");
                }
            }
            String rStr = res.substring(0, res.length() - 1);
            return rStr;
        } catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

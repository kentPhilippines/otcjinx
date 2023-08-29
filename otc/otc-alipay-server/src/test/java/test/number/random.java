package test.number;

import cn.hutool.core.date.DatePattern;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class random {
    public static void main(String[] args) {
        String context = "pjSZnwI9AlLSkmBTw00ncjKJhUIld4V0KASrPerwC9zKAu/SPYEWHNAIf+7UxlDFls0UeHC/OdE0RD7J9nu9WIfNr8guQmrVqllPUFFGyzrboxF6i+GMQOjMevW/MXtbcCKJybyfNYm+zeeep1szNVT2ioT++nbtARHO1bTaU6GRYMWxP8xbmIgQiG+HVEoum7WvBcyHhEb6A5uHDhfWC2t3fmkX9AtpURcrw1/51ODSdW01gpgkARfSBUh/+L+BWVjG2WcLTmvWjYnLAidudU4nOnbjq8JfuIjC707NXdP5ZIhbrgXkWEmL6qIVx0qYSYKiMcocWBbbR/4fTAYIuA==";
        String encode = HttpUtil.encode(context, Charset.defaultCharset());
        Map map = new HashMap();
        map.put("cipherText", encode);
        map.put("userId", "huangjinjia88");
        System.out.println(map.toString());
		String post = HttpUtil.post("http://159.138.26.54:666/deal/pay", map);
		System.out.println(post);
	}

    static void time() {


        long a = 1609780976;
        SimpleDateFormat sdf = new SimpleDateFormat(DatePattern.NORM_DATETIME_PATTERN);
        java.util.Date date = new Date(a * 1000);
        String str = sdf.format(date);
        System.out.println(str);


    }


    private static boolean isNumber(String str) {
        BigDecimal a = new BigDecimal("1");
        System.out.println(a);
        double dInput = a.doubleValue();
        long longPart = (long) dInput;
        BigDecimal bigDecimal = new BigDecimal(Double.toString(dInput));
        BigDecimal bigDecimalLongPart = new BigDecimal(Double.toString(longPart));
        double dPoint = bigDecimal.subtract(bigDecimalLongPart).doubleValue();
        System.out.println("整数部分为:" + longPart + "\n" + "小数部分为: " + dPoint);
        return dPoint > 0;
    }
}

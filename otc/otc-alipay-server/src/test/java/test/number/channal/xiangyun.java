package test.number.channal;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class xiangyun {
    public static void main(String[] args) {
        String key = "fbvezylvrtqoxrjgqs";
        String appid = "1003651345";
        Map map;
        map = new HashMap<>();
        map.put("partner", appid);
        map.put("service", "10101");
        map.put("tradeNo", StrUtil.uuid());
        map.put("amount", "1000");
        map.put("notifyUrl", "http://agiduagdi.ads.notify.com");
        map.put("resultType", "json");
        map.put("extra", "extra");
        map.put("buyer", "刘德华");
        String createParam = createParam(map);
        String md5 = md5(createParam + "&" + key);
        map.put("sign", md5);
        System.out.println("【当前祥云请求参数为：" + map.toString() + "】");
        String post = HttpUtil.post("https://apialb99b.jinyum.vip/unionOrderVip", map);
        System.out.println("【祥云响应参数为：" + post + "】");
        JSONObject parseObj = JSONUtil.parseObj(post);

    }

    public static String createParam(Map<String, Object> map) {
        try {
            if (map == null || map.isEmpty())
                return null;
            Object[] key = map.keySet().toArray();
            Arrays.sort(key);
            StringBuffer res = new StringBuffer(128);
            for (int i = 0; i < key.length; i++)
                if (ObjectUtil.isNotNull(map.get(key[i])))
                    res.append(key[i] + "=" + map.get(key[i]) + "&");
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
        String result = "";
        try {
            md5 = MessageDigest.getInstance("md5");
            md5.update(a.getBytes("utf-8"));
            byte[] temp;
            temp = md5.digest(c.getBytes("utf-8"));
            for (int i = 0; i < temp.length; i++)
                result += Integer.toHexString((0x000000ff & temp[i]) | 0xffffff00).substring(6);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
        }
        return result;
    }

}

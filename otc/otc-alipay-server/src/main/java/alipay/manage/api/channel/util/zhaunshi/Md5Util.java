package alipay.manage.api.channel.util.zhaunshi;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.util.Map;
import java.util.TreeMap;


public class Md5Util {

    private static final String DEFAULT_ENCODE_TYPE = "utf-8";

    public static <T> String md5(T t, String key) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (t instanceof String) {
            return md5Encrypt(t + "&" + key, DEFAULT_ENCODE_TYPE);
        } else if (t instanceof Map) {
            return md5(StringUtil.mapToString((Map<String, String>) t), key);
        }
        return md5(voToMap(t), key);
    }

    public static String md5Encrypt(String paramSrc, String encodeType) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = paramSrc.getBytes(encodeType);
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static <T> Map<String, String> voToMap(T t) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Map<String, String> treeMap = new TreeMap<String, String>();
        Class poClass = t.getClass();
        Field[] fields = poClass.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            Method getMethod = poClass.getMethod(new StringBuilder().append("get").append(fieldName.substring(0, 1).toUpperCase()).append(fieldName.substring(1)).toString());
            String fieldValue = getMethod.invoke(t) != null ? getMethod.invoke(t).toString() : null;
            if (!StringUtil.ifNull(fieldValue) && !"sign".equals(fieldName)) {
                fieldName = fieldName.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
                treeMap.put(fieldName, fieldValue);
            }
        }
        return treeMap;
    }

}
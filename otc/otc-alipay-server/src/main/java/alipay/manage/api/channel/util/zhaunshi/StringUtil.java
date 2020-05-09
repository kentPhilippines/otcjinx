package alipay.manage.api.channel.util.zhaunshi;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
public class StringUtil {
    public static String mapToString(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        for (String key : map.keySet()) {
            String value = map.get(key);
            if (!ifNull(value)) {
                sb.append(key + "=" + value + "&");
            }
        }
        return sb.toString().substring(0, sb.toString().length() - 1);
    }
    public static String convertToHashMapToQueryString(
            Map<String, Object> params) throws Exception {
        StringBuilder sb = new StringBuilder();
        Iterator<?> iter = params.entrySet().iterator();
        while (iter.hasNext()) {
            if (sb.length() > 0) {
                sb.append('&');
            }
            Map.Entry<?, ?> entry = (Map.Entry<?, ?>) iter.next();
            sb.append(entry.getKey()).append("=").append(entry.getValue());
        }
        return sb.toString();
    }
    public static boolean ifNull(String object) {
        if (object == null || object.trim().length() == 0) {
            return true;
        }
        return false;
    }

}

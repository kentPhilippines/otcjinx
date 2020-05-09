package alipay.manage.api.channel.util.qiangui;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * @author lh
 * @version 1.0
 * @className: util
 * @TODO:
 * @create 2020-03-02 21:31
 */
public class Util {
    public static String creatSign(Map map, String key){
        ArrayList list = new ArrayList<>(map.keySet());
        StringBuilder stb=new StringBuilder();
        Collections.sort(list);
        for (Object str : list) {
            stb.append(map.get(str));
        }
        stb.append(key);
        try {
            return MD5.md5(stb.toString());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}

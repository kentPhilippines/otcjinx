import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.http.HttpUtil;

import java.util.HashMap;
import java.util.Map;

public class file {


    public static void main(String[] args) {
        String url = "http://127.0.0.1:9010/notfiy-api-pay/test-notify";


        ThreadUtil.execute(() -> {
            Map map = new HashMap();
            map.put("orderId", "C1598973014033222573");
            HttpUtil.post(url, map);
        });
        ThreadUtil.execute(() -> {
            Map map1 = new HashMap();
            map1.put("orderId", "C1598976699433211041");
            // HttpUtil.post(url,map1);
        });
        ThreadUtil.execute(() -> {
            Map map1 = new HashMap();
            map1.put("orderId", "C1598972774098722533");
            //  HttpUtil.post(url,map1);
        });


      /*
        ThreadUtil.execute(()->{
        Map map2 = new HashMap();
        map2.put("orderId","C1598976492366920157");
        HttpUtil.post(url,map2);
        Map map3 = new HashMap();
        map3.put("orderId", "C1598976382556933055");
            HttpUtil.post(url,map3);
        });
        ThreadUtil.execute(()->{
        Map map4 = new HashMap();
        map4.put("orderId", "C1598976122196246776");
            HttpUtil.post(url, map4);
        });
          ThreadUtil.execute(()->{
              Map map5 = new HashMap();
              map5.put("orderId", "C1598976107432998589");
              HttpUtil.post(url, map5);
          });
*/
    }

}


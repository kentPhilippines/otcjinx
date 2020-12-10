package test.number;

import cn.hutool.core.text.UnicodeUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.http.HttpUtil;

public class IP {
    public static void main(String[] args) {

        for (int a = 0; a <= 100; a++) {
            ThreadUtil.execute(IP::run);

        }


        //https://sp0.baidu.com/8aQDcjqpAAV3otqbppnN2DJv/
        // api.php?query=27.192.0.0&co=&resource_id=5809&t=1607390613198&ie=utf8&oe=gbk&
        // cb=op_aladdin_callback&format=json&tn=baidu&cb=jQuery110206407212602169667_1607389234299&_=1607389234305


    }

    private static void run() {
        String ip = "27.192.0.0";
        String strUrl = "https://sp0.baidu.com/8aQDcjqpAAV3otqbppnN2DJv/api.php?query=";
        String centerUrl = "&co=&resource_id=5809&t=";
        String subUrl = "&ie=utf8&oe=gbk&cb=op_aladdin_callback&format=json&tn=baidu&cb=jQuery110206407212602169667_1607389234299";
        String timeUrl = "&_=";
        String url = strUrl + ip + centerUrl + System.currentTimeMillis() + subUrl + timeUrl + System.currentTimeMillis();
        String s = HttpUtil.get(url);
        System.out.println(UnicodeUtil.toString(s));
    }
}
package test.number;


import alipay.manage.api.channel.deal.jiabao.RSAUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import otc.util.RSAUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.PrivateKey;
import java.util.List;
import java.util.Map;

public class rsa {


    public static void main(String[] args) throws IOException {


        List<String> strings = RSAUtils.genKeyPair();




        for(String s  : strings
        ){
            System.out.println(s);
        }

    }
}

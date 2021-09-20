package test.number;

import alipay.manage.api.channel.amount.recharge.usdt.USDTOrder;
import alipay.manage.bean.DealOrder;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import otc.bean.alipay.OrderDealStatus;
import otc.result.Result;

import javax.crypto.spec.PSource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class USDTTest {
    public static void main(String[] args)  {

        SimpleDateFormat sdf = new SimpleDateFormat(DatePattern.NORM_DATETIME_PATTERN);
        java.util.Date date = new Date(Long.valueOf("1631888946000")*1000 );
        String str = sdf.format(date);


        System.out.println(str);
        DateTime parse = DateUtil.parse(str);

        System.out.println(parse.getTime());




    }









    }

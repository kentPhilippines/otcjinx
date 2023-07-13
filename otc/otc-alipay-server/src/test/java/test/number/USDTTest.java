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
import org.apache.tomcat.util.descriptor.web.JspConfigDescriptorImpl;
import otc.bean.alipay.OrderDealStatus;
import otc.result.Result;

import javax.crypto.spec.PSource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class USDTTest {
    public static void main(String[] args)  {
        BigDecimal a = new BigDecimal("1000.00");


        BigDecimal divide = a.multiply(new BigDecimal(100));

        System.out.println(divide.toBigInteger());


    }









    }

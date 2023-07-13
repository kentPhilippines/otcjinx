package test.number;

import alipay.manage.api.channel.amount.recharge.usdt.USDTOrder;
import alipay.manage.api.channel.amount.recharge.usdt.UsdtPayOut;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.math.BigDecimal;
import java.sql.SQLException;

public class Qrcode {
    public static void main(String[] args) throws SQLException {
        //   String s = HttpUtil.get("https://api.etherscan.io/api?module=account&action=tokentx&address=0x28250971cF8bB17eDB2fD31e72C7fD352ae0eFCB&page=1&offset=5&sort=desc&apikey=JYNM1VJSXN8JE6JCY5M9JGKBDB7KPJDC5M");
        String ethusdtOrderList ="";// UsdtPayOut.findETHUSDTOrderList("0x28250971cF8bB17eDB2fD31e72C7fD352ae0eFCB");
        System.out.println(ethusdtOrderList);
        JSONObject jsonObject = JSONUtil.parseObj(ethusdtOrderList);
        String status = jsonObject.getStr("status");
        if ("1".equals(status)) {
            String resultjson = jsonObject.getStr("result");
            JSONArray result = JSONUtil.parseArray(resultjson);
            for (Object obj : result) {
                USDTOrder usdt = new USDTOrder();
                JSONObject jsonObject1 = JSONUtil.parseObj(obj);
                String to = jsonObject1.getStr("to");
                String timeStamp = jsonObject1.getStr("timeStamp");
                String hash = jsonObject1.getStr("hash");
                String blockHash = jsonObject1.getStr("blockHash");
                String from = jsonObject1.getStr("from");
                String contractAddress = jsonObject1.getStr("contractAddress");
                String value = jsonObject1.getStr("value");
                String key = from + to + value;

                //     3，通过出款账户匹配，确认当前出款数据
                String gasPrice = jsonObject1.getStr("gasPrice");
                String gasUsed = jsonObject1.getStr("gasUsed");
                BigDecimal price = new BigDecimal(gasPrice).divide(new BigDecimal("1000000000"), BigDecimal.ROUND_HALF_EVEN);
                price = price.divide(new BigDecimal("1000000000"));
                System.out.println(price);
                BigDecimal used = new BigDecimal(gasUsed);
                BigDecimal max = price.multiply(used);

                System.out.println(max.toString());
                //1,000,000,000


            }
        }
        if ("1".equals(status)) {
            String resultjson = jsonObject.getStr("result");
            JSONArray result = JSONUtil.parseArray(resultjson);

            //   BigDecimal price = new BigDecimal(gasPrice).divide(new BigDecimal("1000000000"),BigDecimal.ROUND_HALF_EVEN);

        }


    }
}

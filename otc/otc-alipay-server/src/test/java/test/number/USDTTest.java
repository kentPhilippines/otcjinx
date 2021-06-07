package test.number;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

public class USDTTest {
    public static void main(String[] args) {


        //大宗交易出售价格
        String url = "https://otc-api-hk.eiijo.cn/v1/data/trade-market?coinId=2&currency=1&tradeType=buy&currPage=1&payMethod=0&acceptOrder=-1&country=&blockType=block&online=1&range=0&amount=";

        String s = HttpUtil.get(url);
        JSONObject jsonObject = JSONUtil.parseObj(s);
        String code = jsonObject.getStr("code");
        if ("200".contains(code)) {
            String date1 = jsonObject.getStr("data");
            JSONArray date = JSONUtil.parseArray(date1);
            Object[] objects = date.stream().toArray();
            Object object = objects[0];
            JSONObject jsonObject1 = JSONUtil.parseObj(object);
            String price = jsonObject1.getStr("price");
            System.out.printf(price);
            System.out.printf("-");
        }

        /**
         * 大宗买入价格
         */
// https://otc-api-hk.eiijo.cn/v1/data/trade-market?coinId=2&currency=1&tradeType=sell&currPage=1&payMethod=0&acceptOrder=-1&country=&blockType=block&online=1&range=0&amount=

        String urlsell = "https://otc-api-hk.eiijo.cn/v1/data/trade-market?coinId=2&currency=1&tradeType=sell&currPage=1&payMethod=0&acceptOrder=-1&country=&blockType=block&online=1&range=0&amount=";
        String sell = HttpUtil.get(urlsell);
        JSONObject jsonObjectsell = JSONUtil.parseObj(sell);
        String codesell = jsonObjectsell.getStr("code");
        if ("200".contains(codesell)) {
            String date1sell = jsonObjectsell.getStr("data");
            JSONArray datesell = JSONUtil.parseArray(date1sell);
            Object[] objectssell = datesell.stream().toArray();
            Object objectsell = objectssell[0];
            JSONObject jsonObject1sell = JSONUtil.parseObj(objectsell);
            String pricesell = jsonObject1sell.getStr("price");
            System.out.printf(pricesell);
        }
    }
}

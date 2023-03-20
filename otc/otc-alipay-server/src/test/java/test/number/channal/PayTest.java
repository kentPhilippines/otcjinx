package test.number.channal;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;

public class PayTest {


    public static void main(String[] strings){
        pay();
        //query();
        //doNotify();
        //bankCardBalanceQuery();
        //paymentBalanceQuery();
        //merchantSettleableBalance();
        //collectionOrderCashier();
        //collectionOrder();
        //collectionOrderQuery();
    }

    //出款下单接口
    private static void  pay(){
        String key="ANPGNDE0SVRZFOLVBIUIPB4HRYP9SKHJVGROS1AXZKRNBAXOIOWT2BMQZEL04LR25XNHOLEEWFGIA9OETJMI0ERGZASYQZBFV77NU8ZNGNWXSUOG5YMUFKBYKREDEAQN";
        JSONObject paramMap = new JSONObject();
        paramMap.put("mchId", 1320);
        paramMap.put("mchOrderNo", "C6S1S9S179");
        paramMap.put("amount", 1000);
      //  paramMap.put("atmFlag", "10001000");
        paramMap.put("remark", "remark");
        paramMap.put("trueName", "小于");
        paramMap.put("notifyUrl", "http://pay06.skyecdn.com/recharge_notify");
        paramMap.put("cardNo", "6215340300002586130");
        paramMap.put("bankType", "ICBC");
        String reqSign = PayDigestUtil.getSign(paramMap, key);
        paramMap.put("sign", reqSign);
        String reqData = "params=" + paramMap.toJSONString();
        System.out.println("签名::::"+reqSign);
        System.out.println("请求支付中心下单接口,请求数据:" + reqData);
        String url =  "http://payapi.0528.yidas.cc:6080/api/obpay/transfer";
        System.out.println("url::::"+url+reqData);
        String result = HttpClientUtils.doPost(url , paramMap);
        System.out.println("请求支付中心下单接口,响应数据:" + result);
    }

    //出款订单查询接口
    private static void  query(){
        String key="ANPGNDE0SVRZFOLVBIUIPB4HRYP9SKHJVGROS1AXZKRNBAXOIOWT2BMQZEL04LR25XNHOLEEWFGIA9OETJMI0ERGZASYQZBFV77NU8ZNGNWXSUOG5YMUFKBYKREDEAQN";
        JSONObject paramMap = new JSONObject();
        paramMap.put("mchId", 1320);
        paramMap.put("mchOrderNo", "C6S1S9S179");
        String reqSign = PayDigestUtil.getSign(paramMap, key);
        paramMap.put("sign", reqSign);
        String reqData = "params=" + paramMap.toJSONString();
        System.out.println("签名::::"+reqSign);
        System.out.println("请求支付中心下单接口,请求数据:" + reqData);
        String url =  "http://payapi.0528.yidas.cc:6080/api/obpay/getinterorderV2";
        System.out.println("url::::"+url+reqData);
        String result = HttpClientUtils.doPost(url , paramMap);
        System.out.println("请求支付中心下单接口,响应数据:" + result);
    }

    //出款订单状态回调
    private static void  doNotify(){
        String key="ANPGNDE0SVRZFOLVBIUIPB4HRYP9SKHJVGROS1AXZKRNBAXOIOWT2BMQZEL04LR25XNHOLEEWFGIA9OETJMI0ERGZASYQZBFV77NU8ZNGNWXSUOG5YMUFKBYKREDEAQN";
        JSONObject request = new JSONObject();
        request.put("mchId",1320);
        request.put("mchOrderNo","15490860000013");
        request.put("amount",100);
        request.put("orderNo","G01202209140359343620003");
        request.put("status",1);
        request.put("backType","2");
        request.put("tradeTime", DateUtil.date2Str(new Date(),"yyyyMMddHHmmss"));
        request.put("sign","0A6C1F56657DDAF70061DB6A68B3D212");
        String reqSign = PayDigestUtil.getSign(request, key);
        System.out.println(reqSign);
    }


    //出款订单反查接口
    private static void check(){
        String key="ANPGNDE0SVRZFOLVBIUIPB4HRYP9SKHJVGROS1AXZKRNBAXOIOWT2BMQZEL04LR25XNHOLEEWFGIA9OETJMI0ERGZASYQZBFV77NU8ZNGNWXSUOG5YMUFKBYKREDEAQN";
        JSONObject request = new JSONObject();
        request.put("order_id","15490860000013");
        String reqSign = PayDigestUtil.getSign(request, key);
        request.put("sign", reqSign);
        String reqData = "params=" + request.toJSONString();
        System.out.println("签名::::"+reqSign);
        System.out.println("请求订单反查接口,请求数据:" + reqData);
        String url =  "http://127.0.0.1";
        System.out.println("url::::"+url+reqData);
        String result = HttpClientUtils.doPost(url , request);
        System.out.println("请求订单反查接口,响应数据:" + result);
    }


    //银行卡余额查询接口
    private static void bankCardBalanceQuery(){
        String key="ANPGNDE0SVRZFOLVBIUIPB4HRYP9SKHJVGROS1AXZKRNBAXOIOWT2BMQZEL04LR25XNHOLEEWFGIA9OETJMI0ERGZASYQZBFV77NU8ZNGNWXSUOG5YMUFKBYKREDEAQN";
        JSONObject paramMap = new JSONObject();
        paramMap.put("mchId", 1320);
       // paramMap.put("identityNo", "CM88810810");
        String reqSign = PayDigestUtil.getSign(paramMap, key);
        paramMap.put("sign", reqSign);
        String reqData = "params=" + paramMap.toJSONString();
        System.out.println("签名::::"+reqSign);
        System.out.println("请求支付中心下单接口,请求数据:" + reqData);
        String url =  "http://payapi.0528.yidas.cc:6080/api/obpay/getbalance";
        System.out.println("url::::"+url+reqData);
        String result = HttpClientUtils.doPost(url , paramMap);
        System.out.println("请求支付中心下单接口,响应数据:" + result);
    }

    //商户代付余额查询接口
    private static void paymentBalanceQuery(){
        String key="ANPGNDE0SVRZFOLVBIUIPB4HRYP9SKHJVGROS1AXZKRNBAXOIOWT2BMQZEL04LR25XNHOLEEWFGIA9OETJMI0ERGZASYQZBFV77NU8ZNGNWXSUOG5YMUFKBYKREDEAQN";
        JSONObject paramMap = new JSONObject();
        paramMap.put("mchId", 1320);
        paramMap.put("reqTime", DateUtil.date2Str(new Date(),"yyyyMMddHHmmss"));
        String reqSign = PayDigestUtil.getSign(paramMap, key);
        paramMap.put("sign", reqSign);
        String reqData = "params=" + paramMap.toJSONString();
        System.out.println("签名::::"+reqSign);
        System.out.println("请求支付中心下单接口,请求数据:" + reqData);
        String url =  "http://payapi.0528.yidas.cc:6080/api/agentpay/query_balance";
        System.out.println("url::::"+url+reqData);
        String result = HttpClientUtils.doPost(url , paramMap);
        System.out.println("请求支付中心下单接口,响应数据:" + result);

    }

    //商户可结算余额查询接口
    private static void merchantSettleableBalance(){
        String key="ANPGNDE0SVRZFOLVBIUIPB4HRYP9SKHJVGROS1AXZKRNBAXOIOWT2BMQZEL04LR25XNHOLEEWFGIA9OETJMI0ERGZASYQZBFV77NU8ZNGNWXSUOG5YMUFKBYKREDEAQN";
        JSONObject paramMap = new JSONObject();
        paramMap.put("mchId", 1320);
        paramMap.put("timeStamp", DateUtil.date2Str(new Date(),"yyyyMMddHHmmss"));
        String reqSign = PayDigestUtil.getSign(paramMap, key);
        paramMap.put("sign", reqSign);
        String reqData = "params=" + paramMap.toJSONString();
        System.out.println("签名::::"+reqSign);
        System.out.println("请求支付中心下单接口,请求数据:" + reqData);
        String url =  "http://payapi.0528.yidas.cc:6080/api/obpay/getsetbalance";
        System.out.println("url::::"+url+reqData);
        String result = HttpClientUtils.doPost(url , paramMap);
        System.out.println("请求支付中心下单接口,响应数据:" + result);
    }

    //收款下单收银台模式
    private static void collectionOrderCashier(){
        String key="ANPGNDE0SVRZFOLVBIUIPB4HRYP9SKHJVGROS1AXZKRNBAXOIOWT2BMQZEL04LR25XNHOLEEWFGIA9OETJMI0ERGZASYQZBFV77NU8ZNGNWXSUOG5YMUFKBYKREDEAQN";
        JSONObject paramMap = new JSONObject();
        paramMap.put("mchId", 1320);
        paramMap.put("mchOrderNo", "C6S1S9S179");
        paramMap.put("amount", 100);
        paramMap.put("realName", "张三");
        paramMap.put("type", 1);
        paramMap.put("notifyUrl", "127.0.0.1");
        String reqSign = PayDigestUtil.getSign(paramMap, key);
        paramMap.put("sign", reqSign);
        String reqData = "params=" + paramMap.toJSONString();
        System.out.println("签名::::"+reqSign);
        System.out.println("请求支付中心下单接口,请求数据:" + reqData);
        String url =  "http://payapi.0528.yidas.cc:6080/api/obpay/get_cashierurl";
        System.out.println("url::::"+url+reqData);
        String result = HttpClientUtils.doPost(url , paramMap);
        System.out.println("请求支付中心下单接口,响应数据:" + result);

    }

    //收款订单回调
    private static void collectionOrder(){
        String key="ANPGNDE0SVRZFOLVBIUIPB4HRYP9SKHJVGROS1AXZKRNBAXOIOWT2BMQZEL04LR25XNHOLEEWFGIA9OETJMI0ERGZASYQZBFV77NU8ZNGNWXSUOG5YMUFKBYKREDEAQN";
        JSONObject paramMap = new JSONObject();
        paramMap.put("mchId", 1320);
        paramMap.put("mchOrderNo", "C6S1S9S179");
        paramMap.put("amount", 100);
        paramMap.put("amountOriginal", 100);
        paramMap.put("type", 1);
        paramMap.put("orderNo", "C6S1S9S179");
        paramMap.put("status", 1);
        paramMap.put("backType", 2);
        paramMap.put("tradeTime", DateUtil.date2Str(new Date(),"yyyyMMddHHmmss"));
        String reqSign = PayDigestUtil.getSign(paramMap, key);
        paramMap.put("sign", reqSign);
        String reqData = "params=" + paramMap.toJSONString();
        System.out.println("签名::::"+reqSign);
    }

    //收款订单查询
    private static void collectionOrderQuery(){
        String key="ANPGNDE0SVRZFOLVBIUIPB4HRYP9SKHJVGROS1AXZKRNBAXOIOWT2BMQZEL04LR25XNHOLEEWFGIA9OETJMI0ERGZASYQZBFV77NU8ZNGNWXSUOG5YMUFKBYKREDEAQN";
        JSONObject paramMap = new JSONObject();
        paramMap.put("mchId", 1320);
        paramMap.put("mchOrderNo", "C6S1S9S179");
        String reqSign = PayDigestUtil.getSign(paramMap, key);
        paramMap.put("sign", reqSign);
        String reqData = "params=" + paramMap.toJSONString();
        System.out.println("签名::::"+reqSign);
        System.out.println("请求支付中心下单接口,请求数据:" + reqData);
        String url =  "http://payapi.0528.yidas.cc:6080/api/obpay/getremitorder";
        System.out.println("url::::"+url+reqData);
        String result = HttpClientUtils.doPost(url , paramMap);
        System.out.println("请求支付中心下单接口,响应数据:" + result);
    }
}

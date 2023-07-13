package test.number.channal;

import alipay.manage.api.channel.util.yifu.YiFuUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ax安心 {


    /**
     * 商户号	mchId	是	int(20)	1000	商户号
     * 商户单号	mchOrderNo	是	String(32)	1549086000000	商户生成的订单号，
      * 汇款金额	amount	是	int(15)	100	订单金额,单位分
     * 汇款人姓名	trueName	是	String(32)	张三	汇款人姓名
     * 汇款类型	type	是	int(1)	1	1银行汇款 2支付宝  3微信  4云闪付  5数字人民币
     * 付款客户IP	clientIp	是	String(15)	192.168.1.1	客户付款的访问IP
     * 结果回调URL	notifyUrl	否	String(128)	http:/******L
       签名           sign        是 String(32)	6 AE41084CB66F7ABB1AADF2F49BCEC17 签名值，详见签名算法
     * @param args
     */
    public static void main(String[] args) {
    /*    Map<String,Object> map = new HashMap<String,Object>();
        map.put("mchId", "1331");
      //  map.put("realName", "");
        map.put("mchOrderNo",System.currentTimeMillis());
        map.put("amount",  "100000");
        map.put("notifyUrl", "www.2121212dddd.com");
        map.put("clientIp", "112.22.12.4");
        map.put("time", System.currentTimeMillis());
        map.put("type", "1");
        String createParam = YiFuUtil.createParam(map);
        System.out.println("【易付签名前参数："+createParam+"】");
        String md5 = YiFuUtil.md5(createParam + "key="+"9FCFCNQG6GXY0Z1ETWCT6XP31CK25YZNJBJROEPUCSLNU6VHDAYMZFXA1OQJUFP0CBOYM3ITT661P2AGBCHK3SY0LDFHWHVQ5BOBZ0THAXOICLPJNLWTX8XM8E5PPUMZ");
        map.put("sign", md5);
        System.out.println("【请求Yifu参数："+map.toString()+"】");
        String post = HttpUtil.post("http://payapi.avg2kv9s.anpay.cc:6080/api/obpay/get_cashierurl", map);
        JSONObject jsonObject = JSONUtil.parseObj(post);
        System.out.println("【返回Yifu参数："+jsonObject.toString()+"】");

        String key="9FCFCNQG6GXY0Z1ETWCT6XP31CK25YZNJBJROEPUCSLNU6VHDAYMZFXA1OQJUFP0CBOYM3ITT661P2AGBCHK3SY0LDFHWHVQ5BOBZ0THAXOICLPJNLWTX8XM8E5PPUMZ";
        JSONObject paramMap = new JSONObject();
        paramMap.put("mchId", 1331);
        paramMap.put("mchOrderNo",  "I-4422111Y-2023022613205210-vip");
        paramMap.put("amount", 62000);
        paramMap.put("realName", "张三");
        paramMap.put("type", 1);
        paramMap.put("notifyUrl", "127.0.0.1");
   //     paramMap.put("time", System.currentTimeMillis());
        String reqSign = PayDigestUtil.getSign(paramMap, key);
        paramMap.put("sign", reqSign);
        String reqData = "params=" + paramMap.toJSONString();
        System.out.println("签名::::"+reqSign);
        System.out.println("请求支付中心下单接口,请求数据:" + reqData);
     //   String post = HttpUtil.post("http://payapi.avg2kv9s.anpay.cc:6080/api/obpay/get_cashierurl", paramMap);
        String url =  "http://payapi.avg2kv9s.anpay.cc:6080/api/obpay/get_cashierurl";
        System.out.println("url::::"+url+reqData);
        String result = HttpClientUtils.doPost(url , paramMap);
        System.out.println("请求支付中心下单接口,响应数据:" + result);

*/

        String key="9FCFCNQG6GXY0Z1ETWCT6XP31CK25YZNJBJROEPUCSLNU6VHDAYMZFXA1OQJUFP0CBOYM3ITT661P2AGBCHK3SY0LDFHWHVQ5BOBZ0THAXOICLPJNLWTX8XM8E5PPUMZ";


        JSONObject paramMap = new JSONObject();
        paramMap.put("mchId", 1331);
        paramMap.put("mchOrderNo",System.currentTimeMillis());
        paramMap.put("amount", 1000);
      //  paramMap.put("atmFlag", "10001000");
        paramMap.put("remark", "remark");
        paramMap.put("trueName", "小于");
        paramMap.put("notifyUrl", "http://pay06.skyecdn.com/recharge_notify");
        paramMap.put("cardNo", "6211111111186130");
        paramMap.put("bankType", "ICBC");
        String reqSign = PayDigestUtil.getSign(paramMap, key);
        paramMap.put("sign", reqSign);
        String reqData = "params=" + paramMap.toJSONString();
        System.out.println("签名::::"+reqSign);
        System.out.println("请求支付中心下单接口,请求数据:" + reqData);
        String url =  "http://payapi.avg2kv9s.anpay.cc:6080/api/obpay/transfer";
        System.out.println("url::::"+url+reqData);
        String result = HttpClientUtils.doPost(url , paramMap);
        System.out.println("请求支付中心下单接口,响应数据:" + result);
//请求支付中心下单接口,响应数据:{"sign":"5AAB5C9B4EA6173D5BBAC8C16CD26019","retCode":"SUCCESS","retMsg":"下单成功"}



    }
}

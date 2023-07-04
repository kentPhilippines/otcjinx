package test.number.channal;

import alipay.manage.api.channel.util.yifu.YiFuUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.HashMap;
import java.util.Map;

public class yifunew印度支付 {
/**
 * 文档: https://www.showdoc.com.cn/2142755221566946/9626143288754762
 *     文档密码: 222222
 *     接口网关: http://mon-api.yifukeji.top
 *     支付(代收): /api/order
 *     支付查询: /api/order/status
 *     账户查询: /api/account
 *     代付(提现): /api/payment
 *     代付查询: /api/payment/status
 *     appid: 66be8f18cd36a5b36d145687
 *     secret: FFa2Ad7e335735a4C7beE23F336Ac7a4e7B2e937
 *     回调ip: 16.162.234.93
 */


    /**
     * app_id	开发者ID	String	32	是	2001101011
     * product_id	产品渠道ID	int	10	是	123
     * out_trade_no	客户端订单号	String	64	是	123456789
     * notify_url	异步通知url	String	500	是	https://abc.def.gh/notify
     * amount	订单金额(单位:元)	Price		是	100.00
     * time	当前请求秒级时间戳	int	-	是	1500001234
     * desc	订单描述,原样返回	String	200	否	abc
     * ext	扩展字段 不参与签名！	JSON	1000	否	{“bankName”:”bankName”}
     * sign	接口签名,详见签名生成	String	32	是	5cdf1c679b4a2ec216bc339d6edb1f45
     * @param args
     */
    public static void main(String[] args) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("app_id", "66be8f18cd36a5b36d145687");
        map.put("product_id", "11");
        map.put("out_trade_no", StrUtil.uuid().toString());
        map.put("amount",  "1000.00");
        map.put("notify_url", "www.2121212dddd.com");
        map.put("time", System.currentTimeMillis());
        String createParam = YiFuUtil.createParam(map);
        System.out.println("【易付签名前参数："+createParam+"】");
        String md5 = YiFuUtil.md5(createParam + "key="+"FFa2Ad7e335735a4C7beE23F336Ac7a4e7B2e937");
        String sign = md5.toLowerCase();
        map.put("sign", sign);
        System.out.println("【请求Yifu参数："+map.toString()+"】");
        String post = HttpUtil.post("http://mon-api.yifukeji.top/api/order", map);
        JSONObject jsonObject = JSONUtil.parseObj(post);
        System.out.println("【返回Yifu参数："+jsonObject.toString()+"】");
    }
}

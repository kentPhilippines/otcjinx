package alipay.manage.api.channel.deal.baidafeili;

import alipay.manage.api.channel.util.shenfu.PayUtil;
import alipay.manage.api.config.NotfiyChannel;
import cn.hutool.http.HttpUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import otc.common.PayApiConstant;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * code        √ 状态标识 0000-成功
 * msg         √ 信息说明
 * sign        √ 签名
 * merchId     √ 商户号
 * money       √ 订单金额
 * tradeNo     √ 平台交易号
 * orderId     √ 订单号
 * time        √ 请求时间，时间格式：YYYYMMDDhhmmss 14 位数 字，精确到秒。如：20221024145654
 * signType    √ 签名类型 MD5
 * payType     √ 支付类型
 * curType     √ 币种简称
 * floatMoney  √ 浮动金额（实际到账金额以该金额为准）
 */
@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
@RestController
public class NewBaidaFeiliNotify extends NotfiyChannel {
    private static final Log log = LogFactory.get();
    @PostMapping("/NewBaidaFeiliPay-notify")
    public String notify(HttpServletRequest req, HttpServletResponse res) throws IOException {
        Map respo = new HashMap();
        log.info("【收到 百达斐丽 支付成功回调】");
        Map<String, Object> requestBody = getRequestBody(req);
        String clientIP = HttpUtil.getClientIP(req);
        log.info("【当前回调ip为：" + clientIP + "】");
        log.info("【转换为map为：" + requestBody.toString() + "】");
        String sign = (String) requestBody.get("sign");
        String mchOrderNo = (String) requestBody.get("mchOrderNo");
        requestBody.remove("sign");
        String createParam =  SanYangUtil.createParam(requestBody);
        String channelKey = super.getChannelKey(mchOrderNo);
        String md5 = PayUtil.md5(createParam +"&key=" + channelKey).toUpperCase();
        if (md5.equals(sign)) {
            log.info("【当前支付成功回调签名参数：" + sign + "，当前我方验证签名结果：" + md5 + "】");
            log.info("【签名成功】");
        } else {
            log.info("【当前支付成功回调签名参数：" + sign + "，当前我方验证签名结果：" + md5 + "】");
            log.info("【签名失败】");
            return " sgin is error ";
        }
        if("2".equals(requestBody.get("status"))){
            Result dealpayNotfiy = dealpayNotfiy(mchOrderNo, clientIP);
            if (dealpayNotfiy.isSuccess()) {
                return "success";
            }
        }
        return " is errer ";
    }


    public static void main(String[] args) {
        //{income=80000,
        // payOrderId=P01202312181706368180002,
        // amount=80000,
        // mchId=1517,
        // productId=8051,
        // mchOrderNo=1736674312049528832,
        // paySuccTime=1702891740000, sign=590DBA9BC75F6D7B56935745041275DA,
        // channelOrderNo=ALD202312181706360001,
        // backType=2,
        // reqTime=20231218172900, param1=, param2=,
        // appId=3831a5be95114f4fbe7bd683a74b11ea, status=2}
        Map  map = new HashMap();
        map .put("income",80000);
        map .put("payOrderId","P01202312181706368180002");
        map .put("amount","80000");
        map .put("mchId","1517");
        map .put("productId","8051");
        map .put("mchOrderNo","1736674312049528832");
        map .put("paySuccTime","1702891740000");
        map .put("channelOrderNo","ALD202312181706360001");
        map .put("backType","2");
        map .put("reqTime","20231218172900");
        map .put("appId","3831a5be95114f4fbe7bd683a74b11ea");
        map .put("param1","");
        map .put("status","2");
        String createParam =  SanYangUtil.createParam(map);
        String md5 = PayUtil.md5(createParam +"&key=" + "A21CYS85ZNC3ZW6HA5QXL3OOH6JEVBUNMBLSVC4WOVAX65TDRXJWONFH0SF8SY28IEN9ZWKVZYH431RUH5X6QKLLLOUZH0AK38B7TXI050FBSP0VOQGJVKUGRGAPMKLR").toUpperCase();


        System.out.printf(md5);



    }
}

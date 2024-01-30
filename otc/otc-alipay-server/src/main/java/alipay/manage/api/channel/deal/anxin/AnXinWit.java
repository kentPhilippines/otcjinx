package alipay.manage.api.channel.deal.anxin;

import alipay.manage.api.channel.deal.anxin.util.HttpClientUtils;
import alipay.manage.api.channel.deal.anxin.util.PayDigestUtil;
import alipay.manage.api.config.ChannelInfo;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.UserInfo;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.bean.dealpay.Withdraw;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.math.BigDecimal;


@Component("AnXinWit")
public class AnXinWit extends PayOrderService {
    private static final Log log = LogFactory.get();
    private static final String WIT_RESULT = "SUCCESS";
    @Autowired
    private UserInfoService userInfoServiceImpl;



    @Override
    public Result withdraw(Withdraw wit) {
        log.info("【进入安心手动代付代付】");
        try {
            log.info("【本地订单创建成功，开始请求远程三方代付接口】");
            String channel = "";
            if (StrUtil.isNotBlank(wit.getChennelId())) {//支持运营手动推送出款
                channel = wit.getChennelId();
            } else {
                channel = wit.getWitChannel();
            }
            if (isNumber(wit.getAmount().toString())) {
                Result result = withdrawEr(wit, "代付订单不支持小数提交", wit.getRetain2());
                if (result.isSuccess()) {
                    return Result.buildFailMessage("代付订单不支持小数提交");
                }
            }
            UserInfo userInfo = userInfoServiceImpl.findDealUrl(wit.getUserId());
            if (StrUtil.isBlank(userInfo.getDealUrl())) {
                withdrawEr(wit,"url未设置",wit.getRetain2());
                return Result.buildFailMessage("请联系运营为您的商户好设置交易url");
            }
            String createDpay = createDpay(userInfo.getDealUrl()+
                            PayApiConstant.Notfiy.NOTFIY_API_WAI + "/anxin-wit-noyfit",
                    wit, getChannelInfo(channel, wit.getWitType()));
            if (StrUtil.isNotBlank(createDpay) && createDpay.equals(WIT_RESULT)) {
                return Result.buildSuccessMessage("代付成功等待处理");
            } else {
                return Result.buildFailMessage(createDpay);
            }
        } catch (Exception e) {
            log.error("【错误信息打印】" + e.getMessage());
            return Result.buildFailMessage("代付失败");
        }
    }

    private String createDpay(String notify, Withdraw wit, ChannelInfo channelInfo) {
        try {
            String key= channelInfo.getChannelPassword();
            JSONObject paramMap = new JSONObject();
            paramMap.put("mchId", channelInfo.getChannelAppId());
            paramMap.put("mchOrderNo",wit.getOrderId());
            paramMap.put("amount",  wit.getAmount().multiply(new BigDecimal(100)).toBigInteger());
            paramMap.put("remark", "remark");
            paramMap.put("trueName", wit.getAccname());
            paramMap.put("notifyUrl", notify);
            paramMap.put("cardNo", wit.getBankNo());
            paramMap.put("bankType",wit.getBankcode() );
            String reqSign = PayDigestUtil.getSign(paramMap, key);
            paramMap.put("sign", reqSign);
            String reqData = "params=" + paramMap.toJSONString();
            log.info("签名::::"+reqSign);
            log.info("请求支付中心下单接口,请求数据:" + reqData);
            String url =  channelInfo.getWitUrl();
            log.info("url::::"+url+reqData);
            String result = HttpClientUtils.doPost(url , paramMap);
            log.info("请求支付中心下单接口,响应数据:" + result);
            cn.hutool.json.JSONObject jsonObject = JSONUtil.parseObj(result);
            String retCode = jsonObject.getStr("retCode");
            if("SUCCESS".equals(retCode)){
                witComment(wit.getOrderId());
                return WIT_RESULT;
            }else {
                String message = jsonObject.getStr("retMsg");
                withdrawErMsg(wit, message, wit.getRetain2());
            }
        }catch ( Exception e ){
            log.error("请求代付异常", e);
            withdrawErMsg(wit, "代付异常,网络异常", wit.getRetain2());
            return "";
        }
        return "";
    }


    /**
     *
     *
     *
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
     */
}

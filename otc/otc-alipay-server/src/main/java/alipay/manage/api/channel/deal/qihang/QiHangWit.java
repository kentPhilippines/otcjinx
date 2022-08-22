package alipay.manage.api.channel.deal.qihang;

import alipay.manage.api.channel.util.ChannelInfo;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.UserInfo;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.netflix.ribbon.proxy.annotation.Http;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import otc.bean.dealpay.Withdraw;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component("QiHangWit")
public class QiHangWit extends PayOrderService {

    private static final String WIT_RESULT = "SUCCESS";
    @Autowired
    private UserInfoService userInfoServiceImpl;

    @Override
    public Result withdraw(Withdraw wit) {
        log.info("【进入起航代付】");
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
                            PayApiConstant.Notfiy.NOTFIY_API_WAI + "/qihang-wit-notify",
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

    private String createDpay(String noyifyUrl, Withdraw wit, ChannelInfo channelInfo) {
        String signKey = "82c8a4bc8b81ed850ba1fecea0452529516cacd5d0d88fe9e4088c275732";
        String key = "bce8ae69874a73a537369836c5fc93af3d7db440";
        key = channelInfo.getChannelPassword();
        signKey = channelInfo.getDealurl();// 请求充值接口作为 api key 的参数
        String order_no = wit.getOrderId();
        String bank_code = wit.getBankcode();
        String amount = wit.getAmount().toString();
        String card_no = wit.getBankNo();
        String ip = "8.210.34.242";
        String sign = "";
        String mid = channelInfo.getChannelAppId();
        String time = System.currentTimeMillis() / 1000 + "";
        String notify_url = noyifyUrl;
        String holder_name = wit.getAccname();
        Map headers = new HashMap();
        headers.put("Authorization", "api-key " + key.trim());
        headers.put("Content-Type", "application/json");
        Map<String, String> body = new HashMap();
        body.put("order_no", order_no);
        body.put("bank_code", bank_code);
        body.put("amount", amount);
        body.put("card_no", card_no);
        body.put("ip", ip);
        body.put("time", time);
        body.put("mid", mid);
        body.put("notify_url", notify_url);
        body.put("holder_name", holder_name);
        String param1 = createParam(body);
        log.info("加密前串为：" + param1);
        String sign1 = sign( signKey.trim(),param1);
        body.put("sign", sign1);
        JSONObject param = JSONUtil.parseFromMap(body);
        log.info("起航请求参数为：" + param);
        HttpResponse execute = HttpRequest.post(channelInfo.getWitUrl())
                .addHeaders(headers).body(param).execute();
        String ruselt = execute.body().toString();
        log.info("起航响应参数为："+ruselt);
     //   String xml  = "<SuccessResponse><code>200</code><message>OK</message><data>success</data></SuccessResponse>";
        try {
            JSONObject jsonObject = JSONUtil.xmlToJson(ruselt);
            Object successResponse = jsonObject.get("SuccessResponse");
            log.info(successResponse.toString());
            JSONObject jsonObject1 = JSONUtil.parseObj(successResponse);
            Object data = jsonObject1.get("data");
            if(data.toString().equals("success")){
                return WIT_RESULT;
            }
        }catch (RuntimeException s){

        }
        return "";
    }

    public static String sign(String secretKey, String data) {

        byte[] bytes = HmacUtils.hmacSha1(secretKey, data);
        return Base64.getEncoder().encodeToString(bytes);
    }
    public static String createParam(Map<String, String> map) {
        try {
            if (map == null || map.isEmpty())
                return null;
            Object[] key = map.keySet().toArray();
            Arrays.sort(key);
            StringBuffer res = new StringBuffer(128);
            for (int i = 0; i < key.length; i++)
                if (ObjectUtil.isNotNull(map.get(key[i])))
                    res.append(key[i] + "=" + map.get(key[i]) + "&");
            String rStr = res.substring(0, res.length() - 1);
            return rStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

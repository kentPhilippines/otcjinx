package alipay.manage.api.channel.deal.rongsheng;

import alipay.manage.api.channel.util.qiangui.MD5;
import alipay.manage.api.config.ChannelInfo;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.UserInfo;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import otc.bean.dealpay.Withdraw;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.util.Base64;
import java.util.Map;

@Component("RongshengWit")
public class RongshengWit extends PayOrderService {

    private static final String WIT_RESULT = "SUCCESS";
    @Autowired
    private UserInfoService userInfoServiceImpl;

    @Override
    public Result withdraw(Withdraw wit) {
        log.info("【进入Rongsheng代付】");
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
                            PayApiConstant.Notfiy.NOTFIY_API_WAI + "/rongsheng-wit-notify",
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

    private String createDpay(String notifyUrl, Withdraw wit, ChannelInfo channelInfo) {
        String key ="EKGOTRRKBC43D5WQ";
        key = channelInfo.getChannelPassword();
//        String payurl = "http://195.130.202.43/cash";
        String payurl = channelInfo.getDealurl();
        String money = wit.getAmount().intValue()+".00"; // 金额
        String user = channelInfo.getChannelAppId();// 商户id
        String osn = wit.getOrderId();// 20位订单号 时间戳+6位随机字符串组成
//        String notifyUrl = "http://34.92.251.112:9010/notfiy-api-pay/newXiangyunWit-notify";
//        String notifyUrl = noyifyUrl;
        String name = wit.getAccname();
        String card = wit.getBankNo();
        String bankCode = wit.getBankcode();

        MultiValueMap<String, String> param= new LinkedMultiValueMap<String, String>();
        param.add("user", user);
        param.add("bank", bankCode);
        param.add("card", card);
        param.add("name", name);
        param.add("money", money);
        param.add("osn", osn);

        String originalStr = createParam(param.toSingleValueMap())+"&key="+key;
        log.info(originalStr);
        String sign = MD5.MD5Encode(originalStr);
        param.add("sign", sign);
        param.add("nurl", notifyUrl);
        log.info("rongsheng request map:{}",JSONUtil.toJsonStr(param.toSingleValueMap()));
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(param, headers);
        String ruselt = restTemplate.postForObject(payurl, request, String.class);
        log.info("rongsheng响应参数为："+ruselt);
     //   {"result":true,"mark_sell":"XG55201029363b42170472ab","msg":"代付处理中","status":0}
        try {

            JSONObject jsonObject1 = JSONUtil.parseObj(ruselt);
            Object data = jsonObject1.get("result");
            if(data.toString().equalsIgnoreCase("true")){
                return WIT_RESULT;
            }
        }catch (RuntimeException s){
            s.printStackTrace();
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
//            Arrays.sort(key);
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

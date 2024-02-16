package alipay.manage.api.channel.deal.youkuaitong;

import alipay.manage.api.config.ChannelInfo;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.UserInfo;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import otc.bean.dealpay.Withdraw;
import otc.common.PayApiConstant;
import otc.result.Result;
@Component("YouKuaiTongDPayNew")
public class YouKuaiTongDPayNew extends PayOrderService {
    private static final String WIT_RESULT = "SUCCESS";
    private static final Log log = LogFactory.get();
    /**
     *              参数名称                参数含义                是否必填            参与签名            参数说明
     *              mchId               商户编号                       是                   是            平台分配商户号
     *              banknumber          银行卡号/收款账号               是                   是           银行卡的卡号或者其他类型代付方式的准确账号
     *              bankfullname        开户人/收款人姓名               是                   否           银行卡或其他代付方式的准确姓名
     *              bankname            银行名称                        否                   否           目前只有越南代付需传此参数，其他货币不用传此参数，详见越南银行代码列表
     *              tkmoney             代付金额                        是                   是           代付金额
     *              orderid             下发订单号                   是                       是           上送唯一的代付订单号
     *              notifyurl           代付通知地址                  否                   否               不上送此参数没有异步回调
     *              walletUrl           钱包地址否否暂时无用，此接口为预留的虚拟币代付接口
     *              type                代付类型                    是                       否           1 银行卡代付,2印度upi代付
     *              mail                邮箱                      是                       否               代付印度upi账户时 请填写真实收款人准确电子邮箱
     *              ifsc                ifsc                        是                   否               印度银行卡代付时必填，其他情况请填数字1
     *              branchName          收款人地址                   是                   否                  代付印度upi时 请填写真实收款人地址
     *              mobile              电话                      是                   否                       代付印度upi时 请填写真实收款人准确电话号码(去除+91的纯数字电话号码)						sign签名是否
     */




    private  final  static  String  privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDXleYMd6O20JyF4V45pNxJ4+zHb8+LKqm/ujVWor/JvFshLwpXOn6Qj2kYHa3qslMCQVySzr4DKLMM2SSxN6HnDYURvGBj2N8pYW5xNtsOzAp8mHykQV0w59lVV+a9Hv3NUnqf8vyqLMWvcaWoxp52QyK+Eh0B9KEkozwQcaI4m7ceCu6rJTNJ21tZK3/S3KA13mT0wJ87So2MK3itY213mm4mn/1n5AA8OzEoyo+kG84K8hX3RDLeIJI4g5cIarUlh/qcBxH3Tqx/wn4iLyhKabUdDJHxUobUsLgrD97yVj8fD/cL8Kiuf2bhPnqWh1gQbd32ZBrBaYwHNSleg3apAgMBAAECggEANOsnsBHCDwMAGODsNywfosee/ImWoWuUhWqY4y5J86QXnnqkiCrknGmHe5L1ePHS7G/M4IB9JdtcsB+xIQst48Bu1J6v7nJyvP6clI/Jj8VIIbNwjQU6ceHHeGp+ShgMZtUDIrYLjV42HE3CQz6V8SOjqJJbqno4//xNpx+tmUU0hX5I5nL+x3yFS/QyiOyhG4wyg7aCeYbaP1OPmsF5suLNd1QPOAMihP+oKXPdD079VJtw/Xtgfm72d6eggWfQygQgZEAGfuVEW2IKfyGyBRyy0nPd4h9nb1x0AQEex7Gpz7novMV+uLHRKX64IDEIlBpMP2QjRf7OyskUYHd3gQKBgQDrQLGujDJDO+WfNuOK+0/yKztC185vuyugy8muP0VcznNW4L53orIWws4Ae+UwY8VbFcDbAuMBbuSmAzDFIWt1l4gZzayqfi+8t9L5XuzbQWsmPhkPqyFK5NK52jOUFtSuU27D3KqaNce65YrWoKlmUOxkEk/6xJ2SwWPw3Dem8QKBgQDqmS2sUiDcQCFbb6T+pItTomIcHXiGKiapj91oXcXlsEo00nvoBNfdeTNwBnDQTEGPdxj8DMToRHOOyVE53VEKV7xmBxP5p9CTH1eMACgjAr6uths1KF2r7H7LETRkj4c0890G91mbOSJ0FxMkq4RGjH04vwKP00LmAgs5BoH7OQKBgFug6RPpOzKAw+ENifb6B5t1RzGzyI4wQr/wX+9kaWnKZ5YxFz6VufOvkHkTyKLeWWIuyN0E9NH8FUar+3TsnWRBzxrtxxDo8UL5/kxusqZ7hnZwvWYi62a2VXVaDbjiY5g6muviqPhDdjXPbhJGpXRalJgtocU0i4M7m+eWfn6hAoGBANTzyxPbUVzdEeqQQ7Oq5ZY7ltdyoQ8YgBP3NcIhLRy8k/+y+Sq6CsFN3bVZA1rxfamfMJzLcopsaIE6mXLvRTsgPTJYRnefL6P9FVlOYyC0wyaQw83TIISJnubybR7DcrZMj1xdd4eBq5a5w3TMBLyNlrXGHas02Es1m75d++txAoGANcMTEFaZpW/qnKaNCltimrkev2tDVkJO1dLo+/jcqRnDu5iBm/i+xoOUYssFSjo9R+WpcaoEQCcD0dRSM7o5zqP+ykkgYlCp1VxMUi71tsV+cfYgSFA2jzO47Zb2QjdOPgqe/y4XxwCXfp1PLJD0n+SyrwJgZWADbrW97QkptJk=";
    private  final  static  String  publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAriqb6/wy43hyWljbW5Bs0ZyrLHfmSijMNz0hX+rdsHHG59izADHn0wphFvFVWvcU2sEHxUaEcpjIaFqL06/imdSZFWAWu677je45rYvE+0mu6YkyDjdkvmYLfKyAfYbcWjG6RRIH2dU6ULZd9tYWM29qAQnsW9y5BSNNQlsQm8e0Q6zPT7LL091f1HnJmFCVvC0pkK/a2Tz9tldjmOVQSM5+auz/lgVFh5Xb8fOnK+ofumPpTfA3Ej9xGZJVAD522VTg9jgycDm5Ta10T0WFtme275cLdj7R5/DsRBYj4c7478nDXfqvjQVVhs5YQ0vHArZ7w3nb/OK2cfJg+FOvqwIDAQAB";
    @Autowired
    private UserInfoService userInfoServiceImpl;
    @Override
    public Result withdraw(Withdraw wit) {
        log.info("【进入优快通动代付代付】");
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
                            PayApiConstant.Notfiy.NOTFIY_API_WAI + "/youkuaitongnew-wit-noyfit",
                    wit, getChannelInfo(channel, wit.getWitType()));
            if (StrUtil.isNotBlank(createDpay) && createDpay.equals(WIT_RESULT)) {
                return Result.buildSuccessMessage("代付成功等待处理");
            } else {
                return Result.buildFailMessage(createDpay);
            }
        } catch (Exception e) {

            log.error("【错误信息打印" +
                    "】" + e.getMessage());
            return Result.buildFailMessage("代付失败");
        }
    }

    private String createDpay(String notify, Withdraw wit, ChannelInfo channelInfo) {
        try {
            String payurl = channelInfo.getWitUrl()+"/api/agentPay/draw";
            String tkmoney = wit.getAmount().toString(); // 金额
            String mchId = channelInfo.getChannelAppId();// 商户id
            String orderid = wit.getOrderId();// 20位订单号 时间戳+6位随机字符串组成
            String notifyurl = notify;// 通知地址
            String bankfullname = wit.getAccname();
            String banknumber = wit.getBankNo();
            String type = "1";
            String mail = "jinxin.server@gmail.com";
            String ifsc = "1";
            String branchName = "山西省临汾市";
            String mobile = "13542536722";
            StringBuffer s = new StringBuffer();
            s.append("mchId=" + mchId + "&");
            s.append("orderid=" + orderid + "&");
            s.append("tkmoney=" + tkmoney+ "&");
            s.append("banknumber=" + banknumber);
            String sign = RSASignature.sign(s.toString(),privateKey);
            MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
            param.add("sign", sign);
            param.add("tkmoney", tkmoney);
            param.add("mchId", mchId);
            param.add("orderid", orderid);
            param.add("notifyurl", notifyurl);
            param.add("bankfullname", bankfullname);
            param.add("banknumber", banknumber);
            param.add("mail", mail);
            param.add("type", type);
            param.add("ifsc", ifsc);
            param.add("branchName", branchName);
            param.add("mobile", mobile);
            RestTemplate restTemplate = new RestTemplate();
            log.info("请求前参数："+param);
            log.info("payurl："+payurl);
            String rString = restTemplate.postForObject(payurl, param, String.class);
            //{msg=提交成功。 code=0}
            //{msg=请联系客服添加ip白名单。 code=500}
            log.info("优快通代付响应："+rString);
            JSONObject parseObj = JSONUtil.parseObj(rString);
            String object = parseObj.getStr("code");
        if("0".equals(object)){
            witComment(wit.getOrderId());
            return WIT_RESULT;
        }else{
            withdrawErMsg(wit, parseObj.getStr("msg"), wit.getRetain2());
        }
        return "";
    } catch (Throwable e) {
        log.error("请求汇通付代付异常", e);
        withdrawErMsg(wit, "代付异常,网络异常", wit.getRetain2());
        //   withdrawEr(wit, "代付异常", wit.getRetain2());
        return "";
    }

    }
}

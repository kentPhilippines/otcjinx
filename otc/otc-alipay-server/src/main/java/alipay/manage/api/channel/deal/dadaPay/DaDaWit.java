package alipay.manage.api.channel.deal.dadaPay;

import alipay.manage.api.channel.util.ChannelInfo;
import alipay.manage.api.channel.util.shenfu.PayUtil;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.UserInfo;
import alipay.manage.mapper.WithdrawMapper;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.bean.dealpay.Withdraw;
import otc.common.PayApiConstant;
import otc.result.Result;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Component("DaDaWit")
public class DaDaWit extends PayOrderService {
    private static final Log log = LogFactory.get();
    private static final String WIT_RESULT = "SUCCESS";
    @Autowired
    private UserInfoService userInfoServiceImpl;

    @Resource
    private WithdrawMapper withdrawDao;
    private static String getNowDateStr() {
        return DateUtil.format(new Date(), DatePattern.PURE_DATETIME_FORMAT);
    }

    @Override
    public Result withdraw(Withdraw wit) {
        log.info("【进入dada代付】");
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
                            PayApiConstant.Notfiy.NOTFIY_API_WAI + "/dadaWit-noyfit",
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
    static SimpleDateFormat d = new SimpleDateFormat("yyyyMMddHHmmss");
    public DaDaWit(UserInfoService userInfoServiceImpl) {
        this.userInfoServiceImpl = userInfoServiceImpl;
    }
    private String createDpay(String s, Withdraw wit, ChannelInfo channelInfo) {
        String amount = wit.getAmount().multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_UP).toString();
        String key  = "RDBECCGUN2OQ6NMS8WUDOZGAXME9UYPA0RJPTR89COWMISQ2X2JWHRE4YHX2FMCUGOPVF5KGOIO7ZKWXOCNDIFKIWPDK9LGI4OWJCOTDWFTWU1KVZ3KLTDONCD364FC3";
        String mchId  =  channelInfo.getChannelAppId();
        String accountName  = wit.getAccname();
        String orderId = wit.getOrderId();
        String accountNo = wit.getBankNo();
        String notifyUrl = s;
        String remark = "代付";
        Map<String,Object> map   = new HashMap<>();
        map.put("mchId",mchId);
        map.put("mchOrderNo",orderId);
        map.put("amount",amount);
        map.put("accountName",accountName);
        map.put("accountNo",accountNo);
        map.put("notifyUrl",notifyUrl);
        map.put("remark",remark);
        map.put("reqTime",d.format(new Date()));
        String createParam = PayUtil.createParam(map);
        log.info(createParam);
        String md5 = PayUtil.md5(createParam + "&key="+key).toUpperCase(Locale.ROOT);
        log.info(md5);
        map.put("sign",md5);
        log.info(map.toString());
        String post = "";
        try {
            post = HttpUtil.post(channelInfo.getWitUrl()+"/api/agentpay/apply", map);
            log.info(post);
            //{"fee":216,"sign":"4B8ECDF51F8D84DBC9713BBEF1550A5F","resCode":"SUCCESS","retCode":"SUCCESS","agentpayOrderId":"G01202202172105416190012","retMsg":"","status":0}
            JSONObject jsonObject = JSONUtil.parseObj(post);
            String resCode = jsonObject.getStr("resCode");
            String agentpayOrderId = jsonObject.getStr("agentpayOrderId");
            if("SUCCESS".equals(resCode)){
                ThreadUtil.execute(()->{
                    withdrawDao.updateEthFee(orderId,agentpayOrderId);
                });
                witComment(wit.getOrderId());
                return WIT_RESULT;
            }else{
                withdrawErMsg(wit, "三方异常", wit.getRetain2());
            }
        }catch (Exception e ){
            log.error("请求代付异常");
            withdrawErMsg(wit, "代付异常,网络异常", wit.getRetain2());
        }
        return "";

    }


}

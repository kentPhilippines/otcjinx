package alipay.manage.api.channel.deal.huachenghui.v1;

import alipay.manage.api.channel.deal.huachenghui.HuachenghuiPayUtil;
import alipay.manage.api.channel.util.qiangui.MD5;
import alipay.manage.api.config.ChannelInfo;
import alipay.manage.api.config.NotifyApi;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.DealOrder;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.DealWit;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.util.ResultDeal;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;

@Component("HuachenghuiPayv1")
@Slf4j
public class HuachenghuiPay extends PayOrderService {
    final String NOTIFY_TYPE = NotifyApi.NOTIFY_TYPE_FORM;//文档定义的上游回调传参类型
    final String NOTIFY = NOTIFY_TYPE + (NOTIFY_MARK + StrUtil.split(this.getClass().getName(), MARK)[StrUtil.split(this.getClass().getName(), MARK).length - 1]).trim();//"/HongYunTong";
    @Autowired
    HuachenghuiPayUtil huachenghuiPayUtil;
    @Override
    public Result deal(DealOrderApp dealOrderApp, String channel) {
        Result deal = deal(dealOrderApp, channel, NOTIFY);
        if (deal.isSuccess()) {
            DealOrder result = (DealOrder) deal.getResult();
            Result deal1 = deal(huachenghuiPayUtil, result);
            if (deal1.isSuccess()) {
                return Result.buildSuccessResult("支付处理中", ResultDeal.sendUrl(deal1.getResult()));
            } else {
                return deal1;
            }
        } else {
            return deal;
        }
    }
//    @Override
//    public Result withdraw(Withdraw wit, String channelId) {
//        Result withdraw = withdraw(wit, channelId, NOTIFY);
//        if (withdraw.isSuccess()) {
//            DealWit wits = (DealWit) withdraw.getResult();
//            Result withdraw1 = withdraw(huachenghuiPayUtil, wits);
//            if (withdraw1.isSuccess()) {
//                return withdraw1;
//            } else {
//                return Result.buildFail();
//            }
//        } else {
//            return Result.buildFail();
//        }
//    }


    @Override
    public String dealNotify(Map map) {
        Result result = huachenghuiPayUtil.dealNotify(map);
        if(result.isSuccess()){
            return "SUCCESS";
        }
        return super.dealNotify(map);
    }

    @Override
    public String witNotify(Map map) {
        Result result = huachenghuiPayUtil.witNotify(map);
        if(result.isSuccess()){
            return "SUCCESS";
        }
        return super.witNotify(map);
    }

    @Override
    public Result findBalance(String channelId, String payType) {
        huachenghuiPayUtil.findBalance(channelId,getChannelInfo(channelId, payType));
        return Result.buildSuccess();
    }
}

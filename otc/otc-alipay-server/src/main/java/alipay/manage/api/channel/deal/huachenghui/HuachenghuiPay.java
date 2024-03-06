package alipay.manage.api.channel.deal.huachenghui;

import alipay.manage.api.config.NotifyApi;
import alipay.manage.api.config.PayOrderServiceV2;
import alipay.manage.bean.DealOrder;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.DealWit;
import alipay.manage.bean.util.ResultDeal;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.bean.dealpay.Withdraw;
import otc.result.Result;

import java.util.Map;

@Component("HuachenghuiPay")
@Slf4j
public class HuachenghuiPay extends PayOrderServiceV2 {
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
    @Override
    public Result withdraw(Withdraw wit, String channelId) {
        Result withdraw = withdraw(wit, channelId, NOTIFY);
        if (withdraw.isSuccess()) {
            DealWit wits = (DealWit) withdraw.getResult();
            Result withdraw1 = withdraw(huachenghuiPayUtil, wits);
            if (withdraw1.isSuccess()) {
                return withdraw1;
            } else {
                return Result.buildFail();
            }
        } else {
            return Result.buildFail();
        }
    }


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

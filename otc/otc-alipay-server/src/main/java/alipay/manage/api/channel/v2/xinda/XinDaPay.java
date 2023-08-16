package alipay.manage.api.channel.v2.xinda;

import alipay.manage.api.config.NotifyApi;
import alipay.manage.api.config.PayOrderServiceV2;
import alipay.manage.bean.DealOrder;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.DealWit;
import alipay.manage.bean.util.v2.ResultDeal;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.bean.dealpay.Withdraw;
import otc.result.Result;

import java.util.Map;

@Component("XinDa")
public class XinDaPay extends PayOrderServiceV2 {
    final String NOTIFY_TYPE =  NotifyApi.NOTIFY_TYPE_JSON;//文档定义的上游回调传参类型
    final String NOTIFY =  NOTIFY_TYPE + (NOTIFY_MARK + StrUtil.split(this.getClass().getName(), MARK)[StrUtil.split(this.getClass().getName(), MARK).length - 1]).trim() ;//"/HongYunTong";
    @Autowired
    XinDaPayUtil util;
    @Override
    public Result deal(DealOrderApp dealOrderApp, String channelId) {
        findBalance(channelId, dealOrderApp.getPayType()); //充值前更新余额
        Result deal = super.deal(dealOrderApp, channelId, NOTIFY);
        if (!deal.isSuccess()) {
            return deal;
        }
        DealOrder result = (DealOrder) deal.getResult();
        ResultDeal resultDeal = util.channelDealPush(result, getChannelInfo(channelId, dealOrderApp.getPayType()));
        if (resultDeal.isSuccess()) {
            return Result.buildSuccessResult(resultDeal);
        } else {
            return Result.buildFailResult(resultDeal);
        }
    }

    @Override
    public Result withdraw(Withdraw wit, String channelId) {
        Result withdraw = super.withdraw(wit, channelId, NOTIFY);
        if (!withdraw.isSuccess()) {
            return withdraw;
        }
        DealWit result = (DealWit) withdraw.getResult();
        return util.channelWitPush(result, getChannelInfo(channelId, wit.getWitType()));
    }
    @Override
    public String dealNotify(Map map) {
        Result result = util.dealNotify(map);
        if(result.isSuccess()){
            return "SUCCESS";
        }
        return super.dealNotify(map);
    }

    @Override
    public String witNotify(Map map) {
        Result result = util.witNotify(map);
        if(result.isSuccess()){
            return "SUCCESS";
        }
        return super.witNotify(map);
    }

    @Override
    public Result findBalance(String channelId, String payType) {
        util.findBalance(channelId,getChannelInfo(channelId, payType));
        return Result.buildSuccess();
    }

}

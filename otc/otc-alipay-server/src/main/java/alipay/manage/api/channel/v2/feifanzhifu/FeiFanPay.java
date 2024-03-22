package alipay.manage.api.channel.v2.feifanzhifu;

import alipay.manage.api.channel.v2.xinda.XinDaPayUtil;
import alipay.manage.api.config.NotifyApi;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.api.config.PayOrderServiceV2;
import alipay.manage.bean.DealOrder;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.DealWit;
import alipay.manage.bean.util.ResultDeal;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.bean.dealpay.Withdraw;
import otc.result.Result;

import java.util.Map;

@Component("FeiFanPay")
public class FeiFanPay extends PayOrderService {
    final String NOTIFY_TYPE =  NotifyApi.NOTIFY_TYPE_JSON;//文档定义的上游回调传参类型
    final String NOTIFY =  NOTIFY_TYPE + (NOTIFY_MARK + StrUtil.split(this.getClass().getName(), MARK)[StrUtil.split(this.getClass().getName(), MARK).length - 1]).trim() ;//"/HongYunTong";
    @Autowired
    FeiFanUtil util;
    @Override
    public Result deal(DealOrderApp dealOrderApp, String channelId) {
        log.info("【进入  feidan   Pay支付，当前请求产品：" + dealOrderApp.getRetain1() + "，当前请求渠道：" + channelId + "】");
        Result deal = deal(dealOrderApp, channelId, NOTIFY);
        if (deal.isSuccess()) {
            DealOrder result = (DealOrder) deal.getResult();
            Result deal1 = deal(util, result);
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

package alipay.manage.api.channel.deal.qingtian;

import alipay.manage.api.channel.deal.v2qs.V2qsUtil;
import alipay.manage.api.config.ChannelInfo;
import alipay.manage.api.config.NotifyApi;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.DealOrder;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.util.ResultDeal;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.result.Result;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;

@Component("QingTianPay")
public class QingTianPay extends PayOrderService {
    @Autowired
    private UserInfoService userInfoServiceImpl;
    final String NOTIFY_TYPE = NotifyApi.NOTIFY_TYPE_FORM;//文档定义的上游回调传参类型
    final String NOTIFY = NOTIFY_TYPE + (NOTIFY_MARK + StrUtil.split(this.getClass().getName(), MARK)[StrUtil.split(this.getClass().getName(), MARK).length - 1]).trim();//"/HongYunTong";
    @Autowired
    QingTianUtil qtUtil;
    @Override
    public Result deal(DealOrderApp dealOrderApp, String channel) throws Exception {
        log.info("【进入  QingTianPay   Pay支付，当前请求产品：" + dealOrderApp.getRetain1() + "，当前请求渠道：" + channel + "】");
        Result deal = deal(dealOrderApp, channel, NOTIFY);
        if (deal.isSuccess()) {
            DealOrder result = (DealOrder) deal.getResult();
            Result deal1 = deal(qtUtil, result);
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
        Result result = qtUtil.dealNotify(map);
        if(result.isSuccess()){
            return "SUCCESS";
        }
        return super.dealNotify(map);
    }
}

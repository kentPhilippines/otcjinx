package alipay.manage.api.config;

import alipay.manage.bean.DealOrder;
import alipay.manage.bean.DealWit;
import otc.bean.dealpay.Withdraw;
import otc.result.Result;

public interface ChannelLocalUtil {

    public Result channelDealPush(DealOrder order, ChannelInfo channelInfo);
    public Result channelWitPush(DealWit order, ChannelInfo channelInfo);


}

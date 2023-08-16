package alipay.manage.api.channel.v2.xinda;

import alipay.manage.api.config.ChannelInfo;
import alipay.manage.api.config.ChannelUtil;
import alipay.manage.bean.DealOrder;
import alipay.manage.bean.DealWit;
import alipay.manage.bean.util.v2.ResultDeal;
import org.springframework.stereotype.Component;
import otc.result.Result;

import java.util.Map;

@Component
public class XinDaPayUtil extends ChannelUtil {


    public ResultDeal channelDealPush(DealOrder result, ChannelInfo channelInfo) {
        return ResultDeal.error();
    }

    public Result channelWitPush(DealWit result, ChannelInfo channelInfo) {
        return Result.buildFail();

    }

    public Result dealNotify(Map map) {

        return Result.buildFail();

    }

    public Result witNotify(Map map) {






        return Result.buildFail();
    }

    public void findBalance(String channelId, ChannelInfo channelInfo) {

    }
}

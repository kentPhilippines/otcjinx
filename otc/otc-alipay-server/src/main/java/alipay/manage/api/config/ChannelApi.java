package alipay.manage.api.config;

import alipay.config.redis.RedisUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import otc.result.Result;

public class ChannelApi {
    public static final Log log = LogFactory.get();
    @Autowired
    private RedisUtil redisUtil;
    final String GET_BANK_INFO_MARK = ChannelUtil.GET_BANK_INFO_MARK;
    @RequestMapping("/getBankInfo")
    public Result getBankInfo(String orderId) {
        log.info("【收到用户请求银行卡参数，当前请求订单号为：" + orderId + "】");
        Object o = redisUtil.get(GET_BANK_INFO_MARK + orderId);
        if (null != o) {
            return Result.buildSuccessResult(o);
        }
        return Result.buildFailMessage("当前订单已过期");
    }


}

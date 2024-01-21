package alipay.manage.api.config;

import alipay.manage.api.channel.amount.AmountChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>策略模式</p>
 * @author K
 *
 */
@Component
public class FactoryForStrategy {
    @Autowired  Map<String, PayService> strategys = new ConcurrentHashMap<>(3);
    @Autowired  Map<String, PayOrderServiceV2> strategysV2 = new ConcurrentHashMap<>(3);
    @Autowired  Map<String, AmountChannel> strateamount = new ConcurrentHashMap<>(3);


    public PayService getStrategy(String component) throws Exception{
    	PayService strategy = strategys.get(component);
        if (strategy == null) {
            throw new RuntimeException("没有这个实现类，参数配置错误");
        }
        return strategy;
    }
    public PayOrderServiceV2 getStrategyV2(String component) throws Exception{
        PayOrderServiceV2 payOrderServiceV2 = strategysV2.get(component);
        if (payOrderServiceV2 == null) {
            throw new RuntimeException("没有这个实现类，参数配置错误");
        }
        return payOrderServiceV2;
    }
    public AmountChannel getAmountChannel(String component) throws Exception {
        AmountChannel strategy = strateamount.get(component);
        if (strategy == null) {
            throw new RuntimeException("没有这个实现类，参数配置错误");
        }
        return strategy;
    }
}

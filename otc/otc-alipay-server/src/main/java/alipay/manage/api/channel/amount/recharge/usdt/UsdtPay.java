package alipay.manage.api.channel.amount.recharge.usdt;

import alipay.config.redis.RedisUtil;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.BankList;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.util.ResultDeal;
import alipay.manage.service.BankListService;
import alipay.manage.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.result.Result;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("UsdtPay")
public class UsdtPay extends PayOrderService implements USDT {
    private static final String PAY_URL = "http://";
    @Autowired
    RedisUtil redis;
    @Autowired
    private BankListService bankListServiceIMpl;
    @Autowired
    private OrderService orderServiceImpl;

    @Override
    public Result deal(DealOrderApp dealOrderApp, String channel) throws Exception {
        log.info("【进入USDT支付，当前请求产品：" + dealOrderApp.getRetain1() + "，当前请求渠道：" + channel + "】");
        String orderId = create(dealOrderApp, channel);
        Result result = createOrder(
                dealOrderApp.getOrderAmount(),
                orderId, dealOrderApp.getRetain1(),
                bankListServiceIMpl.findBankByAccount("UsdtPay")
        );
        if (result.isSuccess()) {
            return Result.buildSuccessResult("支付处理中", ResultDeal.sendUrl(result.getResult()));
        } else {
            orderEr(dealOrderApp, "错误消息：" + result.getMessage());
            return result;
        }
    }


    private Result createOrder(BigDecimal orderAmount, String orderId, String type, List<BankList> usdtInfo) throws Exception {
        BigInteger bigInteger = new BigDecimal("1000000").toBigInteger();//虚拟币单位放大1000000比对
        BigInteger bigInteger1 = orderAmount.toBigInteger();
        BigInteger money = bigInteger1.multiply(bigInteger);
        for (BankList bank : usdtInfo) {
            String bankinfo = MARS + money + bank.getBankcardAccount();
            Object o = redis.get(bankinfo);
            if (null != o) {
                continue;
            }
            log.info("【缓存支付数据为：" + bankinfo + "】");
            redis.set(bankinfo, orderId, TIME);//当前地址正在使用标记， 当前正在使用唯一标记为： 金额 + 钱包地址
            Object k = redis.get(bankinfo);
            Map cardmap = new HashMap();
            cardmap.put(ADDRESS, bank.getBankcardAccount());//钱包地址
            cardmap.put(MONEY, orderAmount);//钱包地址
            cardmap.put(USDTSCAN, "ethereum:" + bank.getBankcardAccount() + "?decimal=6&value=0");//USDT二维码扫码数据
            redis.hmset(MARS + orderId, cardmap, TIME);//作用为为存储地址信息,作为终端用户支付的地址依据
            //  redis.hset(MARS,orderId,bank.getBankcardAccount(),TIME);  // 获取支付回调的时候用来判断 当前是否存在usdt未支付订单
            long l = redis.sSetAndTime(MARS, TIME, bank.getBankcardAccount() + "_" + orderId);
            /**
             * ##################
             * 以上作为缓存的key   在支付完成后都需要手动删除
             * ##################
             */
            orderServiceImpl.updateBankInfoByOrderId(bank.getBankcardAccount(), orderId);
            //   USDTQrcodeUtil.encode("ethereum:"+bank.getBankcardAccount()+"?decimal=6&value=0",bank.getBankcardAccount());
            return Result.buildSuccessResult(PAY_URL + "47.242.50.29:32437/pay-usdt?orderId=" + orderId + "&type=" + type);
        }
        return Result.buildFailMessage("暂无可用支付钱包地址");

    }

    void createImge(String id) {

    }


}

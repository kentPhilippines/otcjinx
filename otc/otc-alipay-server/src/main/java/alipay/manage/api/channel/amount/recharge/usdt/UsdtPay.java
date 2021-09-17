package alipay.manage.api.channel.amount.recharge.usdt;

import alipay.config.redis.RedisUtil;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.BankList;
import alipay.manage.bean.DealOrder;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.util.ResultDeal;
import alipay.manage.service.BankListService;
import alipay.manage.service.OrderService;
import cn.hutool.core.thread.ThreadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.result.Result;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
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
        String appOrderId = dealOrderApp.getAppOrderId();
        List<BankList> bankList = new ArrayList<>();
        if(!appOrderId.contains("USDT_TRC")){
            bankList = bankListServiceIMpl.findBankByAccount("UsdtPay");
        }else {
            bankList = bankListServiceIMpl.findBankByAccount("UsdtPay_trc");
        }
        Result result = createOrder(
                dealOrderApp.getOrderAmount(),
                orderId, dealOrderApp.getRetain1(),
                bankList,
                dealOrderApp.getAppOrderId()
        );
        if (result.isSuccess()) {
            return Result.buildSuccessResult("支付处理中", ResultDeal.sendUrl(result.getResult()));
        } else {
            orderEr(dealOrderApp, "错误消息：" + result.getMessage());
            return result;
        }
    }
          String LOCATION_MARS;
          Long LOCATION_TIME;
          String LOCATION_ADDRESS;
          String LOCATION_MONEY;
          String LOCATION_USDTSCAN;

    private Result createOrder(BigDecimal orderAmount, String orderId, String type, List<BankList> usdtInfo, String appOrderId) throws Exception {
        String LOCATION_MARS;
        Long LOCATION_TIME;
        String LOCATION_ADDRESS;
        String LOCATION_MONEY;
        String LOCATION_USDTSCAN;
        if(appOrderId.contains("USDT_TRC")){
            LOCATION_MARS = MARS_TRC;
            LOCATION_TIME = TIME_TRC;
            LOCATION_ADDRESS = ADDRESS_TRC;
            LOCATION_MONEY = MONEY_TRC;
            LOCATION_USDTSCAN = USDTSCAN_TRC;
        }else {
            LOCATION_MARS = MARS;
            LOCATION_TIME = TIME;
            LOCATION_ADDRESS = ADDRESS;
            LOCATION_MONEY = MONEY;
            LOCATION_USDTSCAN = USDTSCAN;
        }
        BigDecimal bigInteger = new BigDecimal("1000000");//虚拟币单位放大1000000比对
        BigInteger money = orderAmount.multiply(bigInteger).toBigInteger();
        for (BankList bank : usdtInfo) {
            String bankinfo = LOCATION_MARS + money + bank.getBankcardAccount().toUpperCase();
            Object o = redis.get(bankinfo);
            if (null != o) {
                continue;
            }
            log.info("【缓存支付数据为：" + bankinfo + ",当前订单号为："+orderId+"】");
            redis.set(bankinfo, orderId, LOCATION_TIME);//当前地址正在使用标记， 当前正在使用唯一标记为： 金额 + 钱包地址
            Object k = redis.get(bankinfo);
            Map cardmap = new HashMap();
            cardmap.put(LOCATION_ADDRESS, bank.getBankcardAccount());//钱包地址
            cardmap.put(LOCATION_MONEY, orderAmount);//钱包地址
            if(appOrderId.contains("USDT_TRC")){
                cardmap.put(LOCATION_USDTSCAN,   bank.getBankcardAccount()  );//USDT_TRC二维码扫码数据
            }else {
                cardmap.put(LOCATION_USDTSCAN, "ethereum:" + bank.getBankcardAccount() + "?decimal=6&value=0");//USDT二维码扫码数据
            }
            redis.hmset(LOCATION_MARS + orderId, cardmap, LOCATION_TIME);//作用为为存储地址信息,作为终端用户支付的地址依据
            //  redis.hset(MARS,orderId,bank.getBankcardAccount(),TIME);  // 获取支付回调的时候用来判断 当前是否存在usdt未支付订单
            long l = redis.sSetAndTime(LOCATION_MARS, LOCATION_TIME, bank.getBankcardAccount() + "_" + orderId);
            /**
             * ##################
             * 以上作为缓存的key   在支付完成后都需要手动删除
             * ##################
             */
            orderServiceImpl.updateBankInfoByOrderId(bank.getBankcardAccount(), orderId);
            ThreadUtil.execute(()->{
                List<DealOrder> orderList = orderServiceImpl.findExternalOrderId(appOrderId);
                for(DealOrder order : orderList ){
                    boolean kentusdtmanage = order.getOrderAccount().equals("KENTUSDTMANAGE");//内充 usdt 转 cny 专用账号
                    if (!kentusdtmanage) {//内充原订单
                        String rate = order.getNotify();

                        BigDecimal bg = new BigDecimal(rate);
                        double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        orderServiceImpl.updateBankInfoByOrderId(bank.getBankcardAccount() +":汇率:"+f1 + ": 数量:"+orderAmount,order.getOrderId());
                    }
                }
            });
            //   USDTQrcodeUtil.encode("ethereum:"+bank.getBankcardAccount()+"?decimal=6&value=0",bank.getBankcardAccount());
            return Result.buildSuccessResult(PAY_URL + "47.242.50.29:32437/pay-usdt?orderId=" + orderId + "&type=" + type);
        }
        return Result.buildFailMessage("暂无可用支付钱包地址");

    }

    void createImge(String id) {

    }


}

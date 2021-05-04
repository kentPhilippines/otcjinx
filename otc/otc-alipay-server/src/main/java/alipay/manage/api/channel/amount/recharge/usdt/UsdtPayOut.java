package alipay.manage.api.channel.amount.recharge.usdt;

import alipay.config.redis.RedisUtil;
import alipay.manage.api.config.NotfiyChannel;
import alipay.manage.bean.BankList;
import alipay.manage.bean.DealOrder;
import alipay.manage.bean.UserFund;
import alipay.manage.mapper.USDTMapper;
import alipay.manage.service.BankListService;
import alipay.manage.service.OrderService;
import alipay.manage.service.WithdrawService;
import alipay.manage.util.amount.AmountPublic;
import alipay.manage.util.amount.AmountRunUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import otc.bean.alipay.OrderDealStatus;
import otc.bean.dealpay.Withdraw;
import otc.result.Result;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Component("UsdtPay-my")
public class UsdtPayOut extends NotfiyChannel implements USDT {
    public static final Log log = LogFactory.get();
    //        String s = HttpUtil.get(" https://api.etherscan.io/api?module=account&action=tokentx&address=0x0418F374F25EdAb13D38a7D82b445cE9934Bfc12&page=1&offset=5&sort=asc&apikey=JYNM1VJSXN8JE6JCY5M9JGKBDB7KPJDC5M");
    //
    public static final String FIND_URL = "https://api.etherscan.io/api?module=account&action=tokentx&address=";
    public static final String APP_KEY = "JYNM1VJSXN8JE6JCY5M9JGKBDB7KPJDC5M";
    public static final String ACCOUNT = "UsdtPay";//系统登记的渠道账户也是我们自己登记钱包地址的账户名
    String FIND_AMOUNT_URL = "https://api.etherscan.io/api?module=account&action=balance&address=";
    String FIND_AMOUNT_URL_KEY = "&tag=latest&apikey=JYNM1VJSXN8JE6JCY5M9JGKBDB7KPJDC5M";
    @Resource
    private USDTMapper USDTDao;
    @Autowired
    private OrderService orderServiceImpl;
    @Autowired
    WithdrawService withdrawServiceImpl;
    @Autowired
    private BankListService bankListServiceIMpl;
    @Autowired
    private AmountPublic amountPublicImpl;
    @Autowired
    private AmountRunUtil amountRunUtilImpl;
    @Autowired
    private RedisUtil redis;

    public static String findETHUSDTOrderList(String address) {
        String url = FIND_URL + address + "&page=1&offset=20&sort=desc&apikey=" + APP_KEY;
        String s = HttpUtil.get(url);
        log.info("【查询返回：" + s + "】");
        return s;
    }

    String findAMount(String address) {
        String s = HttpUtil.get(FIND_AMOUNT_URL + address + FIND_AMOUNT_URL_KEY);
        //{"status":"1","message":"OK","result":"8800000000000000"}
        JSONObject jsonObject = JSONUtil.parseObj(s);
        String result = jsonObject.getStr("result");
        return result;
    }

    public Integer insterU(USDTOrder order) throws SQLException {
        return Db.use().insertForGeneratedKey(
                Entity.create("alipay_usdt_order")
                        .set("blockNumber", order.getBlockNumber())
                        .set("timeStamp", order.getTimeStamp())
                        .set("hash", order.getHash())
                        .set("blockHash", order.getBlockHash())
                        .set("fromAccount", order.getFrom())
                        .set("contractAddress", order.getContractAddress())
                        .set("toAccount", order.getTo())
                        .set("value", order.getValue())
                        .set("tokenName", order.getTokenName())
                        .set("tokenSymbol", order.getTokenSymbol())
                        .set("fromNow", order.getFromNow())
                        .set("toNow", order.getToNow())
        ).intValue();
    }

    public void findDealOrderLog() {
        log.info("【执行虚拟币回调订单主动查询】");
        Set<Object> orderList = redis.sGet(MARS);
        if (orderList.size() == 0) {
            log.info("【当前缓存中不存在 USDT 支付数据】");
            return;
        }
        for (Object account : orderList) {
            log.info("【缓存数据为:" + account.toString() + "】");
            String[] split = account.toString().split("_");
            String address = split[0];//待支付成功地址
            String orderId = split[1];
            String s = findETHUSDTOrderList(address);
            JSONObject jsonObject = JSONUtil.parseObj(s);
            String status = jsonObject.getStr("status");
            if ("1".equals(status)) {
                String resultjson = jsonObject.getStr("result");
                JSONArray result = JSONUtil.parseArray(resultjson);
                for (Object obj : result) {
                    log.info("【查询usdt数据为:" + obj.toString() + "】");
                    USDTOrder usdt = new USDTOrder();
                    JSONObject jsonObject1 = JSONUtil.parseObj(obj);
                    String blockNumber = jsonObject1.getStr("blockNumber");
                    String to = jsonObject1.getStr("to");
                    String timeStamp = jsonObject1.getStr("timeStamp");
                    String hash = jsonObject1.getStr("hash");
                    String blockHash = jsonObject1.getStr("blockHash");
                    String from = jsonObject1.getStr("from");
                    String contractAddress = jsonObject1.getStr("contractAddress");
                    String value = jsonObject1.getStr("value");
                    String tokenName = jsonObject1.getStr("tokenName");
                    String tokenSymbol = jsonObject1.getStr("tokenSymbol");
                    usdt.setBlockNumber(blockNumber);
                    usdt.setTo(to);
                    usdt.setTimeStamp(timeStamp);
                    usdt.setHash(hash);
                    usdt.setBlockHash(blockHash);
                    usdt.setFrom(from);
                    usdt.setContractAddress(contractAddress);
                    usdt.setValue(value);
                    usdt.setTokenName(tokenName);
                    usdt.setTokenSymbol(tokenSymbol);
                    usdt.setToNow(findAMount(usdt.getTo()));
                    usdt.setToNow(findAMount(usdt.getFrom()));
                    Object o = redis.get(MARS + usdt.getHash());
                    SimpleDateFormat sdf = new SimpleDateFormat(DatePattern.NORM_DATETIME_PATTERN);
                    java.util.Date date = new Date(Long.valueOf(usdt.getTimeStamp()) * 1000);
                    String str = sdf.format(date);
                    usdt.setTimeStamp(str);
                    if (null == o) {
                        int i = 0;
                        try {
                            log.info("【保存本地usdt数据为：" + usdt.toString() + "】");
                            i = insterU(usdt);
                        } catch (Exception e) {
                            i = 0;
                        }
                        if (i > 0) {
                            log.info("【数据标记成功，当前标记数据hash为：" + usdt.getHash() + "】");
                            redis.set(MARS + usdt.getHash(), usdt.getHash() + "-" + orderId, 60 * 60 * 24 * 30);//缓存一个余额， 减少数据库压力
                        }
                        //验证是否有支付成功
                        String bankinfo = MARS + usdt.getValue() + usdt.getTo();   //支付成功标识
                        log.info("【支付标记数据为：" + bankinfo + "】");
                        Object o1 = redis.get(bankinfo);//支付成功订单号  ，  如果不为空  则为支付成功
                        if (null != o1 && usdt.getTo().toUpperCase().equals(address.toUpperCase())) {
                            log.info("【判定支付成功，当前订单号：" + orderId + "，当前支付地址：" + address + "】");
                            DealOrder order = orderServiceImpl.findOrderByOrderId(orderId);
                            if (order.getStatus().toString().equals(OrderDealStatus.成功.getIndex().toString())) {
                                continue;
                            }
                            Date createTime = order.getCreateTime();
                            long time = createTime.getTime();
                            DateTime parse = DateUtil.parse(usdt.getTimeStamp());
                            long usdtTime = parse.getTime();
                            log.info("【支付时间和订单时间比较，是否符合支付成功判定】");
                            if (((usdtTime - time) < TIME) && i > 1) {
                                DealOrder orderByOrderId = orderServiceImpl.findOrderByOrderId(orderId);
                                String externalOrderId = orderByOrderId.getExternalOrderId();
                                List<DealOrder> orderList1 = orderServiceImpl.findExternalOrderId(externalOrderId);
                                for (DealOrder exOrder : orderList1) {
                                    boolean kentusdtmanage = exOrder.getOrderAccount().equals("KENTUSDTMANAGE");//内充 usdt 转 cny 专用账号
                                    if (kentusdtmanage) {
                                        Result result1 = dealpayNotfiy(exOrder.getOrderId(), "127.0.0.1", "USDT转CNY，USDT到账成功");
                                        if (result1.isSuccess()) {
                                            orderServiceImpl.updateUsdtTxHash(orderId, hash);
                                        }
                                    }
                                }
                                Result result1 = dealpayNotfiy(orderId, "127.0.0.1", "主动查询USDT订单交易成功");
                                if (result1.isSuccess()) {//支付成功， 删除缓存标记
                                    orderServiceImpl.updateUsdtTxHash(orderId, hash);
                                    redis.del(bankinfo);   //支付成功唯一标识
                                    redis.del(MARS + orderId);//终端用户获取支付地址信息
                                    redis.setRemove(MARS, address + "_" + orderId);//当前该地址和订单信息
                                }
                            }
                        }
                    } else {
                        log.info("【数据标记已存在，请重新匹配，当前hash号：" + hash + ",当前标记数据：" + o + "】");
                    }
                }
            }
        }
    }

    /**
     * usdt代付订单自动结算手续费
     *
     * @return
     */
    @Transactional
    public Result autoWitUSDT(String orderId) {
        /**
         *
         * 获取我方所有usdt账号
         * 通过地址拿到该笔交易的公链数据详情
         * 通过出款账户匹配
         * 确认当前出款数据
         * 获取eth消耗  gasPrice *  gsaUsed
         * 换算usdt     [ eth ==>  usdt https://coinyep.com/api/v1/?from=ETH&to=USDT&lang=zh&format=json   汇率减即可]
         * 进行扣款
         *
         */

        log.info("【==============开始进入自动结算usdt手续费方法===============】");
        Withdraw orderWit = withdrawServiceImpl.findOrderId(orderId);
        String bankNo = orderWit.getBankNo();//usdt代付地址
        BigInteger bigInteger = new BigDecimal("1000000").toBigInteger();//虚拟币单位放大1000000比对
        BigInteger bigInteger1 = orderWit.getAmount().toBigInteger();
        BigInteger money = bigInteger1.multiply(bigInteger);
        // 1， 获取我方所有usdt账号
        List<BankList> bankList = bankListServiceIMpl.findBankByAccount(UsdtPayOut.ACCOUNT);
        for (BankList bank : bankList) {
            // 2，通过地址拿到该笔交易的公链数据详情
            String ethusdtOrderList = UsdtPayOut.findETHUSDTOrderList(bank.getBankcardAccount());
            JSONObject jsonObject = JSONUtil.parseObj(ethusdtOrderList);
            String status = jsonObject.getStr("status");
            if ("1".equals(status)) {
                String resultjson = jsonObject.getStr("result");
                JSONArray result = JSONUtil.parseArray(resultjson);
                for (Object obj : result) {
                    JSONObject jsonObject1 = JSONUtil.parseObj(obj);
                    String to = jsonObject1.getStr("to");
                    String timeStamp = jsonObject1.getStr("timeStamp");
                    String hash = jsonObject1.getStr("hash");
                    String blockHash = jsonObject1.getStr("blockHash");
                    String from = jsonObject1.getStr("from");
                    String contractAddress = jsonObject1.getStr("contractAddress");
                    String value = jsonObject1.getStr("value");
                    String key = from + to + value;
                    String sign = bank.getBankcardAccount() + orderWit.getBankNo() + money;
                    if (key.equals(sign)) {//当前订单
                        //     3，通过出款账户匹配，确认当前出款数据
                        String gasPrice = jsonObject1.getStr("gasPrice");
                        String gasUsed = jsonObject1.getStr("gasUsed");
                        BigDecimal price = new BigDecimal(gasPrice).divide(new BigDecimal("1000000000"), BigDecimal.ROUND_HALF_EVEN);
                        price = price.divide(new BigDecimal("1000000000"));
                        BigDecimal used = new BigDecimal(gasUsed);
                        BigDecimal eth = price.multiply(used);
                        //  4,获取eth消耗  gasPrice *  gsaUsed
                        String rateResult = HttpUtil.get("https://coinyep.com/api/v1/?from=ETH&to=USDT&lang=zh&format=json");
                        JSONObject rate = JSONUtil.parseObj(rateResult);
                        String priceUsdt = jsonObject.getStr("price");
                        BigDecimal usdtRate = new BigDecimal(priceUsdt);
                        BigDecimal usdt = eth.multiply(usdtRate);
                        return usdtAmount(orderWit, usdt, price, used, eth, priceUsdt, hash);
                    }
                }
            }
        }
        return Result.buildFailMessage("结算异常");
    }


    /**
     * 结算usdt代付矿工手续费
     *
     * @param orderWit  代付订单
     * @param usdt      花费usdt    折合usdt
     * @param price     汽油单价
     * @param used      使用汽油
     * @param eth       折合eth
     * @param priceUsdt eth -->usdt   汇率
     * @param hash      usdt代付唯一订单
     * @return
     */
    public Result usdtAmount(Withdraw orderWit, BigDecimal usdt, BigDecimal price, BigDecimal used, BigDecimal eth, String priceUsdt, String hash) {
        UserFund fund = new UserFund();
        fund.setUserId(orderWit.getUserId());
        Result result1 = amountPublicImpl.deleteWithdraw(fund, usdt, orderWit.getOrderId());
        if (result1.isSuccess()) {
            String describe = "gasPrice:" + price + ",gasUsed:" + used + ",ETH:" + eth + ",ETH->USDT:" + priceUsdt + "USDT:" + usdt + ",Hash:" + hash + "";
            Result result2 = amountRunUtilImpl.deleteUsdtFeeGas(orderWit, orderWit.getRetain2(), describe, usdt);
            if (result2.isSuccess()) {
                withdrawServiceImpl.updateEthFee(orderWit.getOrderId(), hash);
                return Result.buildSuccessMessage("订单结算完毕");
            }
        }
        return Result.buildSuccessMessage("结算异常");
    }


}

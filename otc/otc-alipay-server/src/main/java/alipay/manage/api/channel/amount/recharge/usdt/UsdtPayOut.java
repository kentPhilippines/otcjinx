package alipay.manage.api.channel.amount.recharge.usdt;

import alipay.config.redis.RedisUtil;
import alipay.manage.api.config.NotfiyChannel;
import alipay.manage.bean.DealOrder;
import alipay.manage.mapper.USDTMapper;
import alipay.manage.service.BankListService;
import alipay.manage.service.OrderService;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.bean.alipay.OrderDealStatus;
import otc.result.Result;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

@Component("UsdtPay-my")
public class UsdtPayOut extends NotfiyChannel implements USDT {
    public static final Log log = LogFactory.get();
    //        String s = HttpUtil.get(" https://api.etherscan.io/api?module=account&action=tokentx&address=0x0418F374F25EdAb13D38a7D82b445cE9934Bfc12&page=1&offset=5&sort=asc&apikey=JYNM1VJSXN8JE6JCY5M9JGKBDB7KPJDC5M");
    //

    private static final String FIND_URL = "https://api.etherscan.io/api?module=account&action=tokentx&address=";
    private static final String APP_KEY = "JYNM1VJSXN8JE6JCY5M9JGKBDB7KPJDC5M";
    private static final String ACCOUNT = "UsdtPay";//系统登记的渠道账户也是我们自己登记钱包地址的账户名
    String FIND_AMOUNT_URL = "https://api.etherscan.io/api?module=account&action=balance&address=";
    String FIND_AMOUNT_URL_KEY = "&tag=latest&apikey=JYNM1VJSXN8JE6JCY5M9JGKBDB7KPJDC5M";
    @Resource
    private USDTMapper USDTDao;
    @Autowired
    private OrderService orderServiceImpl;
    @Autowired
    private BankListService bankListServiceIMpl;
    @Autowired
    private RedisUtil redis;

    public void findDealOrderLog() {
        Set<Object> orderList = redis.sGet(MARS);
        if (orderList.size() == 0) {
            log.info("【当前缓存中不存在 USDT 支付数据】");
            return;
        }
        for (Object account : orderList) {
            String[] split = account.toString().split("-");
            String address = split[0];//待支付成功地址
            String orderId = split[1];
            String s = HttpUtil.get(FIND_URL + address + "&page=1&offset=20&sort=desc&apikey=" + APP_KEY);
            JSONObject jsonObject = JSONUtil.parseObj(s);
            String status = jsonObject.getStr("status");
            if ("1".equals(status)) {
                String resultjson = jsonObject.getStr("result");
                JSONArray result = JSONUtil.parseArray(resultjson);
                for (Object obj : result) {
                    USDTOrder usdt = (USDTOrder) obj;
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
                            i = USDTDao.addOrder(usdt);
                        } catch (Exception e) {

                        }
                        if (i > 0) {
                            redis.set(MARS + usdt.getHash(), usdt.getHash(), 1000 * 60 * 60 * 24 * 30);//缓存一个余额， 减少数据库压力
                        }
                        //验证是否有支付成功
                        String bankinfo = MARS + usdt.getValue() + usdt.getTo();   //支付成功标识
                        Object o1 = redis.get(bankinfo);//支付成功订单号  ，  如果不为空  则为支付成功
                        if (null != o1 && usdt.getTo().equals(address)) {
                            DealOrder order = orderServiceImpl.findOrderByOrderId(orderId);
                            if (order.getStatus().toString().equals(OrderDealStatus.成功.getIndex().toString())) {
                                continue;
                            }
                            Date createTime = order.getCreateTime();
                            long time = createTime.getTime();
                            DateTime parse = DateUtil.parse(usdt.getTimeStamp());
                            long usdtTime = parse.getTime();
                            log.info("【支付时间和订单时间比较，是否符合支付成功判定】");
                            if ((usdtTime - time) < TIME) {
                                Result result1 = dealpayNotfiy(orderId, "127.0.0.1", "主动查询USDT订单交易成功");
                                if (result1.isSuccess()) {//支付成功， 删除缓存标记
                                    redis.del(bankinfo);   //支付成功唯一标识
                                    redis.del(MARS + orderId);//终端用户获取支付地址信息
                                    redis.setRemove(MARS, address + "_" + orderId);//当前该地址和订单信息
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    String findAMount(String address) {
        String s = HttpUtil.get(FIND_AMOUNT_URL + address + FIND_AMOUNT_URL_KEY);
        //{"status":"1","message":"OK","result":"8800000000000000"}
        JSONObject jsonObject = JSONUtil.parseObj(s);
        String result = jsonObject.getStr("result");
        return result;
    }
}

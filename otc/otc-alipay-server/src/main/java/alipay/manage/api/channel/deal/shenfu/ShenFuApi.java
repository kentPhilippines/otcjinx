package alipay.manage.api.channel.deal.shenfu;

import alipay.config.redis.RedisUtil;
import alipay.manage.bean.DealOrder;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.service.OrderAppService;
import alipay.manage.service.OrderService;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import otc.common.PayApiConstant;

import java.util.Map;

@RequestMapping(PayApiConstant.Alipay.ORDER_API + "/ShenFuBankCardInfo")
@RestController
public class ShenFuApi {
    public static final Log log = LogFactory.get();
    private static final String MARS = "SHENFU";
    @Autowired
    RedisUtil redis;
    @Autowired
    private OrderService OrderServiceImpl;
    @Autowired
    private OrderAppService OrderAppServiceImpl;
    @GetMapping()
    public ShenFuBankInfo getBankCardInfo(String orderId) {
        ShenFuBankInfo info = new ShenFuBankInfo();
        log.info("【收到用户请求申付银行卡参数，当前请求订单号为：" + orderId + "】");
        try {
            Map<Object, Object> hmget = redis.hmget(MARS + orderId);
            info.setBank_name(hmget.get("bank_name").toString());
            info.setCard_no(hmget.get("card_no").toString());
            info.setCard_user(hmget.get("card_user").toString());
            info.setMoney_order(hmget.get("money_order").toString());
            info.setNo_order(hmget.get("no_order").toString());
            info.setAddress(hmget.get("address").toString());
            info.setOid_partner(hmget.get("oid_partner").toString());

            DealOrder order = OrderServiceImpl.findOrderByOrderId(orderId);
            if("4".equals(order.getOrderType())){//出款订单 卡商看到
                String orderQr = order.getOrderQr();
                info.setDealDescribe(orderQr);
            }else if("1".equals(order.getOrderType())){// 入款订单  会员看到
                DealOrderApp orderByOrderId = OrderAppServiceImpl.findOrderByOrderId(order.getAssociatedId());
                String dealDescribe = orderByOrderId.getDealDescribe();
                try {
                    String name =     dealDescribe.split("：")[1];
                    if(!isChinese(name)){
                        return info;
                    };
                    info.setDealDescribe(name);
                }catch ( Exception e){
                    log.info("付款人错误，当前订单号："+orderId);
                }

            }



        } catch (Exception e) {
            log.info("【请求缓存银行卡数据失败，当前请求订单号：" + orderId + "】");
        }
        log.info("【当前银行卡信息为：" + info.toString() + "】");
        return info;
    }
    /**
     * 是否为中文
     * @param string
     * @return
     */
    public   boolean isChinese(String string ) {
        int n = 0 ;
        for (int i = 0; i < string.length(); i++) {
            n = (int )string.charAt(i);
            if(!(19968<=n && n < 40869)){
                return false;
            }
        }
        return true;
    }
}

/**
 * // "bank_name":"建设银行",          银行名称
 * // "card_no":"6217003110033402130",      卡号
 * // "card_user":"农洪共",                 持卡人姓名
 * // "money_order":"1000.00",              支付金额
 * // "address":"北京朝阳102支行",              支行地址  如果为null 则查询不到支行地址
 * // "no_order":"C20210212151359978932599",    交易订单号
 * // "oid_partner":"202102101152580034",           交易订单号
 */
class ShenFuBankInfo {
    private String bank_name;
    private String card_no;
    private String card_user;
    private String money_order;
    private String no_order;
    private String oid_partner;
    private String address;
    public String getDealDescribe() {
        return dealDescribe;
    }

    public void setDealDescribe(String dealDescribe) {
        this.dealDescribe = dealDescribe;
    }

    private String dealDescribe;
    public String getNo_order() {
        return no_order;
    }

    public void setNo_order(String no_order) {
        this.no_order = no_order;
    }

    public String getOid_partner() {
        return oid_partner;
    }

    public void setOid_partner(String oid_partner) {
        this.oid_partner = oid_partner;
    }

    public String getMoney_order() {
        return money_order;
    }

    public void setMoney_order(String money_order) {
        this.money_order = money_order;
    }

    public String getCard_user() {

        return card_user;
    }

    public void setCard_user(String card_user) {
        this.card_user = card_user;
    }

    public String getCard_no() {
        return card_no;
    }

    public void setCard_no(String card_no) {
        this.card_no = card_no;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "ShenFuBankInfo{" +
                "bank_name='" + bank_name + '\'' +
                ", card_no='" + card_no + '\'' +
                ", card_user='" + card_user + '\'' +
                ", money_order='" + money_order + '\'' +
                ", no_order='" + no_order + '\'' +
                ", oid_partner='" + oid_partner + '\'' +
                '}';
    }
}
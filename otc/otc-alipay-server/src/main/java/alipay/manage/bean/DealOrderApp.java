package alipay.manage.bean;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>下游商户交易订单</p>
 *
 * @author K
 */
@Data
public class DealOrderApp implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;                                //数据id
    private String orderId;                            //订单号
    private Integer orderType;                        //订单类型:1交易,5代付
    private String orderAccount;                    //订单关联商户账号
    private String orderStatus;                        //订单状态:1处理中2成功3未收到回调4失败5超时6订单申述7人工处理
    private BigDecimal orderAmount;                    //订单金额
    private BigDecimal orderAmount_orderAmount;                    //订单金额
    private String orderIp;                            //请求订单ip
    private String appOrderId;                        //外部订单号
    private Date createTime;
    private Integer feeId;                            //使用费率id
    private String notify;                            //异步回调地址
    private String back;                            //同步跳转地址
    private Date submitTime;
    private String submitSystem;
    private Integer status;
    private String retain1;                         //产品类型
    private String retain2;
    private String retain3;
    private String dealDescribe;                    //交易备注
    private String currency;  ///货币类型
    private String payName;
    private String openType;                        //付款人打开方式
    public String getPayType() {
        return retain1;
    }
    public void setPayType(String PayType) {
         this.retain1 = PayType;
    }
}
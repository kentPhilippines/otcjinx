package alipay.manage.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
/**
 * <p>下游商户交易订单</p>
 * @author K
 */
public class DealOrderApp implements Serializable {
    private Integer id;								//数据id
    private String orderId;							//订单号	
    private Integer orderType;						//订单类型:1交易,5代付
    private String orderAccount;					//订单关联商户账号
    private String orderStatus;						//订单状态:1处理中2成功3未收到回调4失败5超时6订单申述7人工处理
    private BigDecimal orderAmount;					//订单金额
    private String orderIp;							//请求订单ip
    private String appOrderId;						//外部订单号
    private Date createTime;					
    private Integer feeId;							//使用费率id
    private String notify;							//异步回调地址
    private String back;							//同步跳转地址
    private Date submitTime;				
    private String submitSystem;
    private Integer status;
    private String retain1;
    private String retain2;
    private String retain3;
    private String dealDescribe;					//交易备注
    public void setDealDescribe(String dealDescribe) {
		this.dealDescribe = dealDescribe;
	}
	public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getOrderAccount() {
        return orderAccount;
    }

    public void setOrderAccount(String orderAccount) {
        this.orderAccount = orderAccount == null ? null : orderAccount.trim();
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getOrderIp() {
        return orderIp;
    }

    public void setOrderIp(String orderIp) {
        this.orderIp = orderIp == null ? null : orderIp.trim();
    }

    public String getAppOrderId() {
        return appOrderId;
    }

    public void setAppOrderId(String appOrderId) {
        this.appOrderId = appOrderId == null ? null : appOrderId.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getFeeId() {
        return feeId;
    }

    public void setFeeId(Integer feeId) {
        this.feeId = feeId;
    }

    public String getNotify() {
        return notify;
    }

    public void setNotify(String notify) {
        this.notify = notify == null ? null : notify.trim();
    }

    public String getBack() {
        return back;
    }

    public void setBack(String back) {
        this.back = back == null ? null : back.trim();
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    public String getSubmitSystem() {
        return submitSystem;
    }

    public void setSubmitSystem(String submitSystem) {
        this.submitSystem = submitSystem == null ? null : submitSystem.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRetain1() {
        return retain1;
    }

    public void setRetain1(String retain1) {
        this.retain1 = retain1 == null ? null : retain1.trim();
    }

    public String getRetain2() {
        return retain2;
    }

    public void setRetain2(String retain2) {
        this.retain2 = retain2 == null ? null : retain2.trim();
    }

    public String getRetain3() {
        return retain3;
    }

    public void setRetain3(String retain3) {
        this.retain3 = retain3 == null ? null : retain3.trim();
    }

    public String getDealDescribe() {
        return dealDescribe;
    }

    public void dealApp(String dealDescribe) {
        this.dealDescribe = dealDescribe == null ? null : dealDescribe.trim();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
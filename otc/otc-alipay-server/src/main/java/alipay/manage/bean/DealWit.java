package alipay.manage.bean;

import java.math.BigDecimal;
import java.util.Date;

public class DealWit {
    /**
     * 数据id
     */
    private Integer id;

    /**
     * 订单号
     */
    private String orderId;

    /**
     * 关联订单号
     */
    private String associatedId;

    /**
     * 订单状态:0预下单1处理中2成功3未收到回调4失败5超时6订单申述7人工处理
     */
    private String orderStatus;

    /**
     * 订单交易金额
     */
    private BigDecimal dealAmount;

    /**
     * 订单交易手续费
     */
    private BigDecimal dealFee;

    /**
     * 实际到账金额
     */
    private BigDecimal actualAmount;

    /**
     * 订单关联商户账号
     */
    private String orderAccount;

    /**
     * 渠道账户
     */
    private String chanenlId;

    /**
     * 取款详细
     */
    private String witInfo;

    /**
     * 外部订单号(下游商户请求参数,用户数据回调)
     */
    private String externalOrderId;

    /**
     * 订单异步回调地址
     */
    private String notify;

    /**
     * 订单同步回调地址
     */
    private String back;

    /**
     * 是否發送通知 //  yes 已發送    no 未發送
     */
    private String isNotify;
    private Date createTime;
    private Date submitTime;

    /**
     * 使用费率id
     */
    private Integer feeId;

    /**
     * 状态:1可使用；0不可使用
     */
    private Integer status;

    /**
     * 订单交易产品类型
     */
    private String witType;

    /**
     * 货币类型
     */
    private String currency;

    /**
     * 说明
     */
    private String msg;

    /**
     * 交易备注
     */
    private String dealDescribe;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDealDescribe() {

        return dealDescribe;
    }

    public void setDealDescribe(String dealDescribe) {
        this.dealDescribe = dealDescribe;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getWitType() {
        return witType;
    }

    public void setWitType(String witType) {
        this.witType = witType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getFeeId() {
        return feeId;
    }

    public void setFeeId(Integer feeId) {
        this.feeId = feeId;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getIsNotify() {
        return isNotify;
    }

    public void setIsNotify(String isNotify) {
        this.isNotify = isNotify;
    }

    public String getBack() {
        return back;
    }

    public void setBack(String back) {
        this.back = back;
    }

    public String getNotify() {
        return notify;
    }

    public void setNotify(String notify) {
        this.notify = notify;
    }

    public String getExternalOrderId() {
        return externalOrderId;
    }

    public void setExternalOrderId(String externalOrderId) {
        this.externalOrderId = externalOrderId;
    }

    public String getWitInfo() {
        return witInfo;
    }

    public void setWitInfo(String witInfo) {
        this.witInfo = witInfo;
    }

    public String getChanenlId() {
        return chanenlId;
    }

    public void setChanenlId(String chanenlId) {
        this.chanenlId = chanenlId;
    }

    public String getOrderAccount() {
        return orderAccount;
    }

    public void setOrderAccount(String orderAccount) {
        this.orderAccount = orderAccount;
    }

    public BigDecimal getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
    }

    public BigDecimal getDealFee() {
        return dealFee;
    }

    public void setDealFee(BigDecimal dealFee) {
        this.dealFee = dealFee;
    }

    public BigDecimal getDealAmount() {
        return dealAmount;
    }

    public void setDealAmount(BigDecimal dealAmount) {
        this.dealAmount = dealAmount;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getAssociatedId() {
        return associatedId;
    }

    public void setAssociatedId(String associatedId) {
        this.associatedId = associatedId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "DealWit{" +
                "id=" + id +
                ", orderId='" + orderId + '\'' +
                ", associatedId='" + associatedId + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", dealAmount=" + dealAmount +
                ", dealFee=" + dealFee +
                ", actualAmount=" + actualAmount +
                ", orderAccount='" + orderAccount + '\'' +
                ", chanenlId='" + chanenlId + '\'' +
                ", witInfo='" + witInfo + '\'' +
                ", externalOrderId='" + externalOrderId + '\'' +
                ", notify='" + notify + '\'' +
                ", back='" + back + '\'' +
                ", isNotify='" + isNotify + '\'' +
                ", createTime=" + createTime +
                ", submitTime=" + submitTime +
                ", feeId=" + feeId +
                ", status=" + status +
                ", witType='" + witType + '\'' +
                ", currency='" + currency + '\'' +
                ", msg='" + msg + '\'' +
                ", dealDescribe='" + dealDescribe + '\'' +
                '}';
    }
}

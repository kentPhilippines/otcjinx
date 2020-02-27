package alipay.manage.bean;

import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * <p>提现订单</p>
 * @author K
 */
public class Recharge {
    private Integer id;						//数据id
    private String orderId;					//订单id
    private String userId;					//用户id
    private String rechargeType;			//充值交易种类:宝转宝ailipay_qr,宝转卡alipay_card,微信二维码wechar_code,..........(index索引)
    private String isTripartite;			//1存在三方收款渠道；2不存在三方收款渠道(index索引)
    private String orderStatus;				//0预下单1处理中2成功3失败
    private String chargeReason;			//充值理由
    private String depositor;				//备足
    private BigDecimal amount;				//金额
    private String orderType;				//充值类型:1码商充值
    private BigDecimal fee;					//手续费
    private BigDecimal actualAmount;		//真实到账金额
    private String chargeBankcard;			//充值银行卡
    private String phone;					//充值手机号
    private String notfiy;					//成功或者失败回调地址
    private String chargeCard;				//充值银行卡号
    private String chargePerson;			//充值银行卡人
    private Date createTime;
    private Date submitTime;
    private Integer status;
    private String retain1;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getRechargeType() {
        return rechargeType;
    }

    public void setRechargeType(String rechargeType) {
        this.rechargeType = rechargeType == null ? null : rechargeType.trim();
    }

    public String getIsTripartite() {
        return isTripartite;
    }

    public void setIsTripartite(String isTripartite) {
        this.isTripartite = isTripartite == null ? null : isTripartite.trim();
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus == null ? null : orderStatus.trim();
    }

    public String getDepositor() {
        return depositor;
    }

    public void setDepositor(String depositor) {
        this.depositor = depositor == null ? null : depositor.trim();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType == null ? null : orderType.trim();
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public BigDecimal getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
    }

    public String getChargeBankcard() {
        return chargeBankcard;
    }

    public void setChargeBankcard(String chargeBankcard) {
        this.chargeBankcard = chargeBankcard == null ? null : chargeBankcard.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getNotfiy() {
        return notfiy;
    }

    public void setNotfiy(String notfiy) {
        this.notfiy = notfiy == null ? null : notfiy.trim();
    }

    public String getChargeCard() {
        return chargeCard;
    }

    public void setChargeCard(String chargeCard) {
        this.chargeCard = chargeCard == null ? null : chargeCard.trim();
    }

    public String getChargePerson() {
        return chargePerson;
    }

    public void setChargePerson(String chargePerson) {
        this.chargePerson = chargePerson == null ? null : chargePerson.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
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

    public String getChargeReason() {
        return chargeReason;
    }

    public void setChargeReason(String chargeReason) {
        this.chargeReason = chargeReason == null ? null : chargeReason.trim();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
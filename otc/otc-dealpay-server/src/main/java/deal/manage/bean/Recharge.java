package deal.manage.bean;

import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Recharge {
    private Integer id;

    private String orderId;

    private String userId;

    private Integer rechargeType;

    private String orderStatus;

    private String depositor;

    private BigDecimal amount;

    private BigDecimal fee;

    private BigDecimal actualAmount;

    private String chargeBankcard;

    private String phone;

    private String notfiy;

    private String chargeCard;

    private String chargePerson;

    private Date createTime;

    private Date submitTime;

    private Integer status;

    private String retain1;

    private String chargeReason;

    
    private String Time;
    public String getTime() {
		return Time;
	}

	public void setTime(String time) {
		Time = time;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public Integer getRechargeType() {
        return rechargeType;
    }

    public void setRechargeType(Integer rechargeType) {
        this.rechargeType = rechargeType;
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
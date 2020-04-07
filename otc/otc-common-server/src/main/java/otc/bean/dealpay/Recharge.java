package otc.bean.dealpay;

import java.math.BigDecimal;
import java.util.Date;

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
	    private String weight;
	    private String Time;
	    private String charge="1";  //判断页面显示充值 和提现
	    
		public String getCharge() {
			return charge;
		}
		public void setCharge(String charge) {
			this.charge = charge;
		}
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
			this.orderId = orderId;
		}
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
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
			this.orderStatus = orderStatus;
		}
		public String getDepositor() {
			return depositor;
		}
		public void setDepositor(String depositor) {
			this.depositor = depositor;
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
			this.chargeBankcard = chargeBankcard;
		}
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		public String getNotfiy() {
			return notfiy;
		}
		public void setNotfiy(String notfiy) {
			this.notfiy = notfiy;
		}
		public String getChargeCard() {
			return chargeCard;
		}
		public void setChargeCard(String chargeCard) {
			this.chargeCard = chargeCard;
		}
		public String getChargePerson() {
			return chargePerson;
		}
		public void setChargePerson(String chargePerson) {
			this.chargePerson = chargePerson;
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
			this.retain1 = retain1;
		}
		public String getChargeReason() {
			return chargeReason;
		}
		public void setChargeReason(String chargeReason) {
			this.chargeReason = chargeReason;
		}
		public String getWeight() {
			return weight;
		}
		public void setWeight(String weight) {
			this.weight = weight;
		}
}

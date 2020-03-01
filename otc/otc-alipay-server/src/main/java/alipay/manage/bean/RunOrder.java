package alipay.manage.bean;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * <p>流水订单实体</p>
 * @author K
 */
public class RunOrder {
	private Integer id;					//数据id
	private String orderId;				//订单号
	private String associatedId;		//关联订单号
	private String orderAccount;		//关联账户号
	private Integer runOrderType;		//订单类型	流水类型(1充值交易,2系统加款,3交易手续费,4系统扣款,5代付,6代付手续费,7冻结,8解冻,9代付手手续费冻结,10代付冻结,11增加交易点数,12点数扣除,13代理商分润，14码商分润，15人工加款，16人工减款，17人工加点数，18人工减点数，19 卡商交易加钱)
	private BigDecimal amount;			//金额
	private String generationIp;		//关联ip
	private String acountR;				//入款账号
	private String accountW;			//出款账号
	private Date createTime;			//创建时间
	private Date submitTime;			//数据最后一次修改时间
	private Integer status;				//数据状态
	private String runType;				//流失操作状态
	private String amountType;			//支出或收入
	private BigDecimal amountNow;		//流水变动前账户余额
	private String retain4;				
	private String retain5;				
	private String dealDescribe;		//说明
	private String Time;
	private List accountList;
	private List orderAccountList;
	public List getOrderAccountList() {
		return orderAccountList;
	}
	public void setOrderAccountList(List orderAccountList) {
		this.orderAccountList = orderAccountList;
	}
	public List getAccountList() {
		return accountList;
	}
	public void setAccountList(List accountList) {
		this.accountList = accountList;
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
		this.orderId = orderId == null ? null : orderId.trim();
	}
	public String getAssociatedId() {
		return associatedId;
	}
	public void setAssociatedId(String associatedId) {
		this.associatedId = associatedId == null ? null : associatedId.trim();
	}
	public String getOrderAccount() {
		return orderAccount;
	}
	public void setOrderAccount(String orderAccount) {
		this.orderAccount = orderAccount == null ? null : orderAccount.trim();
	}
	public Integer getRunOrderType() {
		return runOrderType;
	}
	public void setRunOrderType(Integer runOrderType) {
		this.runOrderType = runOrderType;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getGenerationIp() {
		return generationIp;
	}
	public void setGenerationIp(String generationIp) {
		this.generationIp = generationIp == null ? null : generationIp.trim();
	}
	public String getAcountR() {
		return acountR;
	}
	public void setAcountR(String acountR) {
		this.acountR = acountR == null ? null : acountR.trim();
	}
	public String getAccountW() {
		return accountW;
	}
	public void setAccountW(String accountW) {
		this.accountW = accountW == null ? null : accountW.trim();
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
	public String getRunType() {
		return runType;
	}
	public void setRunType(String runType) {
		this.runType = runType == null ? null : runType.trim();
	}
	public String getAmountType() {
		return amountType;
	}
	public void setAmountType(String amountType) {
		this.amountType = amountType == null ? null : amountType.trim();
	}
	public BigDecimal getAmountNow() {
		return amountNow;
	}
	public void setAmountNow(BigDecimal amountNow) {
		this.amountNow = amountNow;
	}
	public String getRetain4() {
		return retain4;
	}
	public void setRetain4(String retain4) {
		this.retain4 = retain4 == null ? null : retain4.trim();
	}
	public String getRetain5() {
		return retain5;
	}
	public void setRetain5(String retain5) {
		this.retain5 = retain5 == null ? null : retain5.trim();
	}
	public String getDealDescribe() {
		return dealDescribe;
	}
	public void setDealDescribe(String dealDescribe) {
		this.dealDescribe = dealDescribe == null ? null : dealDescribe.trim();
	}
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
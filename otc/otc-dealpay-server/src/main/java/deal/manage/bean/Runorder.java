package deal.manage.bean;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Runorder {
    private Integer id;

    private String orderId;

    private String associatedId;

    private String orderAccount;

    private Integer runOrderType;

    private BigDecimal amount;

    private String generationIp;

    private String acountR;

    private String accountW;

    private Date createTime;

    private Date submitTime;

    private Integer status;

    private String runType;

    private String amountType;

    private BigDecimal amountNow;

    private String retain4;

    private String retain5;

    private String dealDescribe;

    private String Time;
    
    private List orderAccountList;
    public List getOrderAccountList() {
		return orderAccountList;
	}

	public void setOrderAccountList(List orderAccountList) {
		this.orderAccountList = orderAccountList;
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
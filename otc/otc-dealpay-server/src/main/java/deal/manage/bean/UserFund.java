package deal.manage.bean;

import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class UserFund {
    private Integer id;

    private String userId;

    private String userName;

    private BigDecimal cashBalance;

    private BigDecimal rechargeNumber;

    private BigDecimal freezeBalance;

    private BigDecimal accountBalance;

    private BigDecimal sumDealAmountR;

    private BigDecimal sumDealAmountC;

    private BigDecimal sumProfit;

    private BigDecimal sumAgentProfit;

    private Integer sumOrderCountR;

    private Integer sumOrderCountC;

    private BigDecimal todayDealAmountR;

    private BigDecimal todayDealAmountC;

    private BigDecimal todayProfit;

    private BigDecimal todayAgentProfit;

    private Integer todayOrderCountR;

    private Integer todayOrderCountC;

    private String userType;

    private String agent;

    private String isAgent;

    private Date createTime;

    private Date submitTime;

    private Integer status;

    private Integer version;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public BigDecimal getCashBalance() {
        return cashBalance;
    }

    public void setCashBalance(BigDecimal cashBalance) {
        this.cashBalance = cashBalance;
    }

    public BigDecimal getRechargeNumber() {
        return rechargeNumber;
    }

    public void setRechargeNumber(BigDecimal rechargeNumber) {
        this.rechargeNumber = rechargeNumber;
    }

    public BigDecimal getFreezeBalance() {
        return freezeBalance;
    }

    public void setFreezeBalance(BigDecimal freezeBalance) {
        this.freezeBalance = freezeBalance;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public BigDecimal getSumDealAmountR() {
        return sumDealAmountR;
    }

    public void setSumDealAmountR(BigDecimal sumDealAmountR) {
        this.sumDealAmountR = sumDealAmountR;
    }

    public BigDecimal getSumDealAmountC() {
        return sumDealAmountC;
    }

    public void setSumDealAmountC(BigDecimal sumDealAmountC) {
        this.sumDealAmountC = sumDealAmountC;
    }

    public BigDecimal getSumProfit() {
        return sumProfit;
    }

    public void setSumProfit(BigDecimal sumProfit) {
        this.sumProfit = sumProfit;
    }

    public BigDecimal getSumAgentProfit() {
        return sumAgentProfit;
    }

    public void setSumAgentProfit(BigDecimal sumAgentProfit) {
        this.sumAgentProfit = sumAgentProfit;
    }

    public Integer getSumOrderCountR() {
        return sumOrderCountR;
    }

    public void setSumOrderCountR(Integer sumOrderCountR) {
        this.sumOrderCountR = sumOrderCountR;
    }

    public Integer getSumOrderCountC() {
        return sumOrderCountC;
    }

    public void setSumOrderCountC(Integer sumOrderCountC) {
        this.sumOrderCountC = sumOrderCountC;
    }

    public BigDecimal getTodayDealAmountR() {
        return todayDealAmountR;
    }

    public void setTodayDealAmountR(BigDecimal todayDealAmountR) {
        this.todayDealAmountR = todayDealAmountR;
    }

    public BigDecimal getTodayDealAmountC() {
        return todayDealAmountC;
    }

    public void setTodayDealAmountC(BigDecimal todayDealAmountC) {
        this.todayDealAmountC = todayDealAmountC;
    }

    public BigDecimal getTodayProfit() {
        return todayProfit;
    }

    public void setTodayProfit(BigDecimal todayProfit) {
        this.todayProfit = todayProfit;
    }

    public BigDecimal getTodayAgentProfit() {
        return todayAgentProfit;
    }

    public void setTodayAgentProfit(BigDecimal todayAgentProfit) {
        this.todayAgentProfit = todayAgentProfit;
    }

    public Integer getTodayOrderCountR() {
        return todayOrderCountR;
    }

    public void setTodayOrderCountR(Integer todayOrderCountR) {
        this.todayOrderCountR = todayOrderCountR;
    }

    public Integer getTodayOrderCountC() {
        return todayOrderCountC;
    }

    public void setTodayOrderCountC(Integer todayOrderCountC) {
        this.todayOrderCountC = todayOrderCountC;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType == null ? null : userType.trim();
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent == null ? null : agent.trim();
    }

    public String getIsAgent() {
        return isAgent;
    }

    public void setIsAgent(String isAgent) {
        this.isAgent = isAgent == null ? null : isAgent.trim();
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
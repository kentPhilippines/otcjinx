package alipay.manage.bean;

import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * <p>银行卡列表</p>
 * @author K
 */
public class BankList {
    private Integer id;					//数据id
    private String bankcardId;			//银行卡系统id
    private String bankcardAccount;		//银行卡号
    private String accountHolder;		//银行卡所属人
    private String openAccountBank;		//开户行
    private String bankType;			//银行类型   银行英文简写
    private String bankcode;			//银行卡类型:R收款W出款
    private Integer sysTYpe;			//系统状态 1黑卡 2 可正常使用
    private String account;				//关联账户
    private String phone;				//银行卡手机号
    private BigDecimal limitAmount;		//当日交易限制金额
    private BigDecimal bankAmount;		//银行卡余额
    private Integer cardType;			//系统的卡1，码商的卡2，商户的卡3
    private String qrcodeNote;			//备注
    private Integer status;		
    private Integer isDeal;	
    private Date createTime;
    private Date submitTime;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getBankcardId() {
        return bankcardId;
    }
    public void setBankcardId(String bankcardId) {
        this.bankcardId = bankcardId == null ? null : bankcardId.trim();
    }
    public String getBankcardAccount() {
        return bankcardAccount;
    }
    public void setBankcardAccount(String bankcardAccount) {
        this.bankcardAccount = bankcardAccount == null ? null : bankcardAccount.trim();
    }
    public String getAccountHolder() {
        return accountHolder;
    }
    public void setAccountHolder(String accountHolder) {
        this.accountHolder = accountHolder == null ? null : accountHolder.trim();
    }
    public String getOpenAccountBank() {
        return openAccountBank;
    }
    public void setOpenAccountBank(String openAccountBank) {
        this.openAccountBank = openAccountBank == null ? null : openAccountBank.trim();
    }
    public String getBankType() {
        return bankType;
    }
    public void setBankType(String bankType) {
        this.bankType = bankType == null ? null : bankType.trim();
    }
    public String getBankcode() {
        return bankcode;
    }
    public void setBankcode(String bankcode) {
        this.bankcode = bankcode == null ? null : bankcode.trim();
    }
    public Integer getSysTYpe() {
        return sysTYpe;
    }
    public void setSysTYpe(Integer sysTYpe) {
        this.sysTYpe = sysTYpe;
    }
    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }
    public BigDecimal getLimitAmount() {
        return limitAmount;
    }
    public void setLimitAmount(BigDecimal limitAmount) {
        this.limitAmount = limitAmount;
    }
    public BigDecimal getBankAmount() {
        return bankAmount;
    }
    public void setBankAmount(BigDecimal bankAmount) {
        this.bankAmount = bankAmount;
    }
    public Integer getCardType() {
        return cardType;
    }
    public void setCardType(Integer cardType) {
        this.cardType = cardType;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public Integer getIsDeal() {
        return isDeal;
    }
    public void setIsDeal(Integer isDeal) {
        this.isDeal = isDeal;
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
    public String getQrcodeNote() {
        return qrcodeNote;
    }
    public void setQrcodeNote(String qrcodeNote) {
        this.qrcodeNote = qrcodeNote == null ? null : qrcodeNote.trim();
    }
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
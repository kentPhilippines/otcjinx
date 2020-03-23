package alipay.manage.bean;

import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * <p>提现订单记录</p>
 * @author K
 */
public class Withdraw {
    private Integer id;				//数据id
    private String orderId;			//订单号
    private String userId;			//用户id
    private String withdrawType;	//商户提现1，码商提现2
    private String bankNo;			//提现银行卡
    private String accname;			//提现银行账户
    private String orderStatus;		//提现状态:预下单1处理中2成功3失败
    private String bankName;		//银行姓名
    private BigDecimal amount;		//提现金额
    private BigDecimal fee;			//提现手续费
    private BigDecimal actualAmount;//实际到账费用
    private String mobile;			//提现手机
    private String notify;			//提现成功回调参数
    private Date createTime;			
    private Date submitTime;
    private Integer status;
    private String retain1;	//1  api   代付   				2  后台代付
    private String retain2;		//代付 ip
    private String Time;	
    private String witType;//  代付产品类型
    public String getWitType() {
		return witType;
	}
	public void setWitType(String witType) {
		this.witType = witType;
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
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }
    public String getWithdrawType() {
        return withdrawType;
    }
    public void setWithdrawType(String withdrawType) {
        this.withdrawType = withdrawType == null ? null : withdrawType.trim();
    }
    public String getBankNo() {
        return bankNo;
    }
    public void setBankNo(String bankNo) {
        this.bankNo = bankNo == null ? null : bankNo.trim();
    }
    public String getAccname() {
        return accname;
    }
    public void setAccname(String accname) {
        this.accname = accname == null ? null : accname.trim();
    }
    public String getOrderStatus() {
        return orderStatus;
    }
    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus == null ? null : orderStatus.trim();
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
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
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }
    public String getNotify() {
        return notify;
    }
    public void setNotify(String notify) {
        this.notify = notify == null ? null : notify.trim();
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
    public String getRetain2() {
        return retain2;
    }
    public void setRetain2(String retain2) {
        this.retain2 = retain2 == null ? null : retain2.trim();
    }
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
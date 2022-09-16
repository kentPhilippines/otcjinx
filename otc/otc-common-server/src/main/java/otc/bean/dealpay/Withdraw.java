package otc.bean.dealpay;

import cn.hutool.crypto.symmetric.DES;
import otc.util.DesUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>代付实体类</p>
 * @author hx08
 *
 */
public class Withdraw implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;                //数据id
    private String orderId;            //订单号
    private String userId;            //用户id
    private String withdrawType;    //商户提现1，码商提现2
    private String bankNo;            //提现银行卡				或者提现支付账号【登录账号】
    private String bankNo1;            //提现银行卡		[加密]		或者提现支付账号【登录账号】
    private String accname;            //提现银行账户				或者提现支付宝昵称【支付宝昵称】
    private String orderStatus;        //提现状态:预下单1处理中2成功3失败
    private String bankName;        //银行姓名
    private BigDecimal amount;        //提现金额
    private BigDecimal amount_amount;        //提现金额
    private BigDecimal fee;            //提现手续费
    private BigDecimal actualAmount;//实际到账费用
    private String mobile;            //提现手机
    private String notify;            //提现成功回调参数
    private String appOrderId;        //下游商户订单号【如果为后台代付 则该字段为空】
    private Date createTime;
    private Date submitTime;
    private Date macthTime; //最后一次撮合订单时间
    private Integer status;
    private String retain1;    //1  api   代付   				2  后台代付
    private String retain2;        //代付 ip
    private String Time;
    private String witType;//  代付产品类型
    private String weight;   //代付用户权重  【一般作为下游商户，这个值为空】
    private String apply; //商户后台管理员（申请人）
    private String approval; //订单审核人（后台）
    private String comment; //审核意见
    private String bankcode;//银行标识号
    private String witChannel;
    private String chennelId;
    private String currency;
    private Integer ethFee;  //eth手续费   1 已结算    0 未结算
    private Integer pushOrder;  //1 已推送   0 未推送  默认为已推送

    public String getBankNo1() {
        return bankNo1;
    }

    public void setBankNo1(String bankNo1) {
        this.bankNo1 = bankNo1;
    }

    private String macthMsg;  ///撮合订单 解释
    private Integer macthStatus;  ///撮合订单 状态   1已撮合 未支付     2 已撮合 已支付
    private Integer payStatus;  ///结算状态  1 已扣款结算   2 未扣款结算
    private Integer macthLock;  /// 撮合锁定当前不可以进行任何操作，  默认不锁定 0    1 锁定
    private Integer moreMacth;  /// 是否可以多次撮合[是否挂起]， 0 不可以  1 可以      可以就是挂起
    private Integer macthCount;  ///  撮合次数
    private Integer watingTime;  ///  等待时间 单位 秒


    public Integer getWatingTime() {
        return watingTime;
    }

    public void setWatingTime(Integer watingTime) {
        this.watingTime = watingTime;
    }

    public Date getMacthTime() {
        return macthTime;
    }

    public void setMacthTime(Date macthTime) {
        this.macthTime = macthTime;
    }

    public Integer getMacthCount() {
        return macthCount;
    }

    public void setMacthCount(Integer macthCount) {
        this.macthCount = macthCount;
    }

    public Integer getMoreMacth() {
        return moreMacth;
    }

    public void setMoreMacth(Integer moreMacth) {
        this.moreMacth = moreMacth;
    }

    public Integer getMacthLock() {
        return macthLock;
    }

    public void setMacthLock(Integer macthLock) {
        this.macthLock = macthLock;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Integer getMacthStatus() {
        return macthStatus;
    }

    public void setMacthStatus(Integer macthStatus) {
        this.macthStatus = macthStatus;
    }

    public String getMacthMsg() {
        return macthMsg;
    }

    public void setMacthMsg(String macthMsg) {
        this.macthMsg = macthMsg;
    }

    public Integer getPushOrder() {
        return pushOrder;
    }

    public void setPushOrder(Integer pushOrder) {
        this.pushOrder = pushOrder;
    }

    public Integer getEthFee() {
        return ethFee;
    }

    public void setEthFee(Integer ethFee) {
        this.ethFee = ethFee;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getChennelId() {
        return chennelId;
    }

    public void setChennelId(String chennelId) {
        this.chennelId = chennelId;
    }

    public String getWitChannel() {
        return witChannel;
    }

    public void setWitChannel(String witChannel) {
        this.witChannel = witChannel;
    }

    public String getBankcode() {
        return bankcode;
    }

    public void setBankcode(String bankcode) {
        this.bankcode = bankcode;
	}
	public String getApply() {
		return apply;
	}
	public void setApply(String apply) {
		this.apply = apply;
	}
	public String getAppOrderId() {
		return appOrderId;
	}
	public void setAppOrderId(String appOrderId) {
		this.appOrderId = appOrderId;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
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

    public BigDecimal getAmount_amount() {
        return amount_amount;
    }

    public void setAmount_amount(BigDecimal amount_amount) {
        this.amount_amount = amount_amount;
    }

    public String getBankNo() {
        return DesUtil.decryptStr(bankNo);
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

    public String getApproval() {
        return approval;
    }

    public void setApproval(String approval) {
        this.approval = approval;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
	public String toString() {
		return "Withdraw [id=" + id + ", orderId=" + orderId + ", userId=" + userId + ", withdrawType=" + withdrawType
				+ ", bankNo=" + bankNo + ", accname=" + accname + ", orderStatus=" + orderStatus + ", bankName="
				+ bankName + ", amount=" + amount + ", fee=" + fee + ", actualAmount=" + actualAmount + ", mobile="
				+ mobile + ", notify=" + notify + ", createTime=" + createTime + ", submitTime=" + submitTime
				+ ", status=" + status + ", retain1=" + retain1 + ", retain2=" + retain2 + ", Time=" + Time
				+ ", witType=" + witType + ", approval="+ approval + ", comment=" + comment + "]";
	}
}

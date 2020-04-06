package deal.manage.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class UserInfo implements Serializable{
	   private static final long serialVersionUID =-3774809486372838018L;
	    private Integer id;					//数据id
	    private String userId;				//用户id
	    private String userName;			//用户姓名
	    private String password;			//登录密码
	    private String payPasword;			//支付密码
	    private String salt;				//盐值
	    private Integer userType;			//用户类型：商户1 码商2
	    private Integer switchs;			//当前用户总开关 1开启0关闭【码商商户后台控制,该值只能在后台显示】【交易总开关】
	    private String userNode;			//备注
	    private String email;				//密码重置邮箱
	    private String agent;				//代理商id【如果存在代理商则存在数据,如果不存在代理商则为null】
	    private String isAgent;				//是否为代理商:1代理商2普通码商【分润结算类型看用户类型userType】
	    private BigDecimal credit;			//信用等级为默认为100,掉单一次,或者出现故障一次减分0.001分,可人为加分
	    private Integer receiveOrderState;	//是否处于入款接单状态 1 接单 2 暂停接单【下游商户则为是否可以交易】
	    private Integer remitOrderState;	//是否处于入款接单状态 1 接单 2 暂停接单【下游商户则为是否可以代付】
	    private String QQ;					//联系方式
	    private String telegram;			//联系方式
	    private String skype;				//联系方式
	    private Date createTime;
	    private Date submitTime;
	    private Integer status;
	    private String privateKey;
	    private String publicKey;
	    private String minAmount;
	    private String maxAmount;
	    private BigDecimal totalAmount;
	    private String startTime;
	    private String endTime;
	    private Integer timesTotal;
	    private String amount;			//页面展示金额
	    private String witip; //代付ip
	    private String queueList;//供应队列code  以逗号分隔

		private String inviteCode;//邀请码，注册专用
	    private String newPassword;//新的登录密码【修改密码专用】
	    private String newPayPassword;//新的支付密码【修改支付密码专用】
	    private Integer toDayOrderCount;//
	    private String online;
	    private String agentCount;

	    private String fee;
	    private String cardFee;
	    
	    
	public String getFee() {
			return fee;
		}

		public void setFee(String fee) {
			this.fee = fee;
		}

		public String getCardFee() {
			return cardFee;
		}

		public void setCardFee(String cardFee) {
			this.cardFee = cardFee;
		}

	public String getInviteCode() {
			return inviteCode;
		}

		public void setInviteCode(String inviteCode) {
			this.inviteCode = inviteCode;
		}

		public String getNewPayPassword() {
			return newPayPassword;
		}

		public void setNewPayPassword(String newPayPassword) {
			this.newPayPassword = newPayPassword;
		}

		public Integer getToDayOrderCount() {
			return toDayOrderCount;
		}

		public void setToDayOrderCount(Integer toDayOrderCount) {
			this.toDayOrderCount = toDayOrderCount;
		}

		public String getOnline() {
			return online;
		}

		public void setOnline(String online) {
			this.online = online;
		}

		public String getAgentCount() {
			return agentCount;
		}

		public void setAgentCount(String agentCount) {
			this.agentCount = agentCount;
		}

		public void setNewPassword(String newPassword) {
			this.newPassword = newPassword;
		}

	public String getMinAmount() {
			return minAmount;
		}

		public void setMinAmount(String minAmount) {
			this.minAmount = minAmount;
		}

		public String getMaxAmount() {
			return maxAmount;
		}

		public void setMaxAmount(String maxAmount) {
			this.maxAmount = maxAmount;
		}

		public String getAmount() {
			return amount;
		}

		public void setAmount(String amount) {
			this.amount = amount;
		}

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getPayPasword() {
        return payPasword;
    }

    public void setPayPasword(String payPasword) {
        this.payPasword = payPasword == null ? null : payPasword.trim();
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt == null ? null : salt.trim();
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Integer getSwitchs() {
        return switchs;
    }

    public void setSwitchs(Integer switchs) {
        this.switchs = switchs;
    }

    public String getUserNode() {
        return userNode;
    }

    public void setUserNode(String userNode) {
        this.userNode = userNode == null ? null : userNode.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
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

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public Integer getReceiveOrderState() {
        return receiveOrderState;
    }

    public void setReceiveOrderState(Integer receiveOrderState) {
        this.receiveOrderState = receiveOrderState;
    }

    public Integer getRemitOrderState() {
        return remitOrderState;
    }

    public void setRemitOrderState(Integer remitOrderState) {
        this.remitOrderState = remitOrderState;
    }

    public String getQQ() {
        return QQ;
    }

    public void setQQ(String QQ) {
        this.QQ = QQ == null ? null : QQ.trim();
    }

    public String getTelegram() {
        return telegram;
    }

    public void setTelegram(String telegram) {
        this.telegram = telegram == null ? null : telegram.trim();
    }

    public String getSkype() {
        return skype;
    }

    public void setSkype(String skype) {
        this.skype = skype == null ? null : skype.trim();
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


    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getTimesTotal() {
        return timesTotal;
    }

    public void setTimesTotal(Integer timesTotal) {
        this.timesTotal = timesTotal;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime == null ? null : startTime.trim();
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime == null ? null : endTime.trim();
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey == null ? null : privateKey.trim();
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey == null ? null : publicKey.trim();
    }

    public String getWitip() {
        return witip;
    }

    public void setWitip(String witip) {
        this.witip = witip == null ? null : witip.trim();
    }

    public String getQueueList() {
        return queueList;
    }

    public void setQueueList(String queueList) {
        this.queueList = queueList == null ? null : queueList.trim();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

	public String getNewPassword() {
		// TODO Auto-generated method stub
		return null;
	}
}
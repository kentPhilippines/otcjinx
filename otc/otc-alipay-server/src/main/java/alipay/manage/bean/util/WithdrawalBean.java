package alipay.manage.bean.util;

public class WithdrawalBean {
/**
 * 	hmac				M(32)			接口加密摘要，按ASC码顺序排列后MD5
	appid				M(20)			交易请求平台ID(由平台统一分配给外部商户的)
	apporderid			M(20)			用户的订单号
	ordertime			M(14)			交易时间，格式: yyyyMMddHHmmss
	orderbody			M(20)			订单描述
	amount				M(14)			金额（元）
	bankno				O(20)			发卡行号
	banksettno			M(32)			清算银行号
	acctno				M(20)			银行卡号，des加密
	acctname			M(20)			银行卡持有者姓名，des加密
	bankname			M(20)			银行名称 
	mobile				M(11)			银行卡持有者手机，des加密
	bankcode			M(10)			银行编码，见字典
	certificatecode		M(18)			身份证编码，des加密
	cnapsname			C(50)			开户支行名称，视不同渠道(是否为空) 
	province			M(30)			省
	city				M(30)			市
	notifyurl			M(50)			异步通知地址
 */
	private String appid;						//商户号
	private String apporderid;					//代付订单号
	private String ordertime;					//请求时间yyyyMMddHHmmss
	private String amount;						//代付金额
	private String acctno;						//银行卡号 
    private String bankName;		//银行姓名
	private String acctname;					//银行卡持有者姓名
	private String bankcode;					//银行编码
	private String notifyurl;					//回调
	private String dpaytype;					//代付类型    Bankcard     银行卡 			Alipay		  支付宝 			Wechar		  微信
	//以下为选填
	private String mobile;						//手机号
	private String cnapsname;					//开户支行
	private String province;					//省
	private String city;						//市
	
	private String rsasign;						//签名
	
	private String ip;

	private String apply;						//后台申请人
	
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getDpaytype() {
		return dpaytype;
	}
	public void setDpaytype(String dpaytype) {
		this.dpaytype = dpaytype;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getRsasign() {
		return rsasign;
	}
	public void setRsasign(String rsasign) {
		this.rsasign = rsasign;
	}
	public String getCnapsname() {
		return cnapsname;
	}
	public void setCnapsname(String cnapsname) {
		this.cnapsname = cnapsname;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getApporderid() {
		return apporderid;
	}
	public void setApporderid(String apporderid) {
		this.apporderid = apporderid;
	}
	public String getOrdertime() {
		return ordertime;
	}
	public void setOrdertime(String ordertime) {
		this.ordertime = ordertime;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getAcctno() {
		return acctno;
	}
	public void setAcctno(String acctno) {
		this.acctno = acctno;
	}
	public String getAcctname() {
		return acctname;
	}
	public void setAcctname(String acctname) {
		this.acctname = acctname;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getBankcode() {
		return bankcode;
	}
	public void setBankcode(String bankcode) {
		this.bankcode = bankcode;
	}
	public String getNotifyurl() {
		return notifyurl;
	}
	public void setNotifyurl(String notifyurl) {
		this.notifyurl = notifyurl;
	}

	public String getApply() {
		return apply;
	}

	public void setApply(String apply) {
		this.apply = apply;
	}
}

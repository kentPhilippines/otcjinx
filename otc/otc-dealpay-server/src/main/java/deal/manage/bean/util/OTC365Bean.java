package deal.manage.bean.util;
public class OTC365Bean {

	private String auth = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiIsImp0aSI6IjgxNzAyIn0.eyJqdGkiOiI4MTcwMiIsImlhdCI6MTU3NTc3MzM5NywiZXhwIjoxNTc1Nzc0NTk3fQ.DVUXIzusejYsulkQUeoR30dIVglCWgcgj-kH7Vwupdc";//socket验证订单时候的密钥
	private String balanceStatus = "1";
	private String cancel_time = null;
	private String cancel_times = "0";
	private String confirm_time = null;
	private String ddpayStatus = "1";
	private String ddpayTime = null;
	private String dealer = "任高翔";//交易员
	private String dealer_bankid = "573";//交易员银行编号
	private String dealer_uid = "41607";//交易员id
	private String flashOrder = "1";
	private String is_aipay = "2";
	private String kyc = "2";
	private String operatStatus = "1";//操作状态
	private String orderAmount = "200";//结算金额
	private String orderArrow = "BUY";//订单类别
	private String orderCoin = "USDT";//货币单位
	private String orderId = "600169";
	private String orderNumber = "201912081040382";  //订单号
	private orderPayinfo orderPayinfo;
	private String orderPayway = "1";
	private String orderStatus = "1";//订单状态
	private String orderSumPrice = "1000.00";//订单交易金额
	private String orderTime = "2019-12-08 10:49:52";//订单发起时间
	private String orderUid = "81702";//订单交易本地唯一编号
	private String orderUnitPrice = "7.05";//今日虚拟币单价
	private String order_amount = "0";//
	private String order_duration = "0";
	private String order_price = "0";
	private String originId = null;
	private String payNotice = "1";
	private String platform_user_source = "93";
	private String popStatus = "0";
	private String sync_url = "http://47.244.240.216:45465?company_order_num=BOD01201912081049513791006";// 回调地址
	private String time_out = "15";//超时时间  分钟  配置的超时时间
	private String validTime = "900";//订单有效时间  秒为单位
	  public orderPayinfo getOrderPayinfo() {
		return orderPayinfo;
	}
	public void setOrderPayinfo(orderPayinfo orderPayinfo) {
		this.orderPayinfo = orderPayinfo;
	}
	public class orderPayinfo {
		private String bankName = "光大银行";//银行名称
		private String bank_city = "";//银行城市
		private String bank_id = "573";//银行本地编号
		private String bank_province = "";//
		private String day_sum = "16000.00";//今日银行卡交易总金额
		private String payNo = "6226620504305772";//银行卡号
		private String realName = "任高翔";//所属人姓名
		private String receive_limit = "0";
		private String registerAddress = "莱州支行";//开户行所在地
		public String getBankName() {
			return bankName;
		}
		public void setBankName(String bankName) {
			this.bankName = bankName;
		}
		public String getBank_city() {
			return bank_city;
		}
		public void setBank_city(String bank_city) {
			this.bank_city = bank_city;
		}
		public String getBank_id() {
			return bank_id;
		}
		public void setBank_id(String bank_id) {
			this.bank_id = bank_id;
		}
		public String getBank_province() {
			return bank_province;
		}
		public void setBank_province(String bank_province) {
			this.bank_province = bank_province;
		}
		public String getDay_sum() {
			return day_sum;
		}
		public void setDay_sum(String day_sum) {
			this.day_sum = day_sum;
		}
		public String getPayNo() {
			return payNo;
		}
		public void setPayNo(String payNo) {
			this.payNo = payNo;
		}
		public String getRealName() {
			return realName;
		}
		public void setRealName(String realName) {
			this.realName = realName;
		}
		public String getReceive_limit() {
			return receive_limit;
		}
		public void setReceive_limit(String receive_limit) {
			this.receive_limit = receive_limit;
		}
		public String getRegisterAddress() {
			return registerAddress;
		}
		public void setRegisterAddress(String registerAddress) {
			this.registerAddress = registerAddress;
		}
	}
	

	public String getAuth() {
		return auth;
	}
	public void setAuth(String auth) {
		this.auth = auth;
	}
	public String getBalanceStatus() {
		return balanceStatus;
	}
	public void setBalanceStatus(String balanceStatus) {
		this.balanceStatus = balanceStatus;
	}
	public String getCancel_time() {
		return cancel_time;
	}
	public void setCancel_time(String cancel_time) {
		this.cancel_time = cancel_time;
	}
	public String getCancel_times() {
		return cancel_times;
	}
	public void setCancel_times(String cancel_times) {
		this.cancel_times = cancel_times;
	}
	public String getConfirm_time() {
		return confirm_time;
	}
	public void setConfirm_time(String confirm_time) {
		this.confirm_time = confirm_time;
	}
	public String getDdpayStatus() {
		return ddpayStatus;
	}
	public void setDdpayStatus(String ddpayStatus) {
		this.ddpayStatus = ddpayStatus;
	}
	public String getDdpayTime() {
		return ddpayTime;
	}
	public void setDdpayTime(String ddpayTime) {
		this.ddpayTime = ddpayTime;
	}
	public String getDealer() {
		return dealer;
	}
	public void setDealer(String dealer) {
		this.dealer = dealer;
	}
	public String getDealer_bankid() {
		return dealer_bankid;
	}
	public void setDealer_bankid(String dealer_bankid) {
		this.dealer_bankid = dealer_bankid;
	}
	public String getDealer_uid() {
		return dealer_uid;
	}
	public void setDealer_uid(String dealer_uid) {
		this.dealer_uid = dealer_uid;
	}
	public String getFlashOrder() {
		return flashOrder;
	}
	public void setFlashOrder(String flashOrder) {
		this.flashOrder = flashOrder;
	}
	public String getIs_aipay() {
		return is_aipay;
	}
	public void setIs_aipay(String is_aipay) {
		this.is_aipay = is_aipay;
	}
	public String getKyc() {
		return kyc;
	}
	public void setKyc(String kyc) {
		this.kyc = kyc;
	}
	public String getOperatStatus() {
		return operatStatus;
	}
	public void setOperatStatus(String operatStatus) {
		this.operatStatus = operatStatus;
	}
	public String getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}
	public String getOrderArrow() {
		return orderArrow;
	}
	public void setOrderArrow(String orderArrow) {
		this.orderArrow = orderArrow;
	}
	public String getOrderCoin() {
		return orderCoin;
	}
	public void setOrderCoin(String orderCoin) {
		this.orderCoin = orderCoin;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getOrderPayway() {
		return orderPayway;
	}
	public void setOrderPayway(String orderPayway) {
		this.orderPayway = orderPayway;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getOrderSumPrice() {
		return orderSumPrice;
	}
	public void setOrderSumPrice(String orderSumPrice) {
		this.orderSumPrice = orderSumPrice;
	}
	public String getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
	public String getOrderUid() {
		return orderUid;
	}
	public void setOrderUid(String orderUid) {
		this.orderUid = orderUid;
	}
	public String getOrderUnitPrice() {
		return orderUnitPrice;
	}
	public void setOrderUnitPrice(String orderUnitPrice) {
		this.orderUnitPrice = orderUnitPrice;
	}
	public String getOrder_amount() {
		return order_amount;
	}
	public void setOrder_amount(String order_amount) {
		this.order_amount = order_amount;
	}
	public String getOrder_duration() {
		return order_duration;
	}
	public void setOrder_duration(String order_duration) {
		this.order_duration = order_duration;
	}
	public String getOrder_price() {
		return order_price;
	}
	public void setOrder_price(String order_price) {
		this.order_price = order_price;
	}
	public String getOriginId() {
		return originId;
	}
	public void setOriginId(String originId) {
		this.originId = originId;
	}
	public String getPayNotice() {
		return payNotice;
	}
	public void setPayNotice(String payNotice) {
		this.payNotice = payNotice;
	}
	public String getPlatform_user_source() {
		return platform_user_source;
	}
	public void setPlatform_user_source(String platform_user_source) {
		this.platform_user_source = platform_user_source;
	}
	public String getPopStatus() {
		return popStatus;
	}
	public void setPopStatus(String popStatus) {
		this.popStatus = popStatus;
	}
	public String getSync_url() {
		return sync_url;
	}
	public void setSync_url(String sync_url) {
		this.sync_url = sync_url;
	}
	public String getTime_out() {
		return time_out;
	}
	public void setTime_out(String time_out) {
		this.time_out = time_out;
	}
	public String getValidTime() {
		return validTime;
	}
	public void setValidTime(String validTime) {
		this.validTime = validTime;
	}
	
}

package otc.result;

import cn.hutool.json.JSONUtil;

/**
 * <p>全局返回值类</p>
 * @author K
 *
 * @description
 * {"success":false,"message":"商户交易费率未配置或未开通","result":null,"code":null}
 * 说明：费率没有开通请检查通道编码
 * {"success":false,"message":"签名验证失败","result":null,"code":null}
 * 说明：验签错误
 * 正确响应结果集案例：
 * {"success":true,"message":"支付处理中","result":{"sussess":true,"cod":0,"openType":1,"returnUrl":"http://api.sdsadsadas.com/gateway/bankgateway/payorder/order/43543.html"},"code":1}
 */
public class Result {
	/**
	 * True 成功  false  失败
	 * 【以该数据判断订单是否成功】
	 *
	 */
	private boolean success;
	/**
	 * 提示消息
	 */
	private String message = "";

	/**
	 * sussess  True 成功  false  失败
	 *
	 * returnUrl  支付连接
	 *
	 * openType 1：URL  0 ： HTML
	 *
	 * cod 1 ：成功
	 *
	 */
	private Object result = "";

	/**
	 * 1 ：成功
	 */
	private Integer code;

	public Result(boolean b, Integer o, Object result) {
		this.success=b;
		this.result=result;
		this.code=o;

	}

	/**
	 * <p>成功无返回值</p>
	 * @return
	 */
	public static Result buildSuccess() {
		return new Result(true, null, null, null);
	}
	/**
	 * <p>成功、有通知消息，无code无结果</p>
	 * @param message
	 * @return
	 */
	public static Result buildSuccessMessage(String message) {
		return new Result(true, message, null, null);
	}
	/**
	 * <p>成功、有结果集，无通知，无code</p>
	 * @param result
	 * @return
	 */
	public static Result buildSuccessResult(Object result) {
		return new Result(true, null, result,null);
	}

	/**
	 * <p>成功、无结果集，无通知，无code</p>
	 * @param result
	 * @return
	 */
	public static Result buildSuccessResult( ) {
		return new Result(true, null, null,null);
	}
	/**
	 * <p>成功、有结果集，无通知，无code</p>
	 * @param result
	 * @return
	 */
	public static Result buildSuccessResult(String msg ,Object result) {
		return new Result(true, msg, result, 1);
	}
	public static Result buildSuccessResultCode(String msg ,Object result,Integer i) {
		return new Result(true, msg, result,i);
	}
	/**
	 * <p>返回结果为成功,无返回提示,结果集</p>
	 * <li>boolean success : true // 请求操作成功</li>
	 * <li>String  message : null // 提示消息null</li>
	 * <li>Object  result  : result // 返回结果集result</li>
	 * @param result 返回结果集result
	 * @return
	 */
	public static Result buildSuccessResults(Object result) {
		return new Result(true, null, result);
	}
	/**
	 * <p>失败有结果集</p>
	 * @param result
	 * @return
	 */
	public static Result buildFailResult(Object result) {
		return new Result(false, null, result,null);
	}
	/**
	 * <p>失败</p>
	 * @param result
	 * @return
	 */
	public static Result buildFail( ) {
		return new Result(false, null, null,null);
	}
	/**
	 * <p>失败有通知消息</p>
	 * @param result
	 * @return
	 */
	public static Result buildFailMessage(String message) {
		return new Result(false, message, null,null);
	}

	public Result(boolean success, String message, Object result, Integer code) {
		super();
		this.success = success;
		this.message = message;
		this.result = result;
		this.code = code;
	}
	public Result() {
		super();
	}

	public static Result buildSuccessMessageCode(String msg, Object o, int i) {
		return new Result(true, msg, o,i);
	}
	public static Result buildFailMessageCode(String msg, int i) {
		return new Result(false, msg, null,i);
	}

	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "Result [success=" + success + ", message=" + message + ", result=" + result + ", code=" + code + "]";
	}
	public  String toJson(){
		return JSONUtil.parse(this).toString();
	}
}

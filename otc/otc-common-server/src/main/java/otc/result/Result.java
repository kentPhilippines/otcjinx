package otc.result;

/**
 * <p>全局返回值类</p>
 * @author K
 */
public class Result {
	private boolean success; 
	private String message; 
	private Object result; 
	private Integer code; 
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
		return new Result(true, msg, result,null);
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
}

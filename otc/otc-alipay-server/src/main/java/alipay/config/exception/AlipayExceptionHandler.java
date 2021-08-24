package alipay.config.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import otc.result.Result;

@RestControllerAdvice
public class AlipayExceptionHandler {
	private static final Logger log = LoggerFactory.getLogger(AlipayExceptionHandler.class);
	/**
	 * 请求方式不支持
	 */
	@ExceptionHandler({ HttpRequestMethodNotSupportedException.class })
	public Result handleException(HttpRequestMethodNotSupportedException e) {
		log.error(e.getMessage(), e);
		return Result.buildFailMessage("不支持' " + e.getMethod() + "'请求");
	}
	/**
	 * 拦截未知的运行时异常
	 */
	@ExceptionHandler(RuntimeException.class)
	public Result notFount(Throwable e) {
		log.error("运行时异常:", e.getMessage());
		return Result.buildFailMessage("程序异常:" + e.getMessage());
	}
	/**
	 * 系统异常
	 */
	@ExceptionHandler(Exception.class)
	public Result handleException(Throwable e) {
		log.error(e.getMessage(), e.getMessage());
		return Result.buildFailMessage("服务器错误，请联系管理员");
	}
}

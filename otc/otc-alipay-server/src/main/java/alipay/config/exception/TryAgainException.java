package alipay.config.exception;

/**
 * <p>用户表行锁并发时抛出异常</p>
 */
public class TryAgainException extends RuntimeException {
    private static final long serialVersionUID=1L;
    public TryAgainException(){}
    public TryAgainException(String message){
        super(message);
    }
}

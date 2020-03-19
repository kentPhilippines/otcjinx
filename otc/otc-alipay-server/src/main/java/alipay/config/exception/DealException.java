package alipay.config.exception;

/**
 * <p>交易异常</p>
 */
public class DealException extends RuntimeException {
    private static final long serialVersionUID=1L;
    public DealException(){}
    public DealException(String message){
        super(message);
    }
}

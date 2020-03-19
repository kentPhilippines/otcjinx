package alipay.config.exception;

/**
 * 发生其他错误时的异常
 */
public class OtherErrors extends RuntimeException {
    private static final long serialVersionUID=1L;
    public OtherErrors(){}
    public OtherErrors(String messsage){
        super(messsage);
    }
}

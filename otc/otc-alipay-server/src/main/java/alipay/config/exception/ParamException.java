package alipay.config.exception;

/**
 * 参数为null的异常
 */
public class ParamException extends Throwable {
    private static final long serialVersionUID=1L;
    public ParamException(){}
    public ParamException(String message){
        //super(message);
    }
}

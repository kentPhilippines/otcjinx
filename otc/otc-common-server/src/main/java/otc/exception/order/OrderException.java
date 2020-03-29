package otc.exception.order;

import otc.exception.BaseException;

public class OrderException extends BaseException {
	private static final long serialVersionUID = 1L;
	public OrderException(String code, Object[] args) {
		super("user", code, args, null);
	}
}

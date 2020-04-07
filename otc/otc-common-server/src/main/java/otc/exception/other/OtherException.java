package otc.exception.other;

import otc.exception.BaseException;

public class OtherException extends BaseException {
	private static final long serialVersionUID = 1L;
	public OtherException(String code, Object[] args) {
		super("user", code, args, null);
	}
}

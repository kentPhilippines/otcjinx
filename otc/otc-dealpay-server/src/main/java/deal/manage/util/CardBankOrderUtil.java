package deal.manage.util;

import org.springframework.stereotype.Component;

import otc.result.Result;
@Component
public class CardBankOrderUtil {

	/**
	 * <p>订单置为成功</p>
	 * @param orderId				订单号
	 * @param operation				是否为人工操作
	 * @param ip					操作ip
	 * @return
	 */
	public Result updataOrderStatusSu(String orderId, boolean operation, String ip) {
		return null;
	}

	/**
	 * <p>订单置为失败</p>
	 * @param orderId				订单号
	 * @param operation				是否为人工操作
	 * @param ip					操作ip
	 * @return
	 */
	public Result updataOrderStatusEr(String orderId, boolean operation, String ip) {
		return null;
	}

}

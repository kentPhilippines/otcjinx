package alipay.manage.service.impl;

import alipay.manage.mapper.WithdrawMapper;
import alipay.manage.service.WithdrawService;
import org.springframework.stereotype.Component;
import otc.api.alipay.Common;
import otc.bean.dealpay.Withdraw;

import javax.annotation.Resource;

@Component
public class WithdrawServiceImpl implements WithdrawService {
	@Resource
	private WithdrawMapper withdrawDao;

	@Override
	public boolean addOrder(Withdraw witb) {
		int insertSelective = withdrawDao.insertSelective(witb);
		return insertSelective > 0 && insertSelective < 2;
	}

	@Override
	public Withdraw findOrderId(String orderId) {
		return withdrawDao.findWitOrder(orderId);
	}

    @Override
    public Withdraw findOrderByApp(String appId, String appOrderId) {
        return withdrawDao.findOrderByApp(appId, appOrderId);
    }

    @Override
    public void updateWitError(String orderId) {
        withdrawDao.updataOrderStatusEr(orderId, Common.Order.Wit.ORDER_STATUS_ER, "账户扣减失败，订单失败");
    }

    @Override
    public Withdraw findEthFee() {
        return withdrawDao.findEthFee();
    }

    @Override
    public void updateEthFee(String orderId, String hash) {
        withdrawDao.updateEthFee(orderId, hash);
    }

}

package alipay.manage.service;

import otc.bean.dealpay.Withdraw;

import java.math.BigDecimal;
import java.util.List;

public interface WithdrawService {
	/**
	 * <p>代付订单生成</p>
	 *
	 * @param witb
	 * @return
	 */
	boolean addOrder(Withdraw witb);

	/**
	 * <p>根据代付订单号查询订单</p>
	 *
	 * @param orderId
	 * @return
	 */
	Withdraw findOrderId(String orderId);

	Withdraw findOrderByApp(String appId, String appOrderId);

	/**
	 * 代付订单单纯修改为失败，兼容账户扣减失败
	 *
	 * @param orderId
	 */
	void updateWitError(String orderId);


	/**
	 * 查询当前未结算eth矿工费用代付订单
	 *
	 * @return
	 */
	Withdraw findEthFee();

	/**
	 * 标记当前代付矿工费已经结算
	 *
	 * @param orderId
	 */
	void updateEthFee(String orderId, String hash);

	Withdraw findOrderByAppSum(String appId);


	/**
	 * 查询未推送订单
	 *
	 * @return
	 */
    List<Withdraw> findSuccessAndNotAmount();

    /**
     * 修改订单 ： 已推送处理
     *
     * @param orderId
     */
    void updateSuccessAndAmount(String orderId);

    boolean updatePush(String orderId);

    /**
     * 查询未推送代付订单
     *
     * @return
     */
    List<Withdraw> findNotPush();



    void updateMsg(String orderId, String msg);


	/**
	 * 修改订单为【可再次推送】
	 * @param orderId
	 * @return
	 */
	boolean updatePushAgent(String orderId);

	List<Withdraw> findChannelAndType(String channel, String s);


	/**
	 *  填充 代付订单状态
	 * @param orderId				订单号
	 * @param macthStatus			撮合状态  1撮合未支付
	 * @param macthLock				撮合 订单锁定
	 *                              	 添加当前撮合时间
	 * @return
	 */
    boolean macthLock(String orderId, Integer macthStatus, Integer macthLock);


	/**
	 * // 获取规则：
	 * 1 	不是当前 商户的，
	 * 2，  订单为非锁定状态，
	 * 3，	订单主状态为 审核中
	 * 4 ， 最后一次撮合时间已经过了10分钟 且 订单 为挂起状态
	 * @param orderAccount
	 * @return
	 */
    List<Withdraw> findMacthOrder(String orderAccount);


	/**
	 * 撮合订单解锁
	 *  撮合未支付 ， 且 审核中 且
	 */
    void macthOrderUnLock();

	/**
	 * 撮合订单次数加1
	 *
	 */
	boolean macthCountPush(String orderId);


	/**
	 * 当前订单已结算
	 * @param orderId
	 * @return
	 */
    boolean isPayStatus(String orderId);

    List<Withdraw> findWaitPush();

	List<Withdraw> findSuccessAndAmount();


	boolean updateAmount(BigDecimal amount, BigDecimal fee, BigDecimal actualAmount, String orderId);

    void updateEr(String orderId);

}

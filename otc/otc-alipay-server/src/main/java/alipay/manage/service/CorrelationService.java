package alipay.manage.service;

import alipay.manage.bean.Correlation;
import alipay.manage.bean.CorrelationData;
import alipay.manage.bean.util.UserCountBean;

import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * tree闭包表
 * 设计思路 ， 内容代码是完整的， 可以套用任何多级大数据量账户关系查询
 */
public interface CorrelationService {
	/**
	 * 插入数据
	 *
	 * @param record
	 * @return
	 */
	int insertEntity(Correlation record);

	List<Correlation> selectByExample(Correlation correlationEntity);

	List<Correlation> selectAllParentRelList(String accountId);

	Correlation findEntityByParentId(String accountId);

	/**
	 * <p>新增一个数据</p>
	 * @param entity
	 * @return
	 */
	public boolean addCorrelationDate(CorrelationData  entity);

	/**
	 *	<p>更新数据数据统计服务</p>
	 * @param orderId
	 */
	public void updateCorrelationDate(String orderId);

	/**
	 * <p>开户时添加上下级关系</p>
	 * @param myId
	 * @param myName
	 * @param myType
	 * @param parentId
	 * @param parentName
	 * @return
	 */
	boolean openAccount(String myId ,  String myName,Integer myType ,  String parentId , String parentName);

	/**
	 * <p>新增一个支付宝的时候修改上下级关系数据里的支付宝参数</p>
	 * @param myName			登录名
	 * @param myId				账户数据id
	 * @param mediumId			支付宝数据id
	 * @return
	 */
	boolean addAccountMedium(String myName,String myId , Integer  mediumId) ;

	/**
	 * <p>删除支付宝的时候修改上下级关系数据里的支付宝参数</p>
	 * @param myName			登录名
	 * @param myId				账户数据id
	 * @param mediumId			支付宝数据id
	 * @return
	 */
	boolean deleteAccountMedium(String myName,String myId , Integer  mediumId);

	/**
	 * <p>将用户关系表中的会员性质升级为代理商</p>
	 * @param userId
	 * @return
	 */
	boolean upgradeAaccount(@NotNull String userId);


	/**
	 * <p>查询个人数据【账户代理关系数据】</p>
	 * @param id
	 * @return
	 */
	UserCountBean findMyDateAgen(String id);

	/**
	 * <p>多级交易流水，多级交易笔数</p>
	 * @param id	账户id
	 * @return
	 */
	UserCountBean findDealDate(@NotNull String id);

	/**
	 * <p>元素1为交易情况</p>
	 * <p>元素2为代理关系情况</p>
	 * @param id
	 * @return
	 */
	int[][] findOnline(String id);

	/**
	 * <p>根据账号查询自己顶代账号</p>
	 * @param userId
	 * @return
	 */
	String findAgent(String userId);

}

package alipay.manage.service;


import java.util.List;

import otc.bean.alipay.Medium;

public interface MediumService {
    /**
     * <p>添加收款媒介</p>
     * @param medium
     * @return
     */
    boolean addMedium(Medium medium);

    /**
     * <p>分页拆查询收款媒介</p>
     * @param medium
     * @return
     */
    List<Medium> findMedium(Medium medium);
     /**
      * <p>分页查询收款媒介:当查询条件为空查询收款媒介所属商户</p>
      * @param qrcodeId
      * @return
      */
    List<Medium> findAllMedium(String qrcodeId);    
    /**
     * <p>通过媒介系统 编号查询唯一媒介</p>
     * @param mediumId
     * @return
     */
    Medium findMediumById(String mediumId);
    /**
     * <p>根据媒介的系统编号修改媒介</p>
     * @param medium
     * @return
     */
    Boolean  updataMediumById(Medium medium);

    /**
     * <p>查询当前所有可用的收款媒介</p>
     * @param mediumAlipay 		收款媒介标识
     * @return
     */
    List<Medium> findIsDealMedium(String mediumAlipay);

    /**
     * <p>删除收款媒介</p>
     * @param mediumId
     * @return
     */
    Boolean deleteMedium(String mediumId);

    /**
     * <p>查询自己的支付宝账号</p>
     * @param accountId
     * @return
     */
    List<Medium> findIsMyMediumPage(String accountId);

    /**
     * <p>根据id号置为不可用</p>
     * @param id
     * @return
     */
    boolean updataMediumStatusEr(String id);

    /**
     * <p>根据id查询收款媒介</p>
     * @param id
     * @return
     */
    Medium findMediumId(String id);

    /**
     * <p>置为可受收款</p>
     * @param id
     * @return
     */
    boolean updataMediumStatusSu(String id);

    /**
     * <p>关闭可接单</p>
     * @param qr
     * @return
     */
    boolean updataQr(Medium qr);

    boolean selectOpenAlipayAccount(String accountId);

    Medium findMediumByMediumNumber(String mediumNumber);

    /**
     * <p>根据媒介类型查询所有可用的媒介</p>
     * @param mediumType			媒介类型
     * @return
     */
	List<Medium> findMediumByType(String mediumType);

	/**
	 * <p>根据媒介类型和媒介供应code 查询所有的可用的媒介</p>
	 * @param mediumType				媒介类型
	 * @param code						供应code
	 * @return
	 */
	List<Medium> findMediumByType(String mediumType, String code);
}

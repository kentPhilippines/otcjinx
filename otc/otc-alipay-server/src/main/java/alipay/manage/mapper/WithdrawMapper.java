package alipay.manage.mapper;

import alipay.manage.bean.WithdrawExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import otc.bean.dealpay.Withdraw;

import java.util.List;
@Mapper
public interface WithdrawMapper {
    int countByExample(WithdrawExample example);

    int deleteByExample(WithdrawExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Withdraw record);

    int insertSelective(Withdraw record);

    List<Withdraw> selectByExample(WithdrawExample example);

    Withdraw selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Withdraw record, @Param("example") WithdrawExample example);

    int updateByExample(@Param("record") Withdraw record, @Param("example") WithdrawExample example);

    int updateByPrimaryKeySelective(Withdraw record);

    int updateByPrimaryKey(Withdraw record);

    @Update("update alipay_withdraw set " +
            "orderStatus = #{orderStatus}, " +
            "approval = #{approval}," +
            "comment = #{comment}, " +
            "submitTime = sysdate() ," +
            "ethFee = 0 ," +
            "channelId = #{channelId} " +
            "where orderId = #{orderId}")
    int updataOrderStatus(@Param("orderId") String orderId, @Param("approval") String approval,
                          @Param("comment") String comment, @Param("orderStatus") String orderStatus,
                          @Param("channelId") String channelId);

    @Select("select * from alipay_withdraw where orderId = #{orderId}")
    Withdraw findWitOrder(@Param("orderId") String orderId);


    /**
     * 代付失败  附加修改订单为已推送
     *
     * @param orderId
     * @param orderStatus
     * @param comment
     * @return
     */
    @Update("update alipay_withdraw set " +
            "orderStatus = #{orderStatus}, " +
            "comment = #{comment}, " +
            "submitTime = sysdate() " +
            "where orderId = #{orderId}")
    int updataOrderStatusEr(@Param("orderId") String orderId,
                            @Param("orderStatus") String orderStatus,
                            @Param("comment") String comment);

    @Select("select * from alipay_withdraw where appOrderId = #{orderId} and userId = #{appId}")
    Withdraw findOrderByApp(@Param("appId") String appId, @Param("orderId") String orderId);

    @Update("update alipay_withdraw set " +
            "orderStatus = #{orderStatus}, " +
            "approval = #{approval}," +
            "comment = #{comment}, " +
            "submitTime = sysdate() " +
            "where orderId = #{orderId}")
    int updataOrderStatus1(@Param("orderId") String orderId, @Param("approval") String approval,
                           @Param("comment") String comment, @Param("orderStatus") String orderStatusEr);


    @Update("update alipay_withdraw set " +
            "comment = #{comment} " +
            "where orderId = #{orderId}")
    void updateComment(@Param("orderId") String orderId, @Param("comment") String comment);


    @Select("select orderId from alipay_withdraw where ethFee = 0 and currency = 'USDT' and orderStatus = 2 order by createTime asc limit 1 ")
    Withdraw findEthFee();

    @Update("update alipay_withdraw set ethFee = 1 , txhash = #{hash} where orderId = #{orderId} ")
    void updateEthFee(@Param("orderId") String orderId, @Param("hash") String hash);


    @Select(
            "select  sum(amount) as amount " +
                    " from alipay_withdraw where userId = #{appId} and TO_DAYS(createTime) =  TO_DAYS(now()) and orderStatus = 2   group by orderStatus ; ")
    Withdraw findOrderByAppSum(@Param("appId") String appId);

    @Update("update alipay_withdraw set orderStatus = 4 where orderId = #{orderId}")
    boolean updatePush(@Param("orderId") String orderId);


    /**
     * 未结算且  cny   且  成功
     *
     * @return
     */
    @Select("select * from alipay_withdraw where ethFee = 0 and currency = 'CNY' and orderStatus = 2  and submitTime >= CURRENT_TIMESTAMP - INTERVAL 10 MINUTE    limit 15")
    List<Withdraw> findSuccessAndNotAmount();

    /**
     * 修改订单未已结算
     *
     * @param orderId
     */
    @Update("update alipay_withdraw set ethFee = 1 where orderId = #{orderId}")
    void updateSuccessAndAmount(String orderId);

    @Select("select * from alipay_withdraw where  orderStatus = 1 and payStatus = 2  limit 15")
    List<Withdraw> findNotPush();


    @Update("update alipay_withdraw set    comment = #{msg}  where orderId  = #{orderId} ")
    void updateMsg(@Param("orderId")String orderId, @Param("msg")String msg);
    @Update("update alipay_withdraw set    comment =  '无法处理当前订单,请再次推送' ,pushOrder = 3   where orderId  = #{orderId} ")
    int updatePushAgent(String orderId);
    @Select("select * from alipay_withdraw where  orderStatus = 4   and ( witChannel = #{channel} or chennelId = #{channel} ) and witType = #{type}  and submitTime >= CURRENT_TIMESTAMP - INTERVAL 20 MINUTE      ")
    List<Withdraw> findChannelAndType(@Param("channel") String channel,@Param("witType")   String type);
    @Select("select * from alipay_withdraw  where txhash = #{txhash}   ")
    Withdraw findHash(@Param("txhash")  String txhash);


    @Update("update alipay_withdraw set    macthStatus = #{macthStatus} , macthLock = #{macthLock} where orderId  = #{orderId} ")
    boolean macthLock(@Param("orderId")String orderId, @Param("macthStatus")Integer macthStatus, @Param("macthLock")Integer macthLock);



    /**
     * // 获取规则：
     * 1 	不是当前 商户的，
     * 2，  订单为非锁定状态，
     * 3，	订单主状态为 审核中
     * 4 ， 最后一次撮合时间已经过了10分钟 且 订单 为挂起状态
     * @param
     * @return
     */



    @Select(" ( select * from alipay_withdraw  where ( userId != #{userId}" +
            " and orderStatus = 1 " +
            "     and macthCount = 0  and macthLock = 0 ) " +
            "    or ( " +
            "                  userId != #{userId} " +
            "               and orderStatus = 1  " +
            "               and macthCount = 0 and moreMacth = 1  and macthLock = 0 ) ) " + //首次撮合
            " union all " +
            " ( select * from alipay_withdraw  where " +
            "     (    (userId != #{userId} " +
            "     and orderStatus = 1 " +
            "     and macthCount > 0  and macthLock = 0 ) " +
            "    or ( " +
            "                   userId != #{userId} " +
            "               and orderStatus = 1 " +
            "               and macthCount > 0 and moreMacth = 1  and macthLock = 0 ) " +
            "      )  and   " +
            "    macthTime <= DATE_SUB(NOW(),INTERVAL 10 MINUTE)  ) ") //第二次撮合
    List<Withdraw> findMacthOrder(@Param("userId") String userId);


    @Update("update alipay_withdraw set    macthLock = 0   , macthStatus =  0  where  orderStatus = 1 and  macthStatus = 1 and  macthCount = 1  and    macthTime <= DATE_SUB(NOW(),INTERVAL 10 MINUTE)  ")
    void macthOrderUnLock();


    @Update("update alipay_withdraw set  macthCount = macthCount + 1     where orderId  = #{orderId} ")
    int macthCountPush(@Param("orderId")  String orderId);
    @Update("update alipay_withdraw set  macthTime =  now()    where orderId  = #{orderId} ")
    int macthTime(@Param("orderId")  String orderId);
    @Update("update alipay_withdraw set  payStatus = 1    where orderId  = #{orderId} ")
    int isPayStatus(@Param("orderId")String orderId);

    @Select(" ( select * from alipay_withdraw  where   orderStatus = 1    and payStatus = 1 and      macthLock = 0  and  moreMacth = 0  and  macthCount < 1  and     createTime  <= DATE_SUB(NOW(),INTERVAL 10 MINUTE )  )  " + //首次撮合
            " union all " +
            " ( select * from alipay_withdraw  where  orderStatus = 1   and payStatus = 1 and macthLock  = 0  and moreMacth = 0  and macthCount >  0   and moreMacth = 0  " +
            "  and    macthTime <= DATE_SUB( NOW(),INTERVAL 10 MINUTE )  ) ") //第二次撮合
    List<Withdraw> findWaitPush();



}

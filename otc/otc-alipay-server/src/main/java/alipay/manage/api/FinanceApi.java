package alipay.manage.api;

import alipay.manage.api.channel.amount.recharge.usdt.UsdtPayOut;
import alipay.manage.api.config.FactoryForStrategy;
import alipay.manage.bean.ChannelFee;
import alipay.manage.mapper.ChannelFeeMapper;
import alipay.manage.service.BankListService;
import alipay.manage.service.WithdrawService;
import alipay.manage.util.CheckUtils;
import alipay.manage.util.LogUtil;
import alipay.manage.util.OrderUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import otc.api.alipay.Common;
import otc.bean.dealpay.Withdraw;
import otc.common.SystemConstants;
import otc.result.Result;
import otc.util.RSAUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/finance")
public class FinanceApi {
    Logger logger = LoggerFactory.getLogger(FinanceApi.class);
    @Autowired
    CheckUtils checkUtils;
    @Autowired
    WithdrawService withdrawServiceImpl;
    @Autowired
    OrderUtil orderUtil;
    @Autowired
    LogUtil logUtil;
    @Autowired
    FactoryForStrategy factoryForStrategy;
    @Resource
    private ChannelFeeMapper channelFeeDao;
    @Autowired
    private UsdtPayOut usdtPayOutImpl;
    @Autowired
    private BankListService bankListServiceIMpl;

    /**
     * 财务审核商户提现记录状态
     *
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/check/{param:.+}")
    public Result checkWithdrawal(@PathVariable String param, HttpServletRequest request) {
        logger.info("【==============开始进入财务审核商户提现方法===============】");
        logger.info("【财务审核商户的提现记录】后台传过来的参数：{}", param);
        //平台内部私钥解密后台传过来的参数
        Map<String, Object> paramMap = RSAUtils.getDecodePrivateKey(param, SystemConstants.INNER_PLATFORM_PRIVATE_KEY);
        logger.info("【财务审核商户的提现记录】解密后台参数为：{}", paramMap);
        //验证参数非空
        boolean flag = checkUtils.verifyParamNull(paramMap);
        if (!flag) {
            return Result.buildFailMessage("必传参数为空");
        }
        if (ObjectUtil.isNull(paramMap.get("orderStatus"))) {
            return Result.buildFailMessage("订单状态未传，疑似数据安全问题");
        }
        switch (paramMap.get("orderStatus").toString()) {
            case Common.Order.Wit.ORDER_STATUS_SU:
                logUtil.addLog(request, "当前后台审核商户提现订单操作：" +
                                paramMap.get("orderId") + "，：" + paramMap.get("userId")
                                + "，操作人：" + paramMap.get("approval").toString()
                                + ", 审核意见：" + paramMap.get("comment").toString()
                                + ",实际出款渠道：" + paramMap.get("channelId").toString(),
                        paramMap.get("approval").toString());
                return orderUtil.withrawOrderSu(paramMap.get("orderId").toString(),
                        paramMap.get("approval").toString(), paramMap.get("comment").toString(),
                        paramMap.get("channelId").toString(), paramMap.get("witType").toString());
            case Common.Order.Wit.ORDER_STATUS_ER:
                logUtil.addLog(request, "当前后台审核商户提现订单操作：" + paramMap.get("orderId") + "，：" + paramMap.get("userId") + "，操作人：" + paramMap.get("approval").toString() + ", 审核意见："
                        + paramMap.get("comment").toString(), paramMap.get("approval").toString());
                return orderUtil.withrawOrderEr(paramMap.get("orderId").toString(),
                        paramMap.get("approval").toString(),
                        paramMap.get("comment").toString(),
                        paramMap.get("ip").toString());
            case "100":
                logger.info("【接收到后台确认代付出款的方法调用，：" + paramMap.get("orderId") + " 】");
                String orderId = paramMap.get("orderId").toString();
                String apply = paramMap.get("approval").toString();
                if (StrUtil.isBlank(orderId)) {
                    return Result.buildFailMessage("订单号为空");
                }
                if (StrUtil.isBlank(apply)) {
                    return Result.buildFailMessage("操作人为空");
                }
                String channelId = paramMap.get("channelId").toString();
                logUtil.addLog(request, "后台人员确认代付订单，当前代付订单号：" + orderId, apply);
                Withdraw witOrder = withdrawServiceImpl.findOrderId(orderId);
                String witType = witOrder.getWitType();
                String channel = "";
                if (StrUtil.isNotBlank(witOrder.getChennelId())) {//支持运营手动推送出款
                    channel = witOrder.getChennelId();
                } else {
                    channel = witOrder.getWitChannel();
                }
                if(StrUtil.isNotEmpty(channelId)){
                    channel = channelId;
                }
                ChannelFee channelFee = channelFeeDao.findChannelFee(channel, witType);
                Result withdraw = Result.buildFail();
                try {
                   withdraw = factoryForStrategy.getStrategy(channelFee.getImpl()).withdraw(witOrder);
                } catch (Exception e) {
                    return Result.buildFailMessage("代付渠道未接通或渠道配置错误，请联系技术人员处理");
                }
                return withdraw;
        }

/*
        if (Common.Order.Wit.ORDER_STATUS_SU.equals(paramMap.get("orderStatus"))) {//成功
            logUtil.addLog(request, "当前后台审核商户提现订单操作：" + paramMap.get("orderId") + "，：" + paramMap.get("userId") + "，操作人：" + paramMap.get("approval").toString() + ", 审核意见：" + paramMap.get("comment").toString(), paramMap.get("approval").toString());
            return orderUtil.withrawOrderSu(paramMap.get("orderId").toString(), paramMap.get("approval").toString(), paramMap.get("comment").toString());
        }
        if (Common.Order.Wit.ORDER_STATUS_ER.equals(paramMap.get("orderStatus"))) {//失败
            logUtil.addLog(request, "当前后台审核商户提现订单操作：" + paramMap.get("orderId") + "，：" + paramMap.get("userId") + "，操作人：" + paramMap.get("approval").toString() +  ", 审核意见：" + paramMap.get("comment").toString(), paramMap.get("approval").toString());
            return orderUtil.withrawOrderEr(paramMap.get("orderId").toString(), paramMap.get("approval").toString(), paramMap.get("comment").toString(), paramMap.get("ip").toString());
        }
*/

        return null;
    }


    /**
     * USDT手动结算矿工费用
     *
     * @return
     */
    @PostMapping("/checkUSDT/{param:.+}")
    public Result usdtEthFee(@PathVariable String param, HttpServletRequest request) {
        logger.info("【==============开始进入手动结算USDT矿工费方法===============】");
        logger.info("【手动结算USDT矿工费】后台传过来的参数：{}", param);
        //平台内部私钥解密后台传过来的参数
        Map<String, Object> paramMap = RSAUtils.getDecodePrivateKey(param, SystemConstants.INNER_PLATFORM_PRIVATE_KEY);
        logger.info("【手动结算USDT矿工费】解密后台参数为：{}", paramMap);
        if (ObjectUtil.isNull(paramMap.get("orderStatus"))) {
            return Result.buildFailMessage("订单状态未传，疑似数据安全问题");
        }

    /*    BigDecimal usdt ,   //花费usdt
                price  ,    //汽油价格
                used,       //使用汽油数
                eth,        //花费eth
                priceUsdt;  //eth --> usdt 汇率
        String hash;        //订单hash*/

        Object usdt = paramMap.get("usdt");
        Object orderId = paramMap.get("orderId");
        Object price = paramMap.get("price");
        Object used = paramMap.get("used");
        Object eth = paramMap.get("eth");
        Object approval = paramMap.get("approval");
        Object priceUsdt = paramMap.get("priceUsdt");
        Object hash = paramMap.get("hash");
        if (null == usdt || null == price || null == used
                || null == eth
                || null == approval
                || null == price || null == priceUsdt
                || null == hash || null == orderId) {
            return Result.buildFailMessage("部分数据未获取到，疑似数据安全问题");
        }
        ThreadUtil.execute(() -> {
            logUtil.addLog(request, "当前后台结算usdt代付订单eth矿工费操作：" + paramMap.get("orderId") + "，：" + "，操作人：" + paramMap.get("approval").toString() + ", 结算usdt：" + usdt
                    , paramMap.get("approval").toString());
        });
        Withdraw order = withdrawServiceImpl.findOrderId(orderId.toString());
        return usdtPayOutImpl.usdtAmount(order, new BigDecimal(usdt.toString()), new BigDecimal(price.toString()),
                new BigDecimal(used.toString()), new BigDecimal(eth.toString()), priceUsdt.toString(), hash.toString());
    }
}

package alipay.manage.api.channel.amount.recharge;


import alipay.manage.api.config.FactoryForStrategy;
import alipay.manage.api.deal.DealPay;
import alipay.manage.bean.ChannelFee;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.UserRate;
import alipay.manage.mapper.ChannelFeeMapper;
import alipay.manage.mapper.UserInfoMapper;
import alipay.manage.service.OrderAppService;
import alipay.manage.service.UserRateService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import otc.api.alipay.Common;
import otc.common.PayApiConstant;
import otc.result.Result;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

@RequestMapping(PayApiConstant.Alipay.ORDER_API + "/USDTInfo")
@RestController
public class NameApi {
    Logger log = LoggerFactory.getLogger(NameApi.class);
    @Resource
    private UserInfoMapper userInfoMapper;
    @Autowired
    private OrderAppService orderAppServiceImpl;
    @Resource
    private ChannelFeeMapper channelFeeDao;
    @Autowired
    private FactoryForStrategy factoryForStrategy;
    @Autowired private UserRateService userRateService;
    /**
     * 根据订单号 产品类型 生成订单号并返回 银行卡信息
     */
    public Result deal(String orderId,String name, HttpServletRequest request){
        log.info("当前订单号为空，请求ip为："+ HttpUtil.getClientIP(request));
        if(StrUtil.isEmpty(orderId)){
            log.info("当前订单号为空，请求ip为："+ HttpUtil.getClientIP(request));
            return Result.buildFailMessage("当前订单号为空");
        }
        if(StrUtil.isEmpty(name)){
            log.info("当前传输姓名为空" +
                    "，请求ip为："+ HttpUtil.getClientIP(request));
            return Result.buildFailMessage("当前订单号为空");
        }
        log.info("当前订单号为："+orderId+"，请求ip为："+ HttpUtil.getClientIP(request));
        /**
         * 获取订单号，并根据订单进行匹配
         */
        DealOrderApp orderApp = orderAppServiceImpl.findOrderByOrderId(orderId);
        String passCode = orderApp.getRetain1();//产品类型
        String appId = orderApp.getOrderAccount();
        String dealDescribe = "付款人："+  StrUtil.trim(name);
        orderApp.setDealDescribe(dealDescribe);
        List<UserRate> rateList =  userRateService.findOpenFeeR(appId,passCode);//  所有该产品类型下 开启的充值费率类型
        for(UserRate rate : rateList ){
            ChannelFee channelFee = null;
            BigDecimal systemAmount = new BigDecimal(rate.getRetain2());//金额限制
            BigDecimal bigDecimal =  orderApp.getOrderAmount() ;
            UserInfo channel = userInfoMapper.findUserByUserId(rate.getChannelId());
            channelFee = channelFeeDao.findImpl(rate.getChannelId(), rate.getPayTypr());
            if (Common.Order.DEAL_OFF.equals(channel.getReceiveOrderState())) {
                log.info("渠道关闭，当前订单:"+orderApp.getOrderId()+"，当前渠道："+channel.getUserName()+"，拉单支付金额："+bigDecimal);
                continue;
            }
            if (bigDecimal.compareTo(systemAmount) >= 0 && bigDecimal.compareTo(new BigDecimal(channel.getMaxAmount())) > -1) {
                log.info("bigDecimal:{} systemAmount:{} channel.getMaxAmount：{}",bigDecimal,systemAmount,channel.getMaxAmount());
                try {
                    Result deal = factoryForStrategy.getStrategy(channelFee.getImpl()).deal(orderApp, channelFee.getChannelId());
                    if(!deal.isSuccess()){
                        continue;
                    }
                    return deal;
                } catch (Exception e) {
                 log.info("【渠道异常，当前订单号："+orderApp.getOrderId()+"】");
                    continue;
                }
            } else {
                log.info("通道金额限制，当前订单:"+orderApp.getOrderId()+"，当前通道限额："+systemAmount+"，拉单支付金额："+bigDecimal);
                continue;
            }
        }
        return Result.buildFail();
    }
}

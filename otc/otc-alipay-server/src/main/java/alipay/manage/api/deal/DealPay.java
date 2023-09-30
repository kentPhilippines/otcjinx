package alipay.manage.api.deal;

import alipay.config.redis.RedisUtil;
import alipay.manage.api.AccountApiService;
import alipay.manage.api.V2.vo.DepositRequestVO;
import alipay.manage.api.VendorRequestApi;
import alipay.manage.api.config.FactoryForStrategy;
import alipay.manage.bean.*;
import alipay.manage.bean.util.DealBean;
import alipay.manage.mapper.ChannelFeeMapper;
import alipay.manage.mapper.UserInfoMapper;
import alipay.manage.service.ExceptionOrderService;
import alipay.manage.service.OrderAppService;
import alipay.manage.service.UserInfoService;
import alipay.manage.service.UserRateService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.api.alipay.Common;
import otc.result.Result;
import otc.util.MapUtil;
import otc.util.number.Number;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@Component
public class DealPay {
    @Resource
    private UserInfoMapper userInfoMapper;
    @Autowired
    VendorRequestApi vendorRequestApi;
    Logger log = LoggerFactory.getLogger(DealPay.class);
    @Autowired
    private FactoryForStrategy factoryForStrategy;
    @Autowired
    private AccountApiService accountApiServiceImpl;
    @Autowired
    private OrderAppService orderAppServiceImpl;
    @Autowired
    private UserInfoService userInfoServiceImpl;
    @Resource
    private ChannelFeeMapper channelFeeDao;
    @Autowired
    private ExceptionOrderService exceptionOrderServiceImpl;
    @Autowired
    private RedisUtil redis;

    @Autowired private UserRateService userRateService;
    public Result deal(HttpServletRequest request, DepositRequestVO depositRequestVO) {
        if (ObjectUtil.isNull(request.getParameter("userId")) && depositRequestVO == null) {
            log.info("当前传参，参数格式错误");
            return Result.buildFailMessage("当前传参，参数格式错误，");
        }
        Result pay = vendorRequestApi.pay(request, depositRequestVO);
        if (!pay.isSuccess()) {
            return pay;
        }

        Object result = pay.getResult();
        DealBean mapToBean = MapUtil.mapToBean((Map<String, Object>) result, DealBean.class);
        if (ObjectUtil.isNull(mapToBean)) {
            return Result.buildFailMessage("加密前格式错误，参数为空");
        }
        String clientIP = HttpUtil.getClientIP(request);
        if (StrUtil.isNotBlank(clientIP)) {
            mapToBean.setIp(clientIP);
        }
        Object o = redis.get(mapToBean.getOrderId() + mapToBean.getAppId());
        if (null != o) {
            if (o.toString().equals(mapToBean.getOrderId() + mapToBean.getAppId())) {
                log.info("【当前商户订单号重复：" + mapToBean.getOrderId() + "】");
                exceptionOrderServiceImpl.addDealOrder(mapToBean, "用户报错：商户订单号重复；处理方法：提醒用户换一个订单号提交支付请求", clientIP);
                return Result.buildFailMessage("商户订单号重复");
            }
        }
        String passCode = mapToBean.getPassCode();//产品类型
        String appId = mapToBean.getAppId();
        List<UserRate> rateList =  userRateService.findOpenFeeR(appId,passCode);//  所有该产品类型下 开启的充值费率类型
        log.info("rateList：{}", JSONUtil.toJsonStr(rateList));
        CollUtil.sortByProperty(rateList, "retain1");
        for(UserRate rate : rateList ){
            BigDecimal systemAmount = new BigDecimal(rate.getRetain2());//金额限制
            BigDecimal bigDecimal = new BigDecimal(mapToBean.getAmount());
            UserInfo channel = userInfoMapper.findUserByUserId(rate.getChannelId());

            if (Common.Order.DEAL_OFF.equals(channel.getReceiveOrderState())) {
                log.info("渠道关闭，当前订单:"+mapToBean.getOrderId()+"，当前渠道："+channel.getUserName()+"，拉单支付金额："+bigDecimal);
                exceptionOrderServiceImpl.addDealOrder(mapToBean, "用户报错：渠道关闭，联系运营处理；处理方法：开启渠道充值状态", clientIP);
               continue;
            }
            if (bigDecimal.compareTo(systemAmount) >= 0 && bigDecimal.compareTo(new BigDecimal(channel.getMaxAmount())) > -1) {
                log.info("bigDecimal:{} systemAmount:{} channel.getMaxAmount：{}",bigDecimal,systemAmount,channel.getMaxAmount());
                Result result1 = forQueryOrder(mapToBean, clientIP, rate, depositRequestVO);
                if(!result1.isSuccess()){
                    continue;
                }
                return result1;
            } else {
                log.info("通道金额限制，当前订单:"+mapToBean.getOrderId()+"，当前通道限额："+systemAmount+"，拉单支付金额："+bigDecimal);
                continue;
            }
        }
        return Result.buildFailMessage("请求支付失败");
    }

    DealOrderApp createDealAppOrder(DealBean dealBean, UserRate userRate,DepositRequestVO depositRequestVO) {
        DealOrderApp dealApp = new DealOrderApp();
        dealApp.setAppOrderId(dealBean.getOrderId());
            dealApp.setOrderId(Number.getAppOreder());
        BigDecimal amount = BigDecimal.ZERO;
      /*  if(new BigDecimal(dealBean.getAmount()).compareTo(new BigDecimal(200))<0){
            amount = getAmount(new BigDecimal(dealBean.getAmount())).setScale(2,BigDecimal.ROUND_UP);
        }else{
        }*/
        amount =  new BigDecimal(dealBean.getAmount());
        dealApp.setNotify(dealBean.getNotifyUrl());
        dealApp.setOrderAmount(amount);
        String userId = dealBean.getAppId();
        dealApp.setFeeId(userRate.getId());
        dealApp.setOrderAccount(userId);
        if (StrUtil.isNotBlank(dealBean.getIp())) {
            dealApp.setOrderIp(dealBean.getIp());
        }
        dealApp.setBack(dealBean.getPageUrl());
        dealApp.setOrderStatus(Common.Order.DealOrder.ORDER_STATUS_DISPOSE.toString());
        dealApp.setOrderType(Common.Order.ORDER_TYPE_DEAL);
        String dealDescribe  = "充值交易";
        if(StrUtil.isNotEmpty(dealBean.getUserid())){
            dealDescribe = "付款人："+ dealBean.getUserid();
        }
        dealApp.setRetain1(userRate.getPayTypr());
        dealApp.setRetain3(userRate.getFee().multiply(amount).setScale(2,BigDecimal.ROUND_UP).toString());
        UserFund userFund = userInfoServiceImpl.findCurrency(userId);//缓存以加
        dealApp.setCurrency(userFund.getCurrency());
        if(ObjectUtil.isNotNull(depositRequestVO)){
            dealApp.setPayName(depositRequestVO.getMcPayName());
            dealDescribe = "付款人："+ depositRequestVO.getMcPayName();
        } else {
            if(StrUtil.isNotEmpty(dealBean.getUserid())) {
                dealApp.setPayName(dealBean.getUserid());
            }
        }
        dealApp.setDealDescribe(dealDescribe);
        boolean add = false;
        try {
            add = orderAppServiceImpl.add(dealApp);
        } catch (Throwable e) {
            log.error("异常：",e);
            log.info("【当前商户订单号重复：" + dealApp.getOrderId() + "】");
            exceptionOrderServiceImpl.addDealOrder(dealBean, "用户报错：商户订单号重复；处理方法：提醒用户换一个订单号提交支付请求", dealBean.getIp());
            //throw new OrderException("订单号重复", null);
        }
        if (add) {
            redis.set(dealApp.getAppOrderId() + userId, dealApp.getAppOrderId() + userId, 60 * 60);
            return dealApp;
        }
        return null;
    }

   Result forQueryOrder( DealBean mapToBean , String clientIP ,  UserRate userRate,DepositRequestVO depositRequestVO){
       log.info("【当前请求交易实体类：" + mapToBean.toString() + "】");
       String passcode = mapToBean.getPassCode(); //通道支付编码
       if (StrUtil.isBlank(passcode)) {
           return Result.buildFailMessage("通道编码为空");
       }
       log.info("【当前通道编码：" + passcode + "】");
       ChannelFee channelFee = null;
       try {
           channelFee = channelFeeDao.findImpl(userRate.getChannelId(), userRate.getPayTypr());
       } catch (Throwable e) {
           exceptionOrderServiceImpl.addDealOrder(mapToBean, "用户报错：当前通道编码有误，产品类型设置重复；处理方法：当前配置用户产品的时候配置用户产品重复", clientIP);
           log.error("【当前通道编码设置有误，产品类型设置重复：" + e.getMessage() + "】",e);
           return Result.buildFailMessage("当前通道编码设置有误，产品类型设置重复");
       }
       if (ObjectUtil.isNull(channelFee)) {
           log.info("【通道实体不存在，当前商户订单号：" + mapToBean.getOrderId() + "】");
           log.info("【通道实体不存在，费率配置错误】");
           exceptionOrderServiceImpl.addDealOrder(mapToBean, "用户报错：通道实体不存在；处理方法：渠道费率未设置", clientIP);
           return Result.buildFailMessage("通道实体不存在，费率配置错误");
       }

       DealOrderApp dealBean = createDealAppOrder(mapToBean, userRate,depositRequestVO);
       if (ObjectUtil.isNull(dealBean)) {
           exceptionOrderServiceImpl.addDealOrder(mapToBean, "用户报错：交易预订单生成出错；处理方法：让商户重新发起支付提交请求，或联系技术人员处理", clientIP);
           return Result.buildFailMessage("交易预订单生成出错");
       }
       Result deal = null;
       try {
           deal = factoryForStrategy.getStrategy(channelFee.getImpl()).deal(dealBean, channelFee.getChannelId());
       } catch (Throwable e) {
           log.error("异常：",e);
           log.error("【当前通道编码对于的实体类不存在：" + e.getMessage() + "】", e);
           exceptionOrderServiceImpl.addDealOrder(mapToBean, "用户报错：当前通道编码不存在；处理方法：生成交易订单时候出现错误，或者请求三方渠道支付请求的时候出现异常返回，或联系技术人员处理," +
                   "三方渠道报错信息：" + e.getMessage(), clientIP);
           return Result.buildFailMessage("当前通道编码不存在");
       }
       log.info(deal.toString());
       return deal;
    }
    BigDecimal getAmount(BigDecimal amount ){
        double v = RandomUtil.randomDouble(0.01, 0.5,2, RoundingMode.HALF_UP);
        BigDecimal bb = amount;
        amount =  amount.add(new BigDecimal(v));
        Object o = redis.get(amount.toString());
        if(ObjectUtil.isNotNull(o)){
            return getAmount(bb);
        }else{
            redis.set(amount.toString(),amount,300);
            return amount;
        }
    }



}

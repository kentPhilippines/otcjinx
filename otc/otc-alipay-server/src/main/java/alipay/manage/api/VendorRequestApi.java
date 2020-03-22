package alipay.manage.api;

import alipay.manage.bean.BankList;
import alipay.manage.bean.UserFund;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.UserRate;
import alipay.manage.service.BankListService;
import alipay.manage.service.UserInfoService;
import alipay.manage.util.CheckUtils;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import otc.api.alipay.Common;
import otc.common.SystemConstants;
import otc.exception.BusinessException;
import otc.result.Result;
import otc.util.RSAUtils;
import otc.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class VendorRequestApi {

    Logger log = LoggerFactory.getLogger(VendorRequestApi.class);
    @Autowired AccountApiService accountApiServiceImpl;
    @Autowired CheckUtils checkUtils;
	@Autowired BankListService bankListServiceImpl;
	@Autowired UserInfoService userInfoServiceImpl;
    /**
     * 商户下单支付请求接口
     *
     * @param request
     * @return
     */
    public Result pay(HttpServletRequest request) {
        String userId = request.getParameter("userId");//商户号
        //根据商户号查询商户实体
        UserInfo userInfo = accountApiServiceImpl.findUserInfo(userId);
        if (userInfo == null) {
            return Result.buildFailMessage("商户不存在");
        }
        log.info("--------------【用户开始RSA解密】----------------");
        String rsaSign = request.getParameter("cipherText");//商户传过来的密文
        Map<String, Object> paramMap = RSAUtils.getDecodePrivateKey(rsaSign, userInfo.getPrivateKey());
        log.info("-------------【商户RSA解密的参数】：" + paramMap == null ? "解密参数为空" : paramMap.toString());
        //验证结果
        Result result = checkUtils.requestVerify(request, paramMap);
        if (result.isSuccess()) 
            log.info("【requestVerif】方法验证通过");
         else 
            return result;
        //验证商户是否配置费率
        Integer remitOrderState = userInfo.getReceiveOrderState();// 1 接单 2 暂停接单
        if(Common.Order.DEAL_OFF.equals(remitOrderState)) {
        	 log.info("【当前账户交易权限未开通】");
        	 return Result.buildFailMessage("当前账户交易权限未开通");
        }
        String passCode = paramMap.get("passCode").toString();
        //后期优化从缓存读取数据
        UserRate userRate = accountApiServiceImpl.findUserRateByUserId(userId, passCode);
        if (userRate == null) {
            log.info("--------------【 商户交易费率未配置或未开通】----------------");
            return Result.buildFailMessage("商户交易费率未配置或未开通");
        }
        if (userInfo.getSwitchs() == 0) {
            log.info("--------------【 商户通道已关闭】----------------");
            return Result.buildFailMessage("商户通道未开启");
        }
        // TODO: 2020/3/20  时间、次数、金额三个维度验证
        //从redis缓存读取数据
        return Result.buildSuccessResult(paramMap);
    }
    /**
     * <p>商户下单提现接口</p>
     * @param request				请求
     * @param flag					是否为商户后台提现			true  是   false  否
     * @return
     */
    public Result withdrawal(HttpServletRequest request,boolean flag) {
        String userId = request.getParameter("userId");//商户号
        UserInfo userInfo = accountApiServiceImpl.findUserInfo(userId);
        if (userInfo == null) 
            return Result.buildFailMessage("商户不存在");
        log.info("--------------【用户开始RSA解密】----------------");
        String rsaSign = request.getParameter("cipherText");//商户传过来的密文
        Map<String, Object> paramMap = RSAUtils.getDecodePrivateKey(rsaSign, userInfo.getPrivateKey());
        log.info("-------------【商户RSA解密的参数】：" + paramMap == null ? "解密参数为空" : paramMap.toString());
        if(CollUtil.isEmpty(paramMap))
        	return Result.buildFailMessage("RSA解密参数为空");
        Result result = checkUtils.requestWithdrawalVerify(request, paramMap);
        if (result.isSuccess()) 
            log.info("【requestVerif】方法验证通过");
         else 
            return result;
        UserRate userRate = accountApiServiceImpl.findUserRateWitByUserId(userId);
        if(ObjectUtil.isNull(userRate))
        	return Result.buildFailMessage("代付费率为开通或状态未开启");
        Integer remitOrderState = userInfo.getRemitOrderState();// 1 接单 2 暂停接单
        if(Common.Order.DAPY_OFF.equals(remitOrderState)) {
        	 log.info("【当前账户代付权限未开通】");
        	 return Result.buildFailMessage("当前账户代付权限未开通");
        }
        if(!flag) {
        	String clientIP = HttpUtil.getClientIP(request);
        	if(StrUtil.isBlank(clientIP))
        		return Result.buildFailMessage("未获取到代付ip");
        	log.info("【当前用户代付ip为："+clientIP+"】");
        	String witip = userInfo.getWitip();
        	log.info("【获取当前允许代付ip为："+witip+"】");
        	String[] split = witip.split(",");
        	List<String> asList = Arrays.asList(split);
        	if(!asList.contains(clientIP))
        		return Result.buildFailMessage("请绑定正确的代付ip");
        }else 
        	log.info("【商户从后台提现不验证代付ip】");
       String bankNo =  paramMap.get("acctno").toString();   
       String amount =  paramMap.get("amount").toString();   
       BankList bank =  bankListServiceImpl.findBankByNo(bankNo);
       BigDecimal limitAmount = bank.getLimitAmount();
       BigDecimal bankAmount = bank.getBankAmount();
       BigDecimal add = bankAmount.add(new BigDecimal(amount));
       if(add.compareTo(limitAmount) == -1) {
    	   log.info("【当前银行卡超出当日可用金额】");
    	   return Result.buildFailMessage("当前银行卡超出当日可用金额");
       }
       UserFund userFund = userInfoServiceImpl.findUserFundByAccount(userId);
       BigDecimal accountBalance = userFund.getAccountBalance();
       if(accountBalance.compareTo(new BigDecimal(amount)) == -1 ) {
    	   log.info("【当前账户金额不足】");
    	   return Result.buildFailMessage("当前账户金额不足");
       }
        return  Result.buildSuccessResult(paramMap);
    }


}

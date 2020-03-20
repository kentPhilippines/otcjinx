package alipay.manage.api;

import alipay.manage.bean.UserInfo;
import alipay.manage.bean.UserRate;
import alipay.manage.util.CheckUtils;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import otc.common.SystemConstants;
import otc.exception.BusinessException;
import otc.result.Result;
import otc.util.RSAUtils;
import otc.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@RestController
public class VendorRequestApi {

    Logger log = LoggerFactory.getLogger(VendorRequestApi.class);
    @Autowired
    AccountApiService accountApiServiceImpl;
    @Autowired
    CheckUtils checkUtils;

    /**
     * 商户下单支付请求接口
     *
     * @param request
     * @return
     */
    @PostMapping("/pay")
    public Result pay(HttpServletRequest request) {
        String userId = request.getParameter("userId");//商户号
        //根据商户号查询商户实体
        UserInfo userInfo = accountApiServiceImpl.findUserInfo(userId);
        if (userInfo == null) {
            return Result.buildFailMessage("商户不存在");
        }
        log.info("|--------------【用户开始RSA解密】----------------");
        String rsaSign = request.getParameter("cipherText");//商户传过来的密文
        Map<String, Object> paramMap = RSAUtils.getDecodePrivateKey(rsaSign, userInfo.getPrivateKey());
        log.info("|-------------【商户RSA解密的参数】：" + paramMap == null ? "解密参数为空" : paramMap.toString());
        //验证结果
        Result result = checkUtils.requestVerify(request, paramMap);
        if (result.isSuccess()) {
            log.info("【requestVerif】方法验证通过");
        } else {
            return Result.buildFailMessage(result.getMessage());
        }
        //验证商户是否配置费率
        String passCode = paramMap.get("passCode").toString();
        UserRate userRate = accountApiServiceImpl.findUserRateByUserId(userId, passCode);
        if (userRate == null) {
            log.info("|--------------【15032 :商户未开通交易服务】----------------");
            return Result.buildFailMessage("商户未开通交易服务");
        }
        if (userInfo.getSwitchs() == 0) {
            log.info("|--------------【15033 :商户通道已关闭】----------------");
            return Result.buildFailMessage("商户通道未开启");
        }

        // TODO: 2020/3/20  时间、次数、金额三个维度验证

        return null;

    }


    /**
     * 商户下单提现接口
     *
     * @param request
     * @return
     */
    @PostMapping("/merchant/withdrawal")
    public Result withdrawal(HttpServletRequest request) {
        String userId = request.getParameter("userId");//商户号


        return null;
    }


}

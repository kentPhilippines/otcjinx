package alipay.manage.util;

import alipay.manage.api.AccountApiService;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.common.SystemConstants;
import otc.result.Result;
import otc.util.RSAUtils;
import otc.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Component
public class CheckUtils {
    Logger log = LoggerFactory.getLogger(CheckUtils.class);

    @Autowired
    AccountApiService accountApiServiceImpl;

    public Result requestVerify(HttpServletRequest request, Map<String, Object> paramMap) {
        //验传必填参数是否为空
        if (!checkParam(paramMap)) {
            return Result.buildFailMessage("必传参数为空");
        }
        //验证请求URL
        log.info("访问URL：" + request.getRequestURL());
        log.info("请求参数字符编码: " + request.getCharacterEncoding());
        boolean flag = verifyUrl(request);
        if (!flag) {
            return Result.buildFailMessage("字符编码错误");
        }
        log.info("|--------------【用户开始MD5验签】----------------");
        boolean vSign = RSAUtils.verifySign(paramMap);
        if (!vSign) {
            return Result.buildFailMessage("md5验签失败");
        }
        return Result.buildSuccess();
    }


    /**
     * 验证url设置
     *
     * @param request
     * @return
     */
    public boolean verifyUrl(HttpServletRequest request) {
        log.info("访问URL：" + request.getRequestURL());
        log.info("请求参数字符编码: " + request.getCharacterEncoding());
        if (StrUtil.isBlank(request.getCharacterEncoding())) {
            log.info("|--------------【15030 :字符编码未设置】----------------");
            return false;
        }
        if (!SystemConstants.CODING.equalsIgnoreCase(request.getCharacterEncoding())) {
            log.info("|--------------【15031 :字符编码错误】----------------");
            return false;
        }
        return true;
    }

    /**
     * 参数的非空验证
     *
     * @param map
     * @return
     */
    public boolean checkParam(Map<String, Object> map) {

        String appId = (String) map.get("appId");
        String orderId = (String) map.get("orderId");
        String notifyUrl = (String) map.get("notifyUrl");
        String amount = (String) map.get("amount");
        String passCode = (String) map.get("passCode");
        String applyDate = (String) map.get("applyDate");
        String rsaSign = (String) map.get("sign");
        if (StringUtils.isEmpty(appId)) {
            return false;
        }
        if (StringUtils.isEmpty(orderId)) {
            return false;
        }
        if (StringUtils.isEmpty(notifyUrl)) {
            return false;
        }
        if (StringUtils.isEmpty(amount)) {
            return false;
        }
        if (StringUtils.isEmpty(passCode)) {
            return false;
        }
        if (StringUtils.isEmpty(applyDate)) {
            return false;
        }
        if (StringUtils.isEmpty(rsaSign)) {
            return false;
        }
        return true;
    }
}

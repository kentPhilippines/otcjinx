package alipay.manage.util;

import alipay.manage.bean.UserInfo;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import otc.common.SystemConstants;
import otc.result.Result;
import otc.util.MapUtil;
import otc.util.RSAUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class CheckUtils {
    static Logger log = LoggerFactory.getLogger(CheckUtils.class);


    public static Result requestVerify(HttpServletRequest request, Map<String, Object> paramMap, String key) {
        Result result = checkParam(paramMap);
        if (!result.isSuccess()) {
            return result;
        }
        log.info("【访问URL：" + request.getRequestURL() + "】");
        log.info("【请求参数字符编码: " + request.getCharacterEncoding() + "】");
        boolean flag = verifyUrl(request);
        if (!flag) {
            return Result.buildFailMessage("字符编码错误");
        }
        log.info("--------------【用户开始MD5验签】----------------"+key);
        boolean vSign = verifySign(paramMap, key);
        if (!vSign) {
            return Result.buildFailMessage("签名验证失败");
        }
        return Result.buildSuccess();
    }

    private static Result witcheckParam(Map<String, Object> map) {
        String appid = (String) map.get("appid");
        String orderId = (String) map.get("apporderid");
        String ordertime = (String) map.get("ordertime");
        String amount = (String) map.get("amount");
        String acctno = (String) map.get("acctno");
        String acctname = (String) map.get("acctname");
        String bankcode = (String) map.get("bankcode");
        String notifyurl = (String) map.get("notifyurl");
        String rsasign = (String) map.get("sign");

        if (StrUtil.isEmpty(rsasign)) {
            return Result.buildFailMessage("sign 字段为null,plese check");
        }
        if (StrUtil.isEmpty(notifyurl)) {
            return Result.buildFailMessage("notifyurl 字段为null,plese check");
        }
        if (StrUtil.isEmpty(acctname)) {
            return Result.buildFailMessage("acctname 字段为null,plese check");
        }
        if (StrUtil.isEmpty(ordertime)) {
            return Result.buildFailMessage("ordertime 字段为null,plese check");
        }
        if (StrUtil.isEmpty(acctno)) {
            return Result.buildFailMessage("acctno 字段为null,plese check");
        }
        if (StrUtil.isEmpty(amount)) {
            return Result.buildFailMessage("amount 字段为null,plese check");
        }
        if (StrUtil.isEmpty(bankcode)) {
            return Result.buildFailMessage("bankcode 字段为null,plese check");
        }
        if (StrUtil.isEmpty(orderId)) {
            return Result.buildFailMessage("orderId 字段为null,plese check");
        }
        if (StrUtil.isEmpty(appid)) {
            return Result.buildFailMessage("appid 字段为null,plese check");
        }
        return Result.buildSuccess();
    }

    /**
     * 验证url设置
     *
     * @param request
     * @return
     */
    public static boolean verifyUrl(HttpServletRequest request) {
        log.info("访问URL：" + request.getRequestURL());
        log.info("请求参数字符编码: " + request.getCharacterEncoding());
        if (StrUtil.isBlank(request.getCharacterEncoding())) {
            log.info("--------------【15030 :字符编码未设置】----------------");
            return false;
        }
        if (!SystemConstants.CODING.equalsIgnoreCase(request.getCharacterEncoding())) {
            log.info("--------------【15031 :字符编码错误】----------------");
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
    public static Result checkParam(Map<String, Object> map) {
        String appId = (String) map.get("appId");
        String orderId = (String) map.get("orderId");
        String notifyUrl = (String) map.get("notifyUrl");
        String amount = (String) map.get("amount");
        String passCode = (String) map.get("passCode");
        String applyDate = (String) map.get("applyDate");
        String rsaSign = (String) map.get("sign");
        if (StrUtil.isEmpty(appId)) {
            return Result.buildFailMessage("appId 字段为null,plese check");
        }
        if (StrUtil.isEmpty(orderId)) {
            return Result.buildFailMessage("orderId 字段为null,plese check");
        }
        if (StrUtil.isEmpty(notifyUrl)) {
            return Result.buildFailMessage("notifyUrl 字段为null,plese check");
        }
        if (StrUtil.isEmpty(amount)) {
            return Result.buildFailMessage("amount 字段为null,plese check");
        }
        if (StrUtil.isEmpty(passCode)) {
            return Result.buildFailMessage("passCode 字段为null,plese check");
        }
        if (StrUtil.isEmpty(applyDate)) {
            return Result.buildFailMessage("applyDate 字段为null,plese check");
        }
        if (StrUtil.isEmpty(rsaSign)) {
            return Result.buildFailMessage("rsaSign 字段为null,plese check");
        }
        return Result.buildSuccess();
    }


    public static Result requestWithdrawalVerify(HttpServletRequest request, Map<String, Object> paramMap, String key) {
        Result result = witcheckParam(paramMap);
        if (!result.isSuccess()) {
            return result;
        }
        log.info("【访问URL：" + request.getRequestURL() + "】");
        log.info("【请求参数字符编码: " + request.getCharacterEncoding() + "】");
        boolean flag = verifyUrl(request);
        if (!flag) {
            return Result.buildFailMessage("字符编码错误");
        }
        log.info("--------------【用户开始MD5验签】----------------");
        boolean vSign = verifySign(paramMap, key);
        if (!vSign) {
            return Result.buildFailMessage("签名验证失败");
        }
        return Result.buildSuccess();
    }

    /**
     * <p>验签方法调用</p>
     *
     * @param map 签名参数
     * @param key
     * @return 验签是否通过
     */
    public static boolean verifySign(Map<String, Object> map, String key) {
        String paramStr = MapUtil.createParam(map);
        log.info("【验证签名前的参数为：" + paramStr.toString() + "】");
        String md5 = RSAUtils.md5(paramStr + key);
        Object oldmd5 = map.get("sign");
        if (!oldmd5.toString().equals(md5)) {
            log.info("【当前用户验签不通过】");
            log.info("【请求方签名值为：" + oldmd5 + "】");
            log.info("【我方验签值为：" + md5 + "】");
            return false;
        }
        return true;
    }

    public static String getSign(Map<String, Object> map, String key) {
        String paramStr = MapUtil.createParam(map);
        log.info("【签名前的参数为：" + paramStr.toString() + "】");
        String md5 = RSAUtils.md5(paramStr + key);
        return md5;
    }
 	private static final String UTF_8 = "utf-8";
 	private static final String ENCODE_TYPE = "md5";


    /**
     * 判断时间是否在某个时间段内 一天时间
     *
     * @param date      需要判断的时间
     * @param beginTime 时间段开始时间
     * @param endTime   时间段结束时间
     * @return boolean
     * @throws Exception exception
     */
    public static boolean verifyTimeZone(Date date, String beginTime, String endTime) {
        DateFormat SDF = new SimpleDateFormat("HH:mm:ss");
        Calendar now = Calendar.getInstance();
        Calendar begin = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        try {
            now.setTime(SDF.parse(SDF.format(date)));
            begin.setTime(SDF.parse(beginTime));
            end.setTime(SDF.parse(endTime));
        } catch (ParseException e) {
            return false;
        }

        int bHour = begin.get(Calendar.HOUR_OF_DAY);
        int eHour = end.get(Calendar.HOUR_OF_DAY);

        //没有跨天,时段类似于:20:00:00——23:00:00 或者 20:00:00——20:30:00 或者 20:30:00——20:30:59
        if (end.after(begin) || (bHour == eHour && end.get(Calendar.MINUTE) > begin.get(Calendar.MINUTE)) ||
                (bHour == eHour && end.get(Calendar.MINUTE) == begin.get(Calendar.MINUTE) && end.get(Calendar.SECOND) > begin.get(Calendar.SECOND))) {
            return now.after(begin) && now.before(end);
        }
        //跨天，配置时段类似于:23:00:00——06:00:00
        else {
            // 将时间段拆成 end——23:59:59 和 00:00:00——begin
            Calendar newBegin = (Calendar) begin.clone();
            Calendar newEnd = (Calendar) end.clone();
            newBegin.set(Calendar.HOUR_OF_DAY, 0);
            newBegin.set(Calendar.MINUTE, 0);
            newBegin.set(Calendar.SECOND, 0);

            newEnd.set(Calendar.HOUR_OF_DAY, 23);
            newEnd.set(Calendar.MINUTE, 59);
            newEnd.set(Calendar.SECOND, 59);

            return (now.after(newBegin) && now.before(end)) || (now.after(begin) && now.before(newEnd));
        }
    }

    /**
     * 将字符串参数转成Map集合
     * @param paramStr
     * @return
     */
    public Map<String,Object> paramToMap(String paramStr){
        //将字符串参数转成数据组
        String[] params = paramStr.split("&");
        Map<String, Object> resMap = new HashMap<>();
        for (int i = 0; i < params.length; i++) {
            String[] param = params[i].split("=");
            if (param.length >= 2) {
                String key = param[0];
                String value = param[1];
                for (int j = 2; j < param.length; j++) {
                    value += "=" + param[j];
                }
                resMap.put(key, value);
            }
        }
        return resMap;
    }

    //财务审核商户提现验参
    public boolean verifyParamNull(Map<String, Object> paramMap) {
        Object orderId = paramMap.get("orderId");
        Object userId = paramMap.get("userId");
        Object orderStatus = paramMap.get("orderStatus");
        Object approval = paramMap.get("approval");
        Object comment = paramMap.get("comment");
        if (paramMap.containsKey("ip")) {
            Object ip = paramMap.get("ip");
            if (ObjectUtil.isNull(ip)) {
                return false;
            }
        }
        if (ObjectUtil.isNull(orderId) || ObjectUtil.isNull(orderStatus) || ObjectUtil.isNull(approval) || ObjectUtil.isNull(comment) || ObjectUtil.isNull(userId)) {
            return false;
        }
        return true;
    }


    public static boolean isNumber(BigDecimal a) {
        double dInput = a.doubleValue();
        long longPart = (long) dInput;
        BigDecimal bigDecimal = new BigDecimal(Double.toString(dInput));
        BigDecimal bigDecimalLongPart = new BigDecimal(Double.toString(longPart));
        double dPoint = bigDecimal.subtract(bigDecimalLongPart).doubleValue();
        System.out.println("整数部分为:" + longPart + "\n" + "小数部分为: " + dPoint);
        return dPoint > 0;
    }

    public static Result enterWit(UserInfo user, String orderId, Integer witAamount) {
        if (StrUtil.isEmpty(user.getInterFace())) {
            log.info("【代付反查接口不存在结果为空，商户号：" + user.getUserId() + ",订单号：" + orderId + "】");
            return Result.buildFailMessage("请配置代付反查接口");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("appId", user.getUserId());
        map.put("appOrderId", orderId);
        String paramStr = MapUtil.createParam(map);
        log.info("【验证签名前的参数为：" + paramStr.toString() + "】");
        String md5 = RSAUtils.md5(paramStr + user.getPayPasword());
        map.put("sign", md5);
        String post = HttpUtil.post(user.getInterFace(), map);
        if (StrUtil.isEmpty(post)) {
            log.info("【代付反查结果为空，商户号：" + user.getUserId() + ",订单号：" + orderId + "】");
            return Result.buildFailMessage("代付反查结果为空");
        }
        log.info("【代付反查商户返回结果：" + post + "】");
        JSONObject jsonObject = JSONUtil.parseObj(post);
        /**
         * orderId	商户订单号
         * success	订单状态码
         * amount	金额
         * sign	签名
         * appId	商户号
         */
        if (StrUtil.isEmpty(jsonObject.toString())) {
            log.info("【代付反查结果为空，商户号：" + user.getUserId() + ",订单号：" + orderId + "】");
            return Result.buildFailMessage("代付反查结果为空");
        }
        String orderId1 = jsonObject.getStr("orderId");
        String success = jsonObject.getStr("success");
        if (success.equals("false")) {
            log.info("【当前商户不同意出款，商户号：" + user.getUserId() + ",订单号：" + orderId + "，当前查询金额：" + witAamount + "，当前订单金额：" + witAamount + "】");
            return Result.buildFailMessage("代付反查结果商户不同意出款");

        }
        String amount = jsonObject.getStr("amount");
        String sign = jsonObject.getStr("sign");
        String appId = jsonObject.getStr("appId");
        Integer integer = Integer.valueOf(new BigDecimal(amount).intValue());
        if (!integer.equals(witAamount)) {
            log.info("【代付反查结果与当前金额不一样，商户号：" + user.getUserId() + ",订单号：" + orderId + "，当前查询金额：" + integer + "，当前订单金额：" + witAamount + "】");
            return Result.buildFailMessage("代付反查结果与当前金额不一致");
        }
        Map<String, Object> map1 = new HashMap<>();
        map1.put("orderId", orderId);
        map1.put("success", success);
        map1.put("amount", amount);
        map1.put("appId", appId);
        log.info("【验证代付反查签名前的参数为：" + map1.toString() + "】");
        String mima = MapUtil.createParam(map1);
        log.info("【验证签名前的参数为：" + mima.toString() + "】");
        String sign1 = RSAUtils.md5(mima + user.getPayPasword());
        if (!sign1.equals(sign)) {
            log.info("【签名验证失败，商户号：" + user.getUserId() + ",订单号：" + orderId + "，当前查询金额：" + integer + "，当前订单金额：" + witAamount + "】");
            return Result.buildFailMessage("代付反查签名验证失败");
        }
        if (success.equals("true")) {
            return Result.buildSuccessMessage("代付反查验证成功");
        }
        return Result.buildFailMessage("查询结果有误");
    }

    public static void main(String[] args) {
        String source="appId=GA0044";
        String key = "EB826AD80F91473A8F0488E048B2748D";
        String md5 = RSAUtils.md5(source + key);
        System.out.println(md5);
    }
}

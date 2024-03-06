package alipay.manage.api.channel.deal.xiangyun;

import alipay.manage.api.config.ChannelInfo;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.util.ResultDeal;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component("XiangYunPay")
public class XiangYunPay extends PayOrderService {
    private static final Log log = LogFactory.get();
    @Autowired
    private UserInfoService userInfoServiceImpl;



	public static String createParam(Map<String, Object> map) {
		try {
            if (map == null || map.isEmpty()) {
                return null;
            }
            Object[] key = map.keySet().toArray();
            Arrays.sort(key);
            StringBuffer res = new StringBuffer(128);
            for (int i = 0; i < key.length; i++) {
                if (ObjectUtil.isNotNull(map.get(key[i]))) {
                    res.append(key[i] + "=" + map.get(key[i]) + "&");
                }
            }
            String rStr = res.substring(0, res.length() - 1);
            return rStr;
        } catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String md5(String a) {
		String c = "";
		MessageDigest md5;
		String result = "";
		try {
            md5 = MessageDigest.getInstance("md5");
            md5.update(a.getBytes("utf-8"));
            byte[] temp;
            temp = md5.digest(c.getBytes("utf-8"));
            for (int i = 0; i < temp.length; i++) {
                result += Integer.toHexString((0x000000ff & temp[i]) | 0xffffff00).substring(6);
            }
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
		}
		return result;
	}

	@Override
	public Result deal(DealOrderApp dealOrderApp, String payType) {
		log.info("【进入祥云支付】");
		String channelId = payType;//配置的渠道账号
		String create = create(dealOrderApp, channelId);
		if (StrUtil.isNotBlank(create)) {
			log.info("【本地订单创建成功，开始请求远程三方支付】");
			UserInfo userInfo = userInfoServiceImpl.findUserInfoByUserId(dealOrderApp.getOrderAccount());
			if (StrUtil.isBlank(userInfo.getDealUrl())) {
				orderEr(dealOrderApp, "当前商户交易url未设置");
				return Result.buildFailMessage("请联系运营为您的商户号设置交易url");
			}
			log.info("【回调地址ip为：" + userInfo.getDealUrl() + "】");
			String url = createOrder(userInfo.getDealUrl() + PayApiConstant.Notfiy.NOTFIY_API_WAI +
							"/xiangyun-notfiy",
					dealOrderApp.getOrderAmount(),
					create,
					getChannelInfo(channelId, dealOrderApp.getRetain1()),
					dealOrderApp
			);
			if (StrUtil.isNotEmpty(url)) {
				return Result.buildSuccessResult("支付处理中", ResultDeal.sendUrl(url));
			}
		}
		return Result.buildFailMessage("支付错误");
	}

	/**
	 * partner	str(32)	是	是	商户编号，系统提供
	 * service	str(32)	是	是	参考 service字典
	 * tradeNo	str(32)	是	是	交易订单号
	 * amount	str(32)	是	是	交易金额
	 * notifyUrl	str(256)	是	是	异步通知地址
	 * resultType	str(32)	否	是	固定值web或者json(推荐json)
	 * extra	str(32)	否	是	附加信息，查询、回调时返回商户
	 * sign	str(128)	是	否	签名
	 *
	 * @param notfiy
	 * @param orderAmount
	 * @param orderId
	 * @param channelInfo
	 * @param dealOrderApp
	 * @return
	 */
	private String createOrder(String notfiy, BigDecimal orderAmount,
							   String orderId, ChannelInfo channelInfo,
							   DealOrderApp dealOrderApp) {
		Map<String, Object> map = new HashMap<String, Object>();
        String key = channelInfo.getChannelPassword();
        String appid = channelInfo.getChannelAppId();
        String payType = channelInfo.getChannelType();
        map.put("partner", appid);
        map.put("service", payType);
        map.put("tradeNo", orderId);
        map.put("amount", orderAmount);
        map.put("notifyUrl", notfiy);
        map.put("resultType", "json");
        map.put("extra", "extra");
        String createParam = createParam(map);
        String md5 = md5(createParam + "&" + key);
        map.put("sign", md5);
        log.info("【当前祥云请求参数为：" + map.toString() + "】");
        String post = HttpUtil.post(channelInfo.getDealurl(), map);
        log.info("【祥云响应参数为：" + post + "】");
        JSONObject parseObj = JSONUtil.parseObj(post);
        Object object = parseObj.get("isSuccess");
        if (ObjectUtil.isNotNull(object)) {
            log.info("当前祥云的订单为：" + object + "");
            if ("T".equals(object)) {
                Object object2 = parseObj.get("url");
                if (ObjectUtil.isNotNull(object2)) {
                    log.info("【支付链接为：" + object2 + "】");
                    String url = object2.toString();
                    //https://ap5xt6p0w.vanns.vip/api/bank/info/[唯一识别号]?n= 任意 随便  真实 唯一 实际 确认 付款人
                    //https://ap5xt6p0w.vanns.vip/api/bank/info/[唯一识别号]?t=1605764608393&i=0
                    //"https://ap5xt6p0w.vanns.vip/api/bank/fc9025d82ef745168fea6854a92db802"      //源链接 截取唯一识别号
                    String[] split = url.split("/");
                    String last = CollUtil.getLast(Arrays.asList(split));
                    log.info("【祥云支付链接编号为：" + last + "】");
                    String s = StrUtil.subBefore(url, last, true);
                    log.info("【祥云支付链接主链接接口为：" + s + "】");
                    String bankinfo = s + "/info/" + last;
                    String bankinfo1 = s + "/info/" + last + "?t=" + System.currentTimeMillis() + "&i=0";
                    //	log.info("【开始伪装用户请求："+s+"/info/"+last+"】");
                    //	String s1 = HttpUtil.get(bankinfo);
                    //	log.info("【祥云输入姓名返回为："+s1+"】");
                    //	String s2 = HttpUtil.get(bankinfo1);
                    //		log.info("【祥云获取银行卡详情返回为："+s2+"】");
                    return object2.toString();
                }
			} else {
                Object object2 = parseObj.get("msg");
                String msg = "";
                if (ObjectUtil.isNotNull(object2) && StrUtil.isNotEmpty(object2.toString())) {
                    msg = object2.toString();
                } else {
                    msg = "错误：未知错误";
                }
                orderEr(dealOrderApp, msg);
            }
		}
		return "";
	}
}

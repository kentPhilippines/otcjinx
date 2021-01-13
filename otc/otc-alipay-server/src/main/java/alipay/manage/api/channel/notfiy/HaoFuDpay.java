package alipay.manage.api.channel.notfiy;

import alipay.manage.api.config.NotfiyChannel;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import otc.common.PayApiConstant;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
@RestController
public class HaoFuDpay extends NotfiyChannel {
    private static final Log log = LogFactory.get();

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

	@PostMapping("/HaoFuDpay-noyfit")
	public String notify(HttpServletRequest req, HttpServletResponse res) throws IOException {
		String clientIP = HttpUtil.getClientIP(req);
		log.info("【当前回调ip为：" + clientIP + "】");
		if (!"47.52.109.67".equals(clientIP)) {
			log.info("【当前回调ip为：" + clientIP + "，固定IP登记为：" + "47.52.109.67" + "】");
			log.info("【当前回调ip不匹配】");
			return "ip errer";
		}
        /**
         * 		input_charset			10			是			编码格式:UTF-8
         sign_type				3			是			签名方式:MD5
         sign					256			否			MD5加密串
         request_time			20			是			yyyy-MM-dd HH:mm:ss
         trade_id				32			是			系统订单号
         out_trade_no			32			是			外部订单号
         amount_str				9			是			金额(单位:元,支持两位小数)
         amount_fee				9			是			手续费
				status					2			是			状态:0处理中,1完成,2失败
				for_trade_id			32			是			原始订单号(针对退款)
				business_type			3			是			交易类型
				remark					256			是			备注
				create_time				11			是			yyyy-MM-dd HH:mm:ss
				modified_time			11			是			yyyy-MM-dd HH:mm:ss
		 */
		String key  = "afdfasdf16541asdf51asd6f621sd";
		String input_charset = req.getParameter("input_charset");
		String sign_type = req.getParameter("sign_type");
		String request_time = req.getParameter("request_time");
		String trade_id = req.getParameter("trade_id");
		String out_trade_no = req.getParameter("out_trade_no");
		String amount_str = req.getParameter("amount_str");
		String amount_fee = req.getParameter("amount_fee");
		String status = req.getParameter("status");
		String for_trade_id = req.getParameter("for_trade_id");
		String business_type = req.getParameter("business_type");
		String remark = req.getParameter("remark");
		String create_time = req.getParameter("create_time");
		String modified_time = req.getParameter("modified_time");
		String sign = req.getParameter("sign");
		Map<String,Object> map = new HashMap();
		map.put("input_charset", input_charset);
		map.put("request_time", request_time);
		map.put("trade_id", trade_id);
		map.put("out_trade_no", out_trade_no);
		map.put("sign_type", sign_type);
		map.put("amount_str", amount_str);
		map.put("amount_fee", amount_fee);
		map.put("status", status);
		map.put("business_type", business_type);
		map.put("create_time", create_time);
		map.put("modified_time", modified_time);
		log.info("【豪富返回参数：" + map.toString() + "】");
		String createParam = createParam(map);
		log.info("【豪富返回参数加密验签串：" + createParam + "】");
		String md5 = md5(createParam + "&" + key);
		if (md5.equals(sign)) {
			log.info("【当前验签正确：" + map.toString() + "】");
		} else {
			log.info("当前验签错误，我方验签串" + md5 + "，对方验签串：" + sign + "");
			return "errer";
		}
		if ("1".equals(status)) {
			Result dealpayNotfiy = witNotfy(out_trade_no, clientIP);
			if (dealpayNotfiy.isSuccess()) {
				return "success";
			}
		}
		return "end errer";
	}
}

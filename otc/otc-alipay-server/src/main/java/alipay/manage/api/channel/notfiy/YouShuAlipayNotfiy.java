package alipay.manage.api.channel.notfiy;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import alipay.manage.api.config.NotfiyChannel;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import otc.common.PayApiConstant;
import otc.result.Result;
@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
@RestController
public class YouShuAlipayNotfiy extends NotfiyChannel{
	private static final Log log = LogFactory.get();
	@PostMapping("/youshu-notfiy")
	 public String notify(HttpServletRequest req, HttpServletResponse res) throws IOException {
		/**
		 * 	out_order_id		订单号		是	
			realprice			真实金额		是	
			price				订单金额		是	
			order_id			交易流水号		是	
			type				支付类型		是	
			paytime				支付时间		是	
			extend				扩展返回		否	商户附加数据返回
			sign				签名			否	请看验证签名字段格式
		 */
		String clientIP = HttpUtil.getClientIP(req);
		log.info("【当前回调ip为："+clientIP+"】");
		Map map = new HashMap();
		map.put("202.79.174.105", "202.79.174.105");
		map.put("202.79.174.114", "202.79.174.114");
		Object object = map.get(clientIP);
		if(ObjectUtil.isNull(object)) {
			log.info("【当前回调ip为："+clientIP+"，固定IP登记为："+"202.79.174.105"+"，202.79.174.114】");
			log.info("【当前回调ip不匹配】");
			return "当前回调ip不匹配";
		}
		String order_id = req.getParameter("order_id"); // 流水号
	    String out_order_id = req.getParameter("out_order_id"); // 商户订单号
	    String price = req.getParameter("price"); // 支付金额
	    String realprice = req.getParameter("realprice"); // 实际支付金额
	    String type = req.getParameter("type"); // 支付类型
	    String paytime = req.getParameter("paytime"); // 支付时间
	    String extend = req.getParameter("extend"); // 附加数据
	    String sign = req.getParameter("sign");
	    String keyValue=""; // 商户秘钥
	    String SignTemp = "extend="+extend+"&order_id="+order_id+"&out_order_id="+out_order_id+"&paytime="+paytime+"&price="+price+"&realprice="+realprice+"&type="+type+"key"+keyValue;
	    log.info("【优树参数为："+SignTemp.toString()+"】");
	    String md5sign = md5(SignTemp);//MD5加密
	    if (sign.equals(md5sign)){
	    	//支付成功，写返回数据逻辑
	       res.getWriter().write("success");
	    } else {
	    	log.info("【我方验签参数为："+md5sign+"，请求方签名参数为："+sign+"】");
	    	log.info("【验签失败】");
	    	return "验签失败";
	    } 
	    Result dealpayNotfiy = dealpayNotfiy(out_order_id, clientIP);
	    if(dealpayNotfiy.isSuccess())
	    	return "success";
		return "errer";
	}
	private String md5(String a) {
    	String c = "";
    	MessageDigest md5;
	   	String result="";
		try {
			md5 = MessageDigest.getInstance("md5");
			md5.update(a.getBytes("utf-8"));
			byte[] temp;
			temp=md5.digest(c.getBytes("utf-8"));
			for (int i=0; i<temp.length; i++)
				result+=Integer.toHexString((0x000000ff & temp[i]) | 0xffffff00).substring(6);
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
		}
		return result;
	}
}

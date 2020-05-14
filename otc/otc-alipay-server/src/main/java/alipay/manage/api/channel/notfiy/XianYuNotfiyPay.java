package alipay.manage.api.channel.notfiy;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import alipay.manage.api.config.NotfiyChannel;
import cn.hutool.http.HttpUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import otc.common.PayApiConstant;
import otc.result.Result;
@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
@Controller
public class XianYuNotfiyPay extends NotfiyChannel{
	private static final Log log = LogFactory.get();
	@PostMapping("/xianyu-notfiy")
	 public void notify(HttpServletRequest req, HttpServletResponse res) throws IOException {
		log.info("进入咸鱼支付宝H5 回调处理");
		String clientIP = HttpUtil.getClientIP(req);
		log.info("【当前回调ip为："+clientIP+"】");
		if(!clientIP.equals("103.84.90.39")) {
			log.info("【当前回调ip为："+clientIP+"，固定IP登记为："+"103.84.90.39"+"】");
			log.info("【当前回调ip不匹配】");
			res.getWriter().write("ip错误");
			return;
		}
		String queryString = req.getQueryString();
		log.info("【当前咸鱼请求参数为："+queryString+"】");
		String getid = req.getParameter("fxid");
		String getddh = req.getParameter("fxddh");
		String getorder = req.getParameter("fxorder");
		String getattch = req.getParameter("fxattch");
		String getdesc = req.getParameter("fxdesc");
		String getfee = req.getParameter("fxfee");
		String getstatus = req.getParameter("fxstatus");
		String gettime = req.getParameter("fxtime");
		String getsign = req.getParameter("fxsign");
		String successstatus = "1";
		if (getstatus.equals(successstatus)!=true) {
		    res.getWriter().write("状态错误");
		    return;
		}
		//订单签名 【md5(订单状态+商务号+商户订单号+支付金额+商户秘钥)】
		String mysign = md5(getstatus + getid + getddh + getfee + "AHFuoYCUgZcOdpectBxYiPElWMVGljbc");
		mysign = mysign.toLowerCase();
		if (mysign.equals(getsign)!=true) {
		    log.info("签名错误，我方签名："+mysign+"，对方签名："+getsign+"");
		    res.getWriter().write("签名错误");
		    return;
		}
		 Result dealpayNotfiy = dealpayNotfiy(getddh, clientIP);
		 if(dealpayNotfiy.isSuccess()) {
			 res.getWriter().write("success");
			 log.info("【咸鱼H5 交易成功】 ");
			 return;
		 }
		  return;
	}
	 public static String md5(String a) {
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

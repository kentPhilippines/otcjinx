package alipay.manage.api.channel.notfiy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import alipay.manage.api.config.NotfiyChannel;
import alipay.manage.bean.DealOrder;
import alipay.manage.mapper.DealOrderMapper;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import otc.common.PayApiConstant;
import otc.result.Result;
@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
@RestController
public class XianYuNotfiyPay extends NotfiyChannel{
    @Autowired DealOrderMapper dealOrderDao;
	private static final String KEY = "AHFuoYCUgZcOdpectBxYiPElWMVGljbc";
	private static final String FX_ID = "2020177";
	private static final String ORDER_QUERY = "orderquery";
	private static final Log log = LogFactory.get();
	@RequestMapping("/xianyu-notfiy")
	 public void notify(  
			 HttpServletRequest request,
	         HttpServletResponse response
			    ) throws Exception {
		log.info("进入咸鱼支付宝H5 回调处理");
		String clientIP = HttpUtil.getClientIP(request);
		log.info("【当前回调ip为："+clientIP+"】");
		if(!clientIP.equals("103.84.90.39")) {
			log.info("【当前回调ip为："+clientIP+"，固定IP登记为："+"103.84.90.39"+"】");
			log.info("【当前回调ip不匹配】");
			response.getWriter().write("ip错误");
			return;
		}
		List<DealOrder> findXianYuOrder = dealOrderDao.findXianYuOrder();
		for(DealOrder order : findXianYuOrder) {
			ThreadUtil.execute(()->{
				log.info("【进入咸鱼订单查询处理】 ");
				log.info("【当前咸鱼订单号："+order.getRetain2()+"】 ");
				String fxddh = order.getRetain2();//咸鱼订单号
				String fxaction = ORDER_QUERY;
				Map<String, Object>  map = new ConcurrentHashMap<String, Object>();
				map.put("fxid", FX_ID);
				map.put("fxddh", fxddh);
				map.put("fxaction", fxaction);
				map.put("fxsign", md5(FX_ID+fxddh+fxaction+KEY));
				String post = HttpUtil.post("https://csj.fenvun.com/Pay"  , map);
				log.info("【当前返回数据为：】"+post.toString());
				JSONObject parseObj = JSONUtil.parseObj(post);
				XianYu bean = JSONUtil.toBean(parseObj, XianYu.class);
				log.info("【当前返回数据为：】"+bean.toString());
				if(bean.getFxstatus().equals("1")) {
					String enterOrder = enterOrder(order.getOrderId(), clientIP);
					if(StrUtil.isNotBlank(enterOrder)) {
						try {
							response.getWriter().write("success");
						} catch (IOException e) {
							log.info("【响应数据出错，当前订单号："+order.getOrderId()+"，咸鱼订单号："+order.getRetain2()+"】 ");
						}
					}
				}
			});
		}
	}
	String enterOrder(String orderId ,String ip){
		 Result dealpayNotfiy = dealpayNotfiy(orderId, ip);
		 if(dealpayNotfiy.isSuccess()) {
			 log.info("【咸鱼H5 交易成功】");
			 return "success";
		 }
		return null;
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
class XianYu {
	//{"fxid":"2020177","fxstatus":"1","fxddh":"12365441234","fxorder":"qzf20200516171826589638709215147","fxdesc":"000000","fxfee":"2000.00","fxattch":"test","fxtime":"1589620706","fxsign":"8f31d1ccd2ffeae15885c4e33b08c01c"}
	private String fxid;
	private String fxstatus;
	private String fxddh;
	private String fxorder;
	private String fxdesc;
	private String fxattch;
	private String fxtime;
	private String fxsign;
	public XianYu() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getFxid() {
		return fxid;
	}
	public void setFxid(String fxid) {
		this.fxid = fxid;
	}
	public String getFxstatus() {
		return fxstatus;
	}
	public void setFxstatus(String fxstatus) {
		this.fxstatus = fxstatus;
	}
	public String getFxddh() {
		return fxddh;
	}
	public void setFxddh(String fxddh) {
		this.fxddh = fxddh;
	}
	public String getFxorder() {
		return fxorder;
	}
	public void setFxorder(String fxorder) {
		this.fxorder = fxorder;
	}
	public String getFxdesc() {
		return fxdesc;
	}
	public void setFxdesc(String fxdesc) {
		this.fxdesc = fxdesc;
	}
	public String getFxattch() {
		return fxattch;
	}
	public void setFxattch(String fxattch) {
		this.fxattch = fxattch;
	}
	public String getFxtime() {
		return fxtime;
	}
	public void setFxtime(String fxtime) {
		this.fxtime = fxtime;
	}
	public String getFxsign() {
		return fxsign;
	}
	public void setFxsign(String fxsign) {
		this.fxsign = fxsign;
	}
	@Override
	public String toString() {
		return "XianYu [fxid=" + fxid + ", fxstatus=" + fxstatus + ", fxddh=" + fxddh + ", fxorder=" + fxorder
				+ ", fxdesc=" + fxdesc + ", fxattch=" + fxattch + ", fxtime=" + fxtime + ", fxsign=" + fxsign + "]";
	}
}

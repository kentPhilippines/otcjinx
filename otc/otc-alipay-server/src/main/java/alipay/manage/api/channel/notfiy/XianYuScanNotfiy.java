package alipay.manage.api.channel.notfiy;

import alipay.manage.api.config.NotfiyChannel;
import alipay.manage.bean.DealOrder;
import alipay.manage.mapper.DealOrderMapper;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
@RestController
public class XianYuScanNotfiy extends NotfiyChannel {
	private static final String KEY = "gCwqqQSkBGKAYHKFOHdToMSLAErLVmeQ";
	private static final String FX_ID = "2020183";
	private static final String ORDER_QUERY = "orderquery";
	private static final Log log = LogFactory.get();
	@Autowired
	DealOrderMapper dealOrderDao;

	@RequestMapping("/xianyu-scan-notfiy")
	public void notify(
			HttpServletRequest request,
			HttpServletResponse response
	) throws Exception {
		log.info("进入咸鱼支付宝扫码 回调处理");
		String clientIP = HttpUtil.getClientIP(request);
		log.info("【当前回调ip为：" + clientIP + "】");
		if (!clientIP.equals("185.207.152.3")) {
			log.info("【当前回调ip为：" + clientIP + "，固定IP登记为：" + "185.207.152.3" + "】");
			log.info("【当前回调ip不匹配】");
			response.getWriter().write("ip错误");
			return;
		}
		response.getWriter().write("success");
		List<DealOrder> findXianYuOrder = dealOrderDao.findXianYuOrder1();
		for (DealOrder order : findXianYuOrder) {
			ThreadUtil.execute(() -> {
				log.info("【进入咸鱼支付宝扫码订单查询处理】 ");
				log.info("【当前咸鱼支付宝扫码订单号：" + order.getOrderId() + "】 ");
				String fxddh = order.getOrderId();//咸鱼订单号
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
							log.info("【响应数据出错，当前订单号："+order.getOrderId()+"，咸鱼支付宝扫码订单号："+order.getOrderId()+"】 ");
						}
					}
				}else {
					map = null;
					fxddh =null;
					fxaction = null;
				}
			});
		}
	}
	String enterOrder(String orderId ,String ip){
		 Result dealpayNotfiy = dealpayNotfiy(orderId, ip);
		 if(dealpayNotfiy.isSuccess()) {
			 log.info("【支付宝扫码 交易成功】");
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

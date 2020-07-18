package alipay.manage.api.channel.notfiy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import alipay.manage.api.channel.util.xianyu.XianYuUtil;
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
import otc.common.PayApiConstant;
import otc.result.Result;

@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
@RestController
public class XianYuIosAlipayH5Notfiy extends NotfiyChannel{
    @Autowired DealOrderMapper dealOrderDao;
	private static final String ORDER_QUERY = "orderquery";
	private static final Log log = LogFactory.get();
    @RequestMapping("/chuanshanjia-notfiy")
	 public void notify(
			 HttpServletRequest request,
	         HttpServletResponse response
			    ) throws Exception {
		param(request);
		param2(request);
		log.info("进入 穿山甲 回调处理");
		String clientIP = HttpUtil.getClientIP(request);
		log.info("【当前回调ip为："+clientIP+"】");
		if(!clientIP.equals("185.207.152.3")) {
			log.info("【当前回调ip为："+clientIP+"，固定IP登记为："+"185.207.152.3"+"】");
			log.info("【当前回调ip不匹配】");
			response.getWriter().write("ip错误");
			return;
		}
		response.getWriter().write("success");
		List<DealOrder> findXianYuOrder = dealOrderDao.findXianYuOrder2();
		for(DealOrder order : findXianYuOrder) {
			ThreadUtil.execute(()->{
				log.info("【进入 穿山甲 订单查询处理】 ");
				log.info("【当前 穿山甲 订单号："+order.getOrderId()+"】 ");
				String fxddh = order.getOrderId();//咸鱼订单号
				String fxaction = ORDER_QUERY;
				Map<String, Object>  map = new ConcurrentHashMap<String, Object>();
				map.put("fxid", XianYuUtil.APPID );
				map.put("fxddh", fxddh);
				map.put("fxaction", fxaction);
				map.put("fxsign", XianYuUtil.md5(XianYuUtil.APPID +fxddh+fxaction+XianYuUtil.KEY ));
				String post = HttpUtil.post(XianYuUtil.URL  , map);
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
							log.info("【响应数据出错，当前订单号："+order.getOrderId()+"，穿山甲 订单号："+order.getOrderId()+"】 ");
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

	void param2(HttpServletRequest req){
		String fxddh = req.getParameter("fxddh");
		if(StrUtil.isBlank(fxddh))
			log.info("穿山甲数据获取为空：");

	}
	void param(HttpServletRequest req) throws IOException {
		InputStream inputStream = req.getInputStream();
		String body;
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;
		try {
			if (inputStream != null) {
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				char[] charBuffer = new char[128];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			} else {
				stringBuilder.append("");
			}
		} catch (IOException ex) {
			throw ex;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException ex) {
					throw ex;
				}
			}
		}
	 log.info("穿山甲数据："+stringBuilder);

	}


}

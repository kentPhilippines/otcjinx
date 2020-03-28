package alipay.manage.util;

import alipay.manage.bean.DealOrder;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.UserInfo;
import alipay.manage.mapper.DealOrderAppMapper;
import alipay.manage.service.OrderService;
import alipay.manage.service.UserInfoService;
import cn.hutool.http.HttpUtil;
import otc.api.alipay.Common;
import otc.util.RSAUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
public class NotifyUtil {
    Logger log= LoggerFactory.getLogger(NotifyUtil.class);
    @Autowired  OrderService orderSerciceImpl;
    @Autowired SettingFile settingFile;
    @Autowired DealOrderAppMapper dealOrderAppDao;
    @Autowired CheckUtils checkUtils;
    @Autowired UserInfoService userInfoServiceImpl;
    private static final String SU_MSG = "成功";
    private static final String ER_MSG = "失败";
    private static final String YU_MSG = "处理中";
    /**
     * <p>根据下游交易订单号,和订单状态 发送通知</p>
     * @param orderId  下游交易订单号
     * @param falg  通知成功或者失败 true 成功  false失败
     */
    public void sendMsg(String orderId) {
      log.info("=======[准备向下游商户发送通知]=======");
      DealOrder order = orderSerciceImpl.findOrderByAssociatedId(orderId);
      DealOrderApp orderApp = dealOrderAppDao.findOrderByOrderId(orderId);
      UserInfo userInfo = userInfoServiceImpl.findUserInfoByUserId(orderApp.getOrderAccount());
      /**
       * 		tradesno			M(5)				网关订单号
      			status				M(32)				状态，2-成功，3-失败，1-处理中
      			realamount			M(20)				实际交易金额（元）
      			timeend				M(32)				完成时间,格式: yyyyMMddHHmmss
      			appid				M(20)				平台id
      			apporderid			M(14)				请求订单号
      			statusdesc			M(20)				状态描述
      			hmac				M(32)				签名数据
       */
      String apporderid = order.getExternalOrderId();
      String tradesno = order.getAssociatedId();
      String status = order.getOrderStatus();
      BigDecimal amount = order.getDealAmount();
      String appid = order.getOrderAccount();
      String statusdesc = getOrderMsg(status);
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("apporderid", apporderid);
      map.put("tradesno", apporderid);
      map.put("status", status);
      map.put("amount", apporderid);
      map.put("appid", appid);
      map.put("statusdesc", statusdesc);
      String sign = checkUtils.getSign(map, userInfo.getPayPasword());
      map.put("sign", sign);
      send( order.getNotify(), order.getOrderId(), map);
    }
    /**
     * <p>发送通知</p>
     * @param url   发送通知的地址
     * @param orderId  发送订单ID
     * @param msg  发送通知的内容
     */
    private void send(String url,String orderId,Map<String,Object> msg){
        String result = HttpUtil.post(url, msg);
        log.info("服务器返回结果为: " + result.toString());
        String isNotify="NO";
        if ("seccess".equalsIgnoreCase(result)) {
            isNotify = "YES";
            log.info("【下游商户返回信息为成功,成功收到回调信息】");
        }else
            log.info("【下游商户未收到回调信息，或回调信息下游未成功返回】");
        //更新订单是否通知成功状态
        boolean flag = orderSerciceImpl.updataOrderisNotifyByOrderId(orderId,isNotify);
        if (!flag)
            log.info("【更新是否通知状态失败！】");
    }
  String  getOrderMsg(String status){
    	switch (status) {
		case Common.Order.DealOrder.ORDER_STATUS_DISPOSE:
			return YU_MSG;
		case Common.Order.DealOrder.ORDER_STATUS_ER:
			return ER_MSG;
		case Common.Order.DealOrder.ORDER_STATUS_SU:
			return SU_MSG;
		}
		return SU_MSG;
    }
    
    
    
}

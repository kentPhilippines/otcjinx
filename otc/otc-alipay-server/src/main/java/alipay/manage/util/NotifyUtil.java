package alipay.manage.util;

import alipay.manage.bean.DealOrder;
import alipay.manage.service.OrderService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class NotifyUtil {
    Logger log= LoggerFactory.getLogger(NotifyUtil.class);
    @Autowired  OrderService orderSerciceImpl;
    @Autowired SettingFile settingFile;
    /**
     * <p>根据下游交易订单号,和订单状态 发送通知</p>
     * @param orderId  下游交易订单号
     * @param falg  通知成功或者失败 true 成功  false失败
     */
    public void sendMsg(String orderId, boolean falg) {
      log.info("=======[准备向下游商户发送通知]=======");
      List<DealOrder> orderByAssociateId=orderSerciceImpl.getOrderByAssociatedId(orderId);
      String url=settingFile.getName("yuechuang_notify_url");
      String externalOrderId= CollUtil.getFirst(orderByAssociateId).getAssociatedId();//外部订单号(就是下游商户的订单号)
      Map<String,Object> msg=new HashMap<>();
      msg.put("externalOrderId",externalOrderId);
      msg.put("success",falg);
      log.info("========【发送通知的参数情况:"+msg.toString()+"】=======");
      log.info("========【发送通知的地址:"+url.toString()+"】=======");
      send(url,CollUtil.getFirst(orderByAssociateId).getOrderId(),msg);
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
        boolean flag=orderSerciceImpl.updataOrderisNotifyByOrderId(orderId,isNotify);
        if (!flag)
            log.info("【更新是否通知状态失败！】");
    }

    public void sendYuC(String orderId, boolean flag) {
      Map<String,Object> msg=new HashMap<>();
      msg.put("externalOrderId",orderId);
      msg.put("success",flag);
      HttpUtil.post(settingFile.getName("yuechuang_notity_url"),msg);
    }
}

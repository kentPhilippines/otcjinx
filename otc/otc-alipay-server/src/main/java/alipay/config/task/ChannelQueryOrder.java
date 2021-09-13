package alipay.config.task;

import alipay.manage.api.channel.deal.shenfu.ShenfuHuafeiDPay;
import alipay.manage.api.config.NotfiyChannel;
import alipay.manage.service.WithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.result.Result;

import java.util.List;

@Component
public class ChannelQueryOrder extends NotfiyChannel {
    @Autowired
    private ShenfuHuafeiDPay shenfuHuafeiDPay;
  public void   query(){
      Result order = shenfuHuafeiDPay.findOrder();
      if(order.isSuccess()){
        List<String> orderList =  (List)order.getResult();
        for(String orderId : orderList){
            Result result = witNotfy(orderId, "8.210.34.242");
            if(result.isSuccess()){
                log.info("当前订单号为："+orderId+" 查询回调成功");
            }
        }
      }
  }

















}

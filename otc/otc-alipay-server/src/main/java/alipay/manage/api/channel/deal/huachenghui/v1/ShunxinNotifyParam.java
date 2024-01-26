package alipay.manage.api.channel.deal.huachenghui.v1;

import lombok.Data;

@Data
public class ShunxinNotifyParam {

    private String income;
    private String payOrderId;
    private String amount;
    private String mchId;
    private String productId;
    private String mchOrderNo;
    private String paySuccTime;
    private String sign;
    private String channelOrderNo;
    private String backType;
    private String param1;
    private String param2;
    private String appId;
    private String status;


}

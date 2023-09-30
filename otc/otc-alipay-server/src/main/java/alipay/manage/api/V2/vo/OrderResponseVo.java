package alipay.manage.api.V2.vo;


import lombok.Data;

@Data
public class OrderResponseVo {
    private String orderId;
    private String orderNo;
    private String orderStatus;
    private String amount;
    private String userId;
    private String sign;
}

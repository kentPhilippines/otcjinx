package alipay.manage.service;

import alipay.manage.bean.DealWit;

public interface DealWitService {
    boolean add(DealWit order);

    void updateOrderRequest(String orderId, String toString);

    void updateOrderResponse(String orderId, String toString);

    boolean updateOrderStatus(String orderId, String status);

    DealWit findOrderByOrderId(String orderId);


    void updateWitEr(String orderId, String msg);

    void witOrdererSu(String orderId, String msg);

}

package alipay.manage.service.impl;

import alipay.manage.bean.DealWit;
import alipay.manage.mapper.AlipayDealWitMapper;
import alipay.manage.service.DealWitService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
public class DealWitServIceImpl implements DealWitService {

    @Resource
    AlipayDealWitMapper alipayDealWitDao;

    @Override
    public boolean add(DealWit order) {


        int i = alipayDealWitDao.insertSelective(order);

        return i > 0 && i < 2;


    }

    @Override
    public void updateOrderRequest(String orderId, String msg) {
        alipayDealWitDao.updateOrderRequest(orderId, msg);
    }

    @Override
    public void updateOrderResponse(String orderId, String msg) {
        alipayDealWitDao.updateOrderResponse(orderId, msg);
    }

    @Override
    public boolean updateOrderStatus(String orderId, String status) {
        int a = alipayDealWitDao.updateOrderStatus(orderId, status);
        return a > 0 && a < 2;
    }

    @Override
    public DealWit findOrderByOrderId(String orderId) {
        DealWit wit = alipayDealWitDao.findOrderByOrderId(orderId);
        return wit;

    }

    @Override
    public void updateWitEr(String orderId, String msg) {
        alipayDealWitDao.updateWitEr(orderId,msg);
    }

    @Override
    public void witOrdererSu(String orderId, String msg) {
        alipayDealWitDao.witOrdererSu(orderId,msg);

    }
}

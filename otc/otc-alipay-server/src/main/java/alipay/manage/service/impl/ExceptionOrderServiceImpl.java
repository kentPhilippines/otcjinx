package alipay.manage.service.impl;

import alipay.manage.bean.ExceptionOrder;
import alipay.manage.bean.util.DealBean;
import alipay.manage.bean.util.WithdrawalBean;
import alipay.manage.mapper.ExceptionOrderMapper;
import alipay.manage.service.ExceptionOrderService;
import org.springframework.stereotype.Service;
import otc.util.number.Number;

import javax.annotation.Resource;

@Service
public class ExceptionOrderServiceImpl implements ExceptionOrderService {
    //1交易,2人工加款,4人工扣款,5代付
    private final static Integer DEAL = 1;
    private final static Integer ADD_ADMOUNT = 1;
    private final static Integer DELETE_AMOUNT = 1;
    private final static Integer WIT = 1;
    private final static String OPERATION = "STSTEM";

    @Resource ExceptionOrderMapper exceptionOrderDao;
    @Override
    public boolean addOrder(ExceptionOrder order) {
        int i = exceptionOrderDao.insertSelective(order);
        return i>0 && i < 2;
    }




    @Override
    public boolean addDealOrder(DealBean deal, String msg,String ip) {
        ExceptionOrder exceptionOrder = new ExceptionOrder();
        exceptionOrder.setOrderExceptId(Number.getExc());
        exceptionOrder.setOrderId(deal.getOrderId());
        exceptionOrder.setOrderGenerationIp(ip);
        exceptionOrder.setExplains(msg.trim());
        exceptionOrder.setOrderAccount(deal.getAppId());
        exceptionOrder.setExceptOrderAmount(deal.getAmount());
        exceptionOrder.setExceptStatus(1);
        exceptionOrder.setExceptType(DEAL);
        exceptionOrder.setOperation(OPERATION);
        int i = exceptionOrderDao.insertSelective(exceptionOrder);
        return i>0 && i < 2;
    }

    @Override
    public boolean addDealOrderOthen(String msg, String user, String ip) {
        ExceptionOrder exceptionOrder = new ExceptionOrder();
        exceptionOrder.setOrderExceptId(Number.getExc());
        exceptionOrder.setOrderId(Number.getExc());
        exceptionOrder.setOrderGenerationIp(ip);
        exceptionOrder.setExplains(msg.trim());
        exceptionOrder.setOrderAccount(user);
        exceptionOrder.setExceptType(DEAL);
        exceptionOrder.setExceptStatus(1);
        exceptionOrder.setOperation(OPERATION);
        int i = exceptionOrderDao.insertSelective(exceptionOrder);
        return i>0 && i < 2;
    }

    @Override
    public boolean addWitOrder(WithdrawalBean wit, String msg) {
        return false;
    }


}

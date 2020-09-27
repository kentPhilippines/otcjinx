package alipay.manage.service.impl;

import alipay.manage.bean.ExceptionOrder;
import alipay.manage.mapper.ExceptionOrderMapper;
import alipay.manage.service.ExceptionOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ExceptionOrderServiceImpl implements ExceptionOrderService {
    @Resource ExceptionOrderMapper exceptionOrderDao;
    @Override
    public boolean addOrder(ExceptionOrder order) {
        int i = exceptionOrderDao.insertSelective(order);
        return i>0 && i < 2;
    }
}

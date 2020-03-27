package alipay.manage.service.impl;

import alipay.manage.bean.UserRate;
import alipay.manage.mapper.UserRateMapper;
import alipay.manage.service.UserRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRateServiceImpl implements UserRateService {
    @Autowired
    UserRateMapper userRateMapper;
    @Override
    public UserRate findProductFeeBy(String userId, String productCode) {
        UserRate userRate=userRateMapper.findProductFeeBy(userId,productCode);
        System.out.println("获取结果---->" + userRate);
        return userRate;
    }
}

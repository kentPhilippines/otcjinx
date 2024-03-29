package alipay.manage.service.impl;

import alipay.manage.bean.Correlation;
import alipay.manage.bean.CorrelationData;
import alipay.manage.bean.CorrelationDataExample;
import alipay.manage.bean.CorrelationExample;
import alipay.manage.bean.CorrelationExample.Criteria;
import alipay.manage.bean.util.DataArray;
import alipay.manage.bean.util.UserCountBean;
import alipay.manage.mapper.CorrelationDataMapper;
import alipay.manage.mapper.CorrelationMapper;
import alipay.manage.service.CorrelationService;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import otc.api.alipay.Common;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

@Component
public class CorrelationServiceImpl implements CorrelationService {
    static final Integer IS_AGENT_AGENT = 1;
    static final Integer IS_AGENT_ME = 2;
    Logger log = LoggerFactory.getLogger(CorrelationServiceImpl.class);
    @Resource
    CorrelationDataMapper correlationDataDao;
    @Resource
    CorrelationMapper correlationDao;

    @Override
    public int insertEntity(Correlation record) {
        return correlationDao.insertSelective(record);
    }

    @Override
    public List<Correlation> selectByExample(Correlation correlationEntity) {
        CorrelationExample example = new CorrelationExample();
        Criteria criteria = example.createCriteria();
        criteria.andChildrenNameEqualTo(correlationEntity.getChildrenName());
        criteria.andDistanceEqualTo(1);
        List<Correlation> selectByExample = correlationDao.selectByExample(example);
        return selectByExample;
    }

    @Override
    public List<Correlation> selectAllParentRelList(String accountId) {
        List<Correlation> list = correlationDao.selectAllParentRelList(accountId);
        return list;
    }

    @Override
    public Correlation findEntityByParentId(String accountId) {
        return correlationDao.findEntityByParentId(accountId);
    }

    @Override
    public boolean addCorrelationDate(CorrelationData entity) {
        int insertSelective = correlationDataDao.insertSelective(entity);
        return insertSelective > 0 && insertSelective < 2;
    }

    @Override
    public void updateCorrelationDate(String orderId) {
        Integer orderStatusSu = Integer.valueOf(Common.Order.DealOrder.ORDER_STATUS_SU);
        CorrelationDataExample example = new CorrelationDataExample();
        alipay.manage.bean.CorrelationDataExample.Criteria criteria = example.createCriteria();
        criteria.andOrderIdEqualTo(orderId);
        CorrelationData entity = new CorrelationData();
        entity.setOrderStatus(orderStatusSu);
        int updateByExampleSelective = correlationDataDao.updateByExampleSelective(entity, example);
        if (updateByExampleSelective > 0 && updateByExampleSelective < 2) {
            log.info("当前订单已存入统计数据，当前订单号为：" + orderId);
        }
    }


    @Override
    public boolean openAccount(String myId, String myName, Integer myType, String parentId, String parentName) {
        /**
         * <p>开户逻辑</p>
         * 1，首先根据父类id查询到于父类有关的所有 祖父类、
         * 2，拿到所有父类和祖父类的关系实体
         * 3，距离+1
         */
        List<Correlation> findCorrelation = findCorrelation(parentId);
        for (Correlation corr : findCorrelation) {
            corr.setChildrenName(myName);
            corr.setDistance(corr.getDistance() + 1);
            corr.setChildrenType(myType);
            corr.setMedium("");
        }
        Correlation entity = new Correlation();
        entity.setChildrenName(myName);
        entity.setParentName(myName);
        entity.setParentType(myType);
        entity.setChildrenType(myType);
        entity.setMedium("");
        entity.setDistance(0);
        findCorrelation.add(entity);
        int a = correlationDao.insertCorrlationList(findCorrelation);
        return a > 0;
    }

    List<Correlation> findCorrelation(String myId) {
        CorrelationExample example = new CorrelationExample();
        Criteria criteria = example.createCriteria();
        criteria.andChildrenNameEqualTo(myId);
        log.info("example ->", example);
        List<Correlation> selectByExample = correlationDao.selectByExample(example);
        log.info("selectByExample-->", selectByExample);
        return selectByExample;
    }

    @Override
    public boolean addAccountMedium(String myName, String myId, Integer mediumId) {
        List<Correlation> findCorrelation = findCorrelation(myId);
        for (Correlation corr : findCorrelation) {
            if (StrUtil.isBlank(corr.getMedium())) {
                corr.setMedium(mediumId + ",");
            } else {
                corr.setMedium(corr.getMedium() + mediumId + ",");
            }
        }
        int a = correlationDao.addAccountMedium(findCorrelation);
        return a > 0;
    }

    @Override
    public boolean deleteAccountMedium(String myName, String myId, Integer mediumId) {
        log.info("参数myId" + myName);
        log.info("参数myId" + myId);
        log.info("参数myId" + mediumId);
        List<Correlation> findCorrelation = findCorrelation(myId);
        log.info("获取参数findCorrelation ::: " + findCorrelation);
        for (Correlation corr : findCorrelation) {
            corr.setMedium(corr.getMedium().replace(mediumId + ",", ""));
        }
        int a = correlationDao.addAccountMedium(findCorrelation);
        return a > 0;
    }

    @Override
    public boolean upgradeAaccount(@NotNull String userId) {
        List<Correlation> findCorrelation = findCorrelation(userId);
        int a = 0;
        for (Correlation entity : findCorrelation) {
            entity.setChildrenType(IS_AGENT_AGENT);
            if (entity.getParentName().equals(entity.getChildrenName())) {
                a = entity.getId();
            }
        }
        if (a != 0) {
            correlationDao.updateParentId(a);
        }
        int updateChildren = correlationDao.updateChildren(userId);
        return updateChildren > 0;
    }

    @Override
    public UserCountBean findMyDateAgen(String userId) {
        UserCountBean bean = correlationDao.findMyDateAgen(userId);
        return bean;
    }

    @Override
    public UserCountBean findDealDate(@NotNull String userId) {
        UserCountBean bean = correlationDataDao.findDealDate(userId);
        return bean;
    }

    @Override
    public int[][] findOnline(String userId) {
        int[][] a = new int[3][1];
        List<DataArray> dataArray = correlationDataDao.findOnline(userId);
        int count = correlationDao.findMyUserCount(userId);
        if (dataArray.size() != 0) {
            for (DataArray array : dataArray) {
                a[0][0] = array.getDataArray() + a[0][0];
            }
            a[1][0] = dataArray.size();
        } else {
            a[0][0] = 0;
            a[1][0] = 0;
        }
        a[2][0] = count;
        return a;
    }

    @Override
    public String findAgent(String qrcodeId) {
        return correlationDao.findAgent(qrcodeId);
    }
}

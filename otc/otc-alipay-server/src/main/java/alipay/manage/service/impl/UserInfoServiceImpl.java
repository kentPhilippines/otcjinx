package alipay.manage.service.impl;

import alipay.config.redis.RedisUtil;
import alipay.manage.bean.*;
import alipay.manage.bean.UserInfoExample.Criteria;
import alipay.manage.mapper.FileListMapper;
import alipay.manage.mapper.UserFundMapper;
import alipay.manage.mapper.UserInfoMapper;
import alipay.manage.mapper.UserRateMapper;
import alipay.manage.service.MediumService;
import alipay.manage.service.UserInfoService;
import alipay.manage.util.AttributeUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import otc.bean.alipay.FileList;
import otc.bean.alipay.Medium;
import otc.common.RedisConstant;
import otc.result.Result;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class UserInfoServiceImpl implements UserInfoService {
    static final String QR_CODE = "QR:CODE";
    Logger log = LoggerFactory.getLogger(UserInfoServiceImpl.class);
    @Autowired
    MediumService mediumService;
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private FileListMapper fileListMapper;
    @Resource
    private UserRateMapper userRateDao;
    @Resource
    private UserFundMapper userFundDao;
    @Resource
    private UserInfoMapper userInfoDao;
    @Resource
    private AttributeUtil attributeUtil;
    static Lock lock = new ReentrantLock();
    /**
     * <p>查询自己的子账户</p>
     *
     * @param user
     * @return
     */
    @Override
    public List<UserInfo> findSunAccount(UserInfo user) {
        UserInfoExample example = new UserInfoExample();
        UserInfoExample.Criteria criteria = example.createCriteria();
        if (StrUtil.isNotBlank(user.getUserId())) {
            criteria.andUserIdEqualTo(user.getUserId());
        }
        if (StrUtil.isNotBlank(user.getAgent())) {
            criteria.andAgentEqualTo(user.getAgent());
        }
        List<UserInfo> userInfos = userInfoMapper.selectByExample(example);
        return userInfos;
    }

    @Override
    public List<UserInfo> findSumAgentUserByAccount(String userId) {
        return null;
    }

    @Override
    public UserInfo getUser(String username) {
        UserInfo selectByAccountId = userInfoMapper.selectByUserName(username);
        return selectByAccountId;
    }

    @Override
    public List<String> findSunAccountByUserId(String userId) {
        // TODO Auto-generated method stub
        UserInfoExample example = new UserInfoExample();
        Criteria criteria = example.createCriteria();
        criteria.andAgentEqualTo(userId);
        List<UserInfo> selectByExample = userInfoMapper.selectByExample(example);
        List<String> list = new ArrayList();
        for (UserInfo user : selectByExample) {
            list.add(user.getUserId());
        }
        return list;
    }

    @Override
    public boolean updateIsAgent(String accountId) {
        return false;
    }

    @Override
    public Boolean updataStatusEr(String userId) {
        int a = userInfoMapper.updataStatusEr(userId);
        return a > 0 && a < 2;
    }

    @Override
    public UserInfo findUserInfoByUserId(String userId) {
        UserInfo selectByAccountId = userInfoMapper.findUserByUserId(userId);
        return selectByAccountId;
    }

    @Override
    public Boolean updataAmount(UserFund userFund) {
        UserFundExample example = new UserFundExample();
        UserFundExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userFund.getUserId());
        criteria.andVersionEqualTo(userFund.getVersion());
        userFund.setId(null);
        userFund.setUserName(null);
        userFund.setUserType(null);
        userFund.setIsAgent(null);
        userFund.setCreateTime(null);
        int updateByExampleSelective = 0 ;
        try {
            updateByExampleSelective   = userFundDao.updateByExampleSelective(userFund, example);
        }finally {

        }
        return updateByExampleSelective > 0 && updateByExampleSelective < 2;
    }

    @Override
    public UserRate findUserRateById(Integer feeId) {
        return userRateDao.selectByPrimaryKey(feeId);
    }

    @Override
    public List<String> findSubLevelMembers(String accountId) {
        List<String> childAgentList = userInfoMapper.selectChildAgentListById(accountId);
        String first = CollUtil.getFirst(childAgentList);
        String[] split = first.split(",");
        if (split.length > 2) {
            childAgentList = Arrays.asList(split);
        }
        return childAgentList;
    }

    @Override
    public Result addQrByMedium(String qrcodeId, String mediumId, String amount, String userId, String flag) {
        Medium medium = mediumService.findMediumById(mediumId);
        if (ObjectUtil.isNull(medium)) {
            return Result.buildFailResult("无此收款媒介");
        }
        FileList qrcode = new FileList();
        qrcode.setFileId(qrcodeId);
        qrcode.setConcealId(mediumId);
        qrcode.setCode(medium.getCode() + "_qr");
        qrcode.setStatus(1);
        if ("false".equals(flag)) {
            qrcode.setFixationAmount(new BigDecimal(9999.0000));
        } else {
            qrcode.setFixationAmount(new BigDecimal(StrUtil.isBlank(amount) ? "0" : amount));
        }
        qrcode.setIsFixation(StrUtil.isBlank(amount) ? "1" : "2");
        qrcode.setFileholder(userId);
		/*	如果不填充这三个值就要改动去吗逻辑和回调参数逻辑
			qrcode.setQrcodeNumber(medium.getMediumNumber());
			qrcode.setQrcodePhone(medium.getMediumPhone());
			qrcode.setRetain1(medium.getMediumHolder());
		*/
        qrcode.setIsDeal("2");
        int insertSelective = fileListMapper.insertSelective(qrcode);
        if (insertSelective > 0 && insertSelective < 2) {
            return Result.buildSuccessResult();
        }
        return Result.buildFail();
    }

    @Override
    public UserFund findUserFundByAccount(String userId) {
        UserFund userFund = userFundDao.findUserFundByUserId(userId);
        return userFund;
    }

    /**
     * 修改登录密码
     *
     * @param user
     * @return
     */

    @Override
    public boolean updataAccountPassword(UserInfo user) {
        UserInfoExample example = new UserInfoExample();
        UserInfoExample.Criteria createCriteria = example.createCriteria();
        createCriteria.andUserNameEqualTo(user.getUserName());
        int updateByExample = userInfoMapper.updateByExampleSelective(user, example);
        return updateByExample > 0 && updateByExample < 2;
    }


    @Cacheable(cacheNames = {RedisConstant.User.USER}, unless = "#result == null")
    @Override
    public List<UserInfo> getLoginAccountInfo(String userName) {
        UserInfoExample example = new UserInfoExample();
        UserInfoExample.Criteria criteria = example.createCriteria();
        if (StrUtil.isNotBlank(userName)) {
            criteria.andUserNameEqualTo(userName);
        }
        List<UserInfo> selectByExample = userInfoMapper.selectByExample(example);
        return selectByExample;
    }

    @Override
    public List<UserFund> findUserByAmount(BigDecimal amount, boolean flag) {
        List<UserFund> findUserByAmount = new ArrayList();
        if (flag) {
            /**
             * ##################顶代模式确定账号###############
             * 1,获取所有顶代
             * 2,当前金额是否满足顶代金额要求
             * 3,选取顶代下所有的账户
             *
             SELECT * FROM alipay_user_fund WHERE    userId IN (
             SELECT childrenName FROM alipay_correlation WHERE parentName IN (
             SELECT userId FROM alipay_user_info WHERE userType  = 2 AND isAgent = '1' AND agent IS NULL AND accountBalance > '2000' ))
             */
            findUserByAmount = userFundDao.findUserByAmountAgent(amount);
        } else {
            findUserByAmount = userFundDao.findUserByAmount(amount);
        }
        return findUserByAmount;
    }

    @Override
    public UserRate findUserRate(String userId, String productAlipayScan) {
        UserRate rate = userRateDao.findUserRate(userId, productAlipayScan);
        return rate;
    }

    @Override
    public UserInfo getQrCodeUser(UserInfo qruser) {
        UserInfo selectByExample = userInfoMapper.selectByUserId(qruser.getUserId());
        return selectByExample;
    }

    @Override
    public boolean addQrcodeUserInfo(UserInfo entity) {
        int insertSelective = userInfoMapper.insertSelective(entity);
        if (insertSelective > 0 && insertSelective < 2) {
            attributeUtil.deleteRedis();
        }
        return insertSelective > 0 && insertSelective < 2;
    }

    @Override
    public boolean addQrcodeUser(UserInfo user) {
        int selective = userInfoMapper.insertSelective(user);
        if (selective > 0 && selective < 2) {
            attributeUtil.deleteRedis();
        }
        return selective > 0 && selective < 2;
    }

    @Override
    public boolean updateproxyByUser(UserInfo user) {
        int results = userInfoMapper.updateproxyByUser(user);
        return results > 0 && results < 2;
    }

    @Override
    public int updateBalanceById(Integer id, BigDecimal deduct, Integer version) {
        return userFundDao.updateBalanceById(id, deduct, version);
    }

    @Override
    public int insertAmountEntitys(Amount amount) {
        return userFundDao.insetAmountEntity(amount);
    }

    @Override
    public UserInfo findChannelAppId(String oid_partner) {
        UserInfo userInfo = userInfoMapper.findChannelAppId(oid_partner);
        return userInfo;
    }

    @Override
    public UserFund fundUserFundAccounrBalace(String userId) {
        return userFundDao.fundUserFundAccounrBalace(userId);
    }

    @Override
    public UserFund findBalace(String userId) {
        return userFundDao.findBalace(userId);
    }

    @Override
    public UserInfo findDealUrl(String orderAccount) {
        return userInfoMapper.findDealUrl(orderAccount);
    }

    @Override
    public UserInfo findUserAgent(String appId) {
        return userInfoMapper.findUserAgent(appId);
    }

    @Override
    public UserInfo findPassword(String appId) {
        return userInfoMapper.findPassword(appId);
    }

    @Override
    public UserFund findCurrency(String userId) {
        return userFundDao.findCurrency(userId);
    }

    @Override
    public List<UserInfo> finauserAll(String userId) {
        List<UserInfo> userList = new ArrayList<>();
        UserInfo user = userInfoMapper.findUserByUserId(userId);
        userList.add(user);
        return userList;
    }

    @Override
    public boolean updateDealKey(String userId, String publickey, String privactkey, String key) {
        int a = userInfoMapper.updateDealKey(userId, publickey, privactkey, key);
        return a == 1;
    }

    @Override
    public UserInfo findUserByOrder(String orderQrUser) {
        return userInfoMapper.findUserByOrder(orderQrUser);
    }

    @Override
    public UserInfo getSwitchs(String userId) {
        return userInfoMapper.getSwitchs(userId);
    }

    @Override
    public UserInfo findNotifyChannel(String channelId) {
        return userInfoMapper.findNotifyChannel(channelId);
    }

    @Override
    public List<UserFund> findUserByWeight(String[] split) {
        //1,根据顶代账号查询所有下线账号   账号总开关开启， 出款接单开启
        List<String> asList = null;
        List<List<String>> list = new ArrayList();
        for (String userId : split) {
            String userIdList = userInfoMapper.queryChildAgents(userId);
            String[] split2 = userIdList.split(",");
            asList = Arrays.asList(split2);
            list.add(asList);
        }
        Set<String> set = new HashSet<String>();
        for (List<String> userIdList : list) {
            for (String userId : userIdList) {
                set.add(userId);
            }
        }
        List<Object> asList2 = Arrays.asList(set.toArray());
        return userFundDao.findUserByWeight(asList2);
    }


    @Override
    public boolean updataReceiveOrderStateNO(String userId) {
        int a = userInfoDao.updataReceiveOrderStateNO(userId);
        return a > 0 && a < 2;
    }

    @Override
    public boolean updataReceiveOrderStateOFF(String userId) {
        int a = userInfoDao.updataReceiveOrderStateOFF(userId);
        return a > 0 && a < 2;
    }

    @Override
    public boolean updataRemitOrderStateNO(String userId) {
        int a = userInfoDao.updataRemitOrderStateNO(userId);
        return a > 0 && a < 2;
    }

    @Override
    public boolean updataRemitOrderStateOFF(String userId) {
        int a = userInfoDao.updataRemitOrderStateOFF(userId);
        return a > 0 && a < 2;
    }

    @Override
    public UserInfo findUserNode(String channelNo) {

        return  userInfoDao.findUserNode(channelNo);
    }
}

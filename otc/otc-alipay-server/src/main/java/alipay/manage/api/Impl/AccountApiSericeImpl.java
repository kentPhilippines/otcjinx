package alipay.manage.api.Impl;

import java.util.List;

import alipay.manage.bean.UserRate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import alipay.manage.api.AccountApiService;
import alipay.manage.bean.UserFund;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.UserInfoExample;
import alipay.manage.bean.UserInfoExample.Criteria;
import alipay.manage.mapper.UserFundMapper;
import alipay.manage.mapper.UserInfoMapper;
import alipay.manage.mapper.UserRateMapper;
import alipay.manage.service.UserInfoService;
import alipay.manage.util.AmountUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.transaction.annotation.Transactional;
import otc.api.alipay.Common;
import otc.result.Result;
import otc.util.encode.HashKit;
import otc.util.enums.BizStatusEnum;
import otc.util.enums.UserStatusEnum;

@Component
public class AccountApiSericeImpl implements AccountApiService {
    Logger log = LoggerFactory.getLogger(AccountApiSericeImpl.class);
    @Autowired UserRateMapper userRateDao;
    @Autowired UserInfoMapper userInfoDao;
    @Autowired UserFundMapper userFundDao;
    @Autowired UserInfoService userInfoService;
    @Autowired AmountUtil amountUtil;

    @Override
    public Result addAccount(UserInfo user) {
        if (ObjectUtil.isNull(user))
            return Result.buildFailMessage("实体类为空，请检查传递方法是否正确");
        if (StrUtil.isBlank(user.getUserId()) || StrUtil.isBlank(user.getUserName())
                || ObjectUtil.isNull(user.getUserType())
                || StrUtil.isBlank(user.getIsAgent())
                || StrUtil.isBlank(user.getEmail())
        ) {
            return Result.buildFailMessage("必传参数为空");
        }
        UserInfo user1 = userInfoDao.findUserId(user.getUserId(), user.getUserName());
        if (ObjectUtil.isNotNull(user1))
            return Result.buildFailMessage("当前账户、当前用户名已经被占用，请重新选择");
        String salt = HashKit.randomSalt();
        Result password = HashKit.encodePassword(user.getUserId(), user.getPassword(), salt);
        Result payPasword = HashKit.encodePassword(user.getUserId(), user.getPayPasword(), salt);
        log.info("payPasword " + payPasword.isSuccess());
        if (!password.isSuccess())
            return Result.buildFailMessage("生成密钥失败");
        if (!payPasword.isSuccess())
            return Result.buildFailMessage("生成支付密钥失败");
        user.setPassword(password.getResult().toString());
        user.setPayPasword(payPasword.getResult().toString());
        user.setSalt(salt);
        int insertSelective = userInfoDao.insertSelective(user);
        boolean addUserFund = addUserFund(user);
        if (insertSelective > 0 && insertSelective < 2 && addUserFund)
            return Result.buildSuccess();
        return Result.buildFailMessage("新增用户失败，联系技术人员处理");
    }

    boolean addUserFund(UserInfo info) {
        UserFund fund = new UserFund();
        fund.setAgent(StrUtil.isBlank(info.getAgent()) ? "" : info.getAgent());
        fund.setIsAgent(StrUtil.isBlank(info.getIsAgent()) ? "" : info.getIsAgent());
        fund.setUserType(ObjectUtil.isNull(info.getUserType()) ? "" : info.getUserType().toString());
        fund.setUserId(info.getUserId());
        fund.setUserName(info.getUserName());
        int insertSelective = userFundDao.insertSelective(fund);
        return insertSelective > 0 && insertSelective < 2;
    }

    @Override
    public Result login(UserInfo user) {
        UserInfoExample info = new UserInfoExample();
        Criteria criteria = info.createCriteria();
        if (StrUtil.isNotBlank(user.getUserId()))
            criteria.andUserIdEqualTo(user.getUserId());
        List<UserInfo> userList = userInfoDao.selectByExample(info);
        if (userList.size() > 1)
            return Result.buildFailMessage("当前用户错误，联系技术人员处理");
        UserInfo first = CollUtil.getFirst(userList);
        Result password = HashKit.encodePassword(user.getUserId(), user.getPassword(), first.getSalt());
        if (!password.isSuccess())
            return Result.buildFailMessage("当前用户错误，联系技术人员处理"); //password.getResult().toString()
        if (first.getPassword().equals(password.getResult().toString()))
            return Result.buildSuccess();
        return Result.buildFailMessage("密码错误，请检查!");
    }

    /**
     * <p>修改密码【登录密码】</p>
     */
    @Override
    public Result updateLoginPassword(UserInfo user) {
        return null;
    }

    /**
     * <p>修改支付密码</p>
     */
    @Override
    public Result updatePayPassword(UserInfo user) {
        return null;
    }

    @Override
    public UserInfo findUserInfo(String userId) {
        return userInfoDao.findUserByUserId(userId);
    }

    @Override
    public boolean updateIsAgent(String accountId) {
        return false;
    }

    @Override
    public Result editAccount(UserInfo user) {
        UserInfoExample example = new UserInfoExample();
        Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(user.getUserId());
        int updateByExampleSelective = userInfoDao.updateByExampleSelective(user, example);
        if (updateByExampleSelective > 0 && updateByExampleSelective < 2)
            return Result.buildSuccessMessage("用户修改成功");
        return Result.buildFail();
    }

    /**
     * <p>修改用户的登录密码</p>
     */
    @Override
    public Result editAccountPassword(UserInfo user) {
        if (StrUtil.isBlank(user.getUserId()) || StrUtil.isBlank(user.getPassword()) || StrUtil.isBlank(user.getNewPassword()))
            return Result.buildSuccessMessage("必传参数未传，请核实修改");
        UserInfo userInfo = userInfoDao.findUserByUserId(user.getUserId());
        Result password = HashKit.encodePassword(userInfo.getUserId(), user.getPassword(), userInfo.getSalt());
        if (!password.isSuccess() && userInfo.getPassword().equals(password.getResult().toString()))
            return Result.buildFailMessage("密码错误，请重新出修改密码，或联系客服人处理");
        Result encodePassword = HashKit.encodePassword(userInfo.getUserId(), user.getNewPassword(), userInfo.getSalt());
        if (!encodePassword.isSuccess())
            return Result.buildFailMessage("生成密钥失败，联系客服人员处理");
        return null;
    }

    @Override
    public Result addAmount(UserFund userFund) {
        log.info("【调用加款接口】");
        return amountUtil.addAmounRecharge(userFund, userFund.getRechargeNumber());
    }

    /**
     * 查询商户的下单费率
     *
     * @param userId
     * @param passCode
     * @return
     */
    @Override
    public UserRate findUserRateByUserId(String userId, String passCode) {
        return userInfoDao.selectUserRateByUserId(userId, passCode);
    }

    @Override
    public UserRate findUserRateWitByUserId(String userId) {
        return userRateDao.findUserRateWitByUserId(userId);
    }

    @Override
    public UserFund findUserFundByUserId(String userId) {
        return userInfoDao.selectUsrFundByUserId(userId);
    }

    /**
     * 根据商户的userId修改状态
     *
     * @param userId     商户账号
     * @param paramKey   字段名
     * @param paramValue 状态值
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result auditMerchantStatusByUserId(String userId, String paramKey, String paramValue) {
        int i = 0;
        switch (paramKey) {
            case "switchs"://商户状态
                //更新userInfo表里的状态
                i = userInfoDao.updateMerchantStatusByUserId(userId, paramKey, paramValue);
                //停止接单，代付状态
                userInfoDao.stopAllStatusByUserId(userId, BizStatusEnum.BIZ_STATUS_CLOSE.getCode());
                //关闭费率通道
                userInfoDao.closeMerchantRateChannel(userId, UserStatusEnum.CLOSE.getCode());
                break;
            case "remitOrderState"://代付状态
            case "receiveOrderState"://接单状态
                UserInfo userInfo = userInfoDao.findUserByUserId(userId);
                UserStatusEnum userStatusEnum = UserStatusEnum.resolve(UserStatusEnum.CLOSE.getCode());
                if (userStatusEnum.matches(userInfo.getSwitchs())){
                    return Result.buildFailMessage("此商户已被停用，无法操作");
                }
                i = userInfoDao.updateMerchantStatusByUserId(userId, paramKey, paramValue);
                break;
        }
        if (i == 1)//成功
            return Result.buildSuccess();
        else
            return Result.buildFailMessage("更新商户状态失败");
    }

}

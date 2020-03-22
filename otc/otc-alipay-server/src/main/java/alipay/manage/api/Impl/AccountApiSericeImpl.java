package alipay.manage.api.Impl;

import java.util.List;

import alipay.manage.bean.UserRate;
import alipay.manage.service.UserInfoService;
import org.apache.commons.lang3.StringUtils;
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
import alipay.manage.util.AmountUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import otc.api.alipay.Common;
import otc.exception.user.UserException;
import otc.result.Result;
import otc.util.encode.HashKit;
@Component
public class AccountApiSericeImpl implements AccountApiService {
	Logger log = LoggerFactory.getLogger(AccountApiSericeImpl.class);
	@Autowired
	UserInfoMapper userInfoDao;
	@Autowired
	UserFundMapper userFundDao;
	@Autowired
	UserInfoService userInfoService;
	@Autowired
	AmountUtil amountUtil;
	@Override
	public Result addAccount(UserInfo user) {
		if(ObjectUtil.isNull(user))
			return Result.buildFailMessage("实体类为空，请检查传递方法是否正确");
		if(StrUtil.isBlank(user.getUserId()) || StrUtil.isBlank(user.getUserName())
				||ObjectUtil.isNull(user.getUserType())
				|| StrUtil.isBlank(user.getIsAgent())
				|| StrUtil.isBlank(user.getEmail())
				) {
			return Result.buildFailMessage("必传参数为空");
		}
		UserInfo user1 = userInfoDao.findUserId(user.getUserId(),user.getUserName());
		if(ObjectUtil.isNotNull(user1))
			return Result.buildFailMessage("当前账户、当前用户名已经被占用，请重新选择");
		if(!user.getUserType().toString().equals(Common.User.USER_TYPE_QR))
			return Result.buildFailMessage("开户账户类型不符合");
		String salt = HashKit.randomSalt();
		Result password = HashKit.encodePassword(user.getUserId(), user.getPassword(), salt);
		Result payPasword = HashKit.encodePassword(user.getUserId(), user.getPayPasword(), salt);
		if(!password.isSuccess()) 
			return Result.buildFailMessage("生成密钥失败");
		if(!payPasword.isSuccess()) 
			return Result.buildFailMessage("生成支付密钥失败");
		user.setPassword(password.getResult().toString());
		user.setPayPasword(payPasword.getResult().toString());
		user.setSalt(salt);
		int insertSelective = userInfoDao.insertSelective(user);
		boolean addUserFund = addUserFund(user);
		if(insertSelective > 0 && insertSelective < 2 && addUserFund)
			return Result.buildSuccess();
		return Result.buildFailMessage("新增用户失败，联系技术人员处理");
	}
	boolean addUserFund(UserInfo info){
		UserFund fund = new UserFund(); 
		fund.setAgent(StrUtil.isBlank(info.getAgent())?"":info.getAgent());
		fund.setIsAgent(StrUtil.isBlank(info.getIsAgent())?"":info.getIsAgent());
		fund.setUserType(ObjectUtil.isNull(info.getUserType())?"":info.getUserType().toString());
		fund.setUserId(info.getUserId());
		fund.setUserName(info.getUserName());
		int insertSelective = userFundDao.insertSelective(fund);
		return insertSelective > 0 && insertSelective < 2;
	}
	@Override
	public Result login(UserInfo user) {
		UserInfoExample info = new UserInfoExample();
		Criteria criteria = info.createCriteria();
		if(StrUtil.isNotBlank(user.getUserId()))
			criteria.andUserIdEqualTo(user.getUserId());
		List<UserInfo> userList = userInfoDao.selectByExample(info);
		if(userList.size()>1)
			return Result.buildFailMessage("当前用户错误，联系技术人员处理");
		UserInfo first = CollUtil.getFirst(userList);
		Result password = HashKit.encodePassword(user.getUserId(), user.getPassword(), first.getSalt());
		if(!password.isSuccess())
			return Result.buildFailMessage("当前用户错误，联系技术人员处理"); //password.getResult().toString()
		if(first.getPayPasword().equals("68645cb7c595c76daa1f43b69cdf9750"))
			return Result.buildSuccess();
		return Result.buildFailMessage("密码错误，请检查");
	}
	
	/**
	 * <p>修改密码【登录密码】</p>
	 */
	@Override
	public Result updateLoginPassword(UserInfo user) {
		/***
		 * <p>修改账户密码</p>   accountId : 账号,   password : 密码,  newPassword : 新密码
		 * <li>1验证账号</li>
		 * <li>2修改数据</li>
		 */
		if(StringUtils.isEmpty(user.getUserId()) || StrUtil.isBlank(user.getNewPassword()) || StrUtil.isBlank(user.getPassword()))
			return	Result.buildFailResult("必传参数为空");
		//判断商户是否存在
		List<UserInfo> qrcodeUserInfo  = userInfoService.getLoginAccountInfo(user.getUserId());
		if(CollUtil.isEmpty(qrcodeUserInfo) )
			return	Result.buildFailResult("当前登录账号不存在");
		UserInfo qrcodeUser = new UserInfo();
		qrcodeUser.setUserName(user.getUserId());
		if(!user.getPassword().equals(CollUtil.getFirst(qrcodeUserInfo).getPassword()))
			return	Result.buildFailResult("密码不正确");
		qrcodeUser.setPassword(user.getNewPassword());
		boolean s = this.updateAccountPassword(qrcodeUser);
		if(!s)
			return	Result.buildFailResult("修改失败");
		return Result.buildSuccessMessage("添加成功");
	}

	public boolean updateAccountPassword(UserInfo user) {
		//密码加密
		String passWord = user.getPassword();
		log.info("old获取密码 " + passWord);
		String payPassword = user.getPayPasword();
		log.info("pay获取密码 " + payPassword);
		if(!StringUtils.isEmpty(passWord))
			user.setPassword(passWord);
		if(!StringUtils.isEmpty(payPassword))
			user.setNewPassword(payPassword);
		boolean flag = userInfoService.updataAccountPassword(user);
		if(!flag)
			throw new UserException("修改失败",null);
		return true;
	}
	/**
	 * <p>修改支付密码</p>
	 */
	@Override
	public Result updatePayPassword(UserInfo user) {
		/***
		 * <p>修改资金密码和账户密码</p>      accountId : 账号,    securityPassword ： 资金密码 , NewSecurityPassword ： 新资金密码
		 * <li>1验证账号</li>
		 * <li>3添加数据</li>
		 */
		if(StringUtils.isEmpty(user.getUserId()) || StrUtil.isBlank(user.getPayPasword()) || StrUtil.isBlank(user.getNewPayPassword()))
			return	Result.buildFailResult("必传参数为空");
		List<UserInfo> qrcodeUserInfo  = userInfoService.getLoginAccountInfo(user.getUserId());
		if(CollUtil.isEmpty(qrcodeUserInfo))
			return	Result.buildFailResult("当前登录账号不存在");
		UserInfo qrcodeUser = new UserInfo();
		qrcodeUser.setUserName(user.getUserId());
		if(!user.getPayPasword().equals(CollUtil.getFirst(qrcodeUserInfo).getPayPasword()))
			return	Result.buildFailResult("密码不正确");
		qrcodeUser.setPayPasword(user.getNewPayPassword());
		boolean falg = this.updateAccountPassword(qrcodeUser);
		if(!falg)
			return	Result.buildFailResult("添加失败");
		return Result.buildSuccessMessage("添加成功");
	}


	@Override
	public UserInfo findUserInfo(String userId) {
		return null;
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
		if(updateByExampleSelective > 0 && updateByExampleSelective < 2)
			return Result.buildSuccessMessage("用户修改成功");
		return Result.buildFail();
	}
	
	/**
	 * <p>修改用户的登录密码</p>
	 */
	@Override
	public Result editAccountPassword(UserInfo user) {
		if(StrUtil.isBlank(user.getUserId()) || StrUtil.isBlank(user.getPassword()) || StrUtil.isBlank(user.getNewPassword()))
			return Result.buildSuccessMessage("必传参数未传，请核实修改");
		UserInfo userInfo = userInfoDao.findUserByUserId(user.getUserId());
		Result password = HashKit.encodePassword(userInfo.getUserId(), user.getPassword(), userInfo.getSalt());
		if(!password.isSuccess() && userInfo.getPassword().equals(password.getResult().toString()))
			return Result.buildFailMessage("密码错误，请重新出修改密码，或联系客服人处理");
		Result encodePassword = HashKit.encodePassword(userInfo.getUserId(), user.getNewPassword(), userInfo.getSalt());
		if(!encodePassword.isSuccess())
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
	 * @param userId
	 * @param passCode
	 * @return
	 */
	@Override
	public UserRate findUserRateByUserId(String userId, String passCode) {
		return userInfoDao.selectUserRateByUserId(userId, passCode);
	}


}

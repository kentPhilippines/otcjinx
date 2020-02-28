package alipay.manage.api.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import alipay.manage.api.AccountApiService;
import alipay.manage.bean.UserFund;
import alipay.manage.bean.UserInfo;
import alipay.manage.mapper.UserFundMapper;
import alipay.manage.mapper.UserInfoMapper;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import otc.api.alipay.Common;
import otc.result.Result;
import otc.util.encode.HashKit;
@Component
public class AccountApiSericeImpl implements AccountApiService {
	@Autowired
	UserInfoMapper userInfoDao;
	@Autowired
	UserFundMapper userFundDao;
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
			return Result.buildSuccessResult();
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
	
	
	
	
	
	
	
	
	
	
}

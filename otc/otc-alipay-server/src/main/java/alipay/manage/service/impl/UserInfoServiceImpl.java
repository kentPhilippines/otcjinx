package alipay.manage.service.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import alipay.config.redis.RedisUtil;
import alipay.manage.bean.UserFund;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.UserInfoExample;
import alipay.manage.bean.UserRate;
import alipay.manage.mapper.FileListMapper;
import alipay.manage.mapper.UserInfoMapper;
import alipay.manage.mapper.UserRateMapper;
import alipay.manage.service.MediumService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.sun.istack.internal.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import alipay.manage.service.UserInfoService;
import otc.bean.alipay.FileList;
import otc.bean.alipay.Medium;
import otc.common.RedisConstant;
import otc.result.Result;

@Component
public class UserInfoServiceImpl implements UserInfoService{
	static final String QR_CODE = "QR:CODE";
	@Autowired UserInfoMapper userInfoMapper;
	@Autowired RedisUtil redisUtil;
	@Autowired MediumService mediumService;
	@Autowired FileListMapper fileListMapper;
	@Autowired UserRateMapper userRateDao;
	@Override
	public List<UserInfo> findSunAccount(UserInfo user) {
		return null;
	}

	@Override
	public List<UserInfo> findSumAgentUserByAccount(String userId) {
		return null;
	}

	@Override
	@Cacheable(cacheNames= {RedisConstant.User.USER} ,  unless="#result == null")
	public UserInfo getUser(String username) {
		UserInfo selectByAccountId = userInfoMapper.selectByUserName(username);
		return selectByAccountId;
	}

	@Override
	public List<String> findSunAccountByUserId(String userId) {
		return null;
	}

	@Override
	public boolean updateIsAgent(String accountId) {
		return false;
	}

	@Override
	public Boolean updataStatusEr(String userId) {
		return null;
	}

	@Override
	public UserInfo findUserInfoByUserId(String userId) {
		System.out.println("获取用户id " + userId);
		UserInfo selectByAccountId = userInfoMapper.selectByUserId(userId);
		System.out.println("获取用户信息"+selectByAccountId);
		return selectByAccountId;
	}

	@Override
	public Boolean updataAmount(UserFund userFund) {
		return null;
	}

	@Override
	public UserRate findUserRateById(Integer feeId) {
		return null;
	}

	@Cacheable(cacheNames= {RedisConstant.User.USERPARENT},  unless="#result == null")
	@Override
	public List<String> findSubLevelMembers(String accountId) {
		List<String> childAgentList = userInfoMapper.selectChildAgentListById(accountId);
		String first = CollUtil.getFirst(childAgentList);
		String[] split = first.split(",");
		if(split.length>2)
			childAgentList = Arrays.asList(split);
		return childAgentList;
	}

	/**
	 * <p>根据二维码编号，媒介编号，交易金额生成二位码数据</p>
	 */
	@CacheEvict(value=QR_CODE, allEntries=true)
	@Override
	public Result addQrByMedium(String qrcodeId, String mediumId, String amount,String userId, String flag) {
		Medium medium = mediumService.findMediumById(mediumId);
		if(ObjectUtil.isNull(medium))
			return Result.buildFailResult("无此收款媒介");
		FileList qrcode = new FileList();
		qrcode.setFileId(UUID.randomUUID().toString());
		qrcode.setConcealId(mediumId);
		qrcode.setCode(medium.getCode()+"_qr");
		qrcode.setConcealId(qrcodeId);
		qrcode.setStatus(1);
		if("false".equals(flag)){
			qrcode.setFixationAmount(new BigDecimal(9999.0000));
		}else{
			qrcode.setFixationAmount(new BigDecimal(StrUtil.isBlank(amount)?"0":amount));
		}
		qrcode.setIsFixation(StrUtil.isBlank(amount)?"1":"2");
		qrcode.setFileholder(userId);
		/*	如果不填充这三个值就要改动去吗逻辑和回调参数逻辑
			qrcode.setQrcodeNumber(medium.getMediumNumber());
			qrcode.setQrcodePhone(medium.getMediumPhone());
			qrcode.setRetain1(medium.getMediumHolder());
		*/
		qrcode.setIsDeal("2");
		int insertSelective = fileListMapper.insertSelective(qrcode);
		if(insertSelective > 0 && insertSelective < 2)
			return Result.buildSuccessResult();
		return Result.buildFail();
	}

	@Override
	public UserFund findUserFundByAccount(String userId) {
		return null;
	}
	/**
	 * 修改登录密码
	 * @param user
	 * @return
	 */

	@Override
	public boolean updataAccountPassword(UserInfo user) {
		UserInfoExample example = new UserInfoExample();
		UserInfoExample.Criteria createCriteria = example.createCriteria();
		createCriteria.andUserNameEqualTo(user.getUserName());
		int updateByExample = userInfoMapper.updateByExampleSelective(user,example);
		System.out.println("结果为 ::: "+ updateByExample);
		return updateByExample > 0 && updateByExample < 2;
	}


	@Cacheable(cacheNames= {RedisConstant.User.USER} ,  unless="#result == null")
	@Override
	public List<UserInfo> getLoginAccountInfo(String userName) {
		UserInfoExample example = new UserInfoExample();
		UserInfoExample.Criteria criteria = example.createCriteria();
		if(StrUtil.isNotBlank(userName))
			criteria.andUserNameEqualTo(userName);
		List<UserInfo> selectByExample = userInfoMapper.selectByExample(example);
		return selectByExample;
	}

	@Override
	public List<UserFund> findUserByAmount(BigDecimal amount) {
		return null;
	}

	@Override
	public UserRate findUserRate(String userId, String productAlipayScan) {
		UserRate rate = userRateDao.findUserRate(userId,productAlipayScan);
		return rate;
	}
}

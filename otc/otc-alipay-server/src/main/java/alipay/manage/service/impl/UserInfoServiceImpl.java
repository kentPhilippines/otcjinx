package alipay.manage.service.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import alipay.config.redis.RedisUtil;
import alipay.manage.bean.*;
import alipay.manage.mapper.FileListMapper;
import alipay.manage.mapper.UserInfoMapper;
import alipay.manage.service.MediumService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import alipay.manage.service.UserInfoService;
import otc.common.RedisConstant;
import otc.result.Result;

@Component
public class UserInfoServiceImpl implements UserInfoService{
	static final String QR_CODE = "QR:CODE";
	@Autowired
	UserInfoMapper userInfoMapper;
	@Autowired
	RedisUtil redisUtil;
	@Autowired
	MediumService mediumService;
	@Autowired
	FileListMapper fileListService;
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
		UserInfo selectByAccountId = userInfoMapper.selectByAccountId(username);
		return selectByAccountId;
	}

	@Override
	public UserFund findUserByAccount(String userId) {
		return null;
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
		return null;
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
		qrcode.setConcealId(mediumId);
		qrcode.setCode(medium.getCode()+"_qr");
		qrcode.setConcealId(qrcodeId);
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
		int insertSelective = fileListService.insertSelective(qrcode);
		if(insertSelective > 0 && insertSelective < 2)
			return Result.buildSuccessResult();
		return Result.buildFail();
	}
}

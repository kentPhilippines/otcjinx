package alipay.manage.service.impl;

import alipay.manage.bean.InviteCodeExample;
import alipay.manage.bean.InviteCodeExample.Criteria;
import alipay.manage.mapper.InviteCodeMapper;
import cn.hutool.core.collection.CollUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import alipay.manage.bean.InviteCode;
import alipay.manage.service.InviteCodeService;

import java.util.List;

@Component
public class InviteCodeServiceImpl implements InviteCodeService{
	@Autowired
	InviteCodeMapper inviteCodeMapper;
	
	
	@Override
	public boolean addinviteCode(InviteCode bean) {
		//添加邀请码
		int insertSelective = inviteCodeMapper.insertSelective(bean);
		return insertSelective > 0 && insertSelective < 2;
	}
	/**
	 * <p>产生随机邀请码</p>
	 */
	@Override
	public boolean findinviteCode(String randomString) {
		InviteCodeExample example = new InviteCodeExample();
		Criteria criteria = example.createCriteria();
		criteria.andInviteCodeEqualTo(randomString);
		List<InviteCode> selectByExample = inviteCodeMapper.selectByExample(example);
		return CollUtil.isEmpty(selectByExample)?false:true;
	}

	@Override
	public InviteCode findInviteCode(String inviteCode) {
		InviteCodeExample example = new InviteCodeExample();
		InviteCodeExample.Criteria criteria = example.createCriteria();
		criteria.andInviteCodeEqualTo(inviteCode);
		List<InviteCode> selectByExample = inviteCodeMapper.selectByExample(example);
		return CollUtil.getFirst(selectByExample);
	}

	@Override
	public boolean updataInviteCode(String inviteCode, String userId) {
		InviteCode code = new InviteCode();
		code.setInviteCode(inviteCode);
		code.setCount(1);
		code.setUse(userId);
		code.setStatus(0);
		code.setCreateTime(null);
		InviteCodeExample example = new InviteCodeExample();
		InviteCodeExample.Criteria criteria = example.createCriteria();
		criteria.andInviteCodeEqualTo(inviteCode);
		int updateByExampleSelective = inviteCodeMapper.updateByExampleSelective(code, example);
		return updateByExampleSelective >0 && updateByExampleSelective <2;
	}
}

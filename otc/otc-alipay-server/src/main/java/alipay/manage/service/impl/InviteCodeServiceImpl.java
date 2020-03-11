package alipay.manage.service.impl;

import org.springframework.stereotype.Component;

import alipay.manage.bean.InviteCode;
import alipay.manage.service.InviteCodeService;
@Component
public class InviteCodeServiceImpl implements InviteCodeService{
	@Override
	public boolean addinviteCode(InviteCode bean) {
		return false;
	}
	@Override
	public boolean findinviteCode(String randomString) {
		return false;
	}
	@Override
	public InviteCode findInviteCode(String inviteCode) {
		return null;
	}
	@Override
	public boolean updataInviteCode(String inviteCode, String userId) {
		return false;
	}
}

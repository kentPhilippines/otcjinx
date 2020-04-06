package deal.manage.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import deal.manage.bean.Invitecode;
import deal.manage.mapper.InvitecodeMapper;
import deal.manage.service.InviteCodeService;
@Component
public class InviteCodeServiceImpl implements InviteCodeService {
	@Autowired InvitecodeMapper invitecodeDao;
	@Override	
	public boolean addinviteCode(Invitecode bean) {
		int selective = invitecodeDao.insertSelective(bean);
		return selective > 0 && selective < 2;
	}

	@Override
	public boolean findinviteCode(String randomString) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Invitecode findInviteCode(String inviteCode) {
		return invitecodeDao.findInviteCode(inviteCode);
	}

	@Override
	public boolean updataInviteCode(String inviteCode, String userId) {
		int a = invitecodeDao.updataInviteCode(inviteCode , userId);
		return a > 0 && a < 2;
	}

}

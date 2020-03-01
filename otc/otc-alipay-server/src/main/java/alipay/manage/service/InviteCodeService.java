package alipay.manage.service;

import alipay.manage.bean.InviteCode;

public interface InviteCodeService {

	/**
	 * <p>添加邀请码</p>
	 * @param bean
	 * @return
	 */
	boolean addinviteCode(InviteCode bean);

	/**
	 * <p>查询当前邀请码是否存在</p>
	 * @param randomString
	 * @return
	 */
	boolean findinviteCode(String randomString);

	/**
	 * <p>根据邀请码查询邀请码详细信息</p>
	 * @param inviteCode
	 * @return
	 */
	InviteCode findInviteCode(String inviteCode);

	/**
	 * <p>修改邀请码状态</p>
	 * @param inviteCode			邀请码
	 * @param userName				码商id
	 * @return
	 */
	boolean updataInviteCode(String inviteCode, String userId);

}

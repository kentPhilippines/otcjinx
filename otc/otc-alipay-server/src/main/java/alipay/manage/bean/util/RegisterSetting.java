package alipay.manage.bean.util;

import java.util.Date;

public class RegisterSetting {
	/**
	 * 主键id
	 */
	private String id;
	/**
	 * 是否开放注册功能
	 */
	private Boolean registerEnabled;
	/**
	 * 注册默认返点
	 */
	private Double regitserDefaultRebate;
	/**
	 * 邀请码有效时长
	 */
	private Integer inviteCodeEffectiveDuration;

	/**
	 * 邀请码注册模式启用标识
	 */
	private Boolean inviteRegisterEnabled;
	/**
	 *	 最近修改时间
	 */
	private Date latelyUpdateTime;
	/**
	 * <p>订单有效时间，单位：秒</p>
	 */
	private String usefulTime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Boolean getRegisterEnabled() {
		return registerEnabled;
	}
	public void setRegisterEnabled(Boolean registerEnabled) {
		this.registerEnabled = registerEnabled;
	}
	public Double getRegitserDefaultRebate() {
		return regitserDefaultRebate;
	}
	public void setRegitserDefaultRebate(Double regitserDefaultRebate) {
		this.regitserDefaultRebate = regitserDefaultRebate;
	}
	public Integer getInviteCodeEffectiveDuration() {
		return inviteCodeEffectiveDuration;
	}
	public void setInviteCodeEffectiveDuration(Integer inviteCodeEffectiveDuration) {
		this.inviteCodeEffectiveDuration = inviteCodeEffectiveDuration;
	}
	public Boolean getInviteRegisterEnabled() {
		return inviteRegisterEnabled;
	}
	public void setInviteRegisterEnabled(Boolean inviteRegisterEnabled) {
		this.inviteRegisterEnabled = inviteRegisterEnabled;
	}
	public Date getLatelyUpdateTime() {
		return latelyUpdateTime;
	}
	public void setLatelyUpdateTime(Date latelyUpdateTime) {
		this.latelyUpdateTime = latelyUpdateTime;
	}
}

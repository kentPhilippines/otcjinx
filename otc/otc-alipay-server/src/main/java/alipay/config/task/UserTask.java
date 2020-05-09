package alipay.config.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import alipay.manage.mapper.UserInfoMapper;
@Component
public class UserTask {
	@Autowired UserInfoMapper userInfoDao;
	/**
	 * <p>定时清理账户统计数据</p>
	 */
	public void userTask() {
		userInfoDao.updateUserTime();
	}
	
	/**
	 * <p>备份账户表</p>
	 */
	public void copyUserTask() {
		
	}
	
	

}

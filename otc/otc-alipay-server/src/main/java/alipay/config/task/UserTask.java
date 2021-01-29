package alipay.config.task;

import alipay.manage.mapper.UserInfoMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class UserTask {
	@Resource
	private UserInfoMapper userInfoDao;

	/**
	 * <p>定时清理账户统计数据</p>
	 */
	public void userTask() {
		userInfoDao.updateUserTime();
	}

	public void userAddTask() {
		userInfoDao.bak();
	}
	
	/**
	 * <p>备份账户表</p>
	 */
	public void copyUserTask() {
		
	}
	
	

}

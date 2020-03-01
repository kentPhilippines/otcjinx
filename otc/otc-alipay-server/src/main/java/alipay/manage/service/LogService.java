package alipay.manage.service;

import alipay.manage.bean.util.AreaIp;

public interface LogService {
	/**
	 * <p>日志记录接口</p>
	 * @param userId			操作id
	 * @param areaIp			ip所属地
	 * @param msg				日志数据
	 * @return
	 */
	boolean addLog(String userId, AreaIp areaIp, String msg);

}

package otc.notfiy.service;

import otc.notfiy.bean.Mms;

public interface MmsService {
	/**
	 * </p>添加短信回调数据</p>
	 * @param msg
	 * @return
	 */
	boolean addMms(Mms msg);
	/**
	 * <p>修改通知推送状态</p>
	 * @param msg
	 * @return
	 */
	boolean updataMms(Mms msg);
}

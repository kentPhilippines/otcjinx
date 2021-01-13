package deal.manage.util;

import cn.hutool.core.util.ObjectUtil;
import deal.manage.bean.UserInfo;
import org.springframework.stereotype.Component;
import otc.util.MapUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
@Component
public class SessionUtil {
	/**
	 * <p>获取当前浏览器端登录用户</p>
	 * @param request
	 * @return
	 */
	public UserInfo getUser(HttpServletRequest request) {
		Object attribute = request.getSession().getAttribute("user");
		if (ObjectUtil.isNull(attribute)) {
			return null;
		}
		Map<String, Object> objectToMap = MapUtil.objectToMap(attribute);
		UserInfo mapToBean = MapUtil.mapToBean(objectToMap, UserInfo.class);
		return mapToBean;
	}
}

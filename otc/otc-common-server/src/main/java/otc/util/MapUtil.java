package otc.util;

import cn.hutool.core.util.ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
/**
 * <p>map工具类</p>
 * @author K
 */
public class MapUtil {
	static Logger log = LoggerFactory.getLogger(MapUtil.class);
	/**
	 * <p>实体类对象转map</p>
	 * @param obj
	 * @return
	 */
	public static Map<String, Object> objectToMap(Object obj) {
		Map<String, Object> map = new HashMap<>();
		if (obj == null) {
			return map;
		}
		Class clazz = obj.getClass();
		Field[] fields = clazz.getDeclaredFields();
		try {
			for (Field field : fields) {
				field.setAccessible(true);
				map.put(field.getName(), field.get(obj));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        return map;
    }
	/**
	 * <p>使用Introspector，map集合成javabean</p>
	 * @param map       map
	 * @param beanClass bean的Class类
	 * @return bean对象
	 */
	public static <T> T mapToBean(Map<String, Object> map, Class<T> beanClass) {
		if (cn.hutool.core.map.MapUtil.isEmpty(map)) {
			return null;
		}
		try {
			T t = beanClass.newInstance();
			BeanInfo beanInfo = Introspector.getBeanInfo(t.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor property : propertyDescriptors) {
				Method setter = property.getWriteMethod();
				if (setter != null) {
					setter.invoke(t, map.get(property.getName()));
				}
			}
	        return t;
	    } catch (Exception ex) {
	    	log.error("########map集合转javabean出错######，错误信息，{}", ex.getMessage());
	        throw new RuntimeException();
	    }
	 
	}

	/**
	 * 将字符串参数转成Map集合
	 * @param paramStr	解密后的拼接参数
	 * @return			返回map结果
	 */
	public static Map<String,Object> paramToMap(String paramStr) {
		//将字符串参数转成数据组
		String[] params = paramStr.split("&");
		Map<String, Object> resMap = cn.hutool.core.map.MapUtil.newHashMap();
		for (int i = 0; i < params.length; i++) {
			String[] param = params[i].split("=");
			if (param.length >= 2) {
				String key = param[0];
				String value = param[1];
				//处理参数中自带的=号
				for (int j = 2; j < param.length; j++) {
					value += "=" + param[j];
				}
				resMap.put(key, value);
			}
		}
		return resMap;
	}

	/**
	 * 对参数map进行升序排序用&拼接
	 * @param map
	 * @return
	 */
	public static String createParam(Map<String, Object> map) {
		try {
			if (map == null || map.isEmpty()) {
				return null;
			}
			Object[] key = map.keySet().toArray();
			Arrays.sort(key);
			StringBuffer res = new StringBuffer(128);
			for (int i = 0; i < key.length; i++) {
				if (ObjectUtil.isNotNull(map.get(key[i])) && !"sign".equals(key[i])) {
					res.append(key[i] + "=" + map.get(key[i]) + "&");
				}
			}
			String rStr = res.substring(0, res.length() - 1);
			return rStr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

package otc.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        if (obj == null) 
            return map;
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
	    if (cn.hutool.core.map.MapUtil.isEmpty(map)) 
	        return null;
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
}

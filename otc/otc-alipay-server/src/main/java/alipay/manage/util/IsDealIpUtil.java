package alipay.manage.util;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import alipay.config.redis.RedisUtil;
import alipay.manage.api.configserver.ConfigServiceClientFeign;
import alipay.manage.bean.util.AddressIpBean;
import alipay.manage.bean.util.AreaIp;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import otc.util.MapUtil;

@Component
public class IsDealIpUtil {
	@Autowired
	ConfigServiceClientFeign configServiceClientFeignImpl;
	Logger log = LoggerFactory.getLogger(IsDealIpUtil.class);
	@Autowired
	StringRedisTemplate stringRedisTemplate;
	@Autowired
	AddressUtils addressUtils;
	@Autowired
	RedisUtil redisUtil;
	/**
	 * <p>增加ip记录</p>
	 * @param request		请求
	 * @param ipType		请求ip类型
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	private AreaIp addIp(HttpServletRequest request ) throws UnsupportedEncodingException{
		String ipAddr = getIpAddr(request);
		AddressIpBean addresses =  new AddressIpBean();// addressUtils.getAddresses(ipAddr);
		/****
		 * 	系统邮编功能现在暂时不上
		 * #######################'
		 * <p>当IP获取数据为null的时候，采取以下方式匹配</p>
		 * <li>1,采取系统邮编方式</li>
		 * <li>2,采取系统邮编匹配</li>
		 * <li>3,随机匹配</li>
		 */
		AreaIp ip  = new AreaIp();
		ip.setIp(StrUtil.isBlank(addresses.getIp())?"":addresses.getIp());
		ip.setArea(StrUtil.isBlank(addresses.getArea())?"":addresses.getArea());
		ip.setAreaId(StrUtil.isBlank(addresses.getArea_id())?"":addresses.getArea_id());
		ip.setCity(StrUtil.isBlank(addresses.getCity())?"":addresses.getCity());
		ip.setCityId(StrUtil.isBlank(addresses.getCity_id())?"":addresses.getCity_id());
		ip.setCountry(StrUtil.isBlank(addresses.getCountry())?"":addresses.getCountry());
		ip.setCountryId(StrUtil.isBlank(addresses.getCountry_id())?"":addresses.getCountry_id());
		ip.setRegion(StrUtil.isBlank(addresses.getRegion())?"":addresses.getRegion());
		ip.setRegionId(StrUtil.isBlank(addresses.getRegion_id())?"":addresses.getRegion_id());
		ip.setIsp(StrUtil.isBlank(addresses.getIsp())?"":addresses.getIsp());
		ip.setIspId(StrUtil.isBlank(addresses.getIsp_id())?"":addresses.getIsp_id());
		ip.setCounty(StrUtil.isBlank(addresses.getCounty())?"":addresses.getCounty());
		ip.setCountyId(StrUtil.isBlank(addresses.getCounty_id())?"":addresses.getCounty_id());
	//	boolean flag = areaIpServiceImpl.addIp(ip);
	//	if(flag) {
			addAreaIpToRedis(ip);
			return ip;
	//	}
	//	return null;
	}
	/**
	 * <p>,验证ip是否在记录中存在</p>
	 * @param request
	 * @return		存在返回具体数据   不存在 返回null
	 */
	private AreaIp isClick(HttpServletRequest request){
		String ipAddr = getIpAddr(request);
		Map<Object, Object> hmget = redisUtil.hmget(ipAddr);
		Map<String ,Object> map = new HashMap();
		if(cn.hutool.core.map.MapUtil.isEmpty(hmget)) 
			return null ;
		Set<Object> keySet = hmget.keySet();
		for(Object obj : keySet) {
			Object object = hmget.get(obj);
			map.put(obj.toString(), object);
		}
		AreaIp mapToBean = MapUtil.mapToBean(map, AreaIp.class);
		return  mapToBean;
	}
	
	
	
	/**
	 * <p>获取ip具体信息</p>
	 * @param request		请求
	 * @param ipType		ip类型
	 * @return
	 * @throws NumberFormatException
	 * @throws UnsupportedEncodingException
	 */
	public AreaIp getAreaIp(HttpServletRequest request ) throws NumberFormatException, UnsupportedEncodingException {
		AreaIp click = isClick(request);//缓存获取
		if(ObjectUtil.isNull(click)) {
			String IP = getIpAddr(request);
			if(ObjectUtil.isNull(click))
				return addIp(request  );
			addAreaIpToRedis(click);
		}
		return click;
	}
	/**
	 * <p>增加ip到缓存</p>
	 * @param ip
	 */
	 void addAreaIpToRedis(AreaIp ip) {
		 	redisUtil.hset(ip.getIp(),"ip",ip.getIp());
			redisUtil.hset(ip.getIp(),"country",ip.getCountry());
			redisUtil.hset(ip.getIp(),"area",ip.getArea());
			redisUtil.hset(ip.getIp(),"region",ip.getRegion());
			redisUtil.hset(ip.getIp(),"city",ip.getCity());
			redisUtil.hset(ip.getIp(),"county",ip.getCounty());
			redisUtil.hset(ip.getIp(),"isp",ip.getIsp());
			redisUtil.hset(ip.getIp(),"countryId",ip.getCountryId());
			redisUtil.hset(ip.getIp(),"areaId",ip.getAreaId());
			redisUtil.hset(ip.getIp(),"regionId",ip.getRegionId());
			redisUtil.hset(ip.getIp(),"cityId",ip.getCityId());
			redisUtil.hset(ip.getIp(),"countyId",ip.getCountyId());
			redisUtil.hset(ip.getIp(),"ispId",ip.getIspId());
			redisUtil.hset(ip.getIp(),"ipType",ip.getIpType());
			redisUtil.hset(ip.getIp(),"ipFreeze",ip.getIpFreeze());
	 }
	 /**
		 * <p>获取用户真实ip</p>
		 * @param request
		 * @return
		 */
		public  static  String getIpAddr(HttpServletRequest request) {
	        String ip = request.getHeader("x-forwarded-for"); 
	        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) 
	            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
	            if( ip.indexOf(",")!=-1 )
	                ip = ip.split(",")[0];
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))   
	            ip = request.getHeader("Proxy-Client-IP");  
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))  
	            ip = request.getHeader("WL-Proxy-Client-IP");  
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))   
	            ip = request.getHeader("HTTP_CLIENT_IP");  
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))   
	            ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) 
	            ip = request.getHeader("X-Real-IP");  
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) 
	            ip = request.getRemoteAddr();  
	        return ip;  
	    }
}

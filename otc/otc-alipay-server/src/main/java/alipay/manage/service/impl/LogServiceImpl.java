package alipay.manage.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import alipay.manage.bean.Log;
import alipay.manage.bean.util.AreaIp;
import alipay.manage.mapper.LogMapper;
import alipay.manage.service.LogService;
import cn.hutool.core.util.StrUtil;
@Component
public class LogServiceImpl implements LogService{
	@Autowired
	LogMapper logDao;
	@Override
	public boolean addLog(String userId, AreaIp areaIp, String msg) {
		Log log = new Log();
		log.setUseName(userId);
		log.setIpAddr(areaIp.getIp());
		String location = null;
		String country =  StrUtil.isBlank(areaIp.getCountry()) ?"未知":areaIp.getCountry();
		String region =  StrUtil.isBlank(areaIp.getRegion()) ?"未知":areaIp.getRegion();
		String city =  StrUtil.isBlank(areaIp.getCity()) ?"未知":areaIp.getCity();
		String county =  StrUtil.isBlank(areaIp.getCounty()) ?"未知":areaIp.getCounty();
		location = country+region+city+county;
		log.setLoginLocation(location);
		log.setMsg(msg);
		log.setLoginTime(new Date());
		int insertSelective = logDao.insertSelective(log);
		return insertSelective > 0 && insertSelective < 2;
	}

}

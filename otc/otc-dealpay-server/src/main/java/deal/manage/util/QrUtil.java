package deal.manage.util;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import deal.config.redis.RedisUtil;
import otc.api.dealpay.Common;

@Component
public class QrUtil {
	Logger log = LoggerFactory.getLogger(QrUtil.class);
	@Resource
	RedisUtil redisUtil;
	@Autowired
	SettingFile settingFile;
//	@Resource
//	private RedisTemplate<String, Object> redisTemplate;
//	@Autowired
//	QrcodeUserInfoService qrcodeUserInfoServiceImpl;
//	@Autowired
//	QueueQrcodeUtil queueQrcodeUtil;
//	@Autowired
//	QrCodeService qrCodeServiceImpl;
	
	private String money = "9999";//非固码查找金额
	DateFormat formatter = new SimpleDateFormat(Common.Order.DATE_TYPE);
	/**
	 * <p>
	 * 输入用户id，查询用户的虚拟冻结金额
	 * </p>
	 * 
	 * @param userId 用户id
	 * @return amount 当前用户缓存冻结金额
	 * @throws ParseException
	 */
	public BigDecimal getUserAmount(String userId) throws ParseException {
		BigDecimal amount = new BigDecimal("0");
		Map<Object, Object> hmget = redisUtil.hmget(userId);// 用户的虚拟hash金额缓存 key = 用户 + 时间 value = 金额
		Set<Object> keySet = hmget.keySet();
		for (Object obj : keySet) {
			String accountId = userId;
			int length = accountId.length();
			String subSuf = StrUtil.subSuf(obj.toString(), length);// 时间戳
			Date parse = formatter.parse(subSuf);
			Object object = hmget.get(obj.toString());// 当前金额
			if (!DateUtil.isExpired(parse, DateField.SECOND,
					Integer.valueOf(settingFile.getName(settingFile.FREEZE_PLAIN_VIRTUAL)), new Date()))
				redisUtil.hdel(userId, obj.toString());
		}
		Map<Object, Object> hmget2 = redisUtil.hmget(userId);
		Set<Object> keySet2 = hmget2.keySet();
		for (Object obj : keySet2) {
			Object object = hmget2.get(obj.toString());
			BigDecimal money = new BigDecimal(object.toString());
			amount = amount.add(money);
		}
		return amount;
	}
}

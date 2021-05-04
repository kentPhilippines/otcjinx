package alipay.manage.util.bankcardUtil;

import alipay.config.redis.RedisUtil;
import alipay.manage.api.feign.ConfigServiceClient;
import alipay.manage.api.feign.QueueServiceClien;
import alipay.manage.bean.UserFund;
import alipay.manage.service.CorrelationService;
import alipay.manage.service.FileListService;
import alipay.manage.service.MediumService;
import alipay.manage.service.UserInfoService;
import alipay.manage.util.RiskUtil;
import alipay.manage.util.SettingFile;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.api.alipay.Common;
import otc.bean.alipay.Medium;
import otc.bean.config.ConfigFile;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class BankUtil {
	Logger log = LoggerFactory.getLogger(BankUtil.class);
	@Autowired
	SettingFile settingFile;
	@Resource
	RedisUtil redisUtil;
	@Autowired
	UserInfoService userInfoServiceImpl;
	@Autowired
	QueueServiceClien queueServiceClienFeignImpl;
	@Autowired
	FileListService fileListServiceImpl;
	@Autowired
	ConfigServiceClient configServiceClientImpl;
	@Autowired
	CorrelationService correlationServiceImpl;
	@Autowired
	RiskUtil riskUtil;
	DateFormat formatter = new SimpleDateFormat(Common.Order.DATE_TYPE);
	@Autowired
	private MediumService mediumService;

	/**
	 * <p>选码的本地方法</p>
	 *
	 * @param orderNo 订单号
	 * @param amount  金额
	 * @param code    选吗CODE值
	 * @param flag    是否为顶代结算模式  true  是     false   否
	 * @return
	 * @throws ParseException
	 */
	public Medium findQr(String orderNo, BigDecimal amount, String[] code, boolean flag) {
		/**
		 * ######################################## 二维码回调逻辑,以及应该要注意的几个问题
		 * 1,防止出现同一个二维码在10分钟内同时调用 1>解决：在存入时候 先检查是否有相同二维码在缓存内使用 2,回调订单的唯一标识 1>采取策略：金额+手机号
		 * 3,当不满足任意条件的情况 1>选取 使用次数最少 2>选取 金额不一样
		 *
		 * List<String> keyS = new ArrayList<String>(); // Map<String,List<String>> map
		 * = new HashMap<String,List<String>>(); List<QrCode> qrLi = new
		 * ArrayList<QrCode>(); // qrList = shuffle(qrList); for(QrCode qc: qrList)
		 * {//两次风控规则 if(isClickQrCode(qc.getQrcodeId())) { keyS.add(qc.getQrcodeId());
		 * qrLi.add(qc); } }
		 */
		// 根据金额获取符合条件的用户
		List<String> queue = queueServiceClienFeignImpl.getQueue(code);
		ThreadUtil.execute(() -> {
			for (String cod : queue) {
				log.info("【获取银行卡：" + cod + "】");
			}
		});
		List<UserFund> userList = userInfoServiceImpl.findUserByAmount(amount, flag);
		List<Medium> qcList = mediumService.findBankByAmount(amount, code);
		log.info("【银行卡个数：" + qcList.size() + "】");
		if (CollUtil.isEmpty(userList) || CollUtil.isEmpty(qcList)) {
			return null;
		}
		ConcurrentHashMap<String, Medium> qrCollect = qcList.stream().collect(Collectors.toConcurrentMap(Medium::getMediumNumber, Function.identity(), (o1, o2) -> o1, ConcurrentHashMap::new));
		ConcurrentHashMap<String, UserFund> usercollect = userList.stream().collect(Collectors.toConcurrentMap(UserFund::getUserId, Function.identity(), (o1, o2) -> o1, ConcurrentHashMap::new));
		for (Object obj : queue) {
			String alipayAccount = obj.toString();
			if (StrUtil.isBlank(alipayAccount)) {
				continue;
			}
			Medium qr = qrCollect.get(alipayAccount);// 所属
			if (ObjectUtil.isNull(qr)) {
				continue;
			}
			log.info("【银行卡数据：" + qr.toString() + "】");
			UserFund qrcodeUser = usercollect.get(qr.getQrcodeId());// 所属
			if (ObjectUtil.isNull(qrcodeUser)) {
				continue;
			}
			log.info("【账户数据：" + qrcodeUser.toString() + "】");
			riskUtil.updataUserAmountRedis(qrcodeUser, flag);
			Object object2 = redisUtil.get(qr.getNotfiyMask() + amount.toString());
			//	Object object = redisUtil.get(qr.getPhone());
			boolean clickAmount = riskUtil.isClickAmount(qr.getQrcodeId(), amount, usercollect, flag);
			if (ObjectUtil.isNull(object2) && clickAmount) {
				redisUtil.set(qr.getNotfiyMask() + amount.toString(), orderNo, Integer.valueOf(configServiceClientImpl.getConfig(ConfigFile.ALIPAY, ConfigFile.Alipay.QR_OUT_TIME).getResult().toString())); // 核心回调数据
				//	redisUtil.set(qr.getPhone(), qr.getPhone() + amount.toString(), Integer.valueOf( configServiceClientImpl.getConfig(ConfigFile.ALIPAY, ConfigFile.Alipay.QR_OUT_TIME).getResult().toString() ));
				redisUtil.hset(qr.getQrcodeId(), qr.getQrcodeId() + DateUtil.format(new Date(), Common.Order.DATE_TYPE),
						amount.toString());
				// 该风控规则 后期有需求在加    当前媒介 如果超过  X 次未支付， 则对 当前媒介进行锁定			redisUtil.hset(qr.getFileId(), qr.getFileId() + orderNo, orderNo, Integer.valueOf( configServiceClientImpl.getConfig(ConfigFile.ALIPAY, ConfigFile.Alipay.QR_IS_CLICK).getResult().toString()));
				queueServiceClienFeignImpl.updataNodebank(qr.getMediumNumber(), qr);
				log.info("【获取二维码数据：" + qr.toString() + "】");
				return qr;
			}
		}
		return null;
	}

	/**
	 * <p>
	 * 获取唯一的订单号,根据支付宝回调
	 * </p>
	 *
	 * @return
	 */
	public String findOrderBy(BigDecimal amount, String phone) {
		log.info("【当前寻找回调参数为：amount = " + amount + "，phone = " + phone + "】");
		Object object = redisUtil.get(phone + amount.toString());
		if (ObjectUtil.isNull(object)) {
			return null;
		}
		redisUtil.deleteKey(phone + amount.toString());
		/**
		 * <p>
		 * 对IP解禁
		 * </p>
		 * Object IP = redisUtil.get(object.toString()); Set<Object> sGet =
		 * redisUtil.sGet(IP.toString()); Iterator<Object> iterator = sGet.iterator();
		 * while (iterator.hasNext()) { Object next = iterator.next();//这是具体值
		 * redisUtil.setRemove(IP.toString(),next.toString()); }
		 */
		return object.toString();
	}

	/**
	 * <p>
	 * 输入用户id，查询用户的虚拟冻结金额
	 * </p>
	 *
	 * @param userId 用户id
	 * @return amount 当前用户缓存冻结金额
	 */
	public BigDecimal getUserAmount(String userId) {
		BigDecimal amount = new BigDecimal("0");
		Map<Object, Object> hmget = redisUtil.hmget(userId);// 用户的虚拟hash金额缓存 key = 用户 + 时间 value = 金额
		Set<Object> keySet = hmget.keySet();
		try {
			for (Object obj : keySet) {
				String accountId = userId;
				int length = accountId.length();
				String subSuf = StrUtil.subSuf(obj.toString(), length);// 时间戳
				Date parse = formatter.parse(subSuf);
				Object object = hmget.get(obj.toString());// 当前金额
				if (!DateUtil.isExpired(parse, DateField.SECOND,
						Integer.valueOf(configServiceClientImpl.getConfig(ConfigFile.ALIPAY, ConfigFile.Alipay.FREEZE_PLAIN_VIRTUAL).getResult().toString()), new Date())) {
					redisUtil.hdel(userId, obj.toString());
				}
			}
			Map<Object, Object> hmget2 = redisUtil.hmget(userId);
			Set<Object> keySet2 = hmget2.keySet();
			for (Object obj : keySet2) {
				Object object = hmget2.get(obj.toString());
				BigDecimal money = new BigDecimal(object.toString());
				amount = amount.add(money);
			}
		} catch (ParseException ex) {
			ex.printStackTrace();
		}
		return amount;
	}

}

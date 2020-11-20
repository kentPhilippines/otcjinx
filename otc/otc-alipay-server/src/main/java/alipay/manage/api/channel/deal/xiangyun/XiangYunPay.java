package alipay.manage.api.channel.deal.xiangyun;

import alipay.manage.api.channel.util.ChannelInfo;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.bean.DealOrderApp;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.util.ResultDeal;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.common.PayApiConstant;
import otc.result.Result;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Component("XiangYunPay")
public class XiangYunPay extends PayOrderService {
    private static final Log log = LogFactory.get();
    @Autowired
    private UserInfoService userInfoServiceImpl;

    public static String getChinese() {
        String str = null;
        int highPos, lowPos;
        Random random = new Random();
        highPos = (176 + Math.abs(random.nextInt(71)));//区码，0xA0打头，从第16区开始，即0xB0=11*16=176,16~55一级汉字，56~87二级汉字
        random = new Random();
        lowPos = 161 + Math.abs(random.nextInt(94));//位码，0xA0打头，范围第1~94列

        byte[] bArr = new byte[2];
        bArr[0] = (new Integer(highPos)).byteValue();
        bArr[1] = (new Integer(lowPos)).byteValue();
        try {
            str = new String(bArr, "GB2312");    //区位码组合成汉字
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    String name() {
        Random random = new Random(System.currentTimeMillis());
        /* 598 百家姓 */
        String[] Surname = {"赵", "钱", "孙", "李", "周", "吴", "郑", "王", "冯", "陈", "褚", "卫", "蒋", "沈", "韩", "杨", "朱", "秦", "尤", "许",
                "何", "吕", "施", "张", "孔", "曹", "严", "华", "金", "魏", "陶", "姜", "戚", "谢", "邹", "喻", "柏", "水", "窦", "章", "云", "苏", "潘", "葛", "奚", "范", "彭", "郎",
                "鲁", "韦", "昌", "马", "苗", "凤", "花", "方", "俞", "任", "袁", "柳", "酆", "鲍", "史", "唐", "费", "廉", "岑", "薛", "雷", "贺", "倪", "汤", "滕", "殷",
                "罗", "毕", "郝", "邬", "安", "常", "乐", "于", "时", "傅", "皮", "卞", "齐", "康", "伍", "余", "元", "卜", "顾", "孟", "平", "黄", "和",
                "穆", "萧", "尹", "姚", "邵", "湛", "汪", "祁", "毛", "禹", "狄", "米", "贝", "明", "臧", "计", "伏", "成", "戴", "谈", "宋", "茅", "庞", "熊", "纪", "舒",
                "屈", "项", "祝", "董", "梁", "杜", "阮", "蓝", "闵", "席", "季", "麻", "强", "贾", "路", "娄", "危", "江", "童", "颜", "郭", "梅", "盛", "林", "刁", "钟",
                "徐", "邱", "骆", "高", "夏", "蔡", "田", "樊", "胡", "凌", "霍", "虞", "万", "支", "柯", "昝", "管", "卢", "莫", "经", "房", "裘", "缪", "干", "解", "应",
                "宗", "丁", "宣", "贲", "邓", "郁", "单", "杭", "洪", "包", "诸", "左", "石", "崔", "吉", "钮", "龚", "程", "嵇", "邢", "滑", "裴", "陆", "荣", "翁", "荀",
                "羊", "于", "惠", "甄", "曲", "家", "封", "芮", "羿", "储", "靳", "汲", "邴", "糜", "松", "井", "段", "富", "巫", "乌", "焦", "巴", "弓", "牧", "隗", "山",
                "谷", "车", "侯", "宓", "蓬", "全", "郗", "班", "仰", "秋", "仲", "伊", "宫", "宁", "仇", "栾", "暴", "甘", "钭", "厉", "戎", "祖", "武", "符", "刘", "景",
                "詹", "束", "龙", "叶", "幸", "司", "韶", "郜", "黎", "蓟", "溥", "印", "宿", "白", "怀", "蒲", "邰", "从", "鄂", "索", "咸", "籍", "赖", "卓", "蔺", "屠",
                "蒙", "池", "乔", "阴", "郁", "胥", "能", "苍", "双", "闻", "莘", "党", "翟", "谭", "贡", "劳", "逄", "姬", "申", "扶", "堵", "冉", "宰", "郦", "雍", "却",
                "璩", "桑", "桂", "濮", "牛", "寿", "通", "边", "扈", "燕", "冀", "浦", "尚", "农", "温", "别", "庄", "晏", "柴", "瞿", "阎", "充", "慕", "连", "茹", "习",
                "宦", "艾", "鱼", "容", "向", "古", "易", "慎", "戈", "廖", "庾", "终", "暨", "居", "衡", "步", "都", "耿", "满", "弘", "匡", "国", "文", "寇", "广", "禄",
                "阙", "东", "欧", "殳", "沃", "利", "蔚", "越", "夔", "隆", "师", "巩", "厍", "聂", "晁", "勾", "敖", "融", "冷", "訾", "辛", "阚", "那", "简", "饶", "空",
                "曾", "毋", "沙", "乜", "养", "鞠", "须", "丰", "巢", "关", "蒯", "相", "查", "后", "荆", "红", "游", "郏", "竺", "权", "逯", "盖", "益", "桓", "公", "仉",
                "督", "岳", "帅", "缑", "亢", "况", "郈", "有", "琴", "归", "海", "晋", "楚", "闫", "法", "汝", "鄢", "涂", "钦", "商", "牟", "佘", "佴", "伯", "赏", "墨",
                "哈", "谯", "篁", "年", "爱", "阳", "佟", "言", "福", "南", "火", "铁", "迟", "漆", "官", "冼", "真", "展", "繁", "檀", "祭", "密", "敬", "揭", "舜", "楼",
                "疏", "冒", "浑", "挚", "胶", "随", "高", "皋", "原", "种", "练", "弥", "仓", "眭", "蹇", "覃", "阿", "门", "恽", "来", "綦", "召", "仪", "风", "介", "巨",
                "木", "京", "狐", "郇", "虎", "枚", "抗", "达", "杞", "苌", "折", "麦", "庆", "过", "竹", "端", "鲜", "皇", "亓", "老", "是", "秘", "畅", "邝", "还", "宾",
                "闾", "辜", "纵", "侴", "万俟", "司马", "上官", "欧阳", "夏侯", "诸葛", "闻人", "东方", "赫连", "皇甫", "羊舌", "尉迟", "公羊", "澹台", "公冶", "宗正",
                "濮阳", "淳于", "单于", "太叔", "申屠", "公孙", "仲孙", "轩辕", "令狐", "钟离", "宇文", "长孙", "慕容", "鲜于", "闾丘", "司徒", "司空", "兀官", "司寇",
                "南门", "呼延", "子车", "颛孙", "端木", "巫马", "公西", "漆雕", "车正", "壤驷", "公良", "拓跋", "夹谷", "宰父", "谷梁", "段干", "百里", "东郭", "微生",
                "梁丘", "左丘", "东门", "西门", "南宫", "第五", "公仪", "公乘", "太史", "仲长", "叔孙", "屈突", "尔朱", "东乡", "相里", "胡母", "司城", "张廖", "雍门",
                "毋丘", "贺兰", "綦毋", "屋庐", "独孤", "南郭", "北宫", "王孙"};

        int index = random.nextInt(Surname.length - 1);
        String name = Surname[index]; //获得一个随机的姓氏

        /* 从常用字中选取一个或两个字作为名 */
        if (random.nextBoolean()) {
            name += getChinese() + getChinese();
        } else {
            name += getChinese();
        }
        return name;
    }
	public static String createParam(Map<String, Object> map) {
		try {
			if (map == null || map.isEmpty())
				return null;
			Object[] key = map.keySet().toArray();
			Arrays.sort(key);
			StringBuffer res = new StringBuffer(128);
			for (int i = 0; i < key.length; i++)
				if (ObjectUtil.isNotNull(map.get(key[i])))
					res.append(key[i] + "=" + map.get(key[i]) + "&");
			String rStr = res.substring(0, res.length() - 1);
			return rStr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String md5(String a) {
		String c = "";
		MessageDigest md5;
		String result = "";
		try {
			md5 = MessageDigest.getInstance("md5");
			md5.update(a.getBytes("utf-8"));
			byte[] temp;
			temp = md5.digest(c.getBytes("utf-8"));
			for (int i = 0; i < temp.length; i++)
				result += Integer.toHexString((0x000000ff & temp[i]) | 0xffffff00).substring(6);
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
		}
		return result;
	}

	@Override
	public Result deal(DealOrderApp dealOrderApp, String payType) {
		log.info("【进入祥云支付】");
		String channelId = payType;//配置的渠道账号
		String create = create(dealOrderApp, channelId);
		if (StrUtil.isNotBlank(create)) {
			log.info("【本地订单创建成功，开始请求远程三方支付】");
			UserInfo userInfo = userInfoServiceImpl.findUserInfoByUserId(dealOrderApp.getOrderAccount());
			if (StrUtil.isBlank(userInfo.getDealUrl())) {
				orderEr(dealOrderApp, "当前商户交易url未设置");
				return Result.buildFailMessage("请联系运营为您的商户号设置交易url");
			}
			log.info("【回调地址ip为：" + userInfo.getDealUrl() + "】");
			String url = createOrder(userInfo.getDealUrl() + PayApiConstant.Notfiy.NOTFIY_API_WAI +
							"/xiangyun-notfiy",
					dealOrderApp.getOrderAmount(),
					create,
					getChannelInfo(channelId, dealOrderApp.getRetain1()),
					dealOrderApp
			);
			if (StrUtil.isNotEmpty(url)) {
				return Result.buildSuccessResult("支付处理中", ResultDeal.sendUrl(url));
			}
		}
		return Result.buildFailMessage("支付错误");
	}

	/**
	 * partner	str(32)	是	是	商户编号，系统提供
	 * service	str(32)	是	是	参考 service字典
	 * tradeNo	str(32)	是	是	交易订单号
	 * amount	str(32)	是	是	交易金额
	 * notifyUrl	str(256)	是	是	异步通知地址
	 * resultType	str(32)	否	是	固定值web或者json(推荐json)
	 * extra	str(32)	否	是	附加信息，查询、回调时返回商户
	 * sign	str(128)	是	否	签名
	 *
	 * @param notfiy
	 * @param orderAmount
	 * @param orderId
	 * @param channelInfo
	 * @param dealOrderApp
	 * @return
	 */
	private String createOrder(String notfiy, BigDecimal orderAmount,
							   String orderId, ChannelInfo channelInfo,
							   DealOrderApp dealOrderApp) {
		Map<String, Object> map = new HashMap<String, Object>();
        String key = channelInfo.getChannelPassword();
        String appid = channelInfo.getChannelAppId();
        String payType = channelInfo.getChannelType();
        map.put("partner", appid);
        map.put("service", payType);
        map.put("tradeNo", orderId);
        map.put("amount", orderAmount);
        map.put("notifyUrl", notfiy);
        map.put("resultType", "json");
        map.put("extra", "extra");
        map.put("buyer", name());
        String createParam = createParam(map);
        String md5 = md5(createParam + "&" + key);
        map.put("sign", md5);
        log.info("【当前祥云请求参数为：" + map.toString() + "】");
        String post = HttpUtil.post(channelInfo.getDealurl(), map);
        log.info("【祥云响应参数为：" + post + "】");
        JSONObject parseObj = JSONUtil.parseObj(post);
        Object object = parseObj.get("isSuccess");
        if (ObjectUtil.isNotNull(object)) {
            log.info("当前祥云的订单为：" + object + "");
            if (object.equals("T")) {
                Object object2 = parseObj.get("url");
                if (ObjectUtil.isNotNull(object2)) {
                    log.info("【支付链接为：" + object2 + "】");
                    String url = object2.toString();
                    //https://ap5xt6p0w.vanns.vip/api/bank/info/[唯一识别号]?n= 任意 随便  真实 唯一 实际 确认 付款人
                    //https://ap5xt6p0w.vanns.vip/api/bank/info/[唯一识别号]?t=1605764608393&i=0
                    //"https://ap5xt6p0w.vanns.vip/api/bank/fc9025d82ef745168fea6854a92db802"      //源链接 截取唯一识别号
                    String[] split = url.split("/");
                    String last = CollUtil.getLast(Arrays.asList(split));
                    log.info("【祥云支付链接编号为：" + last + "】");
                    String s = StrUtil.subBefore(url, last, true);
                    log.info("【祥云支付链接主链接接口为：" + s + "】");
                    String bankinfo = s + "/info/" + last;
                    String bankinfo1 = s + "/info/" + last + "?t=" + System.currentTimeMillis() + "&i=0";
                    //	log.info("【开始伪装用户请求："+s+"/info/"+last+"】");
                    //	String s1 = HttpUtil.get(bankinfo);
                    //	log.info("【祥云输入姓名返回为："+s1+"】");
                    //	String s2 = HttpUtil.get(bankinfo1);
                    //		log.info("【祥云获取银行卡详情返回为："+s2+"】");
                    return object2.toString();
                }
			} else {
				Object object2 = parseObj.get("msg");
				String msg = "";
				if (ObjectUtil.isNotNull(object2) && StrUtil.isNotEmpty(object2.toString()))
					msg = object2.toString();
				else
					msg = "错误：未知错误";
				orderEr(dealOrderApp, msg);
			}
		}
		return "";
	}
}

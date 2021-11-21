package test.number;

import alipay.manage.api.channel.util.shenfu.PayUtil;
import alipay.manage.bean.DealOrder;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import otc.result.Result;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MD5 {
	public static void main(String[] args) 	{
		String s = "RSPCOD=000000&RSPMSG=%25E5%259B%259E%25E8%25B0%2583%25E4%25BF%25A1%25E6%2581%25AF&TXNAMT=10000&orderResult=2&orderno=J20211120180645436828812&pOrderNo=JX19920211120180645Pn5D9q7IOou2f5oCGCyQ&resultMsg=%E6%88%90%E5%8A%9F&sign=9886FD8D0656DCF322FF48FB6D9B6DE0&timeStamp=20211121115950";
		HashMap<String, String> stringStringHashMap = HttpUtil.decodeParamMap(s, Charset.defaultCharset().toString());
		System.out.println(stringStringHashMap.toString());
		String sign = "";
		String RSPCOD = "";
		String RSPMSG = "";
		String resultmsg = "";
		String timestamp = "";
		String orderno ="";
		String txnamt = "";
		String orderresult = "";
		String porderno = "";
		String extraData = "";
		sign = stringStringHashMap.get("sign").toUpperCase();
		RSPCOD = stringStringHashMap.get("RSPCOD").toUpperCase();
		RSPMSG = stringStringHashMap.get("RSPMSG").toUpperCase();
		resultmsg = stringStringHashMap.get("resultmsg");
		timestamp = stringStringHashMap.get("timestamp");
		orderno = stringStringHashMap.get("orderno").toUpperCase();
		txnamt = stringStringHashMap.get("txnamt");
		orderresult = stringStringHashMap.get("orderresult");
		porderno = stringStringHashMap.get("porderno");
		extraData = stringStringHashMap.get("extraData");
		String ww  = "txnamt="+txnamt+"&orderresult=2&orderno="+orderno+"&porderno="+porderno+"&resultmsg=成功&timestamp="+timestamp+"&key="+"F7A9BCB6E0C07708";
		//示例：
		//MD5加密原串如下：
		//txnamt=50000&orderresult=2&orderno=100006xj5453079669830466041&porderno=100006xj5453079669830466041&resultmsg=成功&timestamp=20190922091518&key=4C86E85CCFC48D06
		String md5 = PayUtil.md5(ww);
		//9886FD8D0656DCF322FF48FB6D9B6DE0
		System.out.println(md5);
		String a = "FinishTime=20211121122600&MerchantId=63290&MerchantUniqueOrderId=W20211121122030790558268&Solt=386iVr3XnqP29mMRLpXgGm06O&Status=100&WithdrawOrderId=W6329021112112202232";
		String kry = "k4j4mOok99rbKD4eazdNX2nCJ212h64uYMq";
	    a  = a + kry;
		String s1 = md5(a.toUpperCase());
		System.out.println(s1);
		Map<String,Object> query = new HashMap<>();
		/**
		 * 机构号	agtorg	是	String(30)	机构号（平台提供）
		 * 商户号	mercid	是	String(30)	商户号（平台提供）
		 * 订单号	ordernumber	是	String(30)	机构号+不重复订单号
		 */
		String orderId = "J20211120180645436828812";
		query.put("agtorg","JX199");
		query.put("mercid","484584045119506");
		query.put("ordernumber","J20211120180645436828812");
		String sg = "agtorg=JX199&mercid=484584045119506&ordernumber="+orderId+"&key=F7A9BCB6E0C07708";
		query.put("sign",md5(sg).toUpperCase());
		String post = HttpUtil.post( "http://mng.yuegepay.com/709105.tran9",query);
		System.out.println(post);
		JSONObject jsonObject = JSONUtil.parseObj(post);
		String ordernumber = jsonObject.getStr("ORDERNUMBER");
		String paystatus = jsonObject.getStr("PAYSTATUS");
		if(ordernumber.equals(orderId) && "2".equals(paystatus)){
			System.out.println("支付成功");

		}


		/**
		 * MerchantId	是	string	商户ID	见开户文档
		 * MerchantUniqueOrderId	是	string	要查询的订单商户唯一订单ID	您创建代付订单时使用的ID
		 * Timestamp	是	string	时间戳	您服务器当前时间戳，+8时区，格式为yyyyMMddHHmmss
		 * 例如 2019 年 1 月 2 日 3 时 4 分 5 秒则应传递：20190102030405
		 * Sign	是	string	签名	详见签名章节
		 */
		String Timestamp = DateUtil.format(new Date(), DatePattern.PURE_DATETIME_MS_PATTERN);

		Map<String,Object> query1 = new HashMap<>();
		query1.put("MerchantId","63290");
		query1.put("MerchantUniqueOrderId","W20211121122030790558268");
		query1.put("Timestamp",Timestamp);
		String createParam = PayUtil.createParam(query1);
		createParam.toLowerCase();
		System.out.println("优快通代付参数"+createParam);
		String mm = PayUtil.md5(createParam + "k4j4mOok99rbKD4eazdNX2nCJ212h64uYMq");
		query1.put("Sign",mm);

		String aaa = HttpUtil.post( "https://service.api.6izeprtw4j1i0a.bdqwx.xyz/InterfaceV6/QueryWithdrawOrder/",query1);
		System.out.println(aaa);
		JSONObject jsonObject1 = JSONUtil.parseObj(aaa);
		String WithdrawOrderStatus = jsonObject1.getStr("WithdrawOrderStatus");
		String MerchantUniqueOrderId = jsonObject1.getStr("MerchantUniqueOrderId");
		if("W20211121122030790558268".equals(MerchantUniqueOrderId) && "100".equals(WithdrawOrderStatus)){
			System.out.println("支付成功");

		}



	}

	private static final String UTF_8 = "utf-8";
	private static final String ENCODE_TYPE = "md5";
    /**
     * md5加密
     *
     * @param
     * @return
     */
    public static String md5(String a) {
		String c = "";
		MessageDigest md5;
		String result = "";
		try {
			md5 = MessageDigest.getInstance(ENCODE_TYPE);
			md5.update(a.getBytes(UTF_8));
			byte[] temp;
			temp = md5.digest(c.getBytes(UTF_8));
			for (int i = 0; i < temp.length; i++)
				result += Integer.toHexString((0x000000ff & temp[i]) | 0xffffff00).substring(6);
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
		}
		return result;
    }
}

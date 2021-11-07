package test.number;

import alipay.manage.api.channel.util.shenfu.PayUtil;
import alipay.manage.bean.DealOrder;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import otc.result.Result;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MD5 {
	public static void main(String[] args) {
		String pid = "ZRB615074FAB0D2A";
		String amount =  "150.00";
		String order_no = UUID.fastUUID().toString();
		String pay_type = "603";
		String notify_url = "www.beidu.com";
		Map map = new HashMap();
		map.put("pid", pid);
		map.put("order_no", order_no);
		map.put("amount", amount);
		map.put("pay_type", pay_type);
		map.put("notify_url", notify_url);

		System.out.println("【新盛话费加签参数：" + map + "】");
		String createParam = PayUtil.createParam(map);
		String md5 = PayUtil.md5(createParam + "&key=" +  "317138754d41cee1325d30fcc4b18b7c");
		map.put("sign", md5);
		map.put("return_type", "json");
		String post = HttpUtil.post( "https://pay.xshengs.com/pay/order", map);
		System.out.println("【新盛话费返回数据：" + post + "】");
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

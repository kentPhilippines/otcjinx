package test.number;

import alipay.manage.bean.DealOrder;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
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
import java.util.List;

public class MD5 {
	public static void main(String[] args) {

		String m  = "acct_name=李瑞雨&bank_name=中国邮政储蓄银行&card_no=6217991270025039243&channel=4&money_order=2952&no_order=W20210812172548700952368&notify_url=http://starpay888.net:17628/huitongfuwit-noyfit&oid_partner=202108120244595539&sign_type=MD5&time_order=20210812172550";

		String name = "付款人：唐晓君";
		String[] split = name.split("付款人：");
		String a = split[1];
		System.out.println(a);


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

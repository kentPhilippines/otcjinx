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
		List<?> row1 = CollUtil.newArrayList("aa", "bb", "cc", "dd", DateUtil.date(), 3.22676575765);
		List<?> row2 = CollUtil.newArrayList("aa1", "bb1", "cc1", "dd1", DateUtil.date(), 250.7676);
		List<?> row3 = CollUtil.newArrayList("aa2", "bb2", "cc2", "dd2", DateUtil.date(), 0.111);
		List<?> row4 = CollUtil.newArrayList("aa3", "bb3", "cc3", "dd3", DateUtil.date(), 35);
		List<?> row5 = CollUtil.newArrayList("aa4", "bb4", "cc4", "dd4", DateUtil.date(), 28.00);
		List<List<?>> rows = CollUtil.newArrayList(row1, row2, row3, row4, row5);
		BigExcelWriter writer= ExcelUtil.getBigWriter("/xxx.xlsx");
// 一次性写出内容，使用默认样式
		writer.write(rows);
// 关闭writer，释放内存
		writer.close();




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

package test.number;

import cn.hutool.core.date.DatePattern;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class random {
	public static void main(String[] args) {


		String s = HttpUtil.get("https://coinyep.com/api/v1/?from=ETH&to=USDT&lang=zh&format=json");
		JSONObject jsonObject = JSONUtil.parseObj(s);
		String price = jsonObject.getStr("price");
		System.out.println(price);
		/**
		 * {"jsonrpc":"2.0","id":1,"result":
		 *
		 *
		 * {
		 * "blockHash":"0x17f315d57662e25e66d560091d7945a53d5b6f83ce8c52eddfa7900c58cc0d7c",
		 * "blockNumber":"0xb542f0",
		 * "from":"0x0418f374f25edab13d38a7d82b445ce9934bfc12",
		 * "gas":"0xea60",
		 * "gasPrice":"0x2a9ba66bb3",
		 * "hash":"0x1556e72bf02c45a483dafd12e3675762e73efcfa4d6ba25f1147e9467d1ce10c",
		 * "input":"0xa9059cbb00000000000000000000000028250971cf8bb17edb2fd31e72c7fd352ae0efcb0000000000000000000000000000000000000000000000000000000011e94420",
		 * "nonce":"0x2d",
		 * "to":"0xdac17f958d2ee523a2206206994597c13d831ec7",
		 * "transactionIndex":"0xcf",
		 * "value":"0x0",
		 * "v":"0x25",
		 * "r":"0x7398eaef18d41cfe99cd2daf7c4766661db0e692dc43601d5c535939f183f0eb",
		 * "s":"0xa0303adfb6ce57523e41adf7429b4d43aa4f8ffee86a07b5ca3bbb15ae7d89"}}
		 */


	}

	static void time() {


		long a = 1609780976;
		SimpleDateFormat sdf = new SimpleDateFormat(DatePattern.NORM_DATETIME_PATTERN);
		java.util.Date date = new Date(a * 1000);
		String str = sdf.format(date);
		System.out.println(str);


	}


	private static boolean isNumber(String str) {
		BigDecimal a = new BigDecimal("1");
		System.out.println(a);
		double dInput = a.doubleValue();
		long longPart = (long) dInput;
		BigDecimal bigDecimal = new BigDecimal(Double.toString(dInput));
		BigDecimal bigDecimalLongPart = new BigDecimal(Double.toString(longPart));
		double dPoint = bigDecimal.subtract(bigDecimalLongPart).doubleValue();
		System.out.println("整数部分为:" + longPart + "\n" + "小数部分为: " + dPoint);
		return dPoint > 0;
	}
}

package test.number;

import cn.hutool.http.HttpUtil;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class AmountTest {
	public static void main(String[] args) throws NoSuchAlgorithmException, KeyManagementException {
		Map<String, Object> map = new HashMap<>();
		map.put("url", "https://ht-tw.maitoo998.com/v6/callback/JinXingInfrom");
		map.put("amount", 500.000000);
		map.put("tradesno", "C1609745418207841706");
		map.put("appid", "tt678");
		map.put("sign", "8f95c19a305da165b87813e7a22c1aca");
		map.put("statusdesc", "成功");
		map.put("apporderid", "JX9745418117177892");
		map.put("status", "2");
		String post = HttpUtil.post("http://47.242.50.29/forword", map);
		System.out.println(post);

	}

}

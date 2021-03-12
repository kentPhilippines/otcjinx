package test.number;

import cn.hutool.http.HttpUtil;

import java.util.HashMap;
import java.util.Map;

public class thread {
	public static void main(String[] args) {
		String url = "http://localhost/back/merchant/user/list";
		Map map = new HashMap();
		map.put("loginName", "kentMer1");
		String post = HttpUtil.post(url, map);
		System.out.println(post);
	}
	}


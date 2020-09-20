package test.number;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import otc.result.Result;

public class password {
	public static void main(String[] args) {
		JSON asdsad = JSONUtil.parse(Result.buildSuccessResult(Result.buildSuccessResultCode("支付处理中", "www.baidu.com", 1)));
		System.out.println(asdsad.toString());

	}

}

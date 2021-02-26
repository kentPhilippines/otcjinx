package test.number;

import cn.hutool.http.HttpUtil;

public class password {
	public static void main(String[] args) {
		String before = "<span id=\"ContentPlaceHolder1_spanTxFee\"><span data-toggle='tooltip' title='Gas Price * Gas Used by Transaction'>";
		String after = "</span> <button class=\"u-label u-label--value u-label--primary rounded\" type=\"button\" data-toggle=\"tooltip\" data-placement=\"auto\" id=\"txfeebutton\" data-title=\"Displaying current Txn Fee; Click to show Txn Fee on Day of Txn\" data-original-title=\"\" title=\"\">";
		//String s = StrUtil.subBetween(HttpUtil.get("https://etherscan.io/tx/0x1556e72bf02c45a483dafd12e3675762e73efcfa4d6ba25f1147e9467d1ce10c"), before, after);
		String s1 = HttpUtil.get("https://etherscan.io/tx/0x1556e72bf02c45a483dafd12e3675762e73efcfa4d6ba25f1147e9467d1ce10c");
		System.out.println(s1);


	}

}

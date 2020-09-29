package test.number.channal;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import alipay.manage.api.channel.util.yifu.YiFuUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.netflix.ribbon.proxy.annotation.Http;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;

public class YiFu {
	public static void main(String[] args) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("app_id", YiFuUtil.APPID);
		map.put("channel", YiFuUtil.BANK_TO_BANK);
		map.put("out_trade_no", StrUtil.uuid().toString());
		map.put("money",  "100"+".00");
		map.put("callback_url", "www.baidu.com");
		String createParam = YiFuUtil.createParam(map);
		System.out.println("【易付签名前参数："+createParam+"】");
		String md5 = YiFuUtil.md5(createParam + "key="+YiFuUtil.KEY);
		String sign = md5.toUpperCase();
		map.put("sign", sign);
		System.out.println("【请求Yifu参数："+map.toString()+"】");
		//{"code":200,"msg":"ok","data":{"pay_url":"http://kpay8494.168yuju.cn/pay/gateway/order?c=22&o=2020070411330189354","money":"100"}}
		//{"code":419,"msg":"签名不正确","data":[]}
		String post = HttpUtil.post(YiFuUtil.URL, map);
		JSONObject jsonObject = JSONUtil.parseObj(post);
		String code = jsonObject.getStr("code");
		System.out.println("【返回Yifu参数："+jsonObject.toString()+"】");
		

	}

	private static void test() {
		/*String key = "kRrgxUk0QCpxz1bS5QVRJXZKEFPGnrqD";
		String app_id = "-YbBmS2aFEfgBx4mnXI" ;
		String channel = "1011";
		int i = RandomUtil.randomInt(1000000000);
		String out_trade_no =i+"";

		String money = "100";
		String callback_url = "https://www.uz-pay.com/Api/collection/query";
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("app_id", app_id);
		map.put("channel", channel);
		map.put("out_trade_no", out_trade_no);
		map.put("money", money);
		map.put("callback_url", callback_url);
		String createParam = createParam(map);
		System.out.println(createParam);
		String md5 = md5(createParam + "&key="+key);
		String sign = md5.toUpperCase();
		map.put("sign", sign);
		//{"code":200,"msg":"ok","data":{"pay_url":"http://kpay8494.168yuju.cn/pay/gateway/order?c=22&o=2020070411330189354","money":"100"}}
		//{"code":419,"msg":"签名不正确","data":[]}
		String post = HttpUtil.post("https://ef.sanbanye.com/pay", map);
		System.out.println(post);*/


		//app_id=-YbBmS2aFEfgBx4mnXI&money=1000&out_trade_no=C1593861094418016017&pay_at=2020-07-04 19:11:36&
	}
	 public static String md5(String a) {
	    	String c = "";
	    	MessageDigest md5;
		   	String result="";
			try {
				md5 = MessageDigest.getInstance("md5");
				md5.update(a.getBytes("utf-8"));
				byte[] temp;
				temp=md5.digest(c.getBytes("utf-8"));
				for (int i=0; i<temp.length; i++)
					result+=Integer.toHexString((0x000000ff & temp[i]) | 0xffffff00).substring(6);
			} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			}
			return result;
	    }
	public static String createParam(Map<String, Object> map) {
		try {
			if (map == null || map.isEmpty())
				return null;
			Object[] key = map.keySet().toArray();
			Arrays.sort(key);
			StringBuffer res = new StringBuffer(128);
			for (int i = 0; i < key.length; i++) 
				if(ObjectUtil.isNotNull(map.get(key[i])))
					res.append(key[i] + "=" + map.get(key[i]) + "&");
			return res.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

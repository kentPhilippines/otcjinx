package test.number.channal;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpUtils;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

public class alipayH5XIanyu {
	public static void main(String[] args) { 
		/**
		fxid			商务号				是	唯一号，由穿山甲支付-csjPAY提供
		fxddh			商户订单号				是	平台返回商户提交的订单号
		fxaction		商户查询动作			是	商户查询动作，这里填写【orderquery】
		fxsign			签名【md5(商务号+商户订单号+商户查询动作+商户秘钥)】	是	通过签名算法计算得出的签名值。
	
		String key = "AHFuoYCUgZcOdpectBxYiPElWMVGljbc";
		Map<String, Object>  map = new HashMap<String, Object>();
		String fxid = "2020177";
		String fxddh = "1261883409429565440";
		String fxaction = "orderquery";
		map.put("fxid", fxid);
		map.put("fxddh", fxddh);
		map.put("fxaction", fxaction);
		map.put("fxsign", md5(fxid+fxddh+fxaction+key));
		String post = HttpUtil.post("https://csj.fenvun.com/Pay"  , map);
		JSONObject parseObj = JSONUtil.parseObj(post);
		XianYu bean = JSONUtil.toBean(parseObj, XianYu.class);
		System.out.println(bean.toString());
		
		
		 - 【当前回调ip为：202.79.174.113】
		 2020-05-21 15:53:37.168 [http-nio-9010-exec-8] INFO  a.manage.api.channel.notfiy.YouShuAlipayNotfiy - order_id=175061&out_order_id=9fe76c15-038a-4950-aa2d-3e3a58876ef1&paytime=1590027003&price=300.0000&realprice=300.0000&type=alipay&key=zfZ2BTd6PHKvwCxU
		 2020-05-21 15:53:37.168 [http-nio-9010-exec-8] INFO  a.manage.api.channel.notfiy.YouShuAlipayNotfiy - 【加密前参数：order_id=175061&out_order_id=9fe76c15-038a-4950-aa2d-3e3a58876ef1&paytime=1590027003&price=300.0000&realprice=300.0000&type=alipay&key=zfZ2BTd6PHKvwCxU】
		 2020-05-21 15:53:37.168 [http-nio-9010-exec-8] INFO  a.manage.api.channel.notfiy.YouShuAlipayNotfiy - 【优树参数为：8F48C696A8E41A917AAA9C58AAD93114】
		 2020-05-21 15:53:37.168 [http-nio-9010-exec-8] INFO  a.manage.api.channel.notfiy.YouShuAlipayNotfiy - 【我方验签参数为：8F48C696A8E41A917AAA9C58AAD93114，请求方签名参数为：B0447E1EE6A254808FD710B9B06FE2A9】
		 2020-05-21 15:53:37.168 [http-nio-9010-exec-8] INFO  a.manage.api.channel.notfiy.YouShuAlipayNotfiy - 【验签失败】
		 2020-05-21 15:53:42.557 [http-nio-9010-exec-10] INFO  alipay.manage.api.Api - 【当前远程调用，查询所有未剪裁二维码】

			*/
		String extend = "312|xxx";
		String ss = "extend="+extend+"&order_id=175061&out_order_id=9fe76c15-038a-4950-aa2d-3e3a58876ef1&paytime=1590027003&price=300.0000&realprice=300.0000&type=alipay&key=zfZ2BTd6PHKvwCxU";
		String upperCase = md5(ss).toUpperCase();
		System.out.println(upperCase);
		/**
		 * 	money			是		订单金额
			part_sn			是		商家平台订单号
			notify			是		异步回调地址
			id				是		码商账号id
			sign			是		签名
				下单请求地址：http://xianyu.tbuoljh.cn/api/order
				商户key：  8ff661281faf68288666c7fdef535a74
				商户ID：31
				回调ip：110.42.1.189
		String key = "8ff661281faf68288666c7fdef535a74";
		Map<String, Object>  map = new HashMap<String, Object>();
		map.put("money", "2000");
		map.put("part_sn", "222222224422");
		map.put("notify", "http://182.16.89.146:9010/notfiy-api-pay/xianyu-notfiy");
		map.put("id", "31");
		String createParam = createParam(map);
		String a = key+createParam+key;
		System.out.println(a);
		String md5 = md5(key+createParam+key);
		System.out.println(md5);
		map.put("sign", md5);
		beanss ben = new beanss();
		ben.setId("31");
		ben.setMoney("2000");
		ben.setNotify("http://182.16.89.146:9010/notfiy-api-pay/xianyu-notfiy");
		ben.setPart_sn("222222224422");
		ben.setSign(md5);
		JSONObject parseObj = JSONUtil.parseObj(ben);
		System.out.println(parseObj.toString());
		
		String post = HttpUtil.post("http://xianyu.tbuoljh.cn/api/order", map);
		System.out.println(post);
		*/
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
						res.append(key[i]  + map.get(key[i]).toString()  );
				String rStr = res.substring(0, res.length() - 1);
				return rStr;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}


}
class XianYu {
	//{"fxid":"2020177","fxstatus":"1","fxddh":"12365441234","fxorder":"qzf20200516171826589638709215147","fxdesc":"000000","fxfee":"2000.00","fxattch":"test","fxtime":"1589620706","fxsign":"8f31d1ccd2ffeae15885c4e33b08c01c"}
	private String fxid;
	private String fxstatus;
	private String fxddh;
	private String fxorder;
	private String fxdesc;
	private String fxattch;
	private String fxtime;
	private String fxsign;
	public XianYu() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getFxid() {
		return fxid;
	}
	public void setFxid(String fxid) {
		this.fxid = fxid;
	}
	public String getFxstatus() {
		return fxstatus;
	}
	public void setFxstatus(String fxstatus) {
		this.fxstatus = fxstatus;
	}
	public String getFxddh() {
		return fxddh;
	}
	public void setFxddh(String fxddh) {
		this.fxddh = fxddh;
	}
	public String getFxorder() {
		return fxorder;
	}
	public void setFxorder(String fxorder) {
		this.fxorder = fxorder;
	}
	public String getFxdesc() {
		return fxdesc;
	}
	public void setFxdesc(String fxdesc) {
		this.fxdesc = fxdesc;
	}
	public String getFxattch() {
		return fxattch;
	}
	public void setFxattch(String fxattch) {
		this.fxattch = fxattch;
	}
	public String getFxtime() {
		return fxtime;
	}
	public void setFxtime(String fxtime) {
		this.fxtime = fxtime;
	}
	public String getFxsign() {
		return fxsign;
	}
	public void setFxsign(String fxsign) {
		this.fxsign = fxsign;
	}
	@Override
	public String toString() {
		return "XianYu [fxid=" + fxid + ", fxstatus=" + fxstatus + ", fxddh=" + fxddh + ", fxorder=" + fxorder
				+ ", fxdesc=" + fxdesc + ", fxattch=" + fxattch + ", fxtime=" + fxtime + ", fxsign=" + fxsign + "]";
	}
}
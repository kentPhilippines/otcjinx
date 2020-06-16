package test.number.channal;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.methods.HttpPost;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;

public class PDD {
	public static void main(String[] args) {
		test();
	}
	
	static SimpleDateFormat d = new SimpleDateFormat("yyyyMMddHHmmss");
	static void test(){
		/**
		    oid_partner			String(18)			√				参数名称：商家号 商户签约时，分配给商家的唯一身份标识 例如：201411171645530813
			notify_url			String(128)			√				参数名称：服务器异步通知地址 支付成功后，系统会主动发送通知给商户，商户必须指定此通知地址
			user_id				String(32)			√				该用户在商户系统中的唯一编号，要求是该 编号在商户系统中唯一标识该用户
			sign_type			String(10)			√				参数名称：签名方式 1.取值为：MD5
			sign				String				√				参数名称：签名数据 该字段不参与签名，值如何获取，请参考提供的示例代码。
			no_order			String(32)			√				参数名称：商家订单号 商家网站生成的订单号，由商户保证其唯一性，由字母、数字组成。
			time_order			Date				√				参数名称：商家订单时间 时间格式：YYYYMMDDH24MISS 14 位数 字，精确到秒
			money_order			Number(13,2)		√				参数名称：客户实际支付金额与币种对应
			name_goods			String(40)			√				参数名称：商品名称
			info_order			String(255)			×				参数名称：商品描述
			pay_type			String(5)			√				参数名称：支付类型
		 */
		String key ="dtfysghxiazAIGY2BNEWDPOIL51";
		String appid  = "202006032221186176";
		String url = "api.djyq123.com/gateway/bankgateway/pay";
		Map<String, Object> map = new HashMap();
		map.put("oid_partner", appid);
		map.put("notify_url", "www.baidu.com");
		map.put("sign_type", "MD5");
		map.put("user_id", IdUtil.objectId());
		map.put("no_order", UUID.randomUUID().toString());
		map.put("time_order", d.format(new Date()));
		map.put("money_order", "313");
		map.put("name_goods", "alipaycan");
		map.put("pay_type", "111");//PDD PDD 插件通道
		map.put("info_order", "info_order");
		String createParam = createParam(map);
		 String md5 = md5(createParam+key);
		map.put("sign", md5);
		String post = HttpUtil.post(url, map);
		System.out.println(post);
		PddBean bean = JSONUtil.toBean(post, PddBean.class);
		System.out.println(bean.toString());
		
		
		
		
		
		
	}
	 public static String md5(String str) {
	        MessageDigest md5 = null;
	        try {
	            md5 = MessageDigest.getInstance("MD5");
	        } catch (Exception e) {
	            System.out.println(e.toString());
	            e.printStackTrace();
	            return "";
	        }
	        char[] charArray = str.toCharArray();
	        byte[] byteArray = new byte[charArray.length];
	        for (int i = 0; i < charArray.length; i++)
	            byteArray[i] = (byte) charArray[i];
	        byte[] md5Bytes = md5.digest(byteArray);
	        StringBuffer hexValue = new StringBuffer();
	        for (int i = 0; i < md5Bytes.length; i++) {
	            int val = ((int) md5Bytes[i]) & 0xff;
	            if (val < 16)
	                hexValue.append("0");
	            hexValue.append(Integer.toHexString(val));
	        }
	        return hexValue.toString();
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
			String rStr = res.substring(0, res.length() - 1);
			return rStr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
class PddBean{
	private String money_order;
	private String no_order;
	private String oid_partner;
	private String redirect_url;
	private String ret_code;
	private String ret_msg;
	private String sign;
	public String getMoney_order() {
		return money_order;
	}
	public void setMoney_order(String money_order) {
		this.money_order = money_order;
	}
	public String getNo_order() {
		return no_order;
	}
	public void setNo_order(String no_order) {
		this.no_order = no_order;
	}
	public String getOid_partner() {
		return oid_partner;
	}
	public void setOid_partner(String oid_partner) {
		this.oid_partner = oid_partner;
	}
	public String getRedirect_url() {
		return redirect_url;
	}
	public void setRedirect_url(String redirect_url) {
		this.redirect_url = redirect_url;
	}
	public String getRet_code() {
		return ret_code;
	}
	public void setRet_code(String ret_code) {
		this.ret_code = ret_code;
	}
	public String getRet_msg() {
		return ret_msg;
	}
	public void setRet_msg(String ret_msg) {
		this.ret_msg = ret_msg;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	@Override
	public String toString() {
		return "PddBean [money_order=" + money_order + ", no_order=" + no_order + ", oid_partner=" + oid_partner
				+ ", redirect_url=" + redirect_url + ", ret_code=" + ret_code + ", ret_msg=" + ret_msg + ", sign="
				+ sign + "]";
	}
	
}


package deal.manage.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.stereotype.Component;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import deal.manage.bean.util.AddressIpBean;

@Component
public class AddressUtils {
	/**
	 * <p>根据IP查询地域</p>
	 * @param ip			IP地址
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public AddressIpBean getAddresses(String ip ) throws UnsupportedEncodingException  {
		String addresses = getAddresses("ip="+ip,"UTF-8");
		if(StrUtil.isBlank(addresses)) {
			AddressIpBean ipBean = new AddressIpBean();
			ipBean.setIp(ip);
			return ipBean;
		}
		return toBean(addresses);
	}
	AddressIpBean toBean(String json){
		 JSONObject parseObj = JSONUtil.parseObj(json);
		 AddressIpBean data = new AddressIpBean();
		 Object object2 = parseObj.get("data");
		 String code = parseObj.get("code").toString();
		 JSONObject parseObj2 = JSONUtil.parseObj(object2.toString());
		 String country = parseObj2.get("country").toString();//国家
		 String ip = parseObj2.get("ip").toString();//ip
		 String area = parseObj2.get("area").toString();//区域
		 String region = parseObj2.get("region").toString();//省份
		 String city = parseObj2.get("city").toString();//市
		 String isp = parseObj2.get("isp").toString();//运营商
		 String country_id = parseObj2.get("country_id").toString();//国家编码    中国CN
		 String area_id = parseObj2.get("area_id").toString();//区域ID
		 String region_id = parseObj2.get("region_id").toString();//省份ID   国内就是邮编
		 String city_id = parseObj2.get("city_id").toString();//城市ID  国内就是  邮编
		 String county_id = parseObj2.get("county_id").toString();//未知数据
		 String isp_id = parseObj2.get("isp_id").toString();//互联网服务提供商 id   例如  电信为  100017
		 data.setArea(area);
		 data.setArea_id(area_id);
		 data.setCity(city);
		 data.setCity_id(city_id);
		 data.setCountry(country);
		 data.setCountry_id(country_id);
		 data.setIp(ip);
		 data.setIsp(isp);
		 data.setIsp_id(isp_id);
		 data.setRegion(region);
		 data.setRegion_id(region_id);
		 data.setCode(code);
		return data;
	}
	
	/** 
	  * @param content 
	  *          <p>  请求的参数 格式为：name=xxx&pwd=xxx </p>
	  * @param encoding 
	  *          <p>  服务器端请求编码。如GBK,UTF-8等 </p>
	  * @return 
	  * @throws UnsupportedEncodingException 
	  */
	 private String getAddresses(String content, String encodingString)  
	   throws UnsupportedEncodingException {  
	  // 这里调用pconline的接口  
	  String urlStr = "http://ip.taobao.com/service/getIpInfo.php";  
	  // 从http://whois.pconline.com.cn取得IP所在的省市区信息  
	  String returnStr = this.getResult(urlStr, content, encodingString);  
	  if (returnStr != null) {  
	   // 处理返回的省市区信息  
	   return returnStr;  
	  }  
	  return null;  
	 }  
	 /** 
	  * @param urlStr 
	  * <p> 请求的地址  </p>
	  * @param content 
	  * <p> 请求的参数 格式为：name=xxx&pwd=xxx </p>
	  * @param encoding 
	  * <p> 服务器端请求编码。如GBK,UTF-8等  </p>
	  * @return 
	  */  
	 private String getResult(String urlStr, String content, String encoding) {  
	  URL url = null;  
	  HttpURLConnection connection = null;  
	  try {  
	   url = new URL(urlStr);  
	   connection = (HttpURLConnection) url.openConnection();// 新建连接实例  
	   connection.setConnectTimeout(2000);// 设置连接超时时间，单位毫秒  
	   connection.setReadTimeout(2000);// 设置读取数据超时时间，单位毫秒  
	   connection.setDoOutput(true);// 是否打开输出流 true|false  
	   connection.setDoInput(true);// 是否打开输入流true|false  
	   connection.setRequestMethod("POST");// 提交方法POST|GET  
	   connection.setUseCaches(false);// 是否缓存true|false  
	   connection.connect();// 打开连接端口  
	   DataOutputStream out = new DataOutputStream(connection  
	     .getOutputStream());// 打开输出流往对端服务器写数据  
	   out.writeBytes(content);// 写数据,也就是提交你的表单 name=xxx&pwd=xxx  
	   out.flush();// 刷新  
	   out.close();// 关闭输出流  
	   BufferedReader reader = new BufferedReader(new InputStreamReader(  
	     connection.getInputStream(), encoding));// 往对端写完数据对端服务器返回数据  
	   // ,以BufferedReader流来读取  
	   StringBuffer buffer = new StringBuffer();  
	   String line = "";  
	   while ((line = reader.readLine()) != null) {  
	    buffer.append(line);  
	   }  
	   reader.close();  
	   return buffer.toString();  
	  } catch (IOException e) {  
	   e.printStackTrace();  
	  } finally {  
	   if (connection != null) {  
	    connection.disconnect();// 关闭连接  
	   }  
	  }  
	  return null;  
	 }  
	 /** 
	  * unicode 转换成 中文 
	  * @author fanhui 2007-3-15 
	  * @param theString 
	  * @return 
	  */  
	 public static String decodeUnicode(String theString) {  
	  char aChar;  
	  int len = theString.length();  
	  StringBuffer outBuffer = new StringBuffer(len);  
	  for (int x = 0; x < len;) {  
	   aChar = theString.charAt(x++);  
	   if (aChar == '\\') {  
	    aChar = theString.charAt(x++);  
	    if (aChar == 'u') {  
	     int value = 0;  
	     for (int i = 0; i < 4; i++) {  
	      aChar = theString.charAt(x++);  
	      switch (aChar) {  
	      case '0':  
	      case '1':  
	      case '2':  
	      case '3':  
	      case '4':  
	      case '5':  
	      case '6':  
	      case '7':  
	      case '8':  
	      case '9':  
	       value = (value << 4) + aChar - '0';  
	       break;  
	      case 'a':  
	      case 'b':  
	      case 'c':  
	      case 'd':  
	      case 'e':  
	      case 'f':  
	       value = (value << 4) + 10 + aChar - 'a';  
	       break;  
	      case 'A':  
	      case 'B':  
	      case 'C':  
	      case 'D':  
	      case 'E':  
	      case 'F':  
	       value = (value << 4) + 10 + aChar - 'A';  
	       break;  
	      default:  
	       throw new IllegalArgumentException(  
	         "Malformed      encoding.");  
	      }  
	     }  
	     outBuffer.append((char) value);  
	    } else {  
	     if (aChar == 't') {  
	      aChar = '\t';  
	     } else if (aChar == 'r') {  
	      aChar = '\r';  
	     } else if (aChar == 'n') {  
	      aChar = '\n';  
	     } else if (aChar == 'f') {  
	      aChar = '\f';  
	     }  
	     outBuffer.append(aChar);  
	    }  
	   } else {  
	    outBuffer.append(aChar);  
	   }  
	  }  
	  return outBuffer.toString();  
	 }  
	 // 测试  
	 public static void main(String[] args) {  
	  AddressUtils addressUtils = new AddressUtils();  
	  // 测试ip 219.136.134.157 中国=华南=广东省=广州市=越秀区=电信  
	  String ip = "47.91.218.103";  
	  String address = "";  
	  try {  
	   address = addressUtils.getAddresses("ip="+ip, "utf-8");  
	  } catch (UnsupportedEncodingException e) {  
	   // TODO Auto-generated catch block  
	   e.printStackTrace();  
	  }  
	  System.out.println(address);  
	  // 输出结果为：广东省,广州市,越秀区  
	 }  
}

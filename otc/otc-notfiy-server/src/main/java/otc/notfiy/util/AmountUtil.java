package otc.notfiy.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

public class AmountUtil {
	static Logger log = LoggerFactory.getLogger(AmountUtil.class);
	/**
	 * <p>获取一个字符串里面最后一个数字【阿拉伯数字】</p>
	 * @param charAt				字符串
	 * @return
	 */
	public static String isNumber( CharSequence charAt ){
		String regex="\\d+(?:\\.\\d+)?";
	    Matcher m=Pattern.compile(regex, Pattern.MULTILINE).matcher(charAt);
	    List<String> result=new ArrayList<String>();
	    while(m.find())
	        result.add(m.group());
	    if(CollUtil.isNotEmpty(result)) 
	    	return CollUtil.getLast(result);
	    return null;
	}
	/**
	 * <p>接收短信收款回调</p>
	 * @param content			短信内容
	 * @return
	 */
	public static String extractMoney(String content) {
		log.info("【正则表达式开始匹配，传入短信内容为："+content+"】");
		Pattern pattern = Pattern.compile("(收到|收款|向你付款|人民币|收入|转账|存入|转入|成功收款|)(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?");
		Matcher matcher = pattern.matcher(content);
		if(matcher.find()){ 
			String tmp=matcher.group();
			String regEx="[^0-9]";  
			Pattern p = Pattern.compile(regEx);  
			Matcher m = p.matcher(tmp);  
			log.info("【截取完毕之后的短信转账金额为："+m.replaceAll("").trim()+"】");
			BigDecimal mount = new BigDecimal(m.replaceAll("").trim());
			mount = mount.divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP);
			log.info(mount.toString());
			return mount.toString();
		}
		return null;
	}
	/**
	 * <p>获取回调内容里面的数据</p>
	 * @param money
	 * @return
	 */
	public static String getNumber( CharSequence money  ) {
		int a = 0;
		boolean flag1 = true;
		for(int i = money.length()-1;i >=0 ;i-- ) {
			char charAt = money.charAt(i);
			if(((charAt >= '0'&& charAt <= '9')||charAt =='.' )) {//數字
				flag1  = false ; 
			} else { 
				if(flag1) {
					a = i-1;
					break;
				}
			}
		}
		for(int i = a-1 ;i >=0; i--) {
			char charAt = money.charAt(i);
			if(!((charAt >= '0'&& charAt <= '9')||charAt =='.' )) {//數字
				a = i;
				break;
			}  
		}
		String subSuf = isNumber(StrUtil.subSuf(money, a));
		return subSuf;
	}
	public static void main(String[] args) {
		String string = "通过扫码向你支付200，通过扫码向你支付20012元";
		String number = isNumber(string);
		System.out.println(number);
		String number2 = getNumber(string);
		System.out.println(number2);
	}
}

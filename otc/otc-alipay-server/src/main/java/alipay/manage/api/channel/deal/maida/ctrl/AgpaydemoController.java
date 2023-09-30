package alipay.manage.api.channel.deal.maida.ctrl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;

import alipay.manage.api.channel.deal.maida.utils.DateUtil;
import alipay.manage.api.channel.deal.maida.utils.HttpUtil;
import alipay.manage.api.channel.deal.maida.utils.SignUtil;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/agpay")
public class AgpaydemoController {
	
 	private Long mchId;
	
 	private String mchKey;
	
 	private String payHost;
	
	 /**
     * <p><b>Description: </b>查询余额
     * <p>2018年10月29日 下午5:23:31
     * @param request
     * @return
     * @throws IOException
     */

    public String queryBalance(HttpServletRequest request) throws IOException {
    	
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("mchId", mchId + "");  //商户ID
		params.put("reqTime", DateUtil.date2Str(new Date(), DateUtil.FORMAT_YYYYMMDDHHMMSS));  //请求时间, 格式yyyyMMddHHmmss
		params.put("version", "1.0");  //版本号, 固定参数1.0
    	params.put("sign", SignUtil.getSign(params, mchKey)); //签名
    	
    	return  HttpUtil.post(payHost + "/api/agentpay/query_balance", genUrlParams(params));
    }
	
	


    public String createOrder(HttpServletRequest request) throws IOException {
    	
    	String mchOrderNo = request.getParameter("mchOrderNo");
    	String amount = request.getParameter("amount");
		String bankName = request.getParameter("bankName");
		String accountName = request.getParameter("accountName");
		String accountNo = request.getParameter("accountNo");

    	if(StringUtils.isEmpty(mchOrderNo) || StringUtils.isEmpty(amount) || StringUtils.isEmpty(accountName) ||
    	   StringUtils.isEmpty(accountNo) || StringUtils.isEmpty(bankName) ){
    		return "参数丢失！";
    	}
    	
    	//金额转换为  分 为单位
    	String amountParam = new BigDecimal(amount).multiply(new BigDecimal(100)).setScale(0).toString();
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("mchId", mchId + "");  // 商户ID
    	params.put("mchOrderNo", mchOrderNo);  // 商户代付单号
    	params.put("amount", amountParam);   // 代付金额（单位分）
    	params.put("accountAttr", "0");   // 账户属性:0-对私,1-对公,默认对私
		params.put("bankName", bankName);   // 银行名称
		params.put("accountName", accountName);   // 收款人账户名
		params.put("accountNo", accountNo);  // 收款人账户号
		params.put("province", "");  // 开户行所在省份
		params.put("city", "");   // 开户行所在市
    	params.put("bankNumber", "");  // 联行号
    	params.put("notifyUrl", "http://localhost:8080/api/agpay/notify");   // 转账结果回调URL
    	params.put("remark", "");  // 备注
    	params.put("extra", "");   // 扩展域
		params.put("reqTime", DateUtil.date2Str(new Date(), DateUtil.FORMAT_YYYYMMDDHHMMSS));  //请求时间, 格式yyyyMMddHHmmss
		params.put("version", "1.0");  //版本号, 固定参数1.0

    	String sign = SignUtil.getSign(params, mchKey);  //签名
    	params.put("sign", sign);
    	
    	return  HttpUtil.post(payHost + "/api/agentpay/apply", genUrlParams(params));
    	
    }
    
    /**
     * <p><b>Description: </b>查询代付订单
     */
    @RequestMapping(value = "/query")
    @ResponseBody
    public String queryOrder(HttpServletRequest request) throws IOException {
    	
    	String mchOrderNo = request.getParameter("mchOrderNo");
    	
    	if(StringUtils.isEmpty(mchOrderNo)){
    		return "参数丢失！";
    	}
    	
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("mchId", mchId + "");  //商户ID
    	params.put("mchOrderNo", mchOrderNo );  // 商户代付单号
    	params.put("agentpayOrderId", "");   // 代付订单号
    	params.put("executeNotify", "false");   //是否执行回调
		params.put("reqTime", DateUtil.date2Str(new Date(), DateUtil.FORMAT_YYYYMMDDHHMMSS));  //请求时间, 格式yyyyMMddHHmmss
		params.put("version", "1.0");  //版本号, 固定参数1.0
    	
    	params.put("sign", SignUtil.getSign(params, mchKey)); //签名
    	
    	return  HttpUtil.post(payHost + "/api/agentpay/query_order", genUrlParams(params));
    }
    
    /**
     * <p><b>Description: </b>接收支付网关-代付接口异步通知
     */
    @RequestMapping(value = "/notify")
    @ResponseBody
    public String notify(HttpServletRequest request) throws IOException {
    	
    	if(StringUtils.isEmpty(request.getParameter("sign"))){   //sign参数 不存在
    		return "fail(sign not exists)";
    	}
    	
    	String resSign = request.getParameter("sign");  //接口返回sign参数值
    	
    	Map<String, Object> paramsMap = new HashMap<String, Object>();
    	paramsMap.put("agentpayOrderId", request.getParameter("agentpayOrderId"));
    	paramsMap.put("status", request.getParameter("status"));
    	paramsMap.put("fee", request.getParameter("fee"));
    	paramsMap.put("transMsg", request.getParameter("transMsg"));
    	paramsMap.put("extra", request.getParameter("extra"));
		paramsMap.put("reqTime", request.getParameter("reqTime"));
    	
    	String sign = SignUtil.getSign(paramsMap, mchKey);   //根据返回数据 和商户key 生成sign
    	
    	//验签
    	if(!resSign.equals(sign)){
    		return "fail(verify fail)";
    	}
    	
    	//处理业务...
    	return "success";
    }

	/**
	 * 代付二次确认接口
	 */
	@RequestMapping(value = "/confirm")
	@ResponseBody
	public String confirm(HttpServletRequest request) {
		String mchOrderNo = request.getParameter("mchOrderNo");
		String amount = request.getParameter("amount");
		// 根据接收的订单号和金额，与自己系统的对比是否一致
		// 如果一致返回 ok  如果不一致返回 fail
		return "ok";
	}

    /**
     * map 转换为  url参数
     * <p><b>Description: </b>
     * <p>2018年9月30日 上午10:12:33
     * @param paraMap
     * @return
     */
    private static String genUrlParams(Map<String, Object> paraMap) {
        if(paraMap == null || paraMap.isEmpty()) return "";
        StringBuffer urlParam = new StringBuffer();
        Set<String> keySet = paraMap.keySet();
        int i = 0;
        for(String key:keySet) {
            urlParam.append(key).append("=").append(paraMap.get(key));
            if(++i == keySet.size()) break;
            urlParam.append("&");
        }
        return urlParam.toString();
    }
    
}

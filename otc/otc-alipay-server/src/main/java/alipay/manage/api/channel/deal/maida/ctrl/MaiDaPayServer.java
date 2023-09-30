package alipay.manage.api.channel.deal.maida.ctrl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;

import alipay.manage.api.channel.deal.maida.utils.DateUtil;
import alipay.manage.api.channel.deal.maida.utils.SignUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.util.StringUtils;
import otc.result.Result;


public class MaiDaPayServer {
	public static final Log log = LogFactory.get();

	private Long mchId;
	private static String mchKey;
	private  static String payHost = "http://119.8.126.49:3020";
	 /**
     * 创建支付订单
     */
    public static Result  createOrder(Map<String,String> map ) throws IOException {
    	String amount = map.get("amount");
    	String productId = map.get("productId");
    	String mchOrderNo = map.get("mchOrderNo");
    	String notifyUrl = map.get("notifyUrl");
    	String returnUrl = map.get("returnUrl");
    	String mchId = map.get("mchId");
    	String mchKey = map.get("mchKey");
    	String payHost = map.get("payHost");
    	if(StringUtils.isEmpty(amount)
				|| StringUtils.isEmpty(productId)
				|| StringUtils.isEmpty(returnUrl)
				|| StringUtils.isEmpty(mchOrderNo)
				|| StringUtils.isEmpty(notifyUrl)
				|| StringUtils.isEmpty(mchId)
				|| StringUtils.isEmpty(mchKey)
				|| StringUtils.isEmpty(payHost)
		){
    		return Result.buildFailMessage("参数丢失！");
    	}
    	//金额转换为  分 为单位
    	String amountParam = new BigDecimal(amount).multiply(new BigDecimal(100)).setScale(0).toString();
    	
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("mchId", mchId + "");  //商户ID
    	params.put("productId", productId);  //支付产品ID
    	params.put("mchOrderNo", mchOrderNo);   //商户订单号
    	params.put("currency", "cny");   //币种
    	params.put("amount", amountParam);   //支付金额
    	params.put("returnUrl",returnUrl);   //支付结果前端跳转URL
    	params.put("notifyUrl", notifyUrl);   //支付结果后台回调URL
    	params.put("subject", "网络购物");  //商品主题
    	params.put("body", "网络购物");   //商品描述信息
		params.put("reqTime", DateUtil.date2Str(new Date(), DateUtil.FORMAT_YYYYMMDDHHMMSS));  //请求时间, 格式yyyyMMddHHmmss
		params.put("version", "1.0");  //版本号, 固定参数1.0
    	String sign = SignUtil.getSign(params, mchKey);  //签名
    	params.put("sign", sign);

		String post = HttpUtil.post(payHost + "/api/pay/create_order", genUrlParams(params));
		log.info(post);
		JSONObject jsonObject = JSONUtil.parseObj(post);
		String retCode = jsonObject.getStr("retCode");
		if("0".equals(retCode)){
			String payUrl = jsonObject.getStr("payUrl");
			return Result.buildSuccessResult(payUrl);
		}
		return  Result.buildFail();
    }
	/**
	 * <p><b>Description: </b>接收支付网关的异步通知
	 * <p>2018年9月30日 上午10:32:02
	 * @param request
	 * @return
	 * @throws IOException
	 */
    public static String notify(HttpServletRequest request,String mchKey) throws IOException {
    	if(StringUtils.isEmpty(request.getParameter("sign"))){   //sign参数 不存在
    		return "fail(sign not exists)";
    	}
    	String resSign = request.getParameter("sign");  //接口返回sign参数值
    	Map<String, Object> paramsMap = new HashMap<String, Object>();
    	paramsMap.put("payOrderId", request.getParameter("payOrderId"));
    	paramsMap.put("mchId", request.getParameter("mchId"));
    	paramsMap.put("appId", request.getParameter("appId"));
    	paramsMap.put("productId", request.getParameter("productId"));
    	paramsMap.put("mchOrderNo", request.getParameter("mchOrderNo"));
    	paramsMap.put("amount", request.getParameter("amount"));
    	paramsMap.put("status", request.getParameter("status"));
    	paramsMap.put("channelOrderNo", request.getParameter("channelOrderNo"));
    	paramsMap.put("channelAttach", request.getParameter("channelAttach"));
    	paramsMap.put("param1", request.getParameter("param1"));
    	paramsMap.put("param2", request.getParameter("param2"));
    	paramsMap.put("paySuccTime", request.getParameter("paySuccTime"));
    	paramsMap.put("backType", request.getParameter("backType"));
    	paramsMap.put("income", request.getParameter("income"));
		paramsMap.put("reqTime", request.getParameter("reqTime"));
		log.info("【回调签名参数："+paramsMap.toString()+"】");
    	String sign = SignUtil.getSign(paramsMap, mchKey);   //根据返回数据 和商户key 生成sign
    	//验签
    	if(!resSign.equals(sign)){
			log.info("【签名失败："+paramsMap.toString()+"】");
    		return "fail(verify fail)";
    	}
    	//处理业务...
    	return "success";
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

	public static void main(String[] args) throws IOException {
		Map  map = new HashMap<>();
		String amount = "3000";
		String productId = "8035";
		String mchOrderNo = System.currentTimeMillis()+"";
		String notifyUrl = "http://sdadasdd.dasdasdsadsdsa.com";
		String returnUrl = notifyUrl;
		String mchId = "20000419";
		String payHost = "20000419";
		map.put("amount",amount);
		map.put("productId",productId);
		map.put("mchOrderNo",mchOrderNo);
		map.put("notifyUrl",notifyUrl);
		map.put("returnUrl",returnUrl);
		map.put("mchId",mchId);
		map.put("payHost","http://119.8.126.49:3020");
		map.put("mchKey","QBXh84gxeooGQb2Je51S9rOmoAAmTC1TP8LgKjb6cKnksFdEnjhbtEab9dv2wsV6oVajPOHUOpjF2650fov6av8gpGoEX94zENBCR4i9mAcmX4w3XEsxd8lAPXXJLjya");
		Result order = createOrder(map);
	}

}

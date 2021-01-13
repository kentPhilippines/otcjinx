package alipay.manage.api.channel.wit;

import alipay.manage.api.channel.util.qiangui.Util;
import alipay.manage.api.config.PayOrderService;
import alipay.manage.api.feign.ConfigServiceClient;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.api.alipay.Common;
import otc.bean.config.ConfigFile;
import otc.bean.dealpay.Withdraw;
import otc.common.PayApiConstant;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@Component(Common.Deal.WITHDRAW_QAINKUI_ALIPAY)
public class QianGuiAlipayDpay extends PayOrderService {
	private static final Log log = LogFactory.get();
	private static final String ORDER_TYPE_ALIPAY = "103";
	private static final String ORDER_TYPE_CARD = "101";
	private static final String QIANGUI_KEY = "YSYKC5XSF8QCDIZX";
	private static final String QIANGUI_APPID = "4C7DBE26AF28F1AE41056A05721F7D15";
	@Autowired
	ConfigServiceClient configServiceClientImpl;

	@Override
	public Result withdraw(Withdraw wit) {
		Result withdraw = super.withdraw(wit);
		if (!withdraw.isSuccess()) {
			return Result.buildFailMessage("代付失败");
		}
		crtOrder(QIANGUI_APPID, wit.getOrderId(), wit.getAmount().doubleValue(), ORDER_TYPE_ALIPAY, wit.getBankNo(), wit.getAccname(), PayApiConstant.Notfiy.NOTFIY_API_WAI + "/qiankuiDpay-notfiy", QIANGUI_KEY);
		return Result.buildSuccessMessage("代付成功等待处理");
	}
	/**
	 * <p>代付请求</p>
	 * @param appId				商户号
	 * @param orderNo			订单号
	 * @param orderAmt			订单金额
	 * @param orderType			订单类型
	 * @param accNo				代付支付宝账号
	 * @param accName			代付支付吧昵称
	 * @param notifyURL			回调通知接口
	 * @param key				交易密钥
	 */
	  public void crtOrder(String appId,String orderNo,Double orderAmt,String orderType,String accNo,String accName,   String notifyURL,String key){
		  Result config = configServiceClientImpl.getConfig(ConfigFile.ALIPAY, ConfigFile.Alipay.SERVER_IP);
		  String ip = config.getResult().toString();
	        HashMap map = new HashMap<>();
	        map.put("appId",appId);
	        map.put("orderNo",orderNo);
	        map.put("orderAmt",orderAmt);
	        map.put("orderType",orderType);//101-银行卡 102-微信 103-支付宝
	        //orderType 102,103时 accNo accName 必传   bankAccNo bankAccName bankName 不传
	        map.put("accNo",accNo);
	        map.put("accName",accName);
	        String sign = Util.creatSign(map, key);//封装签名方法
	        map.put("sign",sign);
	        map.put("notifyURL",ip+notifyURL);
	        String url="http://47.75.223.103:8080/RaccPay/crtOrder.do";//正式环境地址
	        try {
	            String result = HttpUtil.post(url, map);//http请求 返回标准JSON格式
	            //为确认返回未被劫持 如回调进行验签
	            selOrder(appId, orderNo, key);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    public void selOrder(String appId,String orderNo,String key){
	        HashMap map = new HashMap<>();
	        map.put("appId",appId);
	        map.put("orderNo",orderNo);
	        String sign = Util.creatSign(map, key);//封装签名方法
	        map.put("sign",sign);
	        String url="http://47.75.223.103:8080/RaccPay/crtOrder.do";//正式环境地址
	        try {
				String result = HttpUtil.post(url, map);//http请求 返回标准JSON格式
				//为确认返回未被劫持 如回调进行验签
				JSONObject resultJson = JSONUtil.parseObj(result);//转化为JSON对象
				String code = resultJson.getStr("code");//获取返回code  0000为成功 其他为失败
				if ("0000".equals(code)) {
					JSONObject dataJson = JSONUtil.parseObj(resultJson.getStr("data"));//获取返回data数据转化为JSON对象
					String corderNo = dataJson.getStr("orderNo");
					String cappOrderNo = dataJson.getStr("appOrderNo");
					String corderAmt = dataJson.getStr("orderAmt");
					String corderTime = dataJson.getStr("orderTime");
					String corderStatus = dataJson.getStr("orderStatus");
					String csign = dataJson.getStr("sign");
					HashMap cmap = new HashMap<>();
					cmap.put("orderNo", corderNo);
					cmap.put("appOrderNo", cappOrderNo);
	                cmap.put("orderAmt",corderAmt);
	                cmap.put("orderTime",corderTime);
	                cmap.put("orderStatus",corderStatus);
	                String ccsign = Util.creatSign(map, key);
	                if (csign.equals(ccsign)){//验签
	                	log.info("【代付订单正常】");
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    public String notify(HttpServletRequest req, HttpServletResponse res) throws IOException {
	        res.setHeader("Access-Control-Allow-Origin", "*");
	        String key="";//商户对应key
	        String appId=req.getParameter("appId");
	        String orderNo=req.getParameter("orderNo");
	        String appOrderNo=req.getParameter("appOrderNo");
	        String orderAmt=req.getParameter("orderAmt");
	        String orderTime=req.getParameter("orderTime");
	        String orderStatus=req.getParameter("orderStatus");
	        String sign=req.getParameter("sign");
	        HashMap map = new HashMap<>();
	        map.put("appId",appId);
	        map.put("orderNo",orderNo);
	        map.put("appOrderNo",appOrderNo);
	        map.put("orderAmt",orderAmt);
	        map.put("orderTime",orderTime);
	        map.put("orderStatus",orderStatus);
	        String csign = Util.creatSign(map, key);//计算签名
	        if (csign.equals(sign)){
	            //判断签名是否正确
	            //TODO 任意事情
	            return "SUCCESS";//订单没有任何问题 返回SUCCESS
	        }
	        return "Error";
	    }
}

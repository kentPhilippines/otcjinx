package alipay.manage.api.channel.notfiy;

import alipay.manage.api.channel.util.zhaunshi.Config;
import alipay.manage.api.channel.util.zhaunshi.Md5Util;
import alipay.manage.api.channel.util.zhaunshi.StringUtil;
import alipay.manage.api.config.NotfiyChannel;
import cn.hutool.http.HttpUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import otc.common.PayApiConstant;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
@RestController
public class ZuanShiNotfiy extends NotfiyChannel {
    private static final Log log = LogFactory.get();

    @PostMapping("/zuanshiDpay-notfiy")
    public zuanshi notify(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String clientIP = HttpUtil.getClientIP(req);
        String[] ipList = {"34.84.21.215", "34.85.98.150"};
        List<String> asList = Arrays.asList(ipList);
        if (!asList.contains(clientIP))
            return new zuanshi("1111", 1);
        /**
         * 	payout_id			是	String	系统订单号
         payout_cl_id		是	String	商户订单号
         是	String	商户号
		amount				是	Integer	订单金额(分)
		fee					是	Integer	手续费
		status				是	Integer	交易状态
		create_time			是	Integer	创建时间(秒)
		update_time			是	Integer	更新时间(秒)
		sign				是	String	订单签名
     */
		String queryString = req.getQueryString();//获取代付成功数据
		log.info("【代付成功数据集："+queryString+"】");
        String payout_id=req.getParameter("payout_id");
        String payout_cl_id=req.getParameter("payout_cl_id");
        String platform_id=req.getParameter("platform_id");
        String amount=req.getParameter("amount");
        String fee=req.getParameter("fee");
        String status=req.getParameter("status");
        String create_time=req.getParameter("create_time");
        String update_time=req.getParameter("update_time");
        String sign=req.getParameter("sign");
        HashMap map = new HashMap<>();
        map.put("payout_id",payout_id);
        map.put("payout_cl_id",payout_cl_id);
        map.put("platform_id",platform_id);
        map.put("amount",amount);
        map.put("fee",fee);
        map.put("status",status);
        map.put("create_time",create_time);
        map.put("update_time",update_time);
        String generatedSign;
		try {
			generatedSign = Md5Util.md5(StringUtil.convertToHashMapToQueryString(map), Config.KEY);
		} catch (Exception e) {
			return new zuanshi("1111", 1);
		}
        if (sign.equals(generatedSign) && status.equals("3") && platform_id .equals( Config.PLATFORM_ID)){//我方给下游回调  这里要写一个回调的抽象类  全部继承然后同意记录
            //判断签名是否正确
        	Result witNotfy = witNotfy(payout_cl_id,clientIP);
        	if(witNotfy.isSuccess())
        		log.info("【代付通知成功】");
            //TODO 任意事情
            return new zuanshi("0000", 3);//订单没有任何问题 返回SUCCESS
        }
    	return new zuanshi("1111", 1);
    }
}
class zuanshi{
	private String error_code;
	private Integer status;
	public String getError_code() {
		return error_code;
	}
	public void setError_code(String error_code) {
		this.error_code = error_code;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public zuanshi(String error_code, Integer status) {
		super();
		this.error_code = error_code;
		this.status = status;
	}

}

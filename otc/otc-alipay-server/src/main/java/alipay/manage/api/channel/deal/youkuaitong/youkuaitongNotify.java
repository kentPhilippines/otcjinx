package alipay.manage.api.channel.deal.youkuaitong;

import alipay.manage.api.channel.util.shenfu.PayUtil;
import alipay.manage.api.config.NotfiyChannel;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import otc.common.PayApiConstant;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
@RestController
public class youkuaitongNotify extends NotfiyChannel {
    private static final Log log = LogFactory.get();

    @RequestMapping("/youkuaitong-wit-noyfit")
    public String notify(HttpServletRequest req, HttpServletResponse res ) {
        String clientIP = HttpUtil.getClientIP(req);

        log.info("【当前回调ip为：" + clientIP + "】");
        log.info("【收到优快通支付回调请求：" + clientIP + "】");
        Map mapIp = PayUtil.ipMap;
        Object object = mapIp.get(clientIP);
        if (ObjectUtil.isNull(object)) {
            log.info("【当前回调ip为：" + clientIP + "，固定IP登记为：" + mapIp.toString() + "】");
            log.info("【当前回调ip不匹配】");
            return "ip errer";
        }
        /**
         * FinishTime	string	订单完成时间，格式yyyyMMddHHmmss	是
         * MerchantId	string	您的商户ID，原样返回	是
         * MerchantUniqueOrderId	string	商户唯一订单ID，原样返回	是
         * Others	string	创建订单时传递的Others字段，原样返回
         * 请注意，创建时此字段可空可不传，创建时不参与签名，但通知时总会存在此参数，且参与签名	是
         * Solt	string	16-32位随机字符串	是
         * Status	string	代付状态
         * 100 代付成功
         * -90 代付失败	是
         * WithdrawOrderId	string	平台方系统代付订单号	是
         * Sign	string	平台方计算出的签名
         */
        String FinishTime = req.getParameter("FinishTime");
        String MerchantId = req.getParameter("MerchantId");
        String MerchantUniqueOrderId = req.getParameter("MerchantUniqueOrderId");
        String Solt = req.getParameter("Solt");
        String Status = req.getParameter("Status");
        String WithdrawOrderId = req.getParameter("WithdrawOrderId");
        String sign = req.getParameter("Sign");
        Map<String, Object> map = new HashMap<>();
        map.put("FinishTime", FinishTime);
        map.put("MerchantId", MerchantId);
        map.put("MerchantUniqueOrderId", MerchantUniqueOrderId);
        map.put("Solt", Solt);
        map.put("Status", Status);
        map.put("WithdrawOrderId", WithdrawOrderId);
        String Timestamp = DateUtil.format(new Date(), DatePattern.PURE_DATETIME_MS_PATTERN);
        Map<String,Object> query1 = new HashMap<>();
        query1.put("MerchantId","63290");
        query1.put("MerchantUniqueOrderId",MerchantUniqueOrderId);
        query1.put("Timestamp",Timestamp);
        String createParam = PayUtil.createParam(query1);
        createParam.toLowerCase();
        System.out.println("优快通代付参数"+createParam);
        String mm = PayUtil.md5(createParam + "k4j4mOok99rbKD4eazdNX2nCJ212h64uYMq");
        query1.put("Sign",mm);
        String aaa = HttpUtil.post( "https://service.api.6izeprtw4j1i0a.bdqwx.xyz/InterfaceV6/QueryWithdrawOrder/",query1);
        System.out.println(aaa);
        JSONObject jsonObject1 = JSONUtil.parseObj(aaa);
        String WithdrawOrderStatus = jsonObject1.getStr("WithdrawOrderStatus");
        String MerchantUniqueOrderId1 = jsonObject1.getStr("MerchantUniqueOrderId");
        if(MerchantUniqueOrderId.equals(MerchantUniqueOrderId1) && "100".equals(WithdrawOrderStatus)){
            log.info("支付成功:"+MerchantUniqueOrderId1);
            Result result = witNotfy(MerchantUniqueOrderId, clientIP);
            if (result.isSuccess()) {
                return "success";
            }else {
                witNotSuccess(MerchantUniqueOrderId);
            }
        }
        return "end errer";
    }
}

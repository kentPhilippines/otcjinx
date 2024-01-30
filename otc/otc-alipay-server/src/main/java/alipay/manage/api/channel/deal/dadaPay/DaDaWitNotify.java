package alipay.manage.api.channel.deal.dadaPay;

import alipay.manage.api.channel.util.shenfu.PayUtil;
import alipay.manage.api.config.NotfiyChannel;
import alipay.manage.mapper.WithdrawMapper;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import otc.bean.dealpay.Withdraw;
import otc.common.PayApiConstant;
import otc.result.Result;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
@RestController
public class DaDaWitNotify  extends NotfiyChannel {
    private static final Log log = LogFactory.get();
    @Resource
    private WithdrawMapper withdrawDao;

    public DaDaWitNotify(WithdrawMapper withdrawDao) {
        this.withdrawDao = withdrawDao;
    }
    String key  = "RDBECCGUN2OQ6NMS8WUDOZGAXME9UYPA0RJPTR89COWMISQ2X2JWHRE4YHX2FMCUGOPVF5KGOIO7ZKWXOCNDIFKIWPDK9LGI4OWJCOTDWFTWU1KVZ3KLTDONCD364FC3";

    @RequestMapping("/dadaWit-noyfit")
    public String notify(HttpServletRequest req, HttpServletResponse res ) {
        String clientIP = HttpUtil.getClientIP(req);
        log.info("【当前回调ip为：" + clientIP + "】");
        Map mapIp = PayUtil.ipMap;
        Object object = mapIp.get(clientIP);
        if (ObjectUtil.isNull(object)) {
            log.info("【当前回调ip为：" + clientIP + "，固定IP登记为：" + mapIp.toString() + "】");
            log.info("【当前回调ip不匹配】");
            return "ip errer";
        }
        String agentpayOrderId = req.getParameter("agentpayOrderId");
        String status = req.getParameter("status");
        String fee = req.getParameter("fee");
        String sign = req.getParameter("sign");
        Map<String, Object> map = new HashMap<>();
        map.put("agentpayOrderId",agentpayOrderId);
        map.put("status",status );
        map.put("fee", fee);
        log.info("【dada付代付签名前参数：" + map + "】");
        Withdraw wit =      withdrawDao.findHash(agentpayOrderId);
        String createParam = PayUtil.createParam(map);
        log.info(createParam);
        String md5 = PayUtil.md5(createParam + "&key="+key).toUpperCase(Locale.ROOT);
        if (sign.equals(md5)) {
            if ("2".equals(status)) {
                Result result = witNotfy(wit.getOrderId(), clientIP);
                if (result.isSuccess()) {
                    return "success";
                }
            }else {
                witNotSuccess(agentpayOrderId);
            }
        } else {
            return "error";
        }
        return "end errer";
    }




}

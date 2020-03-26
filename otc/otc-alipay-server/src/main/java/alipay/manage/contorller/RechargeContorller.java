package alipay.manage.contorller;

import alipay.manage.bean.Recharge;
import alipay.manage.bean.UserInfo;
import alipay.manage.service.OrderService;
import alipay.manage.util.LogUtil;
import alipay.manage.util.SessionUtil;
import cn.hutool.core.util.ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/recharge")
public class RechargeContorller {
    Logger log = LoggerFactory.getLogger(RechargeContorller.class);
    @Autowired SessionUtil sessionUtil;
    @Autowired OrderService orderFacade;
    @Autowired LogUtil logUtil;

    /**
     * <p>获取可用的充值渠道</p>
     * <p>这里的渠道就是自营产品</p>
     * @return
     */
    @RequestMapping("/findEnabledPayType")
    @ResponseBody
    public Result findEnabledPayType() {
//        List<Paytype> list = payTypeServiceImpl.findPayTypeAll();
        return Result.buildSuccessMessage(null);
    }

    /**
     * 充值生成订单
     * @param param
     * @param request
     * @return
     */
    @PostMapping("/generateRechargeOrder")
    @ResponseBody
    public Result generateRechargeOrder(Recharge param, HttpServletRequest request) {
        UserInfo user = sessionUtil.getUser(request);
        if(ObjectUtil.isNull(user))
            return Result.buildFailMessage("当前用户未登陆");
        //获取当前登录  账号
        boolean flag = false;
        /**
         *   <p>充值参数</p>
         *   qrUserId  : 充值人账号
         *   createTime　： 充值时间
         *   depositor ： 存款姓名
         *   amount ： 充值金额
         *   qrRechargeType ： 充值类型
         *   mobile : 充值手机号
         *   JsonResult   :
         *   		 success:    true 充值成功     false 失败     默认失败
         *  		 result :    充值链接
         */
        Map<String,String> params = new HashMap<String,String>();
        params.put("amount", param.getAmount().toString());
        params.put("depositor", param.getDepositor().toString());
        params.put("qrUserId", user.getUserId());
        params.put("qrRechargeType", param.getRechargeType().toString());
        params.put("mobile", param.getPhone());
        String msg = "码商发起充值操作,当前提现参数：提现金额："+params.get("amount")+"，充值人姓名："+param.getDepositor().toString()+
                "，关联码商账号："+user.getUserId()+"，充值手机号："+ param.getPhone();
        boolean addLog = logUtil.addLog(request, msg, user.getUserId());
        log.info("获取addLog"+addLog);
        Map<String, Object> jsonResult = orderFacade.createRechangeOrder(params);
        flag = (boolean) jsonResult.get("success");
        if(flag) {
            String payurl = (String) jsonResult.get("result");
            //return JsonResult.buildSuccessResult();
            return Result.buildSuccessResult("支付订单获取成功", payurl);
        }else{
            return null;
        }
    }

}

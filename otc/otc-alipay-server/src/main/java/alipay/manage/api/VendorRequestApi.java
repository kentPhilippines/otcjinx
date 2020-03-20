package alipay.manage.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;

@RestController
public class VendorRequestApi {

    /**
     * 商户下单支付请求接口
     * @param request
     * @return
     */
    @PostMapping("/pay")
    public Result pay(HttpServletRequest request){
        String userId = request.getParameter("userId");//商户号


        return null;
    }


    /**
     * 商户下单提现接口
     * @param request
     * @return
     */
    @PostMapping("/merchant/withdrawal")
    public Result withdrawal(HttpServletRequest request){
        String userId = request.getParameter("userId");//商户号



        return null;
    }



}

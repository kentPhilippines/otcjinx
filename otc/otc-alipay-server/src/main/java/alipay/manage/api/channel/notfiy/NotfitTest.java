package alipay.manage.api.channel.notfiy;

import alipay.manage.api.config.NotfiyChannel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import otc.common.PayApiConstant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
@RestController
public class NotfitTest extends NotfiyChannel {

    @PostMapping("/test-notify")
    public void notify(HttpServletRequest req, HttpServletResponse res) {
        dealpayNotfiy(req.getParameter("orderId"), "127.0.0.1");
        // dealpayNotfiy("C1598976190085918587" ,  "127.0.0.1");
        // dealpayNotfiy("C1598976288678789409" ,  "127.0.0.1");
    }

}

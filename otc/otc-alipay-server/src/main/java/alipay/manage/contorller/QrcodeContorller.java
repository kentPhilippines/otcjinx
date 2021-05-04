package alipay.manage.contorller;

import alipay.config.redis.RedisUtil;
import alipay.manage.bean.DealOrder;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.util.PageResult;
import alipay.manage.service.MediumService;
import alipay.manage.service.OrderService;
import alipay.manage.service.WithdrawService;
import alipay.manage.util.QueueUtil;
import alipay.manage.util.SessionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import otc.bean.alipay.Medium;
import otc.common.PayApiConstant;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/qrcode")
public class QrcodeContorller {
    private static final String MARS = "SHENFU";
    private static final String MARK = ":";
    @Autowired
    private SessionUtil sessionUtil;
    @Autowired
    private MediumService mediumServiceImpl;

    @GetMapping("/findIsMyQrcodePage")
    @ResponseBody
    public Result findIsMyQrcodePage(HttpServletRequest request, String pageNum, String pageSize) {
        UserInfo user = sessionUtil.getUser(request);
        if (ObjectUtil.isNull(user)) {
            return Result.buildFailResult("用户未登录");
        }
        PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
        List<Medium> qmList = mediumServiceImpl.findIsMyMediumPage(user.getUserId());
        //List<QrCode> qrList = qrCodeServiceImpl.findIsMyQrcodePage(qrcode);
        PageInfo<Medium> pageInfo = new PageInfo<Medium>(qmList);
        PageResult<Medium> pageR = new PageResult<Medium>();
        pageR.setContent(pageInfo.getList());
        pageR.setPageNum(pageInfo.getPageNum());
        pageR.setTotal(pageInfo.getTotal());
        pageR.setTotalPage(pageInfo.getPages());
        return Result.buildSuccessResult(pageR);
    }
    /**
     * <p>远程队列入列</p>
     * @param request
     * @param id
     * @return
     */
    @GetMapping("/updataMediumStatusSu")
    @ResponseBody
    public Result updataMediumStatusSu(HttpServletRequest request, String id) {
        UserInfo user = sessionUtil.getUser(request);
        if (ObjectUtil.isNull(user)) {
            return Result.buildFailResult("用户未登录");
        }
        Medium med = new Medium();
        med.setId(Integer.valueOf(id));
        Result addNode = queueUtil.addNode(med);
        return addNode;
    }

    @Autowired
    private QueueUtil queueUtil;
    @Autowired
    private OrderService orderServiceImpl;
    @Autowired
    private WithdrawService withdrawService;
    @Autowired
    private RedisUtil redis;

    @GetMapping("/getBankCardList")
    @ResponseBody
    public Result getBankCardList(HttpServletRequest request, String id) {
        UserInfo user = sessionUtil.getUser(request);
        if (ObjectUtil.isNull(user)) {
            return Result.buildFailResult("用户未登录");
        }
        List<Medium> isMyMediumPage = mediumServiceImpl.findIsMyMediumPage(user.getUserId());
        return Result.buildSuccessResult(isMyMediumPage);
    }

    /**
     * <p>远程队列出列</p>
     *
     * @param request
     * @param id
     * @return
     */
    @GetMapping("/updataMediumStatusEr")
    @ResponseBody
    public Result updataMediumStatusEr(HttpServletRequest request, String id) {
        UserInfo user = sessionUtil.getUser(request);
        if (ObjectUtil.isNull(user)) {
            return Result.buildFailResult("用户未登录");
        }
        Medium med = new Medium();
        med.setId(Integer.valueOf(id));
        Result addNode = queueUtil.pop(med);
        return addNode;
    }

    @GetMapping("/setBankCard")
    @ResponseBody
    public Result setBankCard(HttpServletRequest request, String bankCard, String orderId) {
        UserInfo user = sessionUtil.getUser(request);
        if (ObjectUtil.isNull(user)) {
            return Result.buildFailResult("用户未登录");
        }
        DealOrder order = orderServiceImpl.findOrderByOrderId(orderId);
        String mediumNumber = "";
        String mediumHolder = "";
        String account = "";
        String mediumPhone = "";
        if (StrUtil.isEmpty(order.getOrderQr())) {
            Medium mediumId = mediumServiceImpl.findMediumId(bankCard);
            mediumNumber = mediumId.getMediumNumber();//卡号
            mediumHolder = mediumId.getMediumHolder();//开户人
            account = mediumId.getAccount();//开户行
            mediumPhone = mediumId.getMediumPhone();
            String bankInfo = "";
            bankInfo = account + MARK + mediumHolder + MARK + mediumNumber + MARK + "电话" + MARK + mediumPhone;
            orderServiceImpl.updateBankInfoByOrderId(bankInfo, orderId);
        } else {
            String[] split = order.getOrderQr().split(MARK);
            mediumNumber = split[2];//卡号
            mediumHolder = split[1];//开户人
            account = split[0];//开户行
            mediumPhone = split[3];
        }
        Map cardmap = new HashMap();
        cardmap.put("bank_name", account);
        cardmap.put("card_no", mediumHolder);
        cardmap.put("card_user", mediumNumber);
        cardmap.put("money_order", order.getDealAmount());
        cardmap.put("no_order", orderId);
        cardmap.put("oid_partner", orderId);
        redis.hmset(MARS + orderId, cardmap, 6000);
        return Result.buildSuccessResult(PayApiConstant.Notfiy.OTHER_URL + "/pay?orderId=" + orderId + "&type=203");
    }
}

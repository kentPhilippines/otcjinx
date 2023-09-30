package alipay.manage.api.V2;

import alipay.manage.api.V2.vo.OrderRequestVo;
import alipay.manage.api.deal.QueryV2Api;
import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/v2/deal")
@AllArgsConstructor
/**
 * 订单查询
 */
@CrossOrigin
public class OrderQueryApi1 {
    private QueryV2Api queryV2Api;


    /**
     * 查询订单状态
     *
     * @param orderRequestVo
     * @return
     */
    @PostMapping("/query/order")
    public Result order(HttpServletRequest request, @RequestBody @Valid OrderRequestVo orderRequestVo) {
        log.info("v2 order request:{}", JSONUtil.toJsonStr(orderRequestVo));
        try {
            Result result = queryV2Api.queryOrder(orderRequestVo);
            log.info("v2 order response:{}", JSONUtil.toJsonStr(result));
            return result;
        } catch (Throwable e) {
            return Result.buildFailMessage("查询异常");
        }
    }
}

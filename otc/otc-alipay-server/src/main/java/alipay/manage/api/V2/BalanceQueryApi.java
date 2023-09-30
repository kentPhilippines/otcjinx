package alipay.manage.api.V2;

import alipay.manage.api.V2.vo.BalanceRequestVo;
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
 * 账户查询
 */
@CrossOrigin
public class BalanceQueryApi {
    private QueryV2Api queryV2Api;

    /**
     * 查询账户余额
     *
     * @param balanceRequestVo
     * @return
     */
    @PostMapping("/query/balence")
    public Result balence(HttpServletRequest request, @RequestBody @Valid BalanceRequestVo balanceRequestVo) {
        log.info("v2 balence request:{}", JSONUtil.toJsonStr(balanceRequestVo));
        try {
            Result result = queryV2Api.queryBalance(balanceRequestVo);
            log.info("v2 balence response:{}", JSONUtil.toJsonStr(result));
            return result;
        }catch (Throwable e ){
            return Result.buildFailMessage("查询异常");
        }
    }


}

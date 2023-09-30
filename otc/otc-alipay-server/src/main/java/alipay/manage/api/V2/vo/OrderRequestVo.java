package alipay.manage.api.V2.vo;

import lombok.Data;
import otc.util.MapUtil;
import otc.util.RSAUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.Map;

import static alipay.manage.util.bankcardUtil.CreateOrder.log;

@Data
public class OrderRequestVo {
    /**
     * 商户号
     */
    @NotEmpty(message = "商户号userId不能为空")
    @Size(min = 1, max = 24, message = "商户号appId长度在{min}-{max}")
    private String userId;
    /**
     * 订单号，必须唯一
     */
    @NotEmpty(message = "订单号orderId不能为空")
    @Size(min = 1, max = 128, message = "订单号orderId长度在{min}-{max}")
    private String orderId;
    /**
     * 查询订单类型
     * pay 为充值订单
     * wit 为代付订单
     */
    @NotEmpty(message = "查询订单类型不能为空")
    @Size(min = 1, max = 12, message = "查询订单类型长度在{min}-{max}")
    private String type;
    /**
     * 签名
     * 签名字符串加密，加密方式参考平台方Demo
     * 签名规则如下：
     * 1.将请求参数以k-v形式串接成QueryString，并依ASCII由小至大排序
     * 2.于句尾加上平台钥后密进行MD5运算(以密钥123456为例，于句尾加上”123456”)
     * 3.将此字串作为sign字段带在请求参数中
     * 提供范例：
     * 签名前请求串：userId=AsgRTDFY123456
     * 取得结果：
     * 949a96e061e61b76dc300bb21b65403c
     */
    @NotEmpty(message = "签名sign不能为空")
    private String sign;


    public Boolean isSign(String password) {
        Map map = new HashMap<>();
        map.put("userId", this.userId);
        map.put("orderId", this.orderId);
        map.put("type", this.type);
        map.put("sign", this.sign);
        boolean verifySign = verifySign(map, password);
        return verifySign;
    }
    public static boolean verifySign(Map<String, Object> map, String key) {
        String paramStr = MapUtil.createParam(map);
        log.info("【验证签名前的参数为：" + paramStr.toString() + "】");
        String md5 = RSAUtils.md5(paramStr + key);
        Object oldmd5 = map.get("sign");
        if (!oldmd5.toString().equalsIgnoreCase(md5)) {
            log.info("【当前用户验签不通过】");
            log.info("【请求方签名值为：" + oldmd5 + "】");
            log.info("【我方验签值为：" + md5 + "】");
            return false;
        }
        return true;
    }
}

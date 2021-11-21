package test.number.channal;

import alipay.manage.api.channel.util.shenfu.PayUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class youhuaitong {


    public static void main(String[] args) {
        /**
         *··Amount	                是	string	订单金额	单位元，建议格式#.##
         * BankCardBankName	        是	string	银行中文名称	中文名，我方智能匹配 无需url编码
         * BankCardNumber	        是	string	银行卡号
         * BankCardRealName	        是	string	银行卡户名	无需url编码
         * MerchantId	            是	string	商户ID	见开户文档
         * MerchantUniqueOrderId	是	string	商户唯一订单ID	每次提交不可重复，建议使用guid、uuid或时间戳加随机数最大长度32
         * NotifyUrl	            是	string	通知回调地址（默认不启用）	可传空字符串
         * Timestamp	            是	string	时间戳	您服务器当前时间戳，+8时区，格式为yyyyMMddHHmmss
                                                                             例如 2019 年 1 月 2 日 3 时 4 分 5 秒则应传递：20190102030405
         * WithdrawTypeId	        是	string	下发类型	0 通用模式
         * Sign	                    是	string	签名	详见签名章节
         */
        String Amount = "3000.00";
        String BankCardBankName = "中国银行";
        String MerchantId = "63290";
        String MerchantUniqueOrderId = UUID.randomUUID().toString();
        String NotifyUrl = "www.baidu.com";
        String BankCardNumber = "655356374892373892";
        String Timestamp = DateUtil.format(new Date(), DatePattern.PURE_DATETIME_MS_PATTERN);
        String ReturnUrl = "www.baidu.com";
        String WithdrawTypeId = "0";
        String BankCardRealName = "高见";
        Map<String,Object> map = new HashMap<>();
        map.put("Amount",Amount);
        map.put("WithdrawTypeId",WithdrawTypeId);
        map.put("MerchantId",MerchantId);
        map.put("MerchantUniqueOrderId",MerchantUniqueOrderId);
        map.put("NotifyUrl",NotifyUrl);
        map.put("BankCardBankName",BankCardBankName);
        map.put("BankCardNumber",BankCardNumber);
        map.put("BankCardRealName",BankCardRealName);
        map.put("Timestamp",Timestamp);
        String createParam = PayUtil.createParam(map);
        String s = createParam.toLowerCase();
        System.out.println(s);
        String md5 = PayUtil.md5(createParam + "k4j4mOok99rbKD4eazdNX2nCJ212h64uYMq");
        map.put("sign", md5);
        System.out.println("请求参数："+map.toString());
        String post = HttpUtil.post("https://service.api.6izeprtw4j1i0a.bdqwx.xyz/InterfaceV5/CreateWithdrawOrder/", map);
        System.out.println(post);
    }
}

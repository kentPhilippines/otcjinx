package test.number.channal;

import alipay.manage.api.channel.util.xianyu.XianYuUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;

import java.util.HashMap;
import java.util.Map;

public class 穿山甲 {

    private  final  static  String  privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDXleYMd6O20JyF4V45pNxJ4+zHb8+LKqm/ujVWor/JvFshLwpXOn6Qj2kYHa3qslMCQVySzr4DKLMM2SSxN6HnDYURvGBj2N8pYW5xNtsOzAp8mHykQV0w59lVV+a9Hv3NUnqf8vyqLMWvcaWoxp52QyK+Eh0B9KEkozwQcaI4m7ceCu6rJTNJ21tZK3/S3KA13mT0wJ87So2MK3itY213mm4mn/1n5AA8OzEoyo+kG84K8hX3RDLeIJI4g5cIarUlh/qcBxH3Tqx/wn4iLyhKabUdDJHxUobUsLgrD97yVj8fD/cL8Kiuf2bhPnqWh1gQbd32ZBrBaYwHNSleg3apAgMBAAECggEANOsnsBHCDwMAGODsNywfosee/ImWoWuUhWqY4y5J86QXnnqkiCrknGmHe5L1ePHS7G/M4IB9JdtcsB+xIQst48Bu1J6v7nJyvP6clI/Jj8VIIbNwjQU6ceHHeGp+ShgMZtUDIrYLjV42HE3CQz6V8SOjqJJbqno4//xNpx+tmUU0hX5I5nL+x3yFS/QyiOyhG4wyg7aCeYbaP1OPmsF5suLNd1QPOAMihP+oKXPdD079VJtw/Xtgfm72d6eggWfQygQgZEAGfuVEW2IKfyGyBRyy0nPd4h9nb1x0AQEex7Gpz7novMV+uLHRKX64IDEIlBpMP2QjRf7OyskUYHd3gQKBgQDrQLGujDJDO+WfNuOK+0/yKztC185vuyugy8muP0VcznNW4L53orIWws4Ae+UwY8VbFcDbAuMBbuSmAzDFIWt1l4gZzayqfi+8t9L5XuzbQWsmPhkPqyFK5NK52jOUFtSuU27D3KqaNce65YrWoKlmUOxkEk/6xJ2SwWPw3Dem8QKBgQDqmS2sUiDcQCFbb6T+pItTomIcHXiGKiapj91oXcXlsEo00nvoBNfdeTNwBnDQTEGPdxj8DMToRHOOyVE53VEKV7xmBxP5p9CTH1eMACgjAr6uths1KF2r7H7LETRkj4c0890G91mbOSJ0FxMkq4RGjH04vwKP00LmAgs5BoH7OQKBgFug6RPpOzKAw+ENifb6B5t1RzGzyI4wQr/wX+9kaWnKZ5YxFz6VufOvkHkTyKLeWWIuyN0E9NH8FUar+3TsnWRBzxrtxxDo8UL5/kxusqZ7hnZwvWYi62a2VXVaDbjiY5g6muviqPhDdjXPbhJGpXRalJgtocU0i4M7m+eWfn6hAoGBANTzyxPbUVzdEeqQQ7Oq5ZY7ltdyoQ8YgBP3NcIhLRy8k/+y+Sq6CsFN3bVZA1rxfamfMJzLcopsaIE6mXLvRTsgPTJYRnefL6P9FVlOYyC0wyaQw83TIISJnubybR7DcrZMj1xdd4eBq5a5w3TMBLyNlrXGHas02Es1m75d++txAoGANcMTEFaZpW/qnKaNCltimrkev2tDVkJO1dLo+/jcqRnDu5iBm/i+xoOUYssFSjo9R+WpcaoEQCcD0dRSM7o5zqP+ykkgYlCp1VxMUi71tsV+cfYgSFA2jzO47Zb2QjdOPgqe/y4XxwCXfp1PLJD0n+SyrwJgZWADbrW97QkptJk=";
    private  final  static  String  publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAriqb6/wy43hyWljbW5Bs0ZyrLHfmSijMNz0hX+rdsHHG59izADHn0wphFvFVWvcU2sEHxUaEcpjIaFqL06/imdSZFWAWu677je45rYvE+0mu6YkyDjdkvmYLfKyAfYbcWjG6RRIH2dU6ULZd9tYWM29qAQnsW9y5BSNNQlsQm8e0Q6zPT7LL091f1HnJmFCVvC0pkK/a2Tz9tldjmOVQSM5+auz/lgVFh5Xb8fOnK+ofumPpTfA3Ej9xGZJVAD522VTg9jgycDm5Ta10T0WFtme275cLdj7R5/DsRBYj4c7478nDXfqvjQVVhs5YQ0vHArZ7w3nb/OK2cfJg+FOvqwIDAQAB";

    public static void main(String[] args) {
        String fxnotifyurl = "https://www.qupay88.com/Pay";
        String fxbackurl = "https://www.qupay88.com/Pay";
        String fxattch = "test";
        String fxdesc = "desc";
        String fxfee = 500+"";
        String fxpay = "zfbwap2";
        String fxddh =  "J202112141318222ss1092249100";
        String fxid =  "100727";
        String key = "TVDOAtYWrafwhzbzZXVCIklmbmUnqPAd";
        //订单签名
        String fxsign = XianYuUtil.md5(fxid + fxddh + fxfee + fxnotifyurl + key);
        fxsign = fxsign.toLowerCase();
        Map<String, Object> reqMap = new HashMap<String, Object>();
        reqMap.put("fxid", fxid);
        reqMap.put("fxddh", fxddh);
        reqMap.put("fxfee", fxfee);
        reqMap.put("fxpay", fxpay);
        reqMap.put("fxnotifyurl", fxnotifyurl);
        reqMap.put("fxbackurl", fxbackurl);
        reqMap.put("fxattch", fxattch);
        reqMap.put("fxnotifystyle", "1");
        reqMap.put("fxdesc", fxdesc);
        reqMap.put("fxip", "127.0.0.1");
        reqMap.put("fxsign", fxsign);
        System.out.println("【穿山甲请求参数：" + reqMap.toString() + "】");

        // 支付请求返回结果
        String result = null;
        result = HttpUtil.post( "https://www.qupay88.com/Pay", reqMap);
        System.out.println(result);
    }
}

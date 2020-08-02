package test.number.channal;

public class shunyi {
    public static void main(String[] args) {
        test();
    }

    private static void test() {
        /**
         * oid_partner String(18)       参数名称：商家号
         * notify_url String(128)       参数名称：服务器异步通知地址
         * user_id String(32) √         该用户在商户系统中的唯一编号，要求是该 编号在
         * sign_type String(10) √       取值为：MD5
         * sign String √                参数名称：校验数据取值
         * no_order String(32) √        参数名称：商家订单号
         * time_order Date √            参数名称：商家订单时间 时间格式：YYYYMMDDH24MISS 14 位数 字，精确到
         * money_order Number(13,2) √   参数名称：客户实际支付金额与币种对应 （用户实 际支付金额以异步通知为准。）
         * name_goods String(40) √      参数名称：商品名称
         * pay_type String(5) √
         * 参数名称：支付类型
         * 取值如下
         * 41：微信扫码
         * 48：支付宝扫码
         * 49：支付宝扫码（定额）
         * （用户实际支付金额以异步通知为准。）
         */
    }
}

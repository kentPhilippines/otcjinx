package test.number;

import alipay.manage.util.bankcardUtil.BankTypeUtil;

public class bankTest {

    public static void main(String[] args) {
        String 建设银行 = BankTypeUtil.replaceBank("邮储银行", "563784738273983");
        String[] split = "563784738273983".split(建设银行);
        for (String a : split)
            System.out.println(a);
    }
}

package test.number;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

import java.util.Date;

public class py {

    public static void main(String[] args) {

        DateTime parse = DateUtil.parse("2022-06-07 19:59:22");

        String format = DateUtil.format(parse, "yyyy-MM-dd HH:mm:ss");
        System.out.println(DateUtil.format(parse,"yyyy-MM-dd HH:mm:ss"));

        boolean expired = DateUtil.isExpired(DateUtil.parse(format) , DateField.MINUTE, 10, new Date());
        System.out.println(expired);

    }
}

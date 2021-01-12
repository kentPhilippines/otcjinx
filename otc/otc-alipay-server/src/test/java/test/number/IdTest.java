package test.number;

import cn.hutool.core.lang.UUID;

public class IdTest {
    public static void main(String[] args) {
        String s = UUID.randomUUID().toString(true);
        System.out.println(s);


    }
}

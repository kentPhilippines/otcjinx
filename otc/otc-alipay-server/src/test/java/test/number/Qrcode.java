package test.number;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;

import java.sql.SQLException;

public class Qrcode {
    public static void main(String[] args) throws SQLException {
        Long aLong = Db.use().insertForGeneratedKey(

                Entity.create("alipay_usdt_order")
                        .set("blockNumber", "11111212321321")
                        .set("timeStamp", "2020-11-11 11:11:11")
                        .set("hash", "11111212321321")
                        .set("blockHash", "11111212321321")
                        .set("fromAccount", "11111212321321")
                        .set("contractAddress", "11111212321321")
                        .set("toAccount", "11111212321321")
                        .set("value", "11111212321321")
                        .set("tokenName", "11111212321321")
                        .set("tokenSymbol", "11111212321321")
                        .set("fromNow", "11111212321321")
                        .set("toNow", "11111212321321"));
        System.out.println(aLong);
    }


}

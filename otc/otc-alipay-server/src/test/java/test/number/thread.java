package test.number;

import otc.bean.dealpay.Withdraw;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class thread {

	public static void main(String[] args) {
            BigDecimal orderAmount = new BigDecimal(200);
            List<Withdraw> witList = new ArrayList<>();
            BigDecimal lastnumber = BigDecimal.ZERO;//上一个金额
            BigDecimal number = BigDecimal.ZERO;//当前金额

            Withdraw q1 = new Withdraw();
            q1.setAmount(new BigDecimal( 180));
            witList.add(q1);
            Withdraw q2 = new Withdraw();
            q2.setAmount(new BigDecimal( 199));
            witList.add(q2);
            Withdraw q3 = new Withdraw();
            q3.setAmount(new BigDecimal( 220));
            witList.add(q3);
            Withdraw q4 = new Withdraw();
            q4.setAmount(new BigDecimal( 330));
            witList.add(q4);
            Withdraw q5 = new Withdraw();
            q5.setAmount(new BigDecimal( 100));
            witList.add(q5);


            for(Withdraw wit : witList){
                    if(wit.getAmount().compareTo(orderAmount) == -1 ){// 当前金额小于  对当前金额金额记录
                            lastnumber = wit.getAmount();
                    }
                    if(wit.getAmount().compareTo(orderAmount) == 1 ){// 当前金额大于  对当前金额金额记录
                            number = wit.getAmount();
                    }
                    if(lastnumber.compareTo( BigDecimal.ZERO) != 0  && number.compareTo( BigDecimal.ZERO) != 0){
                            //当前选出的金额是合适的
                            System.out.println(lastnumber + " ------" + number);
                            return;
                    }
            }
    }


}


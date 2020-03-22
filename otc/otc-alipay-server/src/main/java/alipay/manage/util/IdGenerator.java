package alipay.manage.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ID生成器
 */
public enum IdGenerator {
    INSTANCE;
    private Integer posBandCurrent;
    private IdGenerator(){
        posBandCurrent = 1000;
    }
    /**
     * 子订单表
     * @return
     */
    public String orderSnoCreate() {
        String FORMAT1 = "yyyyMMddHHmmssSSS";
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT1);
        String tranDate = sdf.format(new Date());
        StringBuffer sb = new StringBuffer("BOD").append("01");
        if (posBandCurrent > 9998)
            posBandCurrent = 1000;
        posBandCurrent++;
        sb.append(tranDate).append(posBandCurrent);
        return sb.toString();
    }

    /**
     * 卡商订单号
     * @return
     */
    public String cardOrderSnoCreate() {
        String FORMAT1 = "yyyyMMddHHmmssSSS";
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT1);
        String tranDate = sdf.format(new Date());
        StringBuffer sb = new StringBuffer("BOD").append("01");
        if (posBandCurrent > 9998) {
            posBandCurrent = 1000;
        }
        posBandCurrent++;
        sb.append(tranDate).append(posBandCurrent);
        return sb.toString();
    }

    public String dfOrderSnoCreate() {
        String FORMAT1 = "yyyyMMddHHmmssSSS";
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT1);
        String tranDate = sdf.format(new Date());
        StringBuffer sb = new StringBuffer("BDF").append("01");
        if (posBandCurrent > 9998) {
            posBandCurrent = 1000;
        }
        posBandCurrent++;
        sb.append(tranDate).append(posBandCurrent);
        return sb.toString();
    }

    public String dfbzjOrderSnoCreate() {
        String FORMAT1 = "yyyyMMddHHmmssSSS";
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT1);
        String tranDate = sdf.format(new Date());
        StringBuffer sb = new StringBuffer("BZJ").append("01");
        if (posBandCurrent > 9998) {
            posBandCurrent = 1000;
        }
        posBandCurrent++;
        sb.append(tranDate).append(posBandCurrent);
        return sb.toString();
    }
    public static void main(String[] args) throws Exception {
        new Thread(()-> {
            id();
        }).start();
        new Thread(()-> {
            id();
        }).start();
        new Thread(()-> {
            id();
        }).start();
        new Thread(()-> {
            id();
        }).start();
        new Thread(()-> {
            id();
        }).start();
        new Thread(()-> {
            id();
        }).start();


    }
    public static void id() {
        for(int i=0 ;i<10 ;i++) {
            String orderId = IdGenerator.INSTANCE.orderSnoCreate();
        }
    }

}
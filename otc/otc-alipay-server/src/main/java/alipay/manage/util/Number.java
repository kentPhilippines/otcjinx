package alipay.manage.util;

import cn.hutool.core.util.StrUtil;
import otc.api.alipay.Common;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

public class Number {
    // 使用单例模式，不允许直接创建实例
    private Number() {}
    // 创建一个空实例对象，类需要用的时候才赋值
    private static Number instance = null;
    // 单例模式--懒汉模式
    public static synchronized Number getInstance() {
        if (instance == null)
            instance = new Number();
        return instance;
    }
    // 全局自增数
    private static int count = 1;
    // 格式化的时间字符串
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    // 获取当前时间年月日时分秒毫秒字符串
    private static String getNowDateStr() {
        return sdf.format(new Date());
    }
    // 记录上一次的时间，用来判断是否需要递增全局数
    private static String now = null;
    //定义锁对象
    private final static ReentrantLock lock=new ReentrantLock();
    //调用的方法
    private static String GetRandom(final String haed) throws UnknownHostException {
        String Newnumber=null;
        String dateStr=getNowDateStr();
        lock.lock();//加锁
        String ip = "";
        for(String id : StrUtil.split(InetAddress.getLocalHost().getHostAddress().toString(), "."))
            ip+=id;
        //判断是时间是否相同
        if (dateStr.equals(now)) {
            try {
                if (count >= 10000)
                    count = 1;
                if (count<10)
                    Newnumber = haed +ip+ getNowDateStr()+"000"+count;
                else if (count<100)
                    Newnumber = haed +ip+ getNowDateStr()+"00"+count;
                else if(count<1000)
                    Newnumber = haed +ip+ getNowDateStr()+"0"+count;
                else
                    Newnumber = haed +ip+ getNowDateStr()+count;
                count++;
            } catch (Exception e) {
            }finally{
                lock.unlock();
            }
        }else{
            count=1;
            now =getNowDateStr();
            try {
                if (count >= 10000)
                    count = 1;
                if (count<10)
                    Newnumber = haed +ip+ getNowDateStr()+"000"+count;
                else if (count<100)
                    Newnumber = haed +ip+ getNowDateStr()+"00"+count;
                else if(count<1000)
                    Newnumber = haed +ip+ getNowDateStr()+"0"+count;
                else
                    Newnumber = haed +ip+ getNowDateStr()+count;
                count++;
            } catch (Exception e) {
            }finally{
                lock.unlock();
            }
        }
        return Newnumber;//返回的值
    }

    /**
     * <p>交易订单</p>
     * @return
     */
    public static String GetDealOrder(){
        try {
            return GetRandom(Common.Deals.ORDERDEAL);
        } catch (UnknownHostException e) {
            return null;
        }
    }
    /**
     * <p>代付订单</p>
     * @return
     */
    public static String GetWitOrder(){
        try {
            return GetRandom(Common.Deals.ORDERWIT);
        } catch (UnknownHostException e) {
            return null;
        }
    }
    /**
     * <p>流水订单</p>
     * @return
     */
    public static String GetRunOrder(){
        try {
            return GetRandom(Common.Deals.ORDERRUN);
        } catch (UnknownHostException e) {
            return null;
        }
    }
    /**
     * <p>异常订单</p>
     * @return
     */
    public static String GetExceOrder() {
        try {
            return GetRandom(Common.Deals.ORDEREXCE);
        } catch (UnknownHostException e) {
            return null;
        }
    }
    /**
     * <p>加钱订单</p>
     * @return
     */
    public static String GetAddAmount() {
        try {
            return GetRandom(Common.Deals.ADD_MOUNT);
        } catch (UnknownHostException e) {
            return null;
        }
    }
    /**
     * <p>减钱订单</p>
     * @return
     */
    public static String GetDelAmount() {
        try {
            return GetRandom(Common.Deals.DEL_MOUNT);
        } catch (UnknownHostException e) {
            return null;
        }
    }
    public static String getImg() {
        try {
            return GetRandom(Common.Deals.IMG);
        } catch (UnknownHostException e) {
            return null;
        }
    }
    public static String getMedium() {
        try {
            return GetRandom(Common.Deals.MEDIUM);
        } catch (UnknownHostException e) {
            return null;
        }
    }
    public static String getbankNo() {
        try {
            return GetRandom(Common.Deals.BANK);
        } catch (UnknownHostException e) {
            return null;
        }
    }
    /**
     * <p>越创流水订单</p>
     * @return
     */
    public static String GetAccountFlow() {
        try {
            return GetRandom(Common.Deals.YUCHUANG_FLOW);
        } catch (UnknownHostException e) {
            return null;
        }
    }
}

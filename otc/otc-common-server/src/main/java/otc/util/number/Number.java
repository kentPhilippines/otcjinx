package otc.util.number;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import otc.api.alipay.Common;
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
    private static int count = 1;
    private static final SimpleDateFormat  sdf  = new SimpleDateFormat("yyyyMMddHHmmss");
    private static String getNowDateStr() {
        return sdf.format(new Date());
    }
    private static String now = null;
    private final static ReentrantLock lock=new ReentrantLock();
    /**
     * <p>调用这个方法即可</p>
     * @param haed		头部订单编号
     * @return
     * @throws UnknownHostException
     */
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
    
    
  public static  String getAppOreder(){
			  try {
				return GetRandom(Common.Deals.YUCHUANG_FLOW);
			} catch (UnknownHostException e) {
				String randomString2 = RandomUtil.randomNumbers(15);
		    	String orderId = Common.Deals.YUCHUANG_FLOW +randomString2 ; 
				return orderId;
			}
    }
    
  public static  String getWitOrder(){
	  try {
		  return GetRandom(Common.Deals.ORDERWIT_APP);
	  } catch (UnknownHostException e) {
		  String randomString2 = RandomUtil.randomNumbers(15);
		  String orderId = Common.Deals.ORDERWIT_APP +randomString2 ; 
		  return orderId;
	  }
  }


	public static String getImg() {
		 String objectId = IdUtil.objectId().toUpperCase();
		return Common.Medium.IMG_NUMBER+objectId;	
	}


	public static String getMedum() {
		String objectId = IdUtil.objectId().toUpperCase();
		return Common.Medium.MM_NUMBER+objectId;	
	}


	public static String getBank() {
		String objectId = IdUtil.objectId().toUpperCase();
		return Common.Medium.BANK_NUMBER+objectId;	
	}


	public static String getOrderQr() {
		String objectId = IdUtil.objectId().toUpperCase();
		return Common.Deals.ORDERDEAL+objectId;	
	}


	public static String getRunOrderId() {
		String objectId = IdUtil.objectId().toUpperCase();
		return Common.Deals.ORDERRUN+objectId;	
	}
  
    
    
    
    
    
    
}

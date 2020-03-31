package otc.util.date;

import java.util.Date;

/**
 * <p>本地时间处理类</p>
 * @author K
 */
public class DateUtils {
	/**
	 * <p>两个时间是否相等，允许存在误差值</p>
	 * @param second		秒
	 * @param date			时间1
	 * @param dated			时间2
	 * @return
	 */
	public static boolean isTimeScope(int second ,Date date, Date dated) {
		long time = date.getTime();
		long time2 = dated.getTime();
		return isTimeScope(second, time, time2);
	}
	/**
	 * <p>时间是否在一个时间内，并存在允许的误差值</p>
	 * @param second			误差值  秒
	 * @param compare			比较的时间
	 * @param compared			被比较的时间
	 * @return
	 */
	private  static boolean isTimeScope(long second ,long compare  , long compared) {
		second*=1000;
		return compared-second < compare   || compare < compared+second;
	}
}

package otc.util.date;

import cn.hutool.core.date.DateUtil;
import org.assertj.core.util.Lists;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
	 * @param second            误差值  秒
	 * @param compare            比较的时间
	 * @param compared            被比较的时间
	 * @return
	 */
	private static boolean isTimeScope(long second, long compare, long compared) {
		second *= 1000;
		return compared - second < compare || compare < compared + second;
	}

	public static String YYYY = "yyyy";

	public static String YYYY_MM = "yyyy-MM";

	public static String YYYY_MM_DD = "yyyy-MM-dd";

	public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

	public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

	private static String[] parsePatterns = {
			"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
			"yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
			"yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

	/**
	 * 获取当前Date型日期
	 *
	 * @return Date() 当前日期
	 */
	public static Date getNowDate() {
		return new Date();
	}

	/**
	 * 获取当前日期, 默认格式为yyyy-MM-dd
	 *
	 * @return String
	 */
	public static String getDate() {
		return dateTimeNow(YYYY_MM_DD);
	}

	public static final String getTime() {
		return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
	}

	public static final String dateTimeNow() {
		return dateTimeNow(YYYYMMDDHHMMSS);
	}

	public static final String dateTimeNow(final String format) {
		return parseDateToStr(format, new Date());
	}

	public static final String dateTime(final Date date) {
		return parseDateToStr(YYYY_MM_DD, date);
	}

	public static final String parseDateToStr(final String format, final Date date) {
		return new SimpleDateFormat(format).format(date);
	}

	public static final Date dateTime(final String format, final String ts) {
		try {
			return new SimpleDateFormat(format).parse(ts);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 日期路径 即年/月/日 如2018/08/08
	 */
	public static final String datePath() {
		Date now = new Date();
		return DateUtil.format(now, "yyyy/MM/dd");
	}

	/**
	 * 日期路径 即年/月/日 如20180808
	 */
	public static final String dateTime() {
		Date now = new Date();
		return DateUtil.format(now, "yyyyMMdd");
	}
	public static final String time() {
		Date now = new Date();
		return DateUtil.format(now, "HHmmss");
	}

	/**
	 * 获取服务器启动时间
	 */
	public static Date getServerStartDate() {
		long time = ManagementFactory.getRuntimeMXBean().getStartTime();
		return new Date(time);
	}

	/**
	 * 计算两个时间差
	 */
	public static String getDatePoor(Date endDate, Date nowDate) {
		long nd = 1000 * 24 * 60 * 60;
		long nh = 1000 * 60 * 60;
		long nm = 1000 * 60;
		// long ns = 1000;
		// 获得两个时间的毫秒时间差异
		long diff = endDate.getTime() - nowDate.getTime();
		// 计算差多少天
		long day = diff / nd;
		// 计算差多少小时
		long hour = diff % nd / nh;
		// 计算差多少分钟
		long min = diff % nd % nh / nm;
		// 计算差多少秒//输出结果
		// long sec = diff % nd % nh % nm / ns;
		return day + "天" + hour + "小时" + min + "分钟";
	}

	/**
	 * 获取指定时间的区间list
	 *
	 * @param days
	 * @return
	 */
	public static List getDateList(Integer days, Date startTime) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(startTime);
		cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - days);
		List list = Lists.newArrayList();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		list.add(sdf.format(cal.getTime()));
		for (int k = 1; k <= days; k++) {
			cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1);
			list.add(sdf.format(cal.getTime()));
		}
		return list;
	}

	/**
	 * 当天的开始时间
	 *
	 * @return
	 */
	public static String dayStart() {
		return getDate() + " 00:00:00";
	}

	/**
	 * 当天的结束时间
	 *
	 * @return
	 */
	public static String dayEnd() {
		return getDate() + " 23:59:59";
	}
}

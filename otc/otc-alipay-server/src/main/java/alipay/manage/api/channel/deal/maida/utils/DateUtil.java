package alipay.manage.api.channel.deal.maida.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	public static final String FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public static final String FORMAT_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

	public static String date2Str(Date date) {
		return date2Str(date, FORMAT_YYYY_MM_DD_HH_MM_SS);
	}
	public static String date2Str(Date date, String format) {
		if ((format == null) || format.equals("")) {
			format = FORMAT_YYYY_MM_DD_HH_MM_SS;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		if (date != null) {
			return sdf.format(date);
		}
		return "";
	}

}

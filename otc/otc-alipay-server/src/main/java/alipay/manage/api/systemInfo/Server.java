package alipay.manage.api.systemInfo;

import alipay.manage.api.AccountApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class Server {
    private static NumberFormat fmtI = new DecimalFormat("###,###", new DecimalFormatSymbols(Locale.ENGLISH));
    private static NumberFormat fmtD = new DecimalFormat("###,##0.000", new DecimalFormatSymbols(Locale.ENGLISH));
    Logger log = LoggerFactory.getLogger(AccountApi.class);

    protected static String getKindName(String kind) {
        if ("NON_HEAP".equals(kind)) {
            return "NON_HEAP(非堆内存)";
        } else {
            return "HEAP(堆内存)";
        }
    }

    protected static String getPoolName(String poolName) {
        switch (poolName) {
            case "Code Cache":
                return poolName + "(代码缓存区)";
            case "Metaspace":
                return poolName + "(元空间)";
            case "Compressed Class Space":
                return poolName + "(类指针压缩空间)";
            case "PS Eden Space":
                return poolName + "(伊甸园区)";
            case "PS Survivor Space":
                return poolName + "(幸存者区)";
            case "PS Old Gen":
                return poolName + "(老年代)";
            default:
                return poolName;
        }
    }

    protected static String bytesToMB(long bytes) {
        return fmtI.format((long) (bytes / 1024 / 1024)) + " MB";
    }

    protected static String printSizeInKb(double size) {
        return fmtI.format((long) (size / 1024)) + " kbytes";
    }

    protected static String toDuration(double uptime) {
        uptime /= 1000;
        if (uptime < 60) {
            return fmtD.format(uptime) + " seconds";
        }
        uptime /= 60;
        if (uptime < 60) {
            long minutes = (long) uptime;
            String s = fmtI.format(minutes) + (minutes > 1 ? " minutes" : " minute");
            return s;
        }
        uptime /= 60;
        if (uptime < 24) {
            long hours = (long) uptime;
            long minutes = (long) ((uptime - hours) * 60);
            String s = fmtI.format(hours) + (hours > 1 ? " hours" : " hour");
            if (minutes != 0) {
                s += " " + fmtI.format(minutes) + (minutes > 1 ? " minutes" : " minute");
            }
            return s;
        }
        uptime /= 24;
        long days = (long) uptime;
        long hours = (long) ((uptime - days) * 24);
        String s = fmtI.format(days) + (days > 1 ? " days" : " day");
        if (hours != 0) {
            s += " " + fmtI.format(hours) + (hours > 1 ? " hours" : " hour");
        }
        return s;
    }

    public void main(HttpServletRequest req, HttpServletResponse res) {
    }
}
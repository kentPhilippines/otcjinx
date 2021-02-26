package test.number;

import cn.hutool.http.HttpUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class cmd {


    private static final long THREAD_SLEEP_TIME = 1000;

    private static final int DEFAULT_WAIT_TIME = 20 * 60 * 1000;


    public static void main(String[] args) {
        String s = HttpUtil.get("https://coinyep.com/zh/ex/ETH-USDT");


        System.out.println(s);
    }


    private static String cmd(String cmd) {
        Runtime run = Runtime.getRuntime();
        try {
            Process process = run.exec(cmd);
            String line;
            ShellResult processResult = getProcessResult(process, 2000);
            System.out.println(processResult.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    static String curlCmd(String url, String amount, String tradesno, String appid, String sign, String apporderid) {
        String curl = "curl --location --request GET \"\"\"https://ht-tw.maitoo998.com/v6/callback/JinXingInfrom\"\"\" \\\n" +
                "--header \"\"\"Content-Type: application/x-www-form-urlencoded\"\"\" \\\n" +
                "--header \"\"\"Cookie: __cfduid=ddad247915c7a941767647631e924b3ee1609730190\"\"\"\n" +
                "--data-urlencode \"\"\"amount=500.000000\"\"\" \\\n" +
                "--data-urlencode \"\"\"tradesno=C1609745418207841706\"\"\" \\\n" +
                "--data-urlencode \"\"\"appid=tt678\"\"\" \\\n" +
                "--data-urlencode \"\"\"sign=8f95c19a305da165b87813e7a22c1aca\"\"\" \\\n" +
                "--data-urlencode \"\"\"statusdesc=成功\"\"\" \\\n" +
                "--data-urlencode \"\"\"apporderid=JX9745418117177892\"\"\" \\\n" +
                "--data-urlencode \"\"\"status=2\"\"\"";
        return curl;
    }

    public static ShellResult getProcessResult(Process process, long waitTime) {
        ShellResult cmdResult = new ShellResult();
        boolean isTimeout = false;
        long loopNumber = waitTime / THREAD_SLEEP_TIME;
        long realLoopNumber = 0;
        int exitValue = -1;

        StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream());
        StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream());

        errorGobbler.start();
        outputGobbler.start();

        try {
            while (true) {
                try {
                    Thread.sleep(THREAD_SLEEP_TIME);
                    exitValue = process.exitValue();
                    break;
                } catch (InterruptedException e) {
                    realLoopNumber++;
                    if (realLoopNumber >= loopNumber) {
                        isTimeout = true;
                        break;
                    }
                }
            }

            errorGobbler.join();
            outputGobbler.join();

            if (isTimeout) {
                cmdResult.setErrorCode(ShellResult.TIMEOUT);
                return cmdResult;
            }

            cmdResult.setErrorCode(exitValue);
            if (exitValue != ShellResult.SUCCESS) {
                cmdResult.setDescription(errorGobbler.getOutput());
            } else {
                cmdResult.setDescription(outputGobbler.getOutput());
            }
        } catch (InterruptedException e) {
            cmdResult.setErrorCode(ShellResult.ERROR);
        } finally {
            CommonUtils.closeStream(process.getErrorStream());
            CommonUtils.closeStream(process.getInputStream());
            CommonUtils.closeStream(process.getOutputStream());
        }

        return cmdResult;
    }


}

class ShellResult {
    public static final int SUCCESS = 0;

    public static final int ERROR = 1;

    public static final int TIMEOUT = 13;

    private int errorCode;

    private List<String> description;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public List<String> getDescription() {
        return description;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ShellResult{" +
                "errorCode=" + errorCode +
                ", description=" + description +
                '}';
    }

}

class StreamGobbler extends Thread {
    private InputStream is;

    private List<String> output = new ArrayList<String>();

    public StreamGobbler(InputStream is) {
        this.is = is;
    }

    public List<String> getOutput() {
        return output;
    }

    @Override
    public void run() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line = "";
            while ((line = reader.readLine()) != null) {
                output.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            CommonUtils.closeStream(reader);


        }
    }
}

class CommonUtils {

    /**
     * 提供统一关闭流的方法
     *
     * @param stream 待关闭的流
     */
    public static void closeStream(Closeable stream) {
        if (stream == null) {
            return;
        }

        try {
            stream.close();
        } catch (IOException e) {
        }
    }
}
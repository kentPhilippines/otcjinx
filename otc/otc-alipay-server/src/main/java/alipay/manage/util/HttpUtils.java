package alipay.manage.util;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Map.Entry;

public class HttpUtils {
    /**
     * POST请求远程http页面
     *
     * @param contentUrl
     * @param params
     * @return
     */
    public static String post(String contentUrl, Map<String, Object> params) {
        StringBuilder contentBuilder = new StringBuilder();
        HttpURLConnection connection = null;
        DataOutputStream out = null;
        BufferedReader reader = null;
        try {
            connection = (HttpURLConnection) new URL(contentUrl).openConnection();
            // 设置Socket超时
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(20000);
            // Post 请求不能使用缓存
            connection.setUseCaches(false);
            // URLConnection.setInstanceFollowRedirects是成员函数，仅作用于当前函数
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);// 是否输入参数
            // 要注意的是connection.getOutputStream会隐含的进行connect
            connection.connect();

            out = new DataOutputStream(connection.getOutputStream());
            StringBuilder parambBuilder = new StringBuilder();
            if (params != null) {
                for (Entry<String, Object> e : params.entrySet()) {
                    parambBuilder.append(e.getKey()).append("=").append(URLEncoder.encode(String.valueOf(e.getValue()), "UTF-8")).append("&");
                }
                parambBuilder.deleteCharAt(parambBuilder.length() - 1);
            }
            // DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写道流里面
            out.writeBytes(parambBuilder.toString());
            out.flush();

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));

            int buffer = 0;
            while ((buffer = reader.read()) != -1) {
                contentBuilder.append((char) buffer);
            }

        } catch (Exception e) {
            // 异常处理
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            if (connection != null) {
                connection.disconnect();
            }
        }
        return contentBuilder.toString();
    }

    /**
     * Get方法请求
     *
     * @param contentUrl
     * @return
     */
    public static String get(String contentUrl) {
        StringBuilder contentBuilder = new StringBuilder();
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            connection = (HttpURLConnection) new URL(contentUrl).openConnection();
            // 设置Socket超时
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(20000);
            connection.setRequestMethod("GET");
            connection.connect();

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));

            int buffer = 0;
            while ((buffer = reader.read()) != -1) {
                contentBuilder.append((char) buffer);
            }
        } catch (Exception e) {
            // 异常处理
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return contentBuilder.toString();
    }

    /**
     * POST请求远程https页面
     *
     * @param contentUrl
     * @param params
     * @return
     */
    public static String postHttps(String contentUrl, Map<String, Object> params) {
        StringBuilder contentBuilder = new StringBuilder();
        HttpsURLConnection connection = null;
        DataOutputStream out = null;
        BufferedReader reader = null;
        try {
            HostnameVerifier hnv = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            X509TrustManager trustManager = new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                }
            };

            X509TrustManager[] xtmArray = new X509TrustManager[]{trustManager};
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, xtmArray, new java.security.SecureRandom());

            if (sslContext != null) {
                HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            }
            HttpsURLConnection.setDefaultHostnameVerifier(hnv);

            connection = (HttpsURLConnection) new URL(contentUrl).openConnection();
            // 设置Socket超时
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(20000);
            // Post 请求不能使用缓存
            connection.setUseCaches(false);
            // URLConnection.setInstanceFollowRedirects是成员函数，仅作用于当前函数
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);// 是否输入参数
            connection.connect();

            out = new DataOutputStream(connection.getOutputStream());
            StringBuilder parambBuilder = new StringBuilder();
            if (params != null) {
                for (Entry<String, Object> e : params.entrySet()) {
                    parambBuilder.append(e.getKey()).append("=").append(URLEncoder.encode(String.valueOf(e.getValue()), "UTF-8")).append("&");
                }
                parambBuilder.deleteCharAt(parambBuilder.length() - 1);
            }
            // DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写道流里面
            out.writeBytes(parambBuilder.toString());
            out.flush();

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));

            int buffer = 0;
            while ((buffer = reader.read()) != -1) {
                contentBuilder.append((char) buffer);
            }
        } catch (Exception e) {
            // 异常处理
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return contentBuilder.toString();
    }

}

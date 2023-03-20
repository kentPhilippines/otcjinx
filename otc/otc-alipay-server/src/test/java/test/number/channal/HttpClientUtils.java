package test.number.channal;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

public class HttpClientUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

    private static PoolingHttpClientConnectionManager connMgr;
    private static RequestConfig requestConfig;
    private static HttpClientBuilder httpClientBuilder;
    private static HttpClientBuilder httpsClientBuilder;

    private static final int MAX_TIMEOUT = 60000;

    static {
        // 设置连接池
        connMgr = new PoolingHttpClientConnectionManager();
        // 设置连接池大小
        connMgr.setMaxTotal(100);
        connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());

        RequestConfig.Builder configBuilder = RequestConfig.custom();
        // 设置连接超时
        configBuilder.setConnectTimeout(MAX_TIMEOUT);
        // 设置读取超时
        configBuilder.setSocketTimeout(MAX_TIMEOUT);
        // 设置从连接池获取连接实例的超时
        configBuilder.setConnectionRequestTimeout(MAX_TIMEOUT);
        requestConfig = configBuilder.build();
        httpClientBuilder = HttpClients.custom().setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig);
        httpsClientBuilder = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).setSSLHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
                .setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig);
    }

    public static CloseableHttpClient getClient(boolean isHttps) {
        if (isHttps) {
            return httpsClientBuilder.build();
        }
        return httpClientBuilder.build();
    }

    public static String doGet1120(String url) {
        return doGet(url, new HashMap<String, Object>());
    }

    /**
     * 发送 GET 请求（HTTP），K-V形式
     *
     * @param url
     * @param params
     * @return
     */
    public static String doGet(String url, Map<String, Object> params) {
        String apiUrl = url + doGetParam(params);
        String result = null;
        System.out.println(apiUrl);
        HttpResponse response = null;
        try {
            CloseableHttpClient httpclient = getClient(apiUrl.startsWith("https"));
            HttpGet httpPost = new HttpGet(apiUrl);
            response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, "UTF-8");
            }
            logger.info("url:{},param:{},responseCode:{},responseStr:{}", apiUrl, JSON.toJSONString(params),
                    response.getStatusLine().getStatusCode(), result);
        } catch (IOException e) {
            e.printStackTrace();
            result = e.getMessage();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 发送 GET 请求（HTTP），K-V形式
     *
     * @param url
     * @param params
     * @param headers
     * @return
     * @throws IOException
     * @throws
     */
    public static String doGet(String url, Map<String, Object> params, Map<String, String> headers)
            throws ParseException, IOException {

        String apiUrl = params == null ? url : url + doGetParam(params);
        String result = null;
        HttpResponse response = null;
        try {
            CloseableHttpClient httpclient = getClient(apiUrl.startsWith("https"));
            HttpGet httpGet = new HttpGet(apiUrl);
            setHeaders(httpGet, headers);
            response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, "UTF-8");
            }
            logger.info("url:{},param:{},responseCode:{},responseStr:{}", url, JSON.toJSONString(params),
                    response.getStatusLine().getStatusCode(), result);
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    private static void setHeaders(HttpRequestBase request, Map<String, String> headers) {
        if (headers == null) {
            return;
        }
        if (request == null) {
            throw new RuntimeException("request must not be null");
        }
        Iterator<String> iterator = headers.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            request.setHeader(key, headers.get(key));
        }
    }

    /**
     * 发送 POST 请求（HTTP），不带输入数据
     *
     * @param apiUrl
     * @return
     */
    public static String doPost(String apiUrl) {
        return doPost(apiUrl, new HashMap<String, Object>());
    }

    /**
     * 发送 POST 请求（HTTP），K-V形式
     *
     * @param url
     *            API接口URL
     * @param params
     *            参数map
     * @return
     */
    public static String doPost(String url, Map<String, Object> params) {
        String httpStr = null;
        CloseableHttpResponse response = null;
        try {
            CloseableHttpClient httpClient = getClient(url.startsWith("https"));
            List<NameValuePair> pairList = doPostParam(params);
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("UTF-8")));
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                httpStr = EntityUtils.toString(entity, "UTF-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
            httpStr = e.getMessage();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return httpStr;
    }

    public static String doPostStr(String url, String param) {
        String httpStr = null;
        CloseableHttpResponse response = null;
        try {
            CloseableHttpClient httpClient = getClient(false);
            StringEntity stringEntity = new StringEntity(param, "UTF-8");// 解决中文乱码问题
            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType("application/x-www-form-urlencoded");
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(stringEntity);
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                httpStr = EntityUtils.toString(entity, "UTF-8");
            }
            logger.info("url:{},param:{},responseCode:{},responseStr:{}", url, param,
                    response.getStatusLine().getStatusCode(), httpStr);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return httpStr;
    }

    /**
     * 发送 POST 请求（HTTP），JSON形式
     *
     * @param url
     * @param json
     *            json对象
     * @return
     */
    public static String doPostJson(String url, Object json) {
        String httpStr = null;
        CloseableHttpResponse response = null;
        try {
            CloseableHttpClient httpClient = getClient(false);
            StringEntity stringEntity = new StringEntity(json.toString(), "UTF-8");// 解决中文乱码问题
            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType("application/json");
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(stringEntity);
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                httpStr = EntityUtils.toString(entity, "UTF-8");
            }
            logger.info("url:{},param:{},responseCode:{},responseStr:{}", url, json.toString(),
                    response.getStatusLine().getStatusCode(), httpStr);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return httpStr;
    }

    /**
     * 发送 POST 请求（HTTP），K-V形式
     *
     * @param url
     *            API接口URL
     * @param
     *
     * @return
     */
    public static String doPostJsonDXN(String url, JSONObject json, String apiKey) {
        String httpStr = null;
        CloseableHttpResponse response = null;
        try {
            CloseableHttpClient httpClient = getClient(false);
            StringEntity stringEntity = new StringEntity(json.toString(), "UTF-8");// 解决中文乱码问题
            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType("application/json");
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type","application/json");
            httpPost.setHeader("Accept","application/problem+json");
            httpPost.setHeader("Authorization","api-key " + apiKey);
            httpPost.setEntity(stringEntity);
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                httpStr = EntityUtils.toString(entity, "UTF-8");
            }
            logger.info("url:{},param:{},responseCode:{},responseStr:{}", url, json.toString(),
                    response.getStatusLine().getStatusCode(), httpStr);

        } catch (IOException e) {
            e.printStackTrace();
            httpStr = e.getMessage();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return httpStr;
    }

    public static String doPostTerControl(String url, String params, String method) {
        String httpStr = null;
        CloseableHttpResponse response = null;
        try {
            CloseableHttpClient httpClient = getClient(url.startsWith("https"));
            StringEntity stringEntity = new StringEntity(params, "UTF-8");// 解决中文乱码问题
            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType("application/text");
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(stringEntity);
            String token = UUID.randomUUID().toString().replaceAll("-","");
            String sign = PayDigestUtil.md5(params+token+"c24973d24318b75ab81ecda4df30f7e8","utf-8");
            httpPost.setHeader("method", method);
            httpPost.setHeader("token", token);
            httpPost.setHeader("sign", sign);
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                httpStr = EntityUtils.toString(entity, "UTF-8");
            }
            logger.info("url:{},param:{},responseCode:{},responseStr:{}", url, params,
                    response.getStatusLine().getStatusCode(), httpStr);
        } catch (IOException e) {
            e.printStackTrace();
            httpStr = e.getMessage();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return httpStr;
    }

    public static String doPostSandpay(String url, JSONObject jsonObject) {
        String httpStr = null;
        CloseableHttpResponse response = null;
        try {
            CloseableHttpClient httpClient = getClient(url.startsWith("https"));
            StringEntity stringEntity = new StringEntity(jsonObject.toString(), "UTF-8");// 解决中文乱码问题
            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType("application/json");
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(stringEntity);
            String token = UUID.randomUUID().toString().replaceAll("-","");
            String sign = PayDigestUtil.md5(token+"0n5n9wuR5Cq7ExeJ","utf-8");
            httpPost.setHeader("token", token);
            httpPost.setHeader("sign", sign);
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                httpStr = EntityUtils.toString(entity, "UTF-8");
            }
            logger.info("url:{},param:{},responseCode:{},responseStr:{}", url, jsonObject.toJSONString(),
                    response.getStatusLine().getStatusCode(), httpStr);
        } catch (IOException e) {
            e.printStackTrace();
            httpStr = e.getMessage();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return httpStr;
    }

    /**
     * 发送 SSL POST 请求（HTTPS），K-V形式
     *
     * @param url
     *            API接口URL
     * @param params
     *            参数map
     * @return
     */
    public static String doPostSSL(String url, Map<String, Object> params) {
        CloseableHttpResponse response = null;
        String httpStr = null;
        try {
            CloseableHttpClient httpClient = (CloseableHttpClient) getClient(true);
            List<NameValuePair> pairList = doPostParam(params);
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("utf-8")));
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                httpStr = EntityUtils.toString(entity, "utf-8");
            }
            logger.info("url:{},param:{},responseCode:{},responseStr:{}", url, JSON.toJSONString(params),
                    response.getStatusLine().getStatusCode(), httpStr);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return httpStr;
    }

    /**
     * 发送 SSL POST 请求（HTTPS），JSON形式
     *
     * @param url
     *            API接口URL
     * @param json
     *            JSON对象
     * @return
     */
    public static String doPostSSL(String url, Object json) {
        // CloseableHttpClient httpClient =
        // HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory())
        // .setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();

        CloseableHttpResponse response = null;
        String httpStr = null;
        try {
            CloseableHttpClient httpClient = (CloseableHttpClient) getClient(true);
            StringEntity stringEntity = new StringEntity(json.toString(), "UTF-8");// 解决中文乱码问题
            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType("application/json");
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(stringEntity);
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                httpStr = EntityUtils.toString(entity, "utf-8");
            }
            logger.info("url:{},param:{},responseCode:{},responseStr:{}", url, json.toString(),
                    response.getStatusLine().getStatusCode(), httpStr);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return httpStr;
    }
    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url
     *            发送请求的 URL
     * @param params
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @param isproxy
     *               是否使用代理模式
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String params,boolean isproxy) {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        String result = "";

        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn = null;
            if(isproxy){//使用代理模式
                @SuppressWarnings("static-access")
                Proxy proxy = new Proxy(Proxy.Type.DIRECT.HTTP, new InetSocketAddress("127.0.0.1", 1080));
                conn = (HttpURLConnection) realUrl.openConnection(proxy);
            }else{
                conn = (HttpURLConnection) realUrl.openConnection();
            }
            // 打开和URL之间的连接

            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");    // POST方法


            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            conn.connect();

            // 获取URLConnection对象对应的输出流
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            // 发送请求参数

            out.write(params);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static String urlParame(Map<String,Object> map){
        ArrayList<String> list = new ArrayList<String>();
        for(Map.Entry<String,Object> entry:map.entrySet()){
            if(null != entry.getValue() && !"".equals(entry.getValue())){
                if(entry.getValue() instanceof JSONObject) {
                    list.add(entry.getKey() + "=" + getSortJson((JSONObject) entry.getValue()) + "&");
                }else {
                    list.add(entry.getKey() + "=" + entry.getValue() + "&");
                }
            }
        }
        int size = list.size();
        String [] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; i ++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString();
        return result;

    }


    public static String getSortJson(JSONObject obj){
        SortedMap map = new TreeMap();
        Set<String> keySet = obj.keySet();
        Iterator<String> it = keySet.iterator();
        while (it.hasNext()) {
            String key = it.next().toString();
            Object vlaue = obj.get(key);
            map.put(key, vlaue);
        }
        return JSONObject.toJSONString(map);
    }


    public static void main(String[] args) {
        String s = "{\"bankCode\":\"PSBC\",\"amount\":\"55100\",\"mchId\":\"1188\",\"productId\":\"4\",\"accountName\":\"蔺明亮\",\"city\":\"北京市\",\"mchOrderNo\":\"PY202203161529442027346\",\"sign\":\"4F96ED4799C951C3869B48DC749AF4EB\",\"bankName\":\"邮政银行\",\"reqTime\":\"20220316153157\",\"param1\":\"\",\"province\":\"北京\",\"accountNo\":\"6217994980017778387\",\"currency\":\"cny\"}";
        JSONObject jsonObject = JSON.parseObject(s);
        List<NameValuePair> nameValuePairs = doPostParam(jsonObject);
    }

    /**
     * 创建SSL安全连接
     *
     * @return
     */
    private static SSLConnectionSocketFactory createSSLConnSocketFactory() {
        SSLConnectionSocketFactory sslsf = null;
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {

                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            sslsf = new SSLConnectionSocketFactory(sslContext, new X509HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }

                @Override
                public void verify(String host, SSLSocket ssl) throws IOException {
                }

                @Override
                public void verify(String host, X509Certificate cert) throws SSLException {
                }

                @Override
                public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
                }
            });
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return sslsf;
    }

    public static String doGetParam(Map<String, Object> params) {
        StringBuffer param = new StringBuffer();
        int i = 0;
        for (String key : params.keySet()) {
            if (i == 0)
                param.append("?");
            else
                param.append("&");
            param.append(key).append("=").append(params.get(key));
            i++;
        }
        return param.toString();
    }

    public static String doGetParamStr(Map<String, Object> params) {
        StringBuffer param = new StringBuffer();
        int i = 0;
        for (String key : params.keySet()) {
            if (i == 0)
                param.append("?");
            else
                param.append("&");
            param.append(key).append("=").append(params.get(key));
            i++;
        }
        return param.toString();
    }

    private static List<NameValuePair> doPostParam(Map<String, Object> params) {
        List<NameValuePair> pairList = new ArrayList<>(params.size());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            try {
                NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
                pairList.add(pair);
            }catch (Exception e){
                logger.info("doPostParam_error:{},{}", entry.getKey(), entry.getValue());
            }
        }
        return pairList;
    }
}

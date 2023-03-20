package alipay.manage.api.channel.deal.anxin.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EasyPayUtil {

    public static Map<String, Object> makeRetMap(String retCode, String retMsg, String resCode, String errCode, String errDesc) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        if(retCode != null) retMap.put(PayConstant.RETURN_PARAM_RETCODE, retCode);
        if(retMsg != null) retMap.put(PayConstant.RETURN_PARAM_RETMSG, retMsg);
        if(resCode != null) retMap.put(PayConstant.RESULT_PARAM_RESCODE, resCode);
        if(errCode != null) retMap.put(PayConstant.RESULT_PARAM_ERRCODE, errCode);
        if(errDesc != null) retMap.put(PayConstant.RESULT_PARAM_ERRDES, errDesc);
        return retMap;
    }

    public static Map<String, Object> makeRetMap(String retCode, String retMsg, String resCode, PayEnum payEnum) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        if(retCode != null) retMap.put(PayConstant.RETURN_PARAM_RETCODE, retCode);
        if(retMsg != null) retMap.put(PayConstant.RETURN_PARAM_RETMSG, retMsg);
        if(resCode != null) retMap.put(PayConstant.RESULT_PARAM_RESCODE, resCode);
        if(payEnum != null) {
            retMap.put(PayConstant.RESULT_PARAM_ERRCODE, payEnum.getCode());
            retMap.put(PayConstant.RESULT_PARAM_ERRDES, payEnum.getMessage());
        }
        return retMap;
    }

    public static String makeRetData(Map retMap, String resKey) {
        if(PayConstant.RETURN_VALUE_SUCCESS.equals(retMap.get(PayConstant.RETURN_PARAM_RETCODE))) {
            String sign = PayDigestUtil.getSign(retMap, resKey);
            retMap.put(PayConstant.RESULT_PARAM_SIGN, sign);
        }
        System.out.println("生成响应数据:{}"+ retMap);
        return JSON.toJSONString(retMap);
    }

    public static String makeRetData(JSONObject retObj, String resKey) {
        if(PayConstant.RETURN_VALUE_SUCCESS.equals(retObj.get(PayConstant.RETURN_PARAM_RETCODE))) {
            String sign = PayDigestUtil.getSign(retObj, resKey);
            retObj.put(PayConstant.RESULT_PARAM_SIGN, sign);
        }
        System.out.println("生成响应数据:"+retObj);
        return JSON.toJSONString(retObj);
    }

    public static String makeRetFail(Map retMap) {
        System.out.println("生成响应数据:{}"+retMap);
        return JSON.toJSONString(retMap);
    }

    /**
     * 验证支付中心签名
     * @param params
     * @return
     */
    public static boolean verifyPaySign(Map<String,Object> params, String key) {
        String sign = (String)params.get("sign"); // 签名
        params.remove("sign");	// 不参与签名
        String checkSign = PayDigestUtil.getSign(params, key);
        if (!checkSign.equalsIgnoreCase(sign)) {
            return false;
        }
        return true;
    }

    public static String genUrlParams(Map<String, Object> paraMap) {
        if(paraMap == null || paraMap.isEmpty()) return "";
        StringBuffer urlParam = new StringBuffer();
        Set<String> keySet = paraMap.keySet();
        int i = 0;
        for(String key:keySet) {
            urlParam.append(key).append("=");
            if(paraMap.get(key) instanceof String) {
                urlParam.append(URLEncoder.encode((String) paraMap.get(key)));
            }else {
                urlParam.append(paraMap.get(key));
            }
            if(++i == keySet.size()) break;
            urlParam.append("&");
        }
        return urlParam.toString();
    }

    /**
     * 发起HTTP/HTTPS请求(method=POST)
     * @param url
     * @return
     */
    public static String call4Post(String url) {
        try {
            URL url1 = new URL(url);
            if("https".equals(url1.getProtocol())) {
                return HttpClient.callHttpsPost(url);
            }else if("http".equals(url1.getProtocol())) {
                return HttpClient.callHttpPost(url);
            }else {
                return "";
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 判断返回结果是否成功
     * @param retMap
     * @return
     */
    public static Boolean isSuccess(Map retMap) {
        if(retMap == null) return false;
        return "SUCCESS".equalsIgnoreCase(retMap.get("retCode").toString());
    }



}

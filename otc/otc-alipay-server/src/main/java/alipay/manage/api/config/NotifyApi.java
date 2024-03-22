package alipay.manage.api.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import otc.common.PayApiConstant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@RequestMapping(PayApiConstant.Notfiy.NOTFIY_API_WAI)
@RestController
public class NotifyApi {
    @Autowired
    private FactoryForStrategy factoryForStrategy;
    public static final String NOTIFY_TYPE_FORM = "/form-urlencoded";
    public static final String NOTIFY_TYPE_JSON = "/json";

    @RequestMapping(NOTIFY_TYPE_FORM + "/{method}/deal")
    public String notifyDeal(@PathVariable(name = "method") String method, HttpServletRequest req, HttpServletResponse res) {

        Map<String, Object> map = new HashMap<>();
        Enumeration<String> parameterNames = req.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement();
            String value = req.getParameter(key);
            map.put(key, value);
        }
        Map<String, Object> reqMap = getRequestBody(req);
        map.putAll(reqMap);
        try {
            String s = factoryForStrategy.getStrategy(method).dealNotify(map);
            return s;
        } catch (Throwable e) {
            return "";
        }
    }

    @RequestMapping(NOTIFY_TYPE_JSON + "/{method}/deal")
    public String notifyDealJson(@PathVariable(name = "method") String method, HttpServletRequest req, HttpServletResponse res) {

        Map<String, Object> map = new HashMap<>();
        Enumeration<String> parameterNames = req.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement();
            String value = req.getParameter(key);
            map.put(key, value);
        }
        Map<String, Object> reqMap = getRequestBody(req);
        map.putAll(reqMap);
        try {
            String s = factoryForStrategy.getStrategy(method).dealNotify(map);
            return s;
        } catch (Throwable e) {
            return "";
        }
    }

    @RequestMapping(NOTIFY_TYPE_FORM + "/{method}/wit")
    public String notifyWit(@PathVariable(name = "method") String method, HttpServletRequest req, HttpServletResponse res, @RequestBody JSONObject reqBody) {
        Map<String, Object> map = new HashMap<>();
        Enumeration<String> parameterNames = req.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement();
            String value = req.getParameter(key);
            map.put(key, value);
        }
        Map<String, Object> reqMap = getRequestBody(req);
        map.putAll(reqMap);
        try {
            String s = factoryForStrategy.getStrategy(method).witNotify(map);
            return s;
        } catch (Throwable e) {
            return "";
        }
    }


    public Map<String, Object> getRequestBody(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>();
        BufferedReader br;
        try {
            br = request.getReader();
            String str, wholeParams = "";
            while ((str = br.readLine()) != null) {
                wholeParams += str;
            }
            if (StringUtils.isNotBlank(wholeParams)) {
                params = JSON.parseObject(wholeParams, Map.class);
            }
        } catch (IOException e) {
            return params;
        }
        return params;
    }
}

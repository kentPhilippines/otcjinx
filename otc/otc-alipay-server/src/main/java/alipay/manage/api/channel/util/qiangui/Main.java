package alipay.manage.api.channel.util.qiangui;


import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

public class Main {
    public void crtOrder(String appId,String orderNo,Double orderAmt,String orderType,String accNo,String accName,String bankAccNo,String bankAccName,String bankName,String notifyURL,String key){
        HashMap map = new HashMap<>();
        map.put("appId",appId);
        map.put("orderNo",orderNo);
        map.put("orderAmt",orderAmt);
        map.put("orderType",orderType);//101-银行卡 102-微信 103-支付宝
        //orderType 102,103时 accNo accName 必传   bankAccNo bankAccName bankName 不传
        map.put("accNo",accNo);
        map.put("accName",accName);
        String sign = Util.creatSign(map, key);//封装签名方法
        map.put("sign",sign);
        map.put("notifyURL",notifyURL);
        String url="http://www.xxxxxxx.com/kaccPay/crtOrder.do";//正式环境地址
        try {
            String result = HttpUtil.post(url, map);//http请求 返回标准JSON格式
            //为确认返回未被劫持 如回调进行验签

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void selOrder(String appId,String orderNo,String key){
        HashMap map = new HashMap<>();
        map.put("appId",appId);
        map.put("orderNo",orderNo);
        String sign = Util.creatSign(map, key);//封装签名方法
        map.put("sign",sign);
        String url="http://www.xxxxxxx.com/kaccPay/selOrder.do";//正式环境地址
        try {
            String result = HttpUtil.post(url, map);//http请求 返回标准JSON格式
            //为确认返回未被劫持 如回调进行验签
            JSONObject resultJson=JSONUtil.parseObj(result);//转化为JSON对象
            String code=resultJson.getStr("code");//获取返回code  0000为成功 其他为失败
            if (code.equals("0000")){
                JSONObject dataJson=JSONUtil.parseObj(resultJson.getStr("data"));//获取返回data数据转化为JSON对象
                String corderNo=dataJson.getStr("orderNo");
                String cappOrderNo=dataJson.getStr("appOrderNo");
                String corderAmt=dataJson.getStr("orderAmt");
                String corderTime=dataJson.getStr("orderTime");
                String corderStatus=dataJson.getStr("orderStatus");
                String csign=dataJson.getStr("sign");
                HashMap cmap = new HashMap<>();
                cmap.put("orderNo",corderNo);
                cmap.put("appOrderNo",cappOrderNo);
                cmap.put("orderAmt",corderAmt);
                cmap.put("orderTime",corderTime);
                cmap.put("orderStatus",corderStatus);
                String ccsign = Util.creatSign(map, key);
                if (csign.equals(ccsign)){//验签
                    //TODO 进行操作
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @RequestMapping("/notify.do")
    public String notify(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setHeader("Access-Control-Allow-Origin", "*");
        String key="";//商户对应key
        String appId=req.getParameter("appId");
        String orderNo=req.getParameter("orderNo");
        String appOrderNo=req.getParameter("appOrderNo");
        String orderAmt=req.getParameter("orderAmt");
        String orderTime=req.getParameter("orderTime");
        String orderStatus=req.getParameter("orderStatus");
        String sign=req.getParameter("sign");
        HashMap map = new HashMap<>();
        map.put("appId",appId);
        map.put("orderNo",orderNo);
        map.put("appOrderNo",appOrderNo);
        map.put("orderAmt",orderAmt);
        map.put("orderTime",orderTime);
        map.put("orderStatus",orderStatus);
        String csign = Util.creatSign(map, key);//计算签名
        if (csign.equals(sign)){
            //判断签名是否正确
            //TODO 任意事情
            return "SUCCESS";//订单没有任何问题 返回SUCCESS
        }
        return "Error";
    }

}

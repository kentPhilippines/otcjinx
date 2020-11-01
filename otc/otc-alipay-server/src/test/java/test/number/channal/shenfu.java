package test.number.channal;

import alipay.manage.api.channel.util.shenfu.PayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class shenfu {
    static SimpleDateFormat d = new SimpleDateFormat("yyyyMMddHHmmss");
    public static final Log log = LogFactory.get();
    public static void main(String[] args) {
        test();
    }

    private static SimpleDateFormat time = new SimpleDateFormat("yyyyMMddHHmmss");

    private static void test() {
        String p1_merchantno = "MER20201030214604612949";
        String p2_amount = "100.00";
        String p3_orderno = StrUtil.uuid();
        String p4_paytype = "WechatH5_LC";
        String p5_reqtime = time.format(new Date());
        String p6_goodsname = "WechatH5_LC";
        String p8_returnurl = "http://pay.miaodapay.com/orders/post";
        String p9_callbackurl = "http://pay.miaodapay.com/orders/post";
        Map map = new HashMap();
        map.put("p1_merchantno", p1_merchantno);
        map.put("p2_amount", p2_amount);
        map.put("p3_orderno", p3_orderno);
        map.put("p4_paytype", p4_paytype);
        map.put("p5_reqtime", p5_reqtime);
        map.put("p6_goodsname", p6_goodsname);
        map.put("p8_returnurl", p8_returnurl);
        map.put("p9_callbackurl", p9_callbackurl);
        log.info("【绅付话费加签参数：" + map + "】");
        String createParam = PayUtil.createParam(map);
        String md5 = PayUtil.md5(createParam + "&key=" + "c311c01b90364ee5a1f448f86ae41efd");
        map.put("sign", md5.toUpperCase());
        log.info("【绅付话费请求参数：" + map + "】");
        String post = HttpUtil.post("https://sf2api.ceriumpower.com/pay", map);
        log.info("【绅付话费返回数据：" + post + "】");
        JSONObject jsonObject = JSONUtil.parseObj(post);
        String rspcode = jsonObject.getStr("rspcode");
        if (rspcode.equals("A0")) {
            String payurl = jsonObject.getStr("data");
        } else {
            String errmsg = jsonObject.getStr("rspmsg");
        }


/**
 * {"data":"","rspcode":"A4","rspmsg":"签名异常",
 * "sign":"96DB45D9CF54272F8F9900B33DB9059D"}】
 */

        /**
         *{"data":"http://sf2api.ceriumpower.com/pay/gopay?OrderNO=M20110115174920346536"
         * ,"rspcode":"A0","rspmsg":"",
         * "sign":"CA4B7BBC809AB656766193535CEB9393"}】
         */

        //   alipay.manage.api.channel.deal.PddBean bean = JSONUtil.toBean(post, alipay.manage.api.channel.deal.PddBean.class);
    /*    if(ObjectUtil.isNotNull(bean)) {
            if(bean.getRet_code().equals("0000")) {
                return bean;
            }
        }
        return null;*/
    }

    class PddBean {
        private String money_order;
        private String no_order;
        private String oid_partner;
        private String redirect_url;
        private String ret_code;
        private String ret_msg;
        private String sign;

        public String getMoney_order() {
            return money_order;
        }

        public void setMoney_order(String money_order) {
            this.money_order = money_order;
        }

        public String getNo_order() {
            return no_order;
        }

        public void setNo_order(String no_order) {
            this.no_order = no_order;
        }

        public String getOid_partner() {
            return oid_partner;
        }

        public void setOid_partner(String oid_partner) {
            this.oid_partner = oid_partner;
        }

        public String getRedirect_url() {
            return redirect_url;
        }

        public void setRedirect_url(String redirect_url) {
            this.redirect_url = redirect_url;
        }

        public String getRet_code() {
            return ret_code;
        }

        public void setRet_code(String ret_code) {
            this.ret_code = ret_code;
        }

        public String getRet_msg() {
            return ret_msg;
        }

        public void setRet_msg(String ret_msg) {
            this.ret_msg = ret_msg;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        @Override
        public String toString() {
            return "PddBean [money_order=" + money_order + ", no_order=" + no_order + ", oid_partner=" + oid_partner
                    + ", redirect_url=" + redirect_url + ", ret_code=" + ret_code + ", ret_msg=" + ret_msg + ", sign="
                    + sign + "]";
        }
    }
}

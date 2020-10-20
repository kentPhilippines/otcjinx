package test.number.channal;

import alipay.manage.api.channel.util.shenfu.PayUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class shenfu {
    static SimpleDateFormat d = new SimpleDateFormat("yyyyMMddHHmmss");

    public static void main(String[] args) {
        test();
    }

    private static void test() {
        Map<String, Object> map = new HashMap();
        map.put("oid_partner", PayUtil.APPID);
        map.put("notify_url", "www.baidu.com");
        map.put("sign_type", "MD5");
        map.put("user_id", IdUtil.objectId());
        map.put("no_order",  IdUtil.objectId());
        map.put("time_order", d.format(new Date()));
        map.put("money_order",  "100.00");
        map.put("name_goods", "huafei");
        map.put("pay_type", "403");//PDD PDD 插件通道
        map.put("info_order", "info_order");
        String createParam = PayUtil.createParam(map);
        System.out.println("【绅付支付宝扫码请求参数：" + createParam + "】");
        String md5 = PayUtil.md5(createParam + PayUtil.KEY);
        map.put("sign", md5);
        String post = HttpUtil.post(PayUtil.URL, map);
        System.out.println("【绅付支付扫码返回数据：" + post + "】");
        System.out.println(post);


        System.out.println(post);
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

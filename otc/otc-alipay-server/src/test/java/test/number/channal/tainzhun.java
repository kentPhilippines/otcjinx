package test.number.channal;

public class tainzhun {


    public static void main(String[] args) {







      deal();
        wit();

    }

    private static void wit() {



    }

/**
 * 彗星 Comet Pay, [Mar 17, 2023 at 20:39:12 (Mar 17, 2023 at 20:39:24)]:
 * 天尊支付  RMB   通道
 *
 * 接入文档:
 * https://usa.vppps.xyz/Home_Index_document.html
 *
 * 支付/代付网关: https://usa.vppps.xyz/Pay_Index.html
 * 查询网关: https://usa.vppps.xyz/Pay_Trade_query.html
 * 通道代码(即api中的pay_bankcode):
 * 938: 代收
 * 941: 代付
 * 回调ip: 45.150.165.214和116.202.20.205
 *
 * 支付网关补充说明:
 * 1. 请设置 pay_returntype=json
 * 2. 请在文档的参数外，额外增加 realname项，为玩家的实名姓名，编码方式UTF8。不需要签名。
 * 3. 接入测试时，玩家姓名，就是realname，可以填写名字为 测试， 该名字在白名单。 但正式运行时需要使用玩家真实姓名。
 * 4.代付状态 ：00、 01是成功，04是失败。
 * 938通道成功 json，rst:1, payurl: 支付页地址     需要重定向/跳转(302)到该地址。返回的payurl中的参数已经做了urlencode，请勿重复编码。
 * 938通道失败 json, 两种可能的格式  status:error,msg:错误信息  或者  rst:0,msg:错误信息
 * 941通道成功 json, result:1
 * 941通道失败 json, 两种可能的格式  status:error,msg:错误信息  或者  result:0,msg:错误信息
 */
    /**
     * 参数名称	                    参数含义	        是否必填	参与签名	参数说明
     * pay_memberid	                商户号	        是	是	平台分配商户号
     * pay_orderid	                订单号	        是	是	上送订单号唯一, 字符长度20
     * pay_applydate	            提交时间	        是	是	时间格式：2016-12-26 18:18:18
     * pay_bankcode	                银行编码	        是	是	在商户中心查询
     * pay_notifyurl	            服务端通知	    是	是	服务端返回地址.（POST返回数据）
     * pay_callbackurl	            页面跳转通知	    是	是	页面跳转返回地址（POST返回数据）
     * pay_amount	                订单金额	        是	是	单位：元
     * pay_md5sign	                MD5签名	        是	是	请查看签名算法
     * pay_productname	            商品名称	        是	否
     */
    private static void deal() {
        String key = "62lkwwia3agdqokqjbhy1w1w06iq9svq";

        String pay_memberid = "230320229";
        String pay_orderid = System.currentTimeMillis()+"";
        String pay_applydate = "";
        String pay_bankcode = "";
        String pay_notifyurl = "";
        String pay_callbackurl = "";
        String pay_amount = "";
        String pay_md5sign = "";



    }
}

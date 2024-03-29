package alipay.manage.bean.util;

public class ResultDeal {
    private boolean sussess;    //是否成功	        True 成功  false  失败
    private Integer cod;    //订单状态码【“0”为成功】	详情请查看响应状态码
    private Integer openType;//	打开方式	【1】为url打开方式【2】为html浏览器打开方式
    private String returnUrl = "";    //支付内容
    private String payInfo = "";
    private String payInfo1 = "";

    public ResultDeal(boolean sussess, Integer cod, Integer openType, Object returnUrl,String payInfo,String payInfo1) {
        super();
        this.sussess = sussess;
        this.cod = cod;
        this.openType = openType;
        this.returnUrl = returnUrl.toString();
        this.payInfo  =  payInfo.toString();
        this.payInfo1  =  payInfo1.toString();
    }

    public static ResultDeal sendHtml(Object returnUrl) {
        return new ResultDeal(true, 1, 2, returnUrl,"","");
    }

    public static ResultDeal sendUrl(Object returnUrl) {
        return new ResultDeal(true, 1, 1, returnUrl,"","");
    }
    public static ResultDeal sendUrlAndPayInfo(Object returnUrl,String payInfo) {
        return new ResultDeal(true, 1, 1, returnUrl,payInfo,"");
    }
    public static ResultDeal sendUrlAndPayInfo1(Object returnUrl,String payInfo,String payInfo1) {
        return new ResultDeal(true, 1, 1, returnUrl,payInfo,payInfo1);
    }

    public boolean isSussess() {
        return sussess;
    }

    public void setSussess(boolean sussess) {
        this.sussess = sussess;
    }

    public Integer getCod() {
        return cod;
    }

    public void setCod(Integer cod) {
        this.cod = cod;
    }

    public Integer getOpenType() {
        return openType;
    }

    public void setOpenType(Integer openType) {
        this.openType = openType;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getPayInfo() {
        return payInfo;
    }

    public void setPayInfo(String payInfo) {
        this.payInfo = payInfo;
    }

    public String getPayInfo1() {
        return payInfo1;
    }

    public void setPayInfo1(String payInfo1) {
        this.payInfo1 = payInfo1;
    }

    @Override
    public String toString() {
        return "ResultDeal{" +
                "sussess=" + sussess +
                ", cod=" + cod +
                ", openType=" + openType +
                ", returnUrl='" + returnUrl + '\'' +
                ", payInfo='" + payInfo + '\'' +
                ", payInfo1='" + payInfo1 + '\'' +
                '}';
    }
}

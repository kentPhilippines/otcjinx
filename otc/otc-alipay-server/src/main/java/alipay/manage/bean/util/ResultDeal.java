package alipay.manage.bean.util;

public class ResultDeal {
    private boolean sussess;    //是否成功	        True 成功  false  失败
    private Integer cod;    //订单状态码【“0”为成功】	详情请查看响应状态码
    private Integer openType;//	打开方式	【1】为url打开方式【2】为html浏览器打开方式
    private String returnUrl;    //支付内容

    public ResultDeal(boolean sussess, Integer cod, Integer openType, Object returnUrl) {
        super();
        this.sussess = sussess;
        this.cod = cod;
        this.openType = openType;
        this.returnUrl = returnUrl.toString();
    }

    public static ResultDeal sendHtml(Object returnUrl) {
        return new ResultDeal(true, 0, 2, returnUrl);
    }

    public static ResultDeal sendUrl(Object returnUrl) {
        return new ResultDeal(true, 0, 1, returnUrl);
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
}

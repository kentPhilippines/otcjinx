package alipay.manage.bean.util.v2;

public class ResultDeal {
    public static final String URL = "open_url";
    public static final String INFO = "open_info";
    public static final String URL_AND_INFO = "open_url_info";
    private boolean success;    //是否成功	        True 成功  false  失败
    private String openType;//	打开方式	【1】为url打开方式【2】为html浏览器打开方式
    private String returnUrl;    //支付内容
    private String payInfo;

    public ResultDeal(boolean success, String openType, Object returnUrl, String payInfo) {
        super();
        this.success = success;
        this.openType = openType;
        this.returnUrl = returnUrl.toString();
        this.payInfo = payInfo.toString();
    }

    public static ResultDeal sendUrl(String returnUrl) {
        return new ResultDeal(true, URL, returnUrl,"");
    }

    public static ResultDeal sendUrlAndPayInfo(String returnUrl, String payInfo) {
        return new ResultDeal(true, URL_AND_INFO,  returnUrl,payInfo);
    }

    public static ResultDeal sendPayInfo(String returnUrl, String payInfo) {
        return new ResultDeal(true, INFO, returnUrl, payInfo);
    }


    /**
     * 上有渠道处理错误
     * @return
     */
    public static ResultDeal error() {
        return new ResultDeal(false, "", "",  "");
    }




    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getOpenType() {
        return openType;
    }

    public void setOpenType(String openType) {
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


    @Override
    public String toString() {
        return "ResultDeal{" +
                "success=" + success +
                ", openType='" + openType + '\'' +
                ", returnUrl='" + returnUrl + '\'' +
                ", payInfo='" + payInfo + '\'' +
                '}';
    }
}


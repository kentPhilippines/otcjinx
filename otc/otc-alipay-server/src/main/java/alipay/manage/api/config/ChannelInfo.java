package alipay.manage.api.config;

public class ChannelInfo {
    private String channelAppId;
    private String dealurl;
    private String channelPassword;
    private String channelType;
    private String witUrl;
    private String balanceUrl;
    private String email;
    private String privateKey;

    public String getWitUrl() {
        return witUrl;
    }

    public void setWitUrl(String witUrl) {
        this.witUrl = witUrl;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getChannelPassword() {
        return channelPassword;
    }

    public void setChannelPassword(String channelPassword) {
        this.channelPassword = channelPassword;
    }

    public String getDealurl() {
        return dealurl;
    }

    public void setDealurl(String dealurl) {
        this.dealurl = dealurl;
    }


    public String getBalanceUrl() {
        return balanceUrl;
    }

    public void setBalanceUrl(String balanceUrl) {
        this.balanceUrl = balanceUrl;
    }

    public String getChannelAppId() {
        return channelAppId;
    }

    public void setChannelAppId(String channelAppId) {
        this.channelAppId = channelAppId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
}

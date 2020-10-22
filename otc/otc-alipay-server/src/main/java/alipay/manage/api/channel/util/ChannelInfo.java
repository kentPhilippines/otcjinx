package alipay.manage.api.channel.util;

public class ChannelInfo {
    private String channelAppId;
    private String dealurl;
    private String channelPassword;
    private String channelType;


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

    public String getChannelAppId() {
        return channelAppId;
    }

    public void setChannelAppId(String channelAppId) {
        this.channelAppId = channelAppId;
    }
}

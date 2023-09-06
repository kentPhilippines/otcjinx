package alipay.manage.api.channel.util;

import lombok.Data;

@Data
public class ChannelInfo {
    private String channelAppId;
    private String dealurl;
    private String channelPassword;
    private String channelType;
    private String witUrl;
    private String balanceUrl;
}

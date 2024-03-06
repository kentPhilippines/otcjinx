package alipay.manage.api.config;

import lombok.Data;

@Data
public class ChannelInfo {
    private String channelAppId;
    private String dealurl;
    private String channelPassword;
    private String channelType;
    private String witUrl;
    private String balanceUrl;
    private String email;
    private String privateKey;

}

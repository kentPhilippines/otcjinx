package alipay.manage.api.channel.amount;

/**
 * @author water
 */
public class BalanceInfo {
    private String time;
    private String balance;
    private String channel;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}

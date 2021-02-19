package alipay.manage.api.channel.amount.recharge.usdt;

public class USDTOrder {
    private String blockNumber;// 区块号
    private String timeStamp;//时间
    private String hash;   //hash编码
    private String blockHash; // 区块hash
    private String from;        //来自某一个账户
    private String contractAddress;  //   合同地址
    private String to;              //   到某一个账户  收款人账户
    private String value;           // 多少钱
    private String tokenName;   //令牌名称
    private String tokenSymbol; //令牌符号
    private String fromNow;   //付款账户当前余额
    private String toNow;      //  收款账户当前余额

    public USDTOrder() {
        super();
    }

    public String getToNow() {
        return toNow;
    }

    public void setToNow(String toNow) {
        this.toNow = toNow;
    }

    public String getFromNow() {
        return fromNow;
    }

    public void setFromNow(String fromNow) {
        this.fromNow = fromNow;
    }

    @Override
    public String toString() {
        return "USDTOrder{" +
                "blockNumber='" + blockNumber + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", hash='" + hash + '\'' +
                ", blockHash='" + blockHash + '\'' +
                ", from='" + from + '\'' +
                ", contractAddress='" + contractAddress + '\'' +
                ", to='" + to + '\'' +
                ", value='" + value + '\'' +
                ", tokenName='" + tokenName + '\'' +
                ", tokenSymbol='" + tokenSymbol + '\'' +
                '}';
    }

    public String getTokenSymbol() {
        return tokenSymbol;
    }

    public void setTokenSymbol(String tokenSymbol) {
        this.tokenSymbol = tokenSymbol;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(String blockNumber) {
        this.blockNumber = blockNumber;
    }
}

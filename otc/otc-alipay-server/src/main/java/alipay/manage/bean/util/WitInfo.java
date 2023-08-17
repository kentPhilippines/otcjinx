package alipay.manage.bean.util;

public class WitInfo {
    private String bankNo;
    private String bankName;
    private String account;
    private String bankCode;

    @Override
    public String toString() {
        return "WitInfo{" +
                "bankNo='" + bankNo + '\'' +
                ", bankName='" + bankName + '\'' +
                ", account='" + account + '\'' +
                ", bankCode='" + bankCode + '\'' +
                '}';
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }
}

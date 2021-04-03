package alipay.manage.bean.util;

public class OrderAndUserInfo {
    private String userAmount;
    private String witSum;
    private String dealSum;
    private String sign;

    @Override
    public String toString() {
        return "OrderAndUserInfo{" +
                "userAmount='" + userAmount + '\'' +
                ", witSum='" + witSum + '\'' +
                ", dealSum='" + dealSum + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getDealSum() {
        return dealSum;
    }

    public void setDealSum(String dealSum) {
        this.dealSum = dealSum;
    }

    public String getWitSum() {
        return witSum;
    }

    public void setWitSum(String witSum) {
        this.witSum = witSum;
    }

    public String getUserAmount() {
        return userAmount;
    }

    public void setUserAmount(String userAmount) {
        this.userAmount = userAmount;
    }
}

import alipay.manage.bean.UserInfo;
import alipay.manage.util.CheckUtils;
import otc.result.Result;

public class file {


    public static void main(String[] args) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId("XPJ18");
        userInfo.setPayPasword("432343243");
        userInfo.setInterFace("http://173.248.246.96:96/fetchOrder/jinxing  ");
        Result result = CheckUtils.enterWit(userInfo, "123123123123123", 5000);
        System.out.println(result.toString());

    }

}


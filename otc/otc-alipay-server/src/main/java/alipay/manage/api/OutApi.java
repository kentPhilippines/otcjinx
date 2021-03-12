package alipay.manage.api;

import alipay.manage.bean.UserInfo;
import alipay.manage.service.UserInfoService;
import cn.hutool.core.util.IdUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import otc.result.Result;
import otc.util.RSAUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/out")
@RestController
public class OutApi {
    private static final Log log = LogFactory.get();
    private static final String UTF_8 = "utf-8";
    private static final String ENCODE_TYPE = "md5";
    @Autowired
    UserInfoService userInfoServiceImpl;

    public static String md5(String a) {
        String c = "";
        MessageDigest md5;
        String result = "";
        try {
            md5 = MessageDigest.getInstance(ENCODE_TYPE);
            md5.update(a.getBytes(UTF_8));
            byte[] temp;
            temp = md5.digest(c.getBytes(UTF_8));
            for (int i = 0; i < temp.length; i++) {
                result += Integer.toHexString((0x000000ff & temp[i]) | 0xffffff00).substring(6);
            }
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
        }
        return result;
    }

    @GetMapping("/updatePassword")
    public Result updatePassword(String userId) {
        List<UserInfo> userList = userInfoServiceImpl.finauserAll(userId);
        int a = 0;
        Map map = new HashMap<>();
        for (UserInfo user : userList) {
            List<String> strings = RSAUtils.genKeyPair();
            String publickey = strings.get(0);
            String privactkey = strings.get(1);
            String key = md5(IdUtil.objectId().toUpperCase() + IdUtil.objectId().toUpperCase()).toUpperCase();
            log.info("【商户" + user.getUserId() + ",执行更新密钥方法】");
            boolean flag = userInfoServiceImpl.updateDealKey(user.getUserId(), publickey, privactkey, key);
            if (flag) {
                log.info("【商户" + user.getUserId() + ",执行更新密钥方法,成功】");
            } else {
                a++;
                log.info("【商户" + user.getUserId() + ",执行更新密钥方法,失败】");
                map.put(user.getUserId(), user.getUserId());
            }
        }
        if (a == 0) {
            return Result.buildFailMessage("更新密钥成功");
        }
        return Result.buildFailMessage("更换密钥失败,失败个数,失败商户详情：" + map.toString());
    }
}

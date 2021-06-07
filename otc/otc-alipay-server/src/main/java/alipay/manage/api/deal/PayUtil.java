package alipay.manage.api.deal;

import alipay.manage.api.AccountApiService;
import alipay.manage.bean.UserInfo;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.DES;
import cn.hutool.http.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.result.Result;
import otc.util.RSAUtils;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;


@Component
public class PayUtil {
    static final String CHAR = "UTF-8";

    static {
        Charset.forName(CHAR);
    }

    Logger log = LoggerFactory.getLogger(PayUtil.class);
    @Autowired
    private AccountApiService accountApiServiceImpl;

    public Result decode(HttpServletRequest request) {
        //request    可以获取到 des  密文   和  用户账号，
        String userId = request.getParameter("userId");
        String cipher = request.getParameter("cipher");
        if (StrUtil.isEmpty(userId)) {
            log.info("请求加密账号为空");
            return Result.buildFailMessage("加密账号为空");
        }
        if (StrUtil.isEmpty(cipher)) {
            log.info("请求加密密文为空");
            return Result.buildFailMessage("请求加密密文为空");
        }
        UserInfo userInfo = accountApiServiceImpl.findPrivateKey(userId);
        if (null == userInfo || ObjectUtil.isNull(userInfo)) {
            return Result.buildFailMessage("用户不存在");
        }
        String desPassword = userInfo.getPayPasword();
        /**
         * 加密mode 为  cfb  , 补码方式为 PKCS5Padding  , 偏移量为 用户账号 userId
         */
        String salt = "";
        if (desPassword.length() < 24) {
            desPassword += desPassword;
        }
        if (desPassword.length() != 24) {
            String s = StrUtil.subWithLength(desPassword, 0, 24);
            desPassword = s;
        } else {
            desPassword = desPassword;
        }
        salt = desPassword.substring(4, 12);
        DES des = new DES(Mode.CFB, Padding.PKCS5Padding, desPassword.getBytes(), salt.getBytes());
        String data = "";
        try {
            String str = HttpUtil.decode(StrUtil.str(des.decrypt(cipher), "utf-8"), Charset.defaultCharset());//这里即为 用户需要加密的内容
            log.info("【des 解密内容：" + str + "】");
            data = RSAUtils.publicEncrypt(str, userInfo.getPublicKey());
        } catch (Exception e) {
            return Result.buildFailMessage("加密失败");
        }
        return Result.buildSuccessResult("加密成功", data);
    }

}

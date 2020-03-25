package alipay.manage.util;

import alipay.config.exception.OtherErrors;
import alipay.manage.bean.UserInfo;
import alipay.manage.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import otc.result.Result;
import otc.util.StringUtils;

import java.util.List;

/**
 * <p>代理商开户相关接口</p>
 * @author GIGABYTE
 *
 */
@Component
public class AgentApi {
     @Autowired
     UserInfoService userInfoService;
     @Autowired
     SettingFile settingFile;
    public Result openAgentAccount(UserInfo user){
        /**
         * 开户逻辑
         * 1.验证账户
         * 2.验证费率
         * 3.添加数据
         */
        if (StringUtils.isEmpty(user.getUserId()))
            return Result.buildFailMessage("账号不能为空");
        if (StringUtils.isEmpty(user.getUserName()))
            return Result.buildFailMessage("账号名不能为空");
        if (StringUtils.isEmpty(user.getPayPasword()))
            return Result.buildFailMessage("资金密码不能为空");
        if (StringUtils.isEmpty(user.getIsAgent()))
            return Result.buildFailMessage("角色不能为空");
        //判断商户是否存在
        List<UserInfo> loginAccountInfo = userInfoService.getLoginAccountInfo(user.getUserId());
        if (loginAccountInfo!=null && loginAccountInfo.size()!=0)
            return Result.buildFailMessage("此账号已经存在");
        UserInfo qruser=new UserInfo();
        qruser.setUserId(user.getAgent());
        //查到当前代理商的费率
        UserInfo qrcodeUser=userInfoService.getQrCodeUser(qruser);
        if (qrcodeUser==null)
            return Result.buildFailMessage("当前登录账号不存在");
        //新增数据
        boolean s=this.insertUsers(user);
        if (!s)
            return Result.buildFailMessage("添加失败");
        return Result.buildSuccessMessage("添加成功");
    }

    /**
     * 添加用户
     * @param user
     * @return
     */
    private boolean insertUsers(UserInfo user) {
        boolean retu=false;
        //密码加密
        String password = user.getPassword();
        password= Md5Util.md5(password);
        String payPassword = Md5Util.md5(user.getPayPasword());
        UserInfo entity=new UserInfo();
        entity.setUserName(user.getUserId());
        entity.setPassword(password);
        entity.setPayPasword(payPassword);
        entity.setStatus(1);//1:数据可用 2:数据无用
        entity.setEmail(settingFile.getName(""));
        boolean flag=userInfoService.addQrcodeUserInfo(entity);
        if (!flag)
           throw new OtherErrors("添加失败");
        //新增码商代理商数据
        boolean flag1=userInfoService.addQrcodeUser(user);
        if (!flag1)
            throw new OtherErrors("添加失败");
        retu=true;
        return retu;
    }

}

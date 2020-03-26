package alipay.manage.contorller;

import alipay.config.redis.RedisUtil;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.util.PageResult;
import alipay.manage.service.MediumService;
import alipay.manage.util.SessionUtil;
import alipay.manage.util.SettingFile;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import otc.bean.alipay.Medium;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/qrcode")
public class QrcodeContorller {
    @Autowired SessionUtil sessionUtil;
    @Autowired MediumService mediumServiceImpl;
    @Autowired RedisUtil redisUtil;
    @Autowired SettingFile settingFile;
    @GetMapping("/findIsMyQrcodePage")
    @ResponseBody
    public Result findIsMyQrcodePage(HttpServletRequest request, String pageNum, String pageSize ) {
        UserInfo user = sessionUtil.getUser(request);
        if(ObjectUtil.isNull(user))
            return Result.buildFailResult("用户未登录");
        PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
        List<Medium> qmList = mediumServiceImpl.findIsMyMediumPage(user.getUserId());
        //List<QrCode> qrList = qrCodeServiceImpl.findIsMyQrcodePage(qrcode);
        PageInfo<Medium> pageInfo = new PageInfo<Medium>(qmList);
        PageResult<Medium> pageR = new PageResult<Medium>();
        pageR.setContent(pageInfo.getList());
        pageR.setPageNum(pageInfo.getPageNum());
        pageR.setTotal(pageInfo.getTotal());
        pageR.setTotalPage(pageInfo.getPages());
        return Result.buildSuccessResult(pageR);
    }
}

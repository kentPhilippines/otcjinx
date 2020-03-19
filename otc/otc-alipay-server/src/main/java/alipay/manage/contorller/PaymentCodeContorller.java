package alipay.manage.contorller;

import alipay.config.exception.OtherErrors;
import alipay.config.exception.ParamException;
import alipay.config.redis.RedisUtil;
import alipay.manage.bean.FileList;
import alipay.manage.bean.Medium;
import alipay.manage.bean.UserInfo;
import alipay.manage.bean.util.PageResult;
import alipay.manage.service.FileListService;
import alipay.manage.service.MediumService;
import alipay.manage.service.UserInfoService;
import alipay.manage.util.QueueQrcodeUtil;
import alipay.manage.util.SessionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 付款码
 */
@Controller
@RequestMapping("/statisticalAnalysis")
public class PaymentCodeContorller {
    Logger log = LoggerFactory.getLogger(PaymentCodeContorller.class);
    @Autowired
    SessionUtil sessionUtil;
    @Autowired
    MediumService mediumServicel;
    @Autowired
    FileListService fileListService;
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    QueueQrcodeUtil queueQrcodeUtil;
    @Autowired
    RedisUtil redisUtil;
    @GetMapping("/findMediumsByPage")
    @ResponseBody
    public Result findMediumsByPage(Medium medium, HttpServletRequest request, String pageNum, String pageSize) {
        UserInfo user = sessionUtil.getUser(request);
        PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
        if (ObjectUtil.isNull(user))
            throw new OtherErrors("当前用户未登录");
        medium.setQrcodeId(user.getUserId());
        List<Medium> list = mediumServicel.findMedium(medium);
        PageInfo<Medium> pageInfo = new PageInfo<Medium>(list);
        PageResult<Medium> pageR = new PageResult<Medium>();
        pageR.setContent(pageInfo.getList());
        pageR.setPageNum(pageInfo.getPageNum());
        pageR.setTotal(pageInfo.getTotal());
        pageR.setTotalPage(pageInfo.getPages());
        return Result.buildFailResult(pageR);
    }

    /**
     * <p>获取与当前登录用户相关的二维码图片账号</p>
     *
     * @param qr
     * @return
     */
    @GetMapping("/findMyGatheringCodeByPage")
    @ResponseBody
    public Result findMyGatheringCodeByPage(FileList qr, HttpServletRequest request, String pageNum, String pageSize) {
        UserInfo user = sessionUtil.getUser(request);
        PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
        if (ObjectUtil.isNull(user))
            throw new OtherErrors("当前用户未登录");
        qr.setFileholder(user.getUserId());
        List<FileList> list = fileListService.findQrPage(qr);
        PageInfo<FileList> pageInfo = new PageInfo<FileList>(list);
        PageResult<FileList> pageR = new PageResult<FileList>();
        pageR.setContent(pageInfo.getList());
        pageR.setPageNum(pageInfo.getPageNum());
        pageR.setTotal(pageInfo.getTotal());
        pageR.setTotalPage(pageInfo.getPages());
        return Result.buildSuccessResult(pageR);
    }

    /**
     * <p>获取当前收款媒介下所有的二维码</p>
     *
     * @param mediumId
     * @param request
     * @return
     */
    @PostMapping("/findQrByMediumId")
    @ResponseBody
    public Result findQrByMediumId(@RequestBody String mediumId, HttpServletRequest request) {
        UserInfo user = sessionUtil.getUser(request);
        if (ObjectUtil.isNull(user))
            throw new OtherErrors("当前用户未登录");
        log.info("接受的介质参数为：" + mediumId);
        List<FileList> qrList = fileListService.findQrByMediumId(mediumId);
        return Result.buildSuccessResult(qrList);
    }
    /**
     * <p>添加收款媒介</p>
     *
     * @param medium
     * @param request
     * @return
     */
    @PostMapping("/addMedium")
    @ResponseBody
    public Result addMedium(@RequestBody Medium medium, HttpServletRequest request) {
        UserInfo user = sessionUtil.getUser(request);
        if (ObjectUtil.isNull(user))
            throw new ParamException("未获取到登录用户");
        medium.setQrcodeId(user.getUserId());
        if (queueQrcodeUtil.getList().contains(medium.getMediumNumber()))
            return Result.buildFailResult("当前收款媒介正在接单排队，禁止操作");
        boolean flag = mediumServicel.addMedium(medium);
     /*
        if (!redisUtil.hasKey(medium.getMediumNumber() + RedisConstant.User.QUEUEQRNODE)) {
            queueQrcodeUtil.addNode(medium.getMediumNumber());
            redisUtil.set(medium.getMediumNumber() + RedisConstant.User.QUEUEQRNODE, medium.getMediumNumber());//支付宝放入标记
        }
        */
        if (flag) {
            return Result.buildSuccessResult();
        }
        return Result.buildFailResult("支付宝账户重复或其他原因");
    }
    /**
     * 上级查询下级在线人数
     *
     * @param request
     * @return
     */
    @GetMapping("/querySubOnline")
    @ResponseBody
    public Result querySubOnline(HttpServletRequest request) {
        UserInfo user = sessionUtil.getUser(request);
        if (ObjectUtil.isNull(user)) {
            throw new ParamException("未获取到登录用户");
        }
//        //登陆状态的Key
//        Set<String> loginKeys = redisUtil.keys(RedisConstant.User.LOGIN_PARENT + "*");
//        //获取所有key的值
//        List<Object> loginKeysValue = redisUtil.multiGet(loginKeys);
//        //查询用户的下级用户
//        List<String> subLevelMembers = userInfoService.findSubLevelMembers(user.getUserId());
//        String str = CollUtil.getFirst(subLevelMembers);
//
//        //两个集合的交集 在线人员集合
//        List<String> loginMembers = subLevelMembers.stream().filter(item -> loginKeysValue.contains(item)).collect(toList());
//        //接单状态队列
//        Set<String> bizKeyMembers = redisUtil.keys(RedisConstant.User.BIZ_QUEUE + "*");
//        //获取所有key的值
//        List<Object> bizMembersValue = redisUtil.multiGet(bizKeyMembers);
//        //取两个集合的交集 接单人员集合
//        List<String> bizingMembers = subLevelMembers.stream().filter(item -> bizMembersValue.contains(item)).collect(toList());
//        OnlineVO onlineVO = new OnlineVO();
//        onlineVO.setLoginOnlineCount(loginMembers.size());
//        onlineVO.setBizOnlineCount(bizingMembers.size());
//        onlineVO.setOnlineList(loginMembers.size() == 0 ? "" : StringUtils.join(loginMembers.toArray(), "，"));
//        onlineVO.setBizList(bizingMembers.size() == 0 ? "" : StringUtils.join(bizingMembers.toArray(), "，"));
//        onlineVO.setIsAgent(user.getIsAgent());
//        return Result.buildSuccessResult("数据获取成功", onlineVO);
        return null;
    }
}

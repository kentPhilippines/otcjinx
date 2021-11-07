package alipay.manage.api;

import alipay.manage.bean.UserInfo;
import alipay.manage.util.CheckUtils;
import alipay.manage.util.LogUtil;
import alipay.manage.util.UserUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import otc.api.alipay.Common;
import otc.common.PayApiConstant;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RequestMapping(PayApiConstant.Alipay.ACCOUNT_API)
@RestController
public class AccountApi {
    Logger log = LoggerFactory.getLogger(AccountApi.class);
    @Autowired AccountApiService accountApiServiceImpl;
    @Autowired LogUtil logUtil;
    @Autowired UserUtil userUtil;
    @Autowired CheckUtils checkUtils;
    /**
     * <p>开户接口</p>
     * <P>当前开户接口只接受代理商开始，且该商户只能为码商</P>
     *
     * @param user
     * @return
     */
    @PostMapping(PayApiConstant.Alipay.ADD_ACCOUNT)
    @Transactional
    public Result addAccount(UserInfo user) {
        if (ObjectUtil.isNull(user)) {
            return Result.buildFailMessage("实体类为空，请检查传递方法是否正确");
        }
        log.info("【远程调用开通顶级代理的方法】");
        if (StrUtil.isBlank(user.getUserId())
                || StrUtil.isBlank(user.getUserName())
                || ObjectUtil.isNull(user.getUserType())
                || StrUtil.isBlank(user.getIsAgent())
                || StrUtil.isBlank(user.getEmail())
        ) {
            return Result.buildFailMessage("必传参数为空");
        }
        if (!user.getUserType().toString().equals(Common.User.USER_TYPE_QR)) {
            return Result.buildFailMessage("开户账户类型不符合");
        }
        user.setIsAgent(Common.User.USER_IS_AGENT);
        Result addAccount = accountApiServiceImpl.addAccount(user);

        return addAccount;
    }

    @PostMapping(PayApiConstant.Alipay.EDIT_ACCOUNT)
    @Transactional
    public Result editAccount(UserInfo user) {
        log.info("【远程调用修改用户的方法】");
        if (ObjectUtil.isNull(user)) {
            return Result.buildFailMessage("实体类为空，请检查传递方法是否正确");
        }
        if (StrUtil.isBlank(user.getUserId())) {
            return Result.buildFailMessage("必传参数为空");
        }
        return accountApiServiceImpl.editAccount(user);
    }


    /*下面是商户的处理逻辑接口*/

	/**
	 * <p>修改商户的状态《用户状态》《交易状态》《代付状态》</p>
	 * @param request 请求request
	 * @return	返回结果
	 */
    @PostMapping(PayApiConstant.Alipay.AUDIT_MERCHANT_STATUS)
    @Transactional
    public Result auditStatus(HttpServletRequest request) {
        log.info("【远程调用修改商户状态的方法】");




        String params  = request.getQueryString();
        log.info("传入的参数："+ params);
		Map<String, Object> paramMap = checkUtils.paramToMap(params);
		String userId = paramMap.get("userId").toString();
		String paramKey = paramMap.get("paramKey").toString();
		String paramValue = paramMap.get("paramValue").toString();
		//修改数据库
        Result result = accountApiServiceImpl.auditMerchantStatusByUserId(userId,paramKey,paramValue);
		return result;
		// TODO: 2020/3/25 处理redis缓存 暂时使用数据库
    }

}

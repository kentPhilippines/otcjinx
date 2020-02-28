package alipay.manage.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import alipay.manage.bean.UserInfo;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import otc.api.alipay.Common;
import otc.common.PayApiConstant;
import otc.result.Result;

@RequestMapping(PayApiConstant.Alipay.ACCOUNT_API)
@RestController
public class AccountApi {
	@Autowired
	AccountApiService accountApiServiceImpl;
	/**
	 * <p>开户接口</p>
	 * <P>当前开户接口只接受代理商开始，且该商户只能为码商</P>
	 * @param user
	 * @return
	 */
	@GetMapping(PayApiConstant.Alipay.ADD_ACCOUNT)
	@Transactional
	public Result addAccount(UserInfo user) {
		if(ObjectUtil.isNull(user))
			return Result.buildFailMessage("实体类为空，请检查传递方法是否正确");
		if(StrUtil.isBlank(user.getUserId()) || StrUtil.isBlank(user.getUserName())
				||ObjectUtil.isNull(user.getUserType())
				|| StrUtil.isBlank(user.getIsAgent())
				|| StrUtil.isBlank(user.getEmail())
				) {
			return Result.buildFailMessage("必传参数为空");
		}
		if(!user.getUserType().toString().equals(Common.User.USER_TYPE_QR))
			return Result.buildFailMessage("开户账户类型不符合");
		user.setIsAgent(Common.User.USER_IS_AGENT);
		return accountApiServiceImpl.addAccount(user);
	}
	
	
	

}

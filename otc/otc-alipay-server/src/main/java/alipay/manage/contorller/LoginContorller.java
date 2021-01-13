package alipay.manage.contorller;

import alipay.manage.api.AccountApiService;
import alipay.manage.bean.UserInfo;
import alipay.manage.util.LogUtil;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import otc.result.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;

@Controller
public class LoginContorller {
	Logger log = LoggerFactory.getLogger(LoginContorller.class);
	@Autowired
	LogUtil logUtil;
	@Autowired
	AccountApiService accountApi;
	/**
	 * <p>登录</p>
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws NumberFormatException 
	 */
	@PostMapping("/login")
	@ResponseBody
	public Result login(HttpServletRequest request) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		if (StrUtil.isBlank(username)) {
			return Result.buildFailMessage("请输入用户名");
		}
		if (StrUtil.isBlank(password)) {
			return Result.buildFailMessage("请输入密码");
		}
		UserInfo user = new UserInfo();
		user.setUserId(username);
		user.setPassword(password);
		Result login = accountApi.login(user);
		if (login.isSuccess()) {
			HttpSession session = request.getSession();
			session.setAttribute("user", user);
			log.info("登录成功，登录人：" + username);
			logUtil.addLog(request, "登录成功", username);
			return Result.buildSuccessMessage("登录成功");
	    }
	    return login;
	}
	/**
	 * <p>退出登录</p>
	 * @return
	 */
	@PostMapping("/logout")
	@ResponseBody
	public Result logout(HttpSession session) {
		  session.invalidate();
		return null;
	}
}

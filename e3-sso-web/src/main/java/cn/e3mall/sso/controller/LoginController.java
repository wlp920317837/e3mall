package cn.e3mall.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.sso.service.LoginService;

/**
 * 登录表现层
 * @author wlp
 *
 */

@Controller
public class LoginController {

	@Value("${TOKEN_KEY}")
	private String TOKEN_KEY;

	@Autowired
	private LoginService loginService;

	@RequestMapping("/page/login")
	public String showLoginPage(String redirect, HttpServletRequest request) {
		request.setAttribute("redirect", redirect);
		return "login";
	}

	// 登录功能
	@RequestMapping(value = "/user/login", method = RequestMethod.POST)
	public @ResponseBody E3Result login(String username, String password, HttpServletRequest request, HttpServletResponse response) {
		E3Result e3Result = loginService.login(username, password);
		// 判断是否登录成功.如果登录成功,则将token写入cookie
		if (e3Result.getStatus() == 200) {
			String token = e3Result.getData().toString();
			// 将token写入cookie
			CookieUtils.setCookie(request, response, TOKEN_KEY, token);
		}
		return e3Result;
	}
}
package cn.e3mall.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.RegisterService;

@Controller
public class RegisterController {

	// 注册服务service
	@Autowired
	private RegisterService registerService;

	@RequestMapping("/page/register")
	public String showRegisterPage() {
		return "register";
	}

	@RequestMapping("/user/check/{param}/{type}")
	public @ResponseBody E3Result checkData(@PathVariable String param, @PathVariable Integer type) {
		E3Result e3Result = registerService.checkData(param, type);
		return e3Result;
	}

	// 注册
	@RequestMapping(value = "/user/register", method = RequestMethod.POST)
	public @ResponseBody E3Result register(TbUser tbUser) {
		E3Result result = registerService.register(tbUser);
		return result;
	}
}

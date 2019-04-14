package cn.e3mall.sso.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.sso.service.TokenService;

@Controller
public class TokenController {

	@Autowired
	private TokenService tokenService;

	/*@RequestMapping(value = "/user/token/{token}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody String getUserByToken(@PathVariable String token, String callback) {
		E3Result e3Result = tokenService.getUserByToken(token);
		// 判断是否为jsonp请求
		if (StringUtils.isNotBlank(callback)) {
			// 将结果封装成一个js语句
			return callback + "(" + JsonUtils.objectToJson(e3Result) + ");";
		}
		return JsonUtils.objectToJson(e3Result);
	}*/

	@RequestMapping(value = "/user/token/{token}")
	public @ResponseBody Object getUserByToken(@PathVariable String token, String callback) {
		E3Result e3Result = tokenService.getUserByToken(token);
		// 判断是否为jsonp请求
		if (StringUtils.isNotBlank(callback)) {
			// 将结果封装成一个js语句
			MappingJacksonValue jacksonValue = new MappingJacksonValue(e3Result);
			jacksonValue.setJsonpFunction(callback);
			return jacksonValue;
		}
		return e3Result;
	}
}

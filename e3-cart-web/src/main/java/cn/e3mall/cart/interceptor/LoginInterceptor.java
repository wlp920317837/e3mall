package cn.e3mall.cart.interceptor;

import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {

	@Autowired
	private TokenService tokenService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// hander处理之前
		// 从cookoie中取出token
		String token = CookieUtils.getCookieValue(request, "token");
		// 没有token,未登录状态,直接放行
		if (StringUtils.isBlank(token)) {
			return true;
		}
		// 取得token,调用sso服务,根据token取用户信息
		E3Result e3Result = tokenService.getUserByToken(token);
		// 没有取得用户信息.登录过期,直接放行
		if (e3Result.getStatus() != 200) {
			return true;
		}
		// 取到用户信息,登录状态,将登录信息放到handler的request中
		TbUser user = (TbUser) e3Result.getData();
		// 把用户信息放到request中
		request.setAttribute("user", user);
		// 返回true,则继续执行handle.
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		// handler处理之后
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		// 完成处理后,返回视图前
		// 可以处理异常
	}

}

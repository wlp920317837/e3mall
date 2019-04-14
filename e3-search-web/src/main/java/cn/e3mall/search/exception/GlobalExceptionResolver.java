package cn.e3mall.search.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class GlobalExceptionResolver implements HandlerExceptionResolver {

	private Logger logger = LoggerFactory.getLogger(GlobalExceptionResolver.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		// 1.控制台打印
		ex.printStackTrace();
		// 2.写日志
		logger.debug("测试输出的日志");
		logger.info("系统发生异常了");
		logger.error("系统发生异常", ex);
		// 3.发短信.发邮件.通知技术人员处理
		// 使用jmali工具包
		// 4.向用户展示错误页面/e3-search-web/src/main/webapp/WEB-INF/jsp/error/exception.jsp
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/error/exception");
		return mav;
	}

}

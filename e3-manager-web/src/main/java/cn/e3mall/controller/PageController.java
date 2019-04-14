package cn.e3mall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

	// 访问路径为/显示首页
	@RequestMapping("/")
	public String showIndex() {
		return "index";
	}

	//访问路径为/{page}显示访问页
	@RequestMapping("/{page}")
	public String showPage(@PathVariable String page) {
		return page;
	}
}

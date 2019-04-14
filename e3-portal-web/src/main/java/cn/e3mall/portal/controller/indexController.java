package cn.e3mall.portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.e3mall.content.service.ContentService;
import cn.e3mall.pojo.TbContent;

/**
 * 首页展示controller
 * @author wlp
 *
 */

@Controller
public class indexController {

	// 内容分类id,轮播图
	@Value("${CONTENT_LUNBO_ID}")
	private Long CONTENT_LUNBO_ID;

	// 内容服务
	@Autowired
	private ContentService contentService;

	@RequestMapping("/index")
	public String showIndex(Model model) {
		// 轮播图展示
		List<TbContent> list = contentService.getContentListByCid(CONTENT_LUNBO_ID);
		model.addAttribute("ad1List", list);
		return "index";
	}
}

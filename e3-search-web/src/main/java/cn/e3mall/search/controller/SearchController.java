package cn.e3mall.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.service.SearchService;

/**
 * search 表现层
 * @author wlp
 *
 */

@Controller
public class SearchController {

	@Autowired
	private SearchService searchService;

	@RequestMapping("search")
	public String search(String keyword, @RequestParam(defaultValue = "1") Integer page, Model model) throws Exception {
		// 解决get提交乱码
		keyword = new String(keyword.getBytes("iso-8859-1"), "utf-8");
		// 设置每页显示条数
		Integer rows = 32;
		// 调用searchService获得搜索结果
		SearchResult searchResult = searchService.search(keyword, page, rows);

		// 向页面传回数据
		model.addAttribute("query", keyword);
		model.addAttribute("totalPages", searchResult.getTotalPages());
		model.addAttribute("page", page);
		model.addAttribute("recourdCount", searchResult.getRecourdCount());
		model.addAttribute("itemList", searchResult.getItemList());

		// 异常测试
		// int error = 1 / 0;

		return "search";
	}
}

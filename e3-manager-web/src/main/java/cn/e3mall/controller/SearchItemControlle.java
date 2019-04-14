package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.search.service.SearchItemService;

/**
 * 索引服务表现层
 * @author wlp
 *
 */

@Controller
public class SearchItemControlle {

	@Autowired
	private SearchItemService searchItemService;

	@RequestMapping("/index/item/import")
	public @ResponseBody E3Result importAllItems() {
		E3Result result = searchItemService.importAllItems();
		return result;
	}
}

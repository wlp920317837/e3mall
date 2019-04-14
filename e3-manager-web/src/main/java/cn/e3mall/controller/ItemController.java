package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.EasyUiDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.service.ItemService;

@Controller
public class ItemController {

	@Autowired
	private ItemService itemService;

	// 根据商品id获得商品
	@RequestMapping(value = "/item/{itemId}")
	public @ResponseBody TbItem selectItemById(@PathVariable long itemId) {
		TbItem item = itemService.selectItemById(itemId);
		return item;
	}

	// EasyUi分页查询
	@RequestMapping("/item/list")
	public @ResponseBody EasyUiDataGridResult getItemList(Integer page, Integer rows) {
		// 调用服务查询商品列表
		EasyUiDataGridResult result = itemService.getItemList(page, rows);
		return result;
	}

	// 商品添加
	@RequestMapping(value = "/item/save", method = RequestMethod.POST)
	public @ResponseBody E3Result addItem(TbItem tbItem, String desc) {
		E3Result result = itemService.addItem(tbItem, desc);
		return result;
	}
}

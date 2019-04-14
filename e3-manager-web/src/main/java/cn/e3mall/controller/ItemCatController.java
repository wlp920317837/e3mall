package cn.e3mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.EasyUiTreeNode;
import cn.e3mall.service.ItemCatService;

/**
 * 商品类别视图层
 * 
 * @author wlp
 *
 */
@Controller
public class ItemCatController {

	@Autowired
	private ItemCatService itemCatService;

	// 根据parentId查询商品类别
	@RequestMapping("/item/cat/list")
	public @ResponseBody List<EasyUiTreeNode> selectItemCatByParentId(@RequestParam(name = "id", defaultValue = "0") Long parentId) {
		List<EasyUiTreeNode> list = itemCatService.selectItemCatByParentId(parentId);
		return list;
	}
}

package cn.e3mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.EasyUiTreeNode;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.content.service.ContentCategoryService;

/**
 * 内容分类表现层
 * 
 * @author wlp
 *
 */

@Controller
public class ContentCatController {

	@Autowired
	private ContentCategoryService contentCategoryService;

	// 内容分类列表
	@RequestMapping("/content/category/list")
	public @ResponseBody List<EasyUiTreeNode> getContentCatList(@RequestParam(value = "id", defaultValue = "0") Long parentId) {
		List<EasyUiTreeNode> catList = contentCategoryService.getContentCatList(parentId);
		return catList;
	}

	// 添加内容分类
	@RequestMapping(value = "/content/category/create", method = RequestMethod.POST)
	public @ResponseBody E3Result addContentCategory(Long parentId, String name) {
		E3Result e3Result = contentCategoryService.addContentCategory(parentId, name);
		return e3Result;
	}

	// 根据传入id删除当前节点
	@RequestMapping(value = "/content/category/delete/", method = RequestMethod.POST)
	public @ResponseBody E3Result deleteContentCategroy(Long id) {
		E3Result e3Result = contentCategoryService.deleteContentCategroy(id);
		return e3Result;
	}

	// 修改内容分类
	@RequestMapping(value = "/content/category/update", method = RequestMethod.POST)
	public @ResponseBody E3Result editContentCategory(Long id, String name) {
		E3Result e3Result = contentCategoryService.editContentCategory(id, name);
		return e3Result;
	}
}

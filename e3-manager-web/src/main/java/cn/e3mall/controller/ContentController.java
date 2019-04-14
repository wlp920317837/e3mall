package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.EasyUiDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.pojo.TbContent;

/**
 * 内容管理表现层
 * @author wlp
 *
 */

@Controller
public class ContentController {

	// 内容管理service
	@Autowired
	private ContentService contentService;

	// 分页展示内容列表categoryId=90&page=1&rows=20
	@RequestMapping(value = "/content/query/list", method = RequestMethod.GET)
	public @ResponseBody EasyUiDataGridResult getContentList(Integer page, Integer rows, Long categoryId) {
		EasyUiDataGridResult contentList = contentService.getContentList(page, rows, categoryId);
		return contentList;
	}

	// 新增内容,返回值e3result,输入参数表单用pojo接收
	@RequestMapping(value = "/content/save", method = RequestMethod.POST)
	public @ResponseBody E3Result addContent(TbContent content) {
		E3Result result = contentService.addContent(content);
		return result;
	};

	// 修改内容,返回值e3result,输入参数表单用pojo接收
	@RequestMapping(value = "/rest/content/edit", method = RequestMethod.POST)
	public @ResponseBody E3Result editContent(TbContent content) {
		E3Result result = contentService.editContent(content);
		return result;
	}

	// 返回值e3result,根据"ids":ids批量删除内容
	@RequestMapping(value = "/content/delete", method = RequestMethod.POST)
	public @ResponseBody E3Result deleteContent(String ids) {
		E3Result result = contentService.deleteContent(ids);
		return result;
	}
}

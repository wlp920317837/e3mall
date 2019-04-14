package cn.e3mall.content.service;

import cn.e3mall.common.pojo.EasyUiTreeNode;
import cn.e3mall.common.utils.E3Result;

import java.util.List;

public interface ContentCategoryService {

	// 根据parentId获得内容分类列表
	public List<EasyUiTreeNode> getContentCatList(Long parentId);

	// 添加内容分类
	public E3Result addContentCategory(Long parentId, String name);

	// 根据传入id删除当前节点
	public E3Result deleteContentCategroy(Long id);

	// 修改内容分类
	public E3Result editContentCategory(Long id, String name);
}

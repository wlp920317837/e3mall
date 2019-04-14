package cn.e3mall.service;

import java.util.List;

import cn.e3mall.common.pojo.EasyUiTreeNode;

public interface ItemCatService {

	// 根据parentId查询所有子类型
	public List<EasyUiTreeNode> selectItemCatByParentId(Long parentId);
}

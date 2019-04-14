package cn.e3mall.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.e3mall.common.pojo.EasyUiTreeNode;
import cn.e3mall.mapper.TbItemCatMapper;
import cn.e3mall.pojo.TbItemCat;
import cn.e3mall.pojo.TbItemCatExample;
import cn.e3mall.pojo.TbItemCatExample.Criteria;
import cn.e3mall.service.ItemCatService;

@Service
@Transactional
public class ItemCatServiceImpl implements ItemCatService {

	@Autowired
	private TbItemCatMapper itemCatMapper;

	// 根据parentId查询到所有的子类型
	@Override
	public List<EasyUiTreeNode> selectItemCatByParentId(Long parentId) {
		// 根据parentId查询到所有的子类型
		TbItemCatExample example = new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		// 执行查询
		List<TbItemCat> list = itemCatMapper.selectByExample(example);
		// 将查询到的结果转为List<EasyUiTreeNode>
		List<EasyUiTreeNode> treeNodes = new ArrayList<EasyUiTreeNode>();
		for (TbItemCat tbItemCat : list) {
			EasyUiTreeNode treeNode = new EasyUiTreeNode();
			treeNode.setId(tbItemCat.getId());
			treeNode.setText(tbItemCat.getName());
			treeNode.setState(tbItemCat.getIsParent() ? "closed" : "open");
			treeNodes.add(treeNode);
		}
		return treeNodes;
	}

}

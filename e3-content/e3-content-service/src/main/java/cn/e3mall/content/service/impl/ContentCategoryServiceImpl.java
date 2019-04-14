package cn.e3mall.content.service.impl;

import cn.e3mall.common.pojo.EasyUiTreeNode;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.content.service.ContentCategoryService;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import cn.e3mall.pojo.TbContentCategoryExample.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 内容分类管理service
 * @author wlp
 *
 */

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	// contentCategoryMapper
	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;

	// 根据parentId获得内容分类列表
	@Override
	public List<EasyUiTreeNode> getContentCatList(Long parentId) {
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);

		// 转换成List<EasyUiTreeNode>
		List<EasyUiTreeNode> treeNodes = new ArrayList<EasyUiTreeNode>();
		for (TbContentCategory tbContentCategory : list) {
			EasyUiTreeNode treeNode = new EasyUiTreeNode();
			treeNode.setId(tbContentCategory.getId());
			treeNode.setText(tbContentCategory.getName());
			treeNode.setState(tbContentCategory.getIsParent() ? "closed" : "open");
			treeNodes.add(treeNode);
		}

		return treeNodes;
	}

	// 添加内容分类
	@Override
	public E3Result addContentCategory(Long parentId, String name) {
		// 创建contentCategory对象
		TbContentCategory contentCategory = new TbContentCategory();

		// 设置pojo属性
		contentCategory.setName(name);
		contentCategory.setParentId(parentId);
		// 该类目是否为父类目，1为true，0为false
		contentCategory.setIsParent(false);
		contentCategory.setCreated(new Date());
		contentCategory.setUpdated(new Date());
		// SortOrder排列序号，表示同级类目的展现次序
		contentCategory.setSortOrder(1);
		// Status状态。可选值:1(正常),2(删除)'
		contentCategory.setStatus(1);

		// 插入到数据库
		contentCategoryMapper.insertSelective(contentCategory);

		// 判断父节点的isParnet属性,如果为false则改为true
		TbContentCategory parentcategory = contentCategoryMapper.selectByPrimaryKey(parentId);
		if (!parentcategory.getIsParent()) {
			parentcategory.setIsParent(true);
			// 更新到数据库
			contentCategoryMapper.updateByPrimaryKeySelective(parentcategory);
		}

		// 返回E3Result,里面包含pojo(因为要返回id)
		E3Result result = E3Result.ok(contentCategory);
		return result;
	}

	// 根据传入id删除当前节点
	@Override
	public E3Result deleteContentCategroy(Long id) {
		// 获得当前节点
		TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);

		// 判断该节点是否为父节点
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(id);
		int sonnum = contentCategoryMapper.countByExample(example);
		if (sonnum > 0) {
			// 是父节点,不让删除
			return new E3Result(500, "error", null);
		} else {
			// 不是父节点,删除
			contentCategoryMapper.deleteByPrimaryKey(id);
		}

		// 判断当前节点的父节点是否还有子节点,没有,则将isParent改为false
		Long parentId = contentCategory.getParentId();
		TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(parentId);
		example.clear();
		Criteria criteria2 = example.createCriteria();
		criteria2.andParentIdEqualTo(parentId);
		int num = contentCategoryMapper.countByExample(example);
		if (num == 0) {
			parent.setIsParent(false);
		}
		return E3Result.ok();
	}

	// 修改内容分类
	@Override
	public E3Result editContentCategory(Long id, String name) {
		// 获得当前分类节点
		TbContentCategory category = contentCategoryMapper.selectByPrimaryKey(id);
		// 设置分类节点名称
		category.setName(name);
		// 保存修改到数据库
		contentCategoryMapper.updateByPrimaryKeySelective(category);
		return E3Result.ok();
	}
}

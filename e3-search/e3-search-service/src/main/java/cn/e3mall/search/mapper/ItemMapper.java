package cn.e3mall.search.mapper;

import java.util.List;

import cn.e3mall.common.pojo.SearchItem;

public interface ItemMapper {

	// 查询所有商品
	public List<SearchItem> getItemList();

	// 根据id查询SearchItem
	public SearchItem getItemByID(Long itemId);
}

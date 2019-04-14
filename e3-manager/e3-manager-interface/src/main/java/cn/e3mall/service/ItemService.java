package cn.e3mall.service;

import cn.e3mall.common.pojo.EasyUiDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;

public interface ItemService {

	// 根据商品id获得商品
	public TbItem selectItemById(long itemId);

	// 根据商品id获得商品描述
	public TbItemDesc selectItemDescById(long itemId);
	
	// EasyUi分页查询
	public EasyUiDataGridResult getItemList(Integer page, Integer rows);

	// 添加一个商品
	public E3Result addItem(TbItem tbItem, String desc);
}

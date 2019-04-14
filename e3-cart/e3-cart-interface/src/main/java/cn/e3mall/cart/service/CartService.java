package cn.e3mall.cart.service;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbItem;

import java.util.List;

public interface CartService {

	// 添加购物车,返回值e3result,参数
	public E3Result addCart(Long userId, Long itemId, Integer num);

	// 获得购物车列表
	public List<TbItem> getCartlist(Long userId);

	// 合并购物车
	public E3Result mergeCart(Long userId, List<TbItem> list);

	// 更新数量
	public E3Result updateNum(Long userId, Long itemId, Integer num);

	// 删除商品
	public E3Result deleteCart(Long userId, Long itemId);

	// 删除所有商品
	public E3Result clearCart(Long userId);
}
